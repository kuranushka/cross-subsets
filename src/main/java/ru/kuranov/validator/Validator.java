package ru.kuranov.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuranov.entity.Subset;

import java.util.List;
import java.util.stream.Stream;

import static ru.kuranov.message.Message.POINTS_OUT_OF_RANGE;
import static ru.kuranov.message.Message.X_OUT_OF_RANGE;

@Service
@RequiredArgsConstructor
public class Validator {


    // проверяем точки входящих подмножеств на NaN, Infinite
    public void validate(List<Subset> subsets) {
        boolean isValidSubs = subsets.stream()
                .flatMap(subset -> subset.getSegments().stream())
                .flatMap(segment -> Stream.of(segment.getLeft(), segment.getRight()))
                .anyMatch(point -> !point.isNaN() || !point.isInfinite());
        if (!isValidSubs) {
            throw new RuntimeException(POINTS_OUT_OF_RANGE.getMessage());
        }
    }

    // проверяем Х на NaN и Infinite
    public void validate(Double x) {
        if (x.isNaN() || x.isInfinite()) {
            throw new RuntimeException(X_OUT_OF_RANGE.getMessage());
        }
    }
}
