package com.src.ebola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EbolaIndexerModify {
	public static PorterStemmer stemmer = new PorterStemmer();

	// Document Name to Id map for creating RF indexes
	public static HashMap<Integer, String> documentURLIdMap = new HashMap<Integer, String>();

	// Document Name to Id map for creating RF indexes
	public static HashMap<String, Integer> documentNameIdMap = new HashMap<String, Integer>();

	// Map storing document along with all its tokens and frequency
	public static HashMap<Integer, HashMap<String, Integer>> docTokensMap = new HashMap<Integer, HashMap<String, Integer>>();

	// Map storing Document Node Against Doc IDs
	public static HashMap<Integer, DocumentNode> documentIdMap = new HashMap<Integer, DocumentNode>();
	// stemmer index with term node as key, and HashMap object with DocumentId
	// as key and the TF as value
	public static HashMap<TermNode, TreeMap<Integer, Integer>> stemmerIndex = new HashMap<TermNode, TreeMap<Integer, Integer>>();

	// special index for RF1 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF1StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF2 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF2StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF3 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF3StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF4 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF4StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF5 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF5StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF6 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF6StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF7 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF7StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// special index for RF8 terms
	public static HashMap<String, TreeMap<Integer, Integer>> RF8StemmerIndex = new HashMap<String, TreeMap<Integer, Integer>>();

	// loaded annotations CSV Data Structure

	public static HashMap<Integer, HashMap<String, String>> annonationCSV = new HashMap<Integer, HashMap<String, String>>();

	public static HashSet<String> stopWordsStems = new HashSet<String>();
	public static HashSet<String> stopWords = new HashSet<String>();

	public static String docPrefix = "cranfield";
	public static HashMap<String, String> URLCSV = new HashMap<String, String>();

	public static void persistMaps() {
		try {

			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\documentNameIdMap.ser",
			// documentNameIdMap);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\docTokensMap.ser",
			// docTokensMap);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\documentIdMap.ser",
			// documentIdMap);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\stemmerIndex.ser",
			// stemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF1StemmerIndex.ser",
			// RF1StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF2StemmerIndex.ser",
			// RF2StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF3StemmerIndex.ser",
			// RF3StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF4StemmerIndex.ser",
			// RF4StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF5StemmerIndex.ser",
			// RF5StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF6StemmerIndex.ser",
			// RF6StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF7StemmerIndex.ser",
			// RF7StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RF8StemmerIndex.ser",
			// RF8StemmerIndex);
			// FileUtils.writeMap("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\RfUtility.RFValueMap.ser",
			// RfUtility.RFValueMap);
			FileUtils.writeMap(Location.location + "documentNameIdMap.ser", documentNameIdMap);
			FileUtils.writeMap(Location.location + "docTokensMap.ser", docTokensMap);
			FileUtils.writeMap(Location.location + "documentIdMap.ser", documentIdMap);
			FileUtils.writeMap(Location.location + "stemmerIndex.ser", stemmerIndex);
			FileUtils.writeMap(Location.location + "RF1StemmerIndex.ser", RF1StemmerIndex);
			FileUtils.writeMap(Location.location + "RF2StemmerIndex.ser", RF2StemmerIndex);
			FileUtils.writeMap(Location.location + "RF3StemmerIndex.ser", RF3StemmerIndex);
			FileUtils.writeMap(Location.location + "RF4StemmerIndex.ser", RF4StemmerIndex);
			FileUtils.writeMap(Location.location + "RF5StemmerIndex.ser", RF5StemmerIndex);
			FileUtils.writeMap(Location.location + "RF6StemmerIndex.ser", RF6StemmerIndex);
			FileUtils.writeMap(Location.location + "RF7StemmerIndex.ser", RF7StemmerIndex);
			FileUtils.writeMap(Location.location + "RF8StemmerIndex.ser", RF8StemmerIndex);
			FileUtils.writeMap(Location.location + "RfUtility.RFValueMap.ser", RfUtility.RFValueMap);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void launch() {
		long startIndexerTime = System.currentTimeMillis();

		loadStopWords();
		RfUtility.initialize();

		// File cranfieldFolder = new
		// File("E:\\IR\\indexer\\indexer\\corpus\\data");
		File cranfieldFolder = new File(Location.location + "data");

		// String annotationCSVPath =
		// "E:\\IR\\indexer\\indexer\\corpus\\annotations.csv";
		String annotationCSVPath = Location.location + "annotations.csv";
		// String urlsPath="E:\\IR\\indexer\\indexer\\corpus\\urls.csv";
		String urlsPath = Location.location + "urls.csv";

		System.out.println("Running Indexer ");
		try {
			createInvertedIndex(cranfieldFolder);

			// listDfTfLengthInBytes();
			parseAnnonationCSV(annotationCSVPath);
			createRFIndices();
			long endIndexerTime = System.currentTimeMillis();
			long elapsedTime = (long) (endIndexerTime - startIndexerTime);

			System.out.println("Time taken for index creation is :" + (elapsedTime) + " milliseconds");
			System.out.println(RfUtility.RFValueMap);
			persistMaps();
			FileUtils.readMaps();
			parseURLSCSV(urlsPath);
			mapCSVIDToURL();
			FileUtils.documentURLIdMap = documentURLIdMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void parseURLSCSV(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = "";
		// load attributes
		line = reader.readLine();

		while ((line = reader.readLine()) != null) {

			// use comma as separator
			String[] row = line.split(splitter);

			URLCSV.put(row[0], row[1]);

		}
		reader.close();

	}

	public static void mapCSVIDToURL() {
		ArrayList<String> errorFields = new ArrayList<String>();
		for (String docName : documentNameIdMap.keySet()) {

			String[] array = docName.split("\\.");
			String urlId = array[0];
			String URL = URLCSV.get(urlId);
			if (URL == null) {
				errorFields.add(urlId);
				continue;
			}

			documentURLIdMap.put(documentNameIdMap.get(docName), URL);

		}

		System.out.println(errorFields);
		System.out.println(errorFields);
	}

	public static String splitter = ",";

	public static void parseAnnonationCSV(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = "";
		int rowCount = 1;
		// load attributes
		line = reader.readLine();
		String[] attributesArray = line.split(splitter);

		while ((line = reader.readLine()) != null) {

			// use comma as separator
			String[] row = line.split(splitter);
			HashMap<String, String> rowMap = new HashMap<String, String>();
			for (int i = 0; i < row.length; i++) {
				rowMap.put(attributesArray[i], row[i]);
			}
			annonationCSV.put(rowCount, rowMap);
			rowCount++;
		}
		reader.close();

	}

	public static void createRFIndices() {
		ArrayList<String> defectiveRows = new ArrayList<String>();
		ArrayList<String> defectiveFileNames = new ArrayList<String>();
		ArrayList<String> defectiveStemFileNames = new ArrayList<String>();
		ArrayList<String> errorTerm = new ArrayList<String>();

		for (Integer rowId : annonationCSV.keySet()) {

			HashMap<String, String> rowMap = annonationCSV.get(rowId);
			String URLNO = rowMap.get("URLNO");
			String PNO = rowMap.get("PNO");
			String RFN = rowMap.get("RFN");
			String VALUE = rowMap.get("VALUE");

			if (VALUE == null) {
				defectiveRows.add(rowId.toString());
				continue;

			}

			// remove the special symbols other than -
			VALUE = VALUE.replaceAll("[+:^,'?;=%&#~`$!*@_)(}{]", "");
			// remove possessives 's
			VALUE = VALUE.replaceAll("'s", "");
			// remove full stops and acronyms
			VALUE = VALUE.replaceAll("\\.", "");
			// remove - and replace it with space
			VALUE = VALUE.replaceAll("-", "\t");
			// remove numbers
			String[] values = VALUE.split("\\s+");

			String fileName = URLNO + "." + PNO + ".txt";
			Integer docId = documentNameIdMap.get(fileName);

			for (int i = 0; i < values.length; i++) {
				if (stopWordsStems.contains(values[i]))
					continue;

				if (values[i].trim().isEmpty())
					continue;

				if (docId == null) {
					defectiveFileNames.add(fileName);
					continue;
				}
				HashMap<String, Integer> stemFrequency = docTokensMap.get(docId);

				int tf = 0;
				String stem = getStem(values[i].toLowerCase());
				RfUtility.addValuesForRF(RFN, stem);
				DocumentNode docNode = documentIdMap.get(docId);
				incrementRfForDoc(docNode, stem);
				if (stemFrequency.keySet().contains(stem))
					tf = stemFrequency.get(stem);
				else {
					defectiveStemFileNames.add(fileName);
					errorTerm.add(stem);
				}

				if (RFN.trim().equals("1")) {
					insertIntoRFIndex(RF1StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("2")) {
					insertIntoRFIndex(RF2StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("3")) {
					insertIntoRFIndex(RF3StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("4")) {
					insertIntoRFIndex(RF4StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("5")) {
					insertIntoRFIndex(RF5StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("6")) {
					insertIntoRFIndex(RF6StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("7")) {
					insertIntoRFIndex(RF7StemmerIndex, tf, values[i], docId);
				} else if (RFN.trim().equals("8")) {
					insertIntoRFIndex(RF8StemmerIndex, tf, values[i], docId);
				}

			}

		}

		System.out.println(defectiveFileNames);
		System.out.println(defectiveRows);
		System.out.println(defectiveStemFileNames);
		System.out.println(errorTerm);

	}

	public static void insertIntoRFIndex(HashMap<String, TreeMap<Integer, Integer>> rfIndex, int tf, String term,
			int docId) {
		TreeMap<Integer, Integer> postingList = rfIndex.get(term);
		if (postingList == null) {
			postingList = new TreeMap<Integer, Integer>();
			postingList.put(docId, tf);
			rfIndex.put(term, postingList);
		} else {

			postingList.put(docId, tf);

		}

	}

	public static String getStem(String token) {
		String stemsPart1 = stemmer.step1(token);
		String stemsPart2 = stemmer.step2(stemsPart1);
		String stemsPart3 = stemmer.step3(stemsPart2);
		String stemsPart4 = stemmer.step4(stemsPart3);
		String stemsPart5 = stemmer.step5(stemsPart4);

		return stemsPart5;
	}

	public static void createInvertedIndex(File cranfieldFolder) throws IOException {

		String files[] = cranfieldFolder.list();

		int docId = 1;

		for (String file : files) {

			File newFile = new File(cranfieldFolder, file);

			String cranFieldWords = getDocumentText(newFile);

			StringTokenizer tokens = new StringTokenizer(cranFieldWords.toLowerCase().toString());
			DocumentNode docNode = new DocumentNode(docId, file, tokens.countTokens());

			HashMap<String, Integer> stemFrequency = processStemmerIndex(tokens, docNode);
			documentIdMap.put(docId, docNode);
			documentNameIdMap.put(docNode.getFileName(), docId);
			docTokensMap.put(docId, stemFrequency);

			docId++;

		}

	}

	public static void loadStopWords() {
		/*
		 * String stopWordsText = "a,all,an,and,any,are,as,be,been,but,by,few,"
		 * + "for,have,he,her,here,him,his,how,i,in,is,it,its,many,me,my," +
		 * "none,of,on,or,our,she,some,the,their,them,there,they,that," +
		 * "this,us,was,what,when,where,which,who,why,will,with,you,you"; String
		 * stopWordsArray[] = stopWordsText.split(","); for (int i = 0; i <
		 * stopWordsArray.length; i++) { stopWords.add(stopWordsArray[i]);
		 * 
		 * }
		 */

		List<String> stopw = Arrays.asList("a", "a's", "able", "about", "above", "according", "accordingly", "across",
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

		for (String ar : stopw) {

			stopWords.add(ar);
			stopWordsStems.add(ar);
		}

	}

	public static String getDocumentText(File newFile) throws IOException {
		StringBuffer cranFieldText = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(newFile));
		String line;
		while ((line = reader.readLine()) != null) {
			cranFieldText.append("\t" + line);

		}

		// remove all tags and special characters
		String cranFieldWords;

		// remove SGML Tags and replace it with space
		cranFieldWords = cranFieldText.toString().replaceAll("\\<.*?>", "\t");
		// remove the special symbols other than -
		cranFieldWords = cranFieldWords.replaceAll("[+:^,'?;=%&#~`$!*@_)(}{]", "");
		// remove possessives 's
		cranFieldWords = cranFieldWords.replaceAll("'s", "");
		// remove full stops and acronyms
		cranFieldWords = cranFieldWords.replaceAll("\\.", "");
		// remove - and replace it with space
		cranFieldWords = cranFieldWords.replaceAll("-", "\t");
		// remove numbers
		// cranFieldWords = cranFieldWords.replaceAll("[0-9]+", "");
		reader.close();
		return cranFieldWords;

	}

	private static void incrementRfForDoc(DocumentNode docNode, String token) {
		Integer rfId = RfUtility.getRFForValue(token);

		if (rfId == 1)
			docNode.incrementX1();
		else if (rfId == 2)
			docNode.incrementX2();
		else if (rfId == 3)
			docNode.incrementX3();
		else if (rfId == 4)
			docNode.incrementX4();
		else if (rfId == 5)
			docNode.incrementX5();
		else if (rfId == 6)
			docNode.incrementX6();
		else if (rfId == 7)
			docNode.incrementX7();
		else if (rfId == 8)
			docNode.incrementX8();

	}

	private static HashMap<String, Integer> processStemmerIndex(StringTokenizer tokens, DocumentNode docNode) {
		String patt = "[a-zA-Z0-9]*";
		// local Map to store stems and their frequency count in current
		// document
		HashMap<String, Integer> stemFrequency = new HashMap<String, Integer>();
		/**
		 * create inverted index using stems-----Index 1 creation
		 */

		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token.matches(patt)) {
				String stem = getStem(token);

				if (stopWordsStems.contains(stem))
					continue;

				// local map creation
				Integer count = stemFrequency.get(stem);
				if (count == null) {
					stemFrequency.put(stem, 1);
				} else {

					count++;
					stemFrequency.put(stem, count);
				}

				// fetch term node from stemmer index if it exists else add a
				// new term node to stemmer index
				TermNode termNode = null;
				for (TermNode node : stemmerIndex.keySet()) {

					if (node.term.equals(stem)) {
						termNode = node;
						break;
					}

				}
				// if term node does not exist create a new term node and
				// posting list map and add to the index
				if (termNode == null) {
					termNode = new TermNode(stem);
					termNode.incrementTotalCount();
					TreeMap<Integer, Integer> postingListMap = new TreeMap<Integer, Integer>();
					postingListMap.put(docNode.getDocId(), 1);
					stemmerIndex.put(termNode, postingListMap);
				} else {
					termNode.incrementTotalCount();
					TreeMap<Integer, Integer> postingListMap = stemmerIndex.get(termNode);
					// if the current document is already added to the posting
					// list map of this term, simply increase the tf in the map
					if (postingListMap.keySet().contains(docNode.getDocId())) {

						Integer newCount = postingListMap.get(docNode.getDocId());
						newCount++;
						postingListMap.put(docNode.getDocId(), newCount);
					} else {
						// if the current document is NOT already added to the
						// posting list map of this term, simply increase the tf
						// in the map to 1 for this document
						postingListMap.put(docNode.getDocId(), 1);

					}

				}

			}
		}

		topFrequentWord(docNode, stemFrequency);
		return stemFrequency;

	}

	private static void topFrequentWord(DocumentNode docNode, HashMap<String, Integer> dictionary) {
		Sorter sorter = new Sorter(dictionary);
		TreeMap<String, Integer> tokensTreeMap = new TreeMap<String, Integer>(sorter);
		int count = 0;
		tokensTreeMap.putAll(dictionary);
		for (String key : tokensTreeMap.descendingMap().keySet()) {
			count++;
			if (count > 1)
				break;
			int frequency = dictionary.get(key);

			docNode.setMaxtf(frequency);
			docNode.setMaxTfTerm(key);
		}

	}

	static class Sorter implements Comparator<String> {

		Map<String, Integer> tokenMap;

		Sorter(Map<String, Integer> base) {
			this.tokenMap = base;
		}

		@Override
		public int compare(String strOne, String strTwo) {
			if (tokenMap.get(strOne) >= tokenMap.get(strTwo)) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	static class TermNode implements Serializable {

		public String term;
		public int totalCount = 0;

		public void incrementTotalCount() {
			totalCount++;
		}

		public TermNode(TermNode termNode) {
			this.term = termNode.term;
			this.totalCount = termNode.totalCount;
		}

		public TermNode(String term) {
			this.term = term;
			// TODO Auto-generated constructor stub
		}

		public String getTerm() {
			return term;
		}

		public int getTotalCount() {
			return totalCount;
		}

		@Override
		public boolean equals(Object obj) {
			TermNode node = (TermNode) obj;
			if (this.term.equals(node.term)) {
				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			// two randomly chosen prime numbers
			// if deriving: appendSuper(super.hashCode())
			return new HashCodeBuilder(17, 31).append(term).toHashCode();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return term;
		}
	}

	static class DocumentNode implements Serializable {

		String fileName;
		String maxTfTerm = "";

		public int maxtf = 0;
		public int docLen = 0;
		public int DocId;

		// variable to store count of X1 RF_Factor
		public int x1 = 0;
		// variable to store count of X2 RF_Factor
		public int x2 = 0;

		// variable to store count of X3 RF_Factor
		public int x3 = 0;

		// variable to store count of X4 RF_Factor
		public int x4 = 0;

		// variable to store count of X5 RF_Factor
		public int x5 = 0;

		// variable to store count of X6 RF_Factor
		public int x6 = 0;

		public String getFileName() {
			return fileName;
		}

		public int getMaxtf() {
			return maxtf;
		}

		public int getDocLen() {
			return docLen;
		}

		public int getDocId() {
			return DocId;
		}

		public int getX1() {
			return x1;
		}

		public int getX2() {
			return x2;
		}

		public int getX3() {
			return x3;
		}

		public int getX4() {
			return x4;
		}

		public int getX5() {
			return x5;
		}

		public int getX6() {
			return x6;
		}

		public int getX7() {
			return x7;
		}

		public int getX8() {
			return x8;
		}

		// variable to store count of X7 RF_Factor
		public int x7 = 0;
		// variable to store count of X8 RF_Factor
		public int x8 = 0;

		public void incrementX1() {
			x1++;
		}

		public void incrementX2() {
			x2++;
		}

		public void incrementX3() {
			x3++;
		}

		public void incrementX4() {
			x4++;
		}

		public void incrementX5() {
			x5++;
		}

		public void incrementX6() {
			x6++;
		}

		public void incrementX7() {
			x7++;
		}

		public void incrementX8() {
			x8++;
		}

		public String getMaxTfTerm() {
			return maxTfTerm;
		}

		public void setMaxTfTerm(String maxTfTerm) {
			this.maxTfTerm = maxTfTerm;
		}

		public void setMaxtf(int maxtf) {
			this.maxtf = maxtf;
		}

		public DocumentNode(int DocId, String fileName, int docLen) {
			this.DocId = DocId;
			this.fileName = fileName;
			this.docLen = docLen;
		}

		@Override
		public boolean equals(Object obj) {
			DocumentNode node = (DocumentNode) obj;
			if (this.DocId == node.DocId)
				return true;

			return false;
		}

		@Override
		public int hashCode() {
			// two randomly chosen prime numbers
			// if deriving: appendSuper(super.hashCode())
			return new HashCodeBuilder(17, 31).append(DocId).toHashCode();
		}

		@Override
		public String toString() {

			return (new Integer(DocId)).toString();
		}

	}
}
