package br.com.vandersonsampaio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket voteSessionApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.vandersonsampaio.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, buildResponses())
                .globalResponses(HttpMethod.POST, buildResponses());
    }

    private List<Response> buildResponses() {
        return Arrays.asList(
                new ResponseBuilder().code("500").description("Unmapped exception").build(),
                new ResponseBuilder().code("400").description("Some parameter was not informed correctly.").build()
        );
    }

    private ApiInfo metaInfo() {
        return new ApiInfo(
                "Vote in Session - API",
                "API Rest para criação de pauta, sessão de votação, votação e contabilização do resultado.",
                "1.0",
                "Terms of Service",
                (new Contact("Vanderson Sampaio", "http://www.vandersonsampaio.com.br", "vandersons.sampaio@gmail.com")),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/",
                new ArrayList<>()
        );
    }
}
