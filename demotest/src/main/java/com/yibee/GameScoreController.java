package com.yibee;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

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
	public Properties saveScore(String playerName,int score,int level,int lines){ //selling now
		
		Properties p = new Properties();
		GameScore g;
		Optional<GameScore> op = repo.findByPlayerName(playerName);
		boolean willUpdate = false;
		if(op.isPresent()) {
			g = op.get();
			if(score > g.getScore()) willUpdate = true;
		}
		else {
			g = new GameScore();
			willUpdate = true;
			g.setId(0L);
			g.setPlayerName(playerName);
			
		}
		
		if(willUpdate) {
			g.setScore(score);
			g.setLevel(level);
			g.setLines(lines);
			g.setSaveTime(new Date());
			repo.save(g);
		}
		
		Long n = repo.getRankingIndex(g.getScore());
		p.put("updated",willUpdate);
		p.put("score",g.getScore());
		p.put("rank",n);
		return p;
		
	}
	
	@CrossOrigin(origins = "*", maxAge = 3600)
	@GetMapping(value="/getRankingList")
	public List<GameScore> getRankingList(@RequestParam(defaultValue="20") int pageSize){
		Pageable pageable = PageRequest.of(0, pageSize, Sort.by("score").descending());
		return repo.findAll(pageable).toList();
	}
}
