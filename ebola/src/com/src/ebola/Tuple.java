package com.src.ebola;

public class Tuple {
	private int termFreq;
	private boolean relevance;
	
	public Tuple(int termFreq, boolean relevance) {
		this.termFreq = termFreq;
		this.relevance = relevance;
	}


	public int getTermFreq() {
		return termFreq;
	}

	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
	}

	public boolean isRelevance() {
		return relevance;
	}

	public void setRelevance(boolean relevance) {
		this.relevance = relevance;
	}

}
