package sistema_bancario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Sistema Bancário").version("v1").description("Sistema Bancário project")
				.termsOfService("github.io.raphaelmun1z/sistema-bancario")
				.license(new License().name("Apache 2.0").url("github.io.raphaelmun1z/sistema-bancario")));
	}
}
