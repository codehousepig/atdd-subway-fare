package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SearchType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {

	private final LineService lineService;
	private final StationService stationService;

	public PathResponse findPath(Long source, Long target, SearchType type) {
		Station upStation = stationService.findById(source);
		Station downStation = stationService.findById(target);
		List<Line> lines = lineService.findLines();
		SubwayMap subwayMap = new SubwayMap(lines);
		Path path = subwayMap.findPath(upStation, downStation, type);

		return PathResponse.of(path);
	}
}
