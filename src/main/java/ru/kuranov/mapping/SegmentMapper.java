package ru.kuranov.mapping;

import org.springframework.stereotype.Service;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.entity.Segment;

@Service
public class SegmentMapper {

    public SegmentDto convertToSegmentDto(Segment segment) {
        return SegmentDto.builder()
                .leftPoint(segment.getLeft())
                .rightPoint(segment.getRight())
                .build();
    }
}

