package ru.kuranov.dto;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PointDto {

    private Double point;

}
