package nextstep.subway.documentation;

import static nextstep.subway.applicaion.dto.SearchType.*;
import static nextstep.subway.steps.PathDocumentationSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.path.PathService;
import nextstep.subway.path.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

public class PathDocumentation extends Documentation {

	@MockBean
	private PathService pathService;

	@Test
	void path() {
		// given
		PathResponse pathResponse = new PathResponse(
			Lists.newArrayList(
				new StationResponse(1L, "강남역"),
				new StationResponse(2L, "역삼역")
			), 10, 5, 1250
		);
		when(pathService.findPath(any(), any())).thenReturn(pathResponse);

		// when
		ExtractableResponse<Response> searchResponse = searchPathDistance(spec, 1L, 2L, DISTANCE);

		// then
		assertThat(searchResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
	}
}
