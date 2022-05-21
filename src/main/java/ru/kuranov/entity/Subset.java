package ru.kuranov.entity;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Builder
public class Subset {
    private final List<Segment> segments;

    public List<Segment> getSegments() {
        return segments;
    }

}
