package ru.kuranov.service;

import org.springframework.stereotype.Service;
import ru.kuranov.dto.PointDto;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.entity.Segment;
import ru.kuranov.entity.Subset;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FunctionHandler {

    // todo
    public List<SegmentDto> getSegments(List<Subset> subs, Double x) {
        return null;
    }

    public PointDto getPoint(List<Subset> subs, Double x) {
        return null;
    }

    private void solver() {
        int x = 36;

        Subset subset1 = new Subset(List.of(new Segment(10D, 15D)));
        Subset subset2 = new Subset(List.of(new Segment(-Double.MAX_VALUE, -5D), new Segment(10D, Double.MAX_VALUE)));
        Subset subset3 = new Subset(List.of(new Segment(-Double.MAX_VALUE, 12D)));

        List<Subset> subs = List.of(subset1, subset2, subset3);

        //      collect all points
        Set<Double> points = getAllPoints(subs);

        //        collect all cross points
        TreeSet<Double> crossDots = new TreeSet<>();
        for (Double point : points) {
            int counter = 0;
            for (Subset subset : subs) {
                for (Segment segment : subset.getSegments()) {
                    if (segment.getLeft().compareTo(point) <= 0 && segment.getRight().compareTo(point) >= 0) {
                        counter++;
                    }
                }
            }
            if (counter == subs.size()) {
                crossDots.add(point);
            }
        }



        // todo неправильно
//        Double near = crossDots.stream().flatMap(point -> Map.of(point, point + x)
//                        .entrySet().stream()
//                        .sorted((o1, o2) -> o1.getValue().compareTo(o2.getValue()))
//                        .map(Map.Entry::getKey))
//                .findFirst().orElseThrow();
//
//        System.out.println(near);
    }

    public Set<Double> getAllPoints(List<Subset> subs) {
        Set<Double> points = subs.stream()
                .flatMap(subset -> subset.getSegments()
                        .stream().flatMap(segment -> Stream.of(segment.getLeft(), segment.getRight())))
                .collect(Collectors.toSet());
        return points;
    }
}

