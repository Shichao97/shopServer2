package com.yibee.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * The persistent class for the schools database table.
 * 
 */
@Entity
@Table(name="game")
@NamedQuery(name="School.findAll", query="SELECT s FROM School s")
public class GameScore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private String gameName;
	private String playerName;

	private int score;
	private int lines;
	private int level;
	
	@JsonSerialize(using = DateToLongSerializer.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="save_time")
	private Date saveTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}
	



}