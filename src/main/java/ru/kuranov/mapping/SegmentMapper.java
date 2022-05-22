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

    public Segment convertToSegment(SegmentDto segmentDto) {

        // если точки в отрезках перепутаны, меняем их местами
        if (segmentDto.getLeftPoint().compareTo(segmentDto.getRightPoint()) > 0) {
            return Segment.builder()
                    .right(segmentDto.getLeftPoint())
                    .left(segmentDto.getRightPoint())
                    .build();
        }
        return Segment.builder()
                .left(segmentDto.getLeftPoint())
                .right(segmentDto.getRightPoint())
                .build();
    }
}

