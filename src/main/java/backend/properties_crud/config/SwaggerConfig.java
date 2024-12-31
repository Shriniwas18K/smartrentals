package backend.properties_crud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Properties-CRUD Service")
                .version("1.0")
                .description(
                    "It is property rental service offering CRUD operations with property postings"
                        + " and public search api.Users can register using public endpoint"
                        + " '/registration' and need to be authenticated using HttpBasic"
                        + " Authentication in each request being stateless api.It is thus REST API"
                        + " with httpBasic authentication. "));
  }
}
