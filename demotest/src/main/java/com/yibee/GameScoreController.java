package com.yibee;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yibee.entity.GameScore;

@RestController
@RequestMapping("/game")
public class GameScoreController {
	@Resource
	private GameScoreRepository repo;
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/saveScore")
	public int saveScore(){ //selling now

		
		return 0;
	}
	
	public Page<GameScore> getRankingList(){
		Pageable pageable = PageRequest.of(0, 10, Sort.by("score").descending());
		return repo.findAll(pageable);
	}
}
