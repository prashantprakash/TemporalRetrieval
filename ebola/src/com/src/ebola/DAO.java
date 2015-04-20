package com.src.ebola;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DAO {
	private final String indexFilesDir = "/WEB-INF/classes/";
	/**
	 * This map stores the tokens in the query and its occurrence.
	 */
	private Map<String, Integer> queryIndex;

	/**
	 * This stores the score of the document-query (this query) pair using
	 * weighing function 1.
	 */
	private Map<Integer, Double> docRankingByW1;

	/**
	 * This is the constructor to initialize all the member variables./
	 */
	public DAO() {
		this.queryIndex = new TreeMap<String, Integer>();
		this.docRankingByW1 = new TreeMap<Integer, Double>();
	}

	public List<String> getQueries() {
		List<String> queries = new ArrayList<String>();
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\"
					+ "corpus\\queries.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				queries.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return queries;
	}

	/**
	 * Method create and index for this query.
	 * 
	 * @param query
	 * @param stopWords
	 */
	public void indexQuery(String query) {
		String[] tokens = query.replaceAll("[^a-zA-Z0-9'.]", " ").replaceAll("\\s+", " ").trim().split(" ");
		ArrayList<String> listOfTokensInQuery = new ArrayList<String>();
		for (String rawToken : tokens) {
			if (rawToken.length() > 0)
				listOfTokensInQuery.add(rawToken.trim());
		}
		Map<String, Integer> tokenMap = new HashMap<String, Integer>();
		for (String rawToken : listOfTokensInQuery) {
			ArrayList<String> processedTokens = Util.processToken(rawToken);
			if (processedTokens != null) {
				for (String processedToken : processedTokens) {
					if (tokenMap.get(processedToken) == null)
						tokenMap.put(processedToken, 1);
					else
						tokenMap.put(processedToken, tokenMap.get(processedToken) + 1);

				}
			}
		}
		this.queryIndex = Util.getStemmedTokens(tokenMap);
	}

	/**
	 * This method calculates the score of this query with all the documents and
	 * sort the score map in the reverse order of value.
	 * 
	 * @param index
	 */
	public void generateRaking() {

		for (Integer docid : FileUtils.documentIdMap.keySet()) {
			calculateByW1(docid);
		}
		this.docRankingByW1 = Util.sortByValue(docRankingByW1);
	}

	/**
	 * This method uses weighing function 1 to calculate the score.
	 * 
	 * @param docId
	 * @param index
	 */
	private void calculateByW1(int docId) {
		double w1 = 0.0d;
		FileUtils fileUtils = new FileUtils();
		try{
		FileUtils.readMaps();
		} catch(Exception ex) {
			System.err.println("error in reading map");
		}
		int maxF = FileUtils.documentIdMap.get(docId).maxtf;
		int cs = fileUtils.getCollectionSize();
		for (String queryToken : this.queryIndex.keySet()) {
			Tuple tuple = fileUtils.termFreqInDoc(queryToken, docId);
			int df = fileUtils.getDocumentFrequency(queryToken);
			if (tuple != null) {
				int tf = tuple.getTermFreq();
				if (tuple.isRelevance()) {
					double w = (2.0d + 0.4d + 0.6d * Math.log(tf + 0.5d) / Math.log(maxF + 1.0d))
							* (Math.log((double) cs / (double) df) / Math.log(cs));
					w1 += w;
				} else {
					double w = (0.4d + 0.6d * Math.log(tf + 0.5d) / Math.log(maxF + 1.0d))
							* (Math.log((double) cs / (double) df) / Math.log(cs));
					w1 += w;
				}
			}
		}
		this.docRankingByW1.put(docId, w1);
	}

	/**
	 * This method returns the score of this query with each document calculated
	 * using weighing function 1.
	 * 
	 * @return
	 */
	public Map<Integer, Double> getRankingByW1() {
		return this.docRankingByW1;
	}

}
