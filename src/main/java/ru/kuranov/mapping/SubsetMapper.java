package ru.kuranov.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kuranov.dto.SubsetDto;
import ru.kuranov.entity.Segment;
import ru.kuranov.entity.Subset;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubsetMapper {

    private final SegmentMapper mapper;

    public List<Subset> convertToSubsets(List<SubsetDto> subsets) {
        return subsets.stream()
                .map(subsetDto -> {
                    List<Segment> segments = subsetDto.getSegmentDtoList().stream()
                            .map(mapper::convertToSegment)
                            .collect(Collectors.toList());
                    return Subset.builder().segments(segments).build();
                })
                .collect(Collectors.toList());
    }
}
