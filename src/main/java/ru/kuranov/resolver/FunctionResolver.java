package ru.kuranov.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuranov.dto.PointDto;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.entity.Segment;
import ru.kuranov.entity.Subset;
import ru.kuranov.mapping.SegmentMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.kuranov.message.Message.NO_CROSS_SEGMENTS;

@Service
@RequiredArgsConstructor
public class FunctionResolver {

    private final SegmentMapper mapper;


    // переменная для сохранения дистанции
    Double distance = 0d;

    // переменная для сохранения ближайшей точки
    Double nearPoint = 0d;

    Set<Double> nearestPointSet = new TreeSet<>();


    public PointDto findNearestPoint(List<Subset> subs, Double x) {

        // коллекция для хранения расстояний от X до точек отрезков, key = расстояние, value = точка
        Map<Double, Double> neaR = new TreeMap<>();

        // если подмножество в единственном экземпляре, то обрабатыаем отдельно
        if (subs.size() == 1) {
            return handleSingleSubset(subs, x);
        }

        // извлечение и сортировка точек принадлежащих всем подмножествам
        List<Double> crossPoints = new ArrayList<>(getCrossPoints(subs));

        // поиск отрезков принадлежащих всем подмножествам
        List<Segment> crossSegments = getSegments(subs, crossPoints);

        // если среди точек пересекающих все подмножества есть MAX_VALUE и X = MAX_VALUE, то возвращаем X
        if (x.compareTo(Double.MAX_VALUE) == 0 && !crossPoints.contains(Double.MAX_VALUE)) {
            Double nearestToX = crossSegments.stream()
                    .flatMap(segment -> Stream.of(segment.getLeft(), segment.getRight()))
                    .max(Double::compareTo)
                    .orElseThrow();
            return PointDto.builder()
                    .point(nearestToX)
                    .build();
        }

        // если среди точек пересекающих все подмножества есть - MAX_VALUE и X = - MAX_VALUE, то возвращаем X
        if (x.compareTo(-Double.MAX_VALUE) == 0 && !crossPoints.contains(-Double.MAX_VALUE)) {
            Double nearestToX = crossSegments.stream()
                    .flatMap(segment -> Stream.of(segment.getLeft(), segment.getRight()))
                    .min(Double::compareTo).orElseThrow();
            return PointDto.builder()
                    .point(nearestToX)
                    .build();
        }

        // если среди точек пересекающих все подмножества есть 0.0 и X = 0.0, то возвращаем X
        if (x.compareTo(0d) == 0 && crossPoints.contains(0d)) {
            return PointDto.builder()
                    .point(x)
                    .build();
        }

        // ищем самую близкую точку
        for (Segment segment : crossSegments) {

            // если отрезок пересекающий все подмножества содержит Х возвращаем Х
            if (isExistInSegment(segment, x)) {
                return PointDto.builder()
                        .point(x)
                        .build();
            } else {

                // определяем расстояние до левой точки отрезка
                if (segment.getLeft().compareTo(x) < 0) {
                    neaR.put(x - segment.getLeft(), segment.getLeft());
                } else {
                    neaR.put(segment.getLeft() - x, segment.getLeft());
                }


                // определяем расстояние до правой точки отрезка
                if (segment.getRight().compareTo(x) < 0) {
                    neaR.put(x - segment.getRight(), segment.getRight());
                } else {
                    neaR.put(segment.getRight() - x, segment.getRight());
                }
            }
        }
        neaR.remove(Double.NEGATIVE_INFINITY);
        neaR.remove(Double.POSITIVE_INFINITY);
        return PointDto.builder()
                .point(neaR.values().stream().findFirst().orElseThrow())
                .build();
    }

    private Double getNearestPoint(Set<Double> nearestPointSet, Double x) {
        nearPoint = 0d;
        for (Double point : nearestPointSet) {
            if (distance.compareTo(Math.abs(point - x)) < 0) {
                distance = Math.abs(point - x);
                nearPoint = point;
            }
        }
        return nearPoint;
    }


    private PointDto handleSingleSubset(List<Subset> subs, Double x) {
        for (Segment segment : subs.get(0).getSegments()) {
            if (isExistInSegment(segment, x)) {
                return PointDto.builder()
                        .point(x)
                        .build();
            }
        }
        throw new RuntimeException(NO_CROSS_SEGMENTS.getMessage());
    }


    public List<SegmentDto> findAllCrossSegments(List<Subset> subs) {

        // извлечение и сортировка точек принадлежащих всем подмножествам
        List<Double> crossPoints = new ArrayList<>(getCrossPoints(subs));

        // поиск отрезков принадлежащих всем подмножествам
        List<Segment> crossSegments = getSegments(subs, crossPoints);

        return crossSegments.stream()
                .map(mapper::convertToSegmentDto)
                .collect(Collectors.toList());
    }


    // поиск отрезков принадлежащих всем подмножествам
    private List<Segment> getSegments(List<Subset> subs, List<Double> crossPoints) {

        List<Segment> crossSegments = new ArrayList<>();

        for (int i = 0; i < crossPoints.size() - 1; i++) {

            // определяем среднюю точку принадлежащую отрезку
            double average = (crossPoints.get(i) + crossPoints.get(i + 1)) / 2;
            int counter = 0;

            // итерация по подмножествам
            for (Subset subset : subs) {

                // итерация по отрезкам
                for (Segment segment : subset.getSegments()) {
                    if (isExistInSegment(segment, average)) {
                        counter++;
                        break;
                    }
                }
            }

            // если точка принадлежит на отрезке всем подмножествам, то сохраняем отрекок
            if (counter == subs.size()) {
                crossSegments.add(new Segment(crossPoints.get(i), crossPoints.get(i + 1)));
            }
        }
        return crossSegments;
    }


    // извлечение и сортировка точек принадлежащих всем подмножествам
    private TreeSet<Double> getCrossPoints(List<Subset> subs) {

        // извлечение всех точек - концов отрезков
        Set<Double> points = subs
                .stream()
                .flatMap(subset -> subset.getSegments().stream())
                .flatMap(segment -> Stream.of(segment.getLeft(), segment.getRight()))
                .collect(Collectors.toSet());

        // сортировка точек
        TreeSet<Double> sortedPoints = new TreeSet<>();

        // извлечение точек принадлежащих всем подмножествам
        for (Subset subset : subs) {
            for (Segment segment : subset.getSegments()) {
                for (Double point : points) {
                    if (isExistInSegment(segment, point)) {
                        sortedPoints.add(point);
                    }
                }
            }
        }
        return sortedPoints;
    }


    // проверка принадлежит ли точка отрезку
    private boolean isExistInSegment(Segment segment, double average) {
        return segment.getLeft().compareTo(average) <= 0 && segment.getRight().compareTo(average) >= 0;
    }

    // проверяем принадлежит ли точка всем подмножествам
    private boolean isExistInAllSubset(List<Subset> subs, Double point) {
        int counter = 0;
        for (Subset subset : subs) {
            for (Segment segment : subset.getSegments()) {
                if (isExistInSegment(segment, point)) {
                    counter++;
                }
            }
        }
        return counter == subs.size();
    }
}

