package ru.kuranov.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API ДЛЯ ОПРЕДЕЛЕНИЯ ПЕРЕСЕКАЮЩИХСЯ ПОДМНОЖЕСТВ И ОХ ОТНОШЕНИЯ К ТОЧКЕ Х")
                        .version("0.0.1")
                        .contact(
                                new Contact()
                                        .email("kuranov.andrey@gmal.com")
                                        .url("https://hh.ru/resume/946b79acff0747362c0039ed1f5a4379364f6f")
                                        .name("Куранов Андрей")));
    }

}
