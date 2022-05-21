package ru.kuranov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kuranov.entity.Segment;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SubsetDto {

    private  List<Segment> segments;

}
