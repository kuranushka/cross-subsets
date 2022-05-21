package ru.kuranov.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuranov.dto.PointDto;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.entity.Segment;
import ru.kuranov.entity.Subset;
import ru.kuranov.mapping.SegmentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FunctionResolver {

    private final SegmentMapper mapper;

    Double distance = Double.MAX_VALUE;
    Double nearPoint = 0D;

    public PointDto findNearestPoint(List<Subset> subs, Double x) {
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
        if (x.compareTo(0D) == 0 && crossPoints.contains(0D)) {
            return PointDto.builder()
                    .point(x)
                    .build();
        }

        // ищем самую близкую точку
        for (Segment segment : crossSegments) {
                if (Math.abs(segment.getLeft() - x) < distance) {
                    nearPoint = segment.getLeft();
                    distance = Math.abs(segment.getLeft() - x);
                }
                if (Math.abs(segment.getRight() - x) < distance) {
                    nearPoint = segment.getRight();
                    distance = Math.abs(segment.getRight() - x);
                }
            }
        return PointDto.builder()
                .point(nearPoint)
                .build();
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

    private List<Segment> getSegments(List<Subset> subs, List<Double> crossPoints) {
        List<Segment> crossSegments = new ArrayList<>();
        for (int i = 0; i < crossPoints.size() - 1; i++) {
            double average = (crossPoints.get(i) + crossPoints.get(i + 1)) / 2;
            int counter = 0;
            for (Subset subset : subs) {
                for (Segment segment : subset.getSegments()) {
                    if (isExistInSegment(segment, average)) {
                        counter++;
                    }
                }
            }
            if (counter == subs.size()) {
                crossSegments.add(new Segment(crossPoints.get(i), crossPoints.get(i + 1)));
            }
        }
        return crossSegments;
    }

    private TreeSet<Double> getCrossPoints(List<Subset> subs) {
        return subs
                .stream()
                .flatMap(subset -> subset.getSegments().stream())
                .flatMap(segment -> Stream.of(segment.getLeft(), segment.getRight()))
                .filter(point -> isExistInAllSubset(subs, point))
                .collect(Collectors.toCollection(TreeSet::new));
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

