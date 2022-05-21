package ru.kuranov.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Subset {

    private  List<Segment> segments;

}
