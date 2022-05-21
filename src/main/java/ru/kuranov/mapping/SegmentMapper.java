package ru.kuranov.mapping;

import org.springframework.stereotype.Service;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.entity.Segment;

@Service
public class SegmentMapper {

    public Segment convertToSegment(SegmentDto segmentDto) {
        return new Segment(segmentDto.getA(), segmentDto.getB());
    }

    public SegmentDto convertToSegmentDto(Segment segment) {
        return new SegmentDto(segment.getLeft(), segment.getRight());
    }
}
