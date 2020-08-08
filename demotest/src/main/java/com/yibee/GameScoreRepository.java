package com.yibee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.GameScore;
import com.yibee.entity.Member;

public interface GameScoreRepository  extends PagingAndSortingRepository<GameScore, Long>{
	@Query(value = "select count(g) FROM GameScore g where g.score>?1")
	long getRankOfPlayer(String gamaName,int score);

//	@Query(value="From GameScore g")
//	Page<GameScore> getRankList(String gameName,Pageable pageable);
}
