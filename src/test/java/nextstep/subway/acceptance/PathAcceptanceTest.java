package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.steps.LineSteps.*;
import static nextstep.subway.steps.PathSteps.*;
import static nextstep.subway.steps.SectionSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
class PathAcceptanceTest extends AcceptanceTest {
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    /**
	 * 					(di:10, dr:2)
     * 교대역		---	*2호선*	---	강남역
     * |								|
     * *3호선*						*신분당선*
	 * (di:2, dr:10)					(di:10, dr:3)
     * |								|
     * 남부터미널역	---	*3호선*	---	양재역
	 * 					(di:3, dr:10)
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

		이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10, 2).jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10, 3).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2, 10).jsonPath().getLong("id");

		지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3, 10);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        // when
        ExtractableResponse<Response> response = 두_역의_최단_거리_경로_조회를_요청(교대역, 양재역);

        // then
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

	/**
	 * Given 지하철역이 등록되어있음
	 * And 지하철 노선이 등록되어있음
	 * And 지하철 노선에 지하철역이 등록되어있음
	 * When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
	 * Then 최소 시간 기준 경로를 응답
	 * And 총 거리와 소요 시간을 함께 응답함
	 * **/
	@DisplayName("두 역의 최소 시간 경로를 조회")
	@Test
	void findPathByTime() {
		// when
		ExtractableResponse<Response> response = 두_역의_최소_시간_경로_조회를_요청(교대역, 양재역, "DURATION");

		// then
		assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 강남역, 양재역);
	}
}
