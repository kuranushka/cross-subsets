package ru.kuranov.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Message {

    POINTS_OUT_OF_RANGE("ОДНА ИЗ ТОЧЕК МНОЖЕСТВА НЕ ПРИНАДЛЕЖИТ ДИАПОЗОНУ ОТ -1.7976931348623157E308 ДО 1.7976931348623157E308"),
    X_OUT_OF_RANGE("ЧИСЛО Х ДОЛЖНО ПРИНАДЛЕЖАТь ДИАПОЗОНУ ОТ -1.7976931348623157E308 ДО 1.7976931348623157E308, НЕ ДОЛЖНО БЫТЬ NaN, НЕ ДОЛЖНО БЫТЬ INFINITE"),
    NO_CROSS_SEGMENTS("НЕТ ПЕРЕСЕКАЮЩИХСЯ ОТРЕЗКОВ");

    @Getter
    private final String message;
}
