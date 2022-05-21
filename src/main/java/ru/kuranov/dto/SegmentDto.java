package ru.kuranov.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Builder
public class SegmentDto {
    private final Double a;
    private final Double b;

    public Double getA() {
        return a;
    }

    public Double getB() {
        return b;
    }

}
