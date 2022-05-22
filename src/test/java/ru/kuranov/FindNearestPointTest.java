package ru.kuranov;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.kuranov.dto.SegmentDto;
import ru.kuranov.dto.SubsetDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FindNearestPointTest {

    String baseUrl = "/api/v1/function/nearest-point";
    LinkedMultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
    List<SubsetDto> subs = new ArrayList<>();


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        SubsetDto subset1 = new SubsetDto(List.of(new SegmentDto(-Double.MAX_VALUE, 0d), new SegmentDto(10d, Double.MAX_VALUE)));
        SubsetDto subset2 = new SubsetDto(List.of(new SegmentDto(-Double.MAX_VALUE, 15d)));
        subs = List.of(subset1, subset2);
    }

    @Test
    void findNearestPointOutOfCrossSegmentX() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        Double x = 17.0d;
        requestParam.put("x", Collections.singletonList(String.valueOf(x)));

        mockMvc.perform(post(baseUrl)
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value("15.0"));
    }

    @Test
    void findNearestPointInCrossSegmentX() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Double x = 2.0;
        requestParam.put("x", Collections.singletonList(String.valueOf(x)));
        mockMvc.perform(post(baseUrl)
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value("0.0"));
    }

    @Test
    void findNearestMinusDoubleMaxValueX() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Double x = -Double.MAX_VALUE;
        requestParam.put("x", Collections.singletonList(String.valueOf(x)));
        mockMvc.perform(post(baseUrl)
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value("-1.7976931348623157E+308"));
    }

    @Test
    void findNearestDoubleMaxValueX() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Double x = Double.MAX_VALUE;
        requestParam.put("x", Collections.singletonList(String.valueOf(x)));
        mockMvc.perform(post(baseUrl)
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value("15.0"));
    }

    @Test
    void findNearestZeroValueX() throws Exception {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        Double x = 0d;
        requestParam.put("x", Collections.singletonList(String.valueOf(x)));
        mockMvc.perform(post(baseUrl)
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subs)))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value("0.0"));
    }
}
