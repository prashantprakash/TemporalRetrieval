package com.src.ebola;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

	public HashMap<String, Integer> documentNameIdMap;
	
	private static List<String> stopWords = new ArrayList<String>();

	/**
	 * This is the constructor to initialize all the member variables./
	 */
	public DAO() {
		this.queryIndex = new TreeMap<String, Integer>();
		this.docRankingByW1 = new TreeMap<Integer, Double>();
		listStopWords();
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
			if (rawToken.length() > 0 && ! stopWords.contains(rawToken))
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
		FileUtils fileUtils = new FileUtils();
		try {
			FileUtils.readMaps();
		} catch (Exception ex) {
			System.err.println("error in reading map");
		}
		for (Integer docid : FileUtils.documentIdMap.keySet()) {
			calculateByW1(docid, fileUtils);
		}
		this.docRankingByW1 = Util.sortByValue(docRankingByW1);
		documentNameIdMap = fileUtils.documentNameIdMap;

	}

	/**
	 * This method uses weighing function 1 to calculate the score.
	 * 
	 * @param docId
	 * @param index
	 */
	private void calculateByW1(int docId, FileUtils fileUtils) {
		double w1 = 0.0d;

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

	public Map<String, Integer> getDocumentNameMap() {
		return this.documentNameIdMap;
	}

	
	private void listStopWords() {
		stopWords = Arrays.asList("a", "a's", "able", "about", "above", "according", "accordingly", "across",
				"actually", "after", "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost",
				"alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and",
				"another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart",
				"appear", "appreciate", "appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking",
				"associated", "at", "available", "away", "awfully", "b", "be", "became", "because", "become",
				"becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside",
				"besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "c", "c'mon", "c's",
				"came", "can", "can't", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes",
				"clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering",
				"contain", "containing", "contains", "corresponding", "could", "couldn't", "course", "currently", "d",
				"definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't", "doing",
				"don't", "done", "down", "downwards", "during", "e", "each", "edu", "eg", "eight", "either", "else",
				"elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody",
				"everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "f", "far", "few",
				"fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth",
				"four", "from", "further", "furthermore", "g", "get", "gets", "getting", "given", "gives", "go",
				"goes", "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn't", "happens", "hardly",
				"has", "hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence", "her", "here",
				"here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself",
				"his", "hither", "hopefully", "how", "howbeit", "however", "i", "i'd", "i'll", "i'm", "i've", "ie",
				"if", "ignored", "immediate", "in", "inasmuch", "inc", "inc.", "indeed", "indicate", "indicated",
				"indicates", "inner", "insofar", "instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll",
				"it's", "its", "itself", "j", "just", "k", "keep", "keeps", "kept", "know", "knows", "known", "l",
				"last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like",
				"liked", "likely", "little", "look", "looking", "looks", "ltd", "m", "mainly", "many", "may", "maybe",
				"me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must",
				"my", "myself", "n", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither",
				"never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor",
				"normally", "not", "nothing", "novel", "now", "nowhere", "o", "obviously", "of", "off", "often", "oh",
				"ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise",
				"ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "p", "particular",
				"particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably",
				"provides", "q", "que", "quite", "qv", "r", "rather", "rd", "re", "really", "reasonably", "regarding",
				"regardless", "regards", "relatively", "respectively", "right", "s", "said", "same", "saw", "say",
				"saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen",
				"self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she",
				"should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something",
				"sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify",
				"specifying", "still", "sub", "such", "sup", "sure", "t", "t's", "take", "taken", "tell", "tends",
				"th", "than", "thank", "thanks", "thanx", "that", "that's", "thats", "the", "their", "theirs", "them",
				"themselves", "then", "thence", "there", "there's", "thereafter", "thereby", "therefore", "therein",
				"theres", "thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "think", "third",
				"this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus",
				"to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying",
				"twice", "two", "u", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up",
				"upon", "us", "use", "used", "useful", "uses", "using", "usually", "uucp", "v", "value", "various",
				"very", "via", "viz", "vs", "w", "want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll",
				"we're", "we've", "welcome", "well", "went", "were", "weren't", "what", "what's", "whatever", "when",
				"whence", "whenever", "where", "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon",
				"wherever", "whether", "which", "while", "whither", "who", "who's", "whoever", "whole", "whom",
				"whose", "why", "will", "willing", "wish", "with", "within", "without", "won't", "wonder", "would",
				"would", "wouldn't", "x", "y", "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your",
				"yours", "yourself", "yourselves", "z", "zero");
	}

}
