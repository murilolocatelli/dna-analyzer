package br.com.mercadolivre.dnaanalyzer;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = BaseApplication.BASE_PACKAGE)
@EnableSwagger2
public abstract class BaseApplication {

    static final String BASE_PACKAGE = "br.com.mercadolivre.dnaanalyzer";
    static final String CONTROLLER_PACKAGE = "br.com.mercadolivre.dnaanalyzer.controller";

    @Value("${application.name}")
    private String applicationName;

    @Value("${application.description}")
    private String applicationDescription;

    @Value("${application.version}")
    private String applicationVersion;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(this.apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
            this.applicationName, this.applicationDescription, applicationVersion,
            null, null, null, null, Collections.emptyList());
    }

}
