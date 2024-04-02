package com.example.mypuzzl.sharedInterface;

import java.io.Serializable;

public class GameConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String[] arrangement;
	private int time;
	private int cardsEmount;
	private String theme;
	private int level;
	
	public GameConfiguration () {
		this.level=0;
		this.theme="";
		this.time=0;
		this.arrangement = new String[0];
		this.cardsEmount=0;
	}
	
	public void setStartSettings(String theme, int level) {
		this.theme = theme;
		this.level = level;
		this.cardsEmount=(level==1) ? 4 : ((level==2) ? 6 : 8);
	}
	
	public String getTheme() {
		return this.theme;
	}

	public int getLevel() {
		return this.level;
	}

	public String[] getArrangement() {
		return this.arrangement;
	}
	
	public void setArrangement(String[] arrangement) {
		this.arrangement = arrangement;
	}

	public int getCardsEmount() {
		return this.cardsEmount;
	}

	public void setCardsEmount(int openedPairs) {
		this.cardsEmount=openedPairs;
	}
	
	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time=time;
	}
	
	public String getImage(int position) {
		return arrangement[position];
	}
	
}
