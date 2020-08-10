package com.yibee;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.GameScore;
import com.yibee.entity.Member;

public interface GameScoreRepository  extends PagingAndSortingRepository<GameScore, Long>{
	@Query(value = "select count(g) FROM GameScore g where g.score>?1")
	Long getRankingIndex(int score);
	
	@Query(value="From GameScore g where playerName=?1")
	Optional<GameScore> findByPlayerName(String playerName);

//	@Query(value="From GameScore g")
//	Page<GameScore> getRankList(String gameName,Pageable pageable);
	
}
