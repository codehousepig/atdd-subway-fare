package nextstep.subway.steps;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PathSteps {

	public static ExtractableResponse<Response> searchPath(RequestSpecification spec, Long source, Long target) {
		Map<String, Long> params = new HashMap<>();
		params.put("source", source);
		params.put("target", target);

		ExtractableResponse<Response> searchResponse = RestAssured
			.given(spec).log().all()
			.filter(document("path",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())))
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.params(params)
			.when().get("/paths")
			.then().log().all()
			.extract();
		return searchResponse;
	}

	public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}", source, target)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target, String type) {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
			.then().log().all().extract();
	}
}
