package za.co.dariel.deap.endpointsecurity;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class EndpointsecurityApplication {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Docket swaggerConfigurations(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/employee/**"))
				.apis(RequestHandlerSelectors.basePackage("za.co.dariel"))
				.build()
				.apiInfo(apiDetails());
	}

	//http://localhost:9090/swagger-ui.html
	private ApiInfo apiDetails() {
		return new ApiInfo(
				"DEAP Project API",
				"DEAP Employee Demo Application",
				"1.0.0",
				"Owned by Dariel Solutions",
				new springfox.documentation.service.Contact("Dariel", "https://www.dariel.co.za/", "info@dariel.co.za"),
				"",
				"",
				Collections.emptyList()
		);
	}
	

	public static void main(String[] args) {
		SpringApplication.run(EndpointsecurityApplication.class, args);
	}

}
