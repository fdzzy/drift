package com.gy.ke.bean;

public class Keyword implements Comparable<Keyword> {
	private String name;
	private float score;
	private String cx;//词性
	private float idf;
	private int freq;
	private float weight;

	

	public Keyword(String name,String cx,Float idf,Float weight) {
		if(idf==null)
			idf=100f;//idf缺省值，偏小
		if(weight==null)
			weight=0.05f;//pos最低权值
		
		idf=(float)Math.log(idf)/(float)Math.log(10);
		
		this.name = name;
		this.score = idf*weight;
		this.weight=weight;
		this.idf = idf;
		this.cx=cx;
		freq=1;;
	}

	
	public void updateScore() {
		this.score += weight * idf;
		freq++;
	}

	public int getFreq() {
		return freq;
	}

	@Override
	public int compareTo(Keyword o) {
		if (this.score < o.score) {
			return 1;
		} else {
			return -1;
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Keyword) {
			Keyword k = (Keyword) obj;
			return k.name.equals(name);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return name + "\t" + score+"\r\n";//+"/"+cx+"/"+freq+"/"+weight+"/"+idf+"\r\n";// "="+score+":"+freq+":"+idf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}



}
