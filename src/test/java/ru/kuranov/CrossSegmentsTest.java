package ru.kuranov;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.dto.SubsetDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CrossSegmentsTest {

    String baseUrl = "/api/v1/function/cross-subsets";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void findManyCrossSegments() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        SubsetDto subset1 = new SubsetDto(
                List.of(
                        new SegmentDto(-Double.MAX_VALUE, 0d),
                        new SegmentDto(10d, Double.MAX_VALUE)));

        SubsetDto subset2 = new SubsetDto(
                List.of(
                        new SegmentDto(-Double.MAX_VALUE, 15d)));

        List<SubsetDto> subs = List.of(subset1, subset2);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].leftPoint").value("-1.7976931348623157E+308"))
                .andExpect(jsonPath("$[0].rightPoint").value("0.0"))
                .andExpect(jsonPath("$[1].leftPoint").value("10.0"))
                .andExpect(jsonPath("$[1].rightPoint").value("15.0"));
    }

    @Test
    void findOneCrossSegment() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        SubsetDto subset1 = new SubsetDto(
                List.of(
                        new SegmentDto(-Double.MAX_VALUE, 120d)));

        SubsetDto subset2 = new SubsetDto(
                List.of(
                        new SegmentDto(-280d, -220d)));
        SubsetDto subset3 = new SubsetDto(
                List.of(
                        new SegmentDto(-350d, Double.MAX_VALUE)));
        SubsetDto subset4 = new SubsetDto(
                List.of(
                        new SegmentDto(-Double.MAX_VALUE, -100d),
                        new SegmentDto(-50d, Double.MAX_VALUE)));
        SubsetDto subset5 = new SubsetDto(
                List.of(
                        new SegmentDto(-300d, -200d)));

        List<SubsetDto> subs = List.of(subset1, subset2, subset3, subset4, subset5);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].leftPoint").value("-280.0"))
                .andExpect(jsonPath("$[0].rightPoint").value("-220.0"));
    }
}
