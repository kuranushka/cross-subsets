package ru.kuranov.controller;

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
@RequestMapping
public class FunctionController {

    private final FunctionResolver resolver;
    private final Validator validator;
    private final SubsetMapper mapper;

    //TODO проверка всего и вся и переписать на DTO
    @PostMapping(value = "/cross-subsets")
    public ResponseEntity<?> findAllCrossSubsets(@RequestBody List<SubsetDto> subsets) {
        validator.validate(mapper.convertToSubsets(subsets));

        List<SegmentDto> segments = resolver.findAllCrossSegments(mapper.convertToSubsets(subsets));
        return new ResponseEntity<>(segments, HttpStatus.OK);
    }


    @PostMapping(value = "/nearest-point")
    public ResponseEntity<?> findNearestPoint(@RequestBody List<SubsetDto> subsets, @RequestParam Double x) {
        validator.validate(mapper.convertToSubsets(subsets));
        validator.validate(x);

        PointDto point = resolver.findNearestPoint(mapper.convertToSubsets(subsets), x);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }


//    @GetMapping(value = "/cross-subsets")
//    public ResponseEntity<?> getSubsets() {
//        Subset subset1 = new Subset(List.of(new Segment(-Double.MAX_VALUE, 0d), new Segment(10d, Double.MAX_VALUE)));
//        Subset subset2 = new Subset(List.of(new Segment(-Double.MAX_VALUE, 15d)));
//        List<Subset> subs = List.of(subset1, subset2);
//        Double x = 19.0d;
//        return new ResponseEntity<>(x, HttpStatus.OK);
//    }
}
