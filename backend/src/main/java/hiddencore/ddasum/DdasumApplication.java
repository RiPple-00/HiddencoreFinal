package hiddencore.ddasum;

<<<<<<< HEAD
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
=======
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Ddasum API",
				version = "v1",
				description = "Ddasum backend API 문서"
		)
)

<<<<<<< HEAD

// @OpenAPIDefinition은 필수는 아닙니다.

// springdoc-openapi-starter-webmvc-ui를 쓰고 있으므로 애플리케이션은 정상 실행되고 OpenAPI UI도 나옵니다.
// 해당 애너테이션은 OpenAPI 문서의 title, version, description 같은 메타데이터를 설정하는 용도입니다.
// 문서 정보가 기본값으로도 괜찮다면 제거해도 됩니다.
// 단, API 문서에 원하는 제목/버전/설명을 넣고 싶으면 그대로 두어야 합니다.
// 즉, 없어도 동작은 하지만, 문서 헤더 정보를 직접 지정하려면 필요합니다.

=======
>>>>>>> 1039d55f3b99df2abaf1450d9e2f351e3b91d9bf
public class DdasumApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdasumApplication.class, args);
	}

}
