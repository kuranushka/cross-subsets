package ru.kuranov.entity;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Builder
public class Segment {

    private final Double left;
    private final Double right;

    public Double getLeft() {
        return left;
    }

    public Double getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
