package com.example.assistant.domain.riot.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.assistant.domain.riot.dto.response.MatchResponse;
import com.example.assistant.domain.riot.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {
	private final MatchRepository matchRepository;

	public List<MatchResponse> getRecentMatches(String riotId, String tagline) {
		Pageable pageable = PageRequest.of(0, 20);
		// List<Match> matches = matchRepository.findTopBy20ByRiotIdAndTagline(riotId, tagline, pageable);
		//
		// return matches.stream()
		//         .map(match -> MatchResponse.builder()
		//                 .matchId(match.getMatchId())
		//                 .championName(match.getChamptionName())
		//                 .kda(match.getKda())
		//                 .result(match.getResult())
		//                 .playDate(match.getPlayDate())
		//                 .duration(match.getDuration())
		//                 .build())
		//         .collect(Collectors.toList());
		return null;
	}
}
