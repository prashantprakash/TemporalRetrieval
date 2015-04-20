package com.src.ebola;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Util {
	/**
	 * This method takes a raw token and process it and returns a list of
	 * processed tokens.
	 * 
	 * @param rawToken
	 *            a String type.
	 * @return ArrayList of type String.
	 */
	public static ArrayList<String> processToken(String rawToken) {
		ArrayList<String> processedTokens = new ArrayList<String>();
		ArrayList<String> processedTokensTemp = new ArrayList<String>();

		// splitting tokens if it contains "-"
		if (rawToken.contains("-")) {
			for (String hypenRemovedString : processHypens(rawToken)) {
				processedTokens.add(hypenRemovedString.toLowerCase());
			}
		} else {
			processedTokens.add(rawToken.toLowerCase());
		}

		// removing "." from abbreviations like "U.N."
		for (String token : processedTokens) {
			if (token.contains(".") && token.length() != 1)
				processedTokensTemp.add(token.replaceAll("\\.", ""));
			else if (!token.equals("."))
				processedTokensTemp.add(token);
		}
		processedTokens.clear();

		// removing "'s" from the end of the tokens
		for (String token : processedTokensTemp) {
			if (token.endsWith("'s"))
				processedTokens.add(token.substring(0, token.length() - 2));
			else {
				String freshToken = token.replace("'", "");
				if (freshToken.length() > 0)
					processedTokens.add(freshToken);
			}

		}
		processedTokensTemp.clear();
		return processedTokens;
	}

	/**
	 * This method splits a string by hyphens in it and returns the list of
	 * strings.
	 * 
	 * @param rawToken
	 *            a String type
	 * @return List of type String.
	 */
	private static List<String> processHypens(String rawToken) {
		return Arrays.asList((rawToken.split("-")));
	}

	/**
	 * This method do stemming on each term of the map and constructs a new map
	 * with stemmed tokens.
	 * 
	 * @param tokensFromFile
	 * @return
	 */
	public static Map<String, Integer> getStemmedTokens(Map<String, Integer> tokensFromFile) {
		PorterStemmer stemmer = new PorterStemmer();
		Map<String, Integer> stemmedTokens = new HashMap<String, Integer>();
		for (String token : tokensFromFile.keySet()) {
			String stemsPart1 = stemmer.step1(token);
			String stemsPart2 = stemmer.step2(stemsPart1);
			String stemsPart3 = stemmer.step3(stemsPart2);
			String stemsPart4 = stemmer.step4(stemsPart3);
			String stemsPart5 = stemmer.step5(stemsPart4);

			String stemmedToken = stemsPart5.toString();
			if (stemmedTokens.get(stemmedToken) == null)
				stemmedTokens.put(stemmedToken, tokensFromFile.get(token));
			else
				stemmedTokens.put(stemmedToken, stemmedTokens.get(stemmedToken) + tokensFromFile.get(token));
		}
		return stemmedTokens;
	}

	/**
	 * This method sorts the map in the descending order of values and returns a
	 * sorted map.
	 * 
	 * @param map
	 *            a HashMap type
	 * @return Map type
	 */
	public static Map<Integer, Double> sortByValue(Map<Integer, Double> map) {
		// List of entries of map
		List<Entry<Integer, Double>> list = new LinkedList<Entry<Integer, Double>>(map.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, Double>>() {
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// creating a sorted map.
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Entry<Integer, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
