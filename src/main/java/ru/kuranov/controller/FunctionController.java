package ru.kuranov.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuranov.dto.PointDto;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.dto.SubsetDto;
import ru.kuranov.mapping.SubsetMapper;
import ru.kuranov.resolver.FunctionResolver;
import ru.kuranov.validator.Validator;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/function")
public class FunctionController {

    private final FunctionResolver resolver;
    private final Validator validator;
    private final SubsetMapper mapper;

    @Operation(
            summary = "Получить все отрезки принадлежащие всем подмножествам",
            description = "ПЕРЕДАВАЕМЫЕ ЗНАЧЕНИЯ ДОЛЖНЫ ПРИНАДЛЕЖАТЬ ДИАПОЗОНУ ОТ -1.7976931348623157E308 ДО 1.7976931348623157E308, ПРИ ЭТОМ НЕ БЫТЬ NaN И НЕ БЫТЬ INFINITE")
    @PostMapping(value = "/cross-subsets")
    public ResponseEntity<?> findAllCrossSubsets(@RequestBody List<SubsetDto> subsets) {

        // валидируем входящие подмножества
        validator.validate(mapper.convertToSubsets(subsets));

        // извлекаем отрезки принадлежащие всем подмножествам
        List<SegmentDto> segments = resolver.findAllCrossSegments(mapper.convertToSubsets(subsets));
        return new ResponseEntity<>(segments, HttpStatus.OK);
    }

    @Operation(
            summary = "Получить ближайшую к точке Х точку из отрезка, принадлежащего всем подмножествам, или если точка Х принадлежит этому отрезку вернуть её",
            description = "ПЕРЕДВАЕМЫЕ ЗНАЧЕНИЯ, В ТОМ ЧИСЛЕ ТОЧКА Х ДОЛЖНЫ ПРИНАДЛЕЖАТЬ ДИАПОЗОНУ ОТ -1.7976931348623157E308 ДО 1.7976931348623157E308, ПРИ ЭТОМ НЕ БЫТЬ NaN И НЕ БЫТЬ INFINITE")
    @PostMapping(value = "/nearest-point")
    public ResponseEntity<?> findNearestPoint(@RequestBody List<SubsetDto> subsets, @RequestParam Double x) {

        // валидируем входящие подмножества
        validator.validate(mapper.convertToSubsets(subsets));

        // валидируем Х
        validator.validate(x);

        // извлекаем ближайшую точку, либо возвращаем Х, если он принадлежит отрезку принадлежащему всем подмножествам
        PointDto point = resolver.findNearestPoint(mapper.convertToSubsets(subsets), x);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }
}
