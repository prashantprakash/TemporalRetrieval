package com.src.ebola;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.src.ebola.EbolaIndexerModify.DocumentNode;
import com.src.ebola.EbolaIndexerModify.TermNode;

public class FileUtils {

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

	@SuppressWarnings("rawtypes")
	public static void writeMap(String fileName, Map map) throws Exception {

		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(map);
		oos.close();
	}

	@SuppressWarnings("unchecked")
	public static void readMaps() throws Exception {
		FileInputStream fis = new FileInputStream("documentNameIdMap.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		documentNameIdMap = (HashMap<String, Integer>) ois.readObject();
		ois.close();

		FileInputStream fis1 = new FileInputStream("docTokensMap.ser");
		ObjectInputStream ois1 = new ObjectInputStream(fis1);
		docTokensMap = (HashMap<Integer, HashMap<String, Integer>>) ois1.readObject();
		ois1.close();

		FileInputStream fis2 = new FileInputStream("documentIdMap.ser");
		ObjectInputStream ois2 = new ObjectInputStream(fis2);
		documentIdMap = (HashMap<Integer, DocumentNode>) ois2.readObject();
		ois2.close();

		FileInputStream fis3 = new FileInputStream("stemmerIndex.ser");
		ObjectInputStream ois3 = new ObjectInputStream(fis3);
		stemmerIndex = (HashMap<TermNode, TreeMap<Integer, Integer>>) ois3.readObject();
		ois3.close();

		FileInputStream fis4 = new FileInputStream("RF1StemmerIndex.ser");
		ObjectInputStream ois4 = new ObjectInputStream(fis4);
		RF1StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois4.readObject();
		ois4.close();

		FileInputStream fis5 = new FileInputStream("RF2StemmerIndex.ser");
		ObjectInputStream ois5 = new ObjectInputStream(fis5);
		RF2StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois5.readObject();
		ois5.close();

		FileInputStream fis6 = new FileInputStream("RF3StemmerIndex.ser");
		ObjectInputStream ois6 = new ObjectInputStream(fis6);
		RF3StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois6.readObject();
		ois6.close();

		FileInputStream fis7 = new FileInputStream("RF4StemmerIndex.ser");
		ObjectInputStream ois7 = new ObjectInputStream(fis7);
		RF4StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois7.readObject();
		ois7.close();

		FileInputStream fis8 = new FileInputStream("RF5StemmerIndex.ser");
		ObjectInputStream ois8 = new ObjectInputStream(fis8);
		RF5StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois8.readObject();
		ois8.close();

		FileInputStream fis9 = new FileInputStream("RF6StemmerIndex.ser");
		ObjectInputStream ois9 = new ObjectInputStream(fis9);
		RF6StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois9.readObject();
		ois9.close();

		FileInputStream fis10 = new FileInputStream("RF7StemmerIndex.ser");
		ObjectInputStream ois10 = new ObjectInputStream(fis10);
		RF7StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois10.readObject();
		ois10.close();

		FileInputStream fis11 = new FileInputStream("RF8StemmerIndex.ser");
		ObjectInputStream ois11 = new ObjectInputStream(fis11);
		RF8StemmerIndex = (HashMap<String, TreeMap<Integer, Integer>>) ois11.readObject();
		ois11.close();

	}

	/**
	 * This method gets the number of times a term occur in all the documents.
	 * 
	 * @param term
	 * @return
	 */
	public Tuple termFreqInDoc(String term, int docId) {
		TreeMap<Integer, Integer> postings = null;
		postings = RF1StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF2StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF3StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF4StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF5StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF6StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF7StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = RF8StemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), true);
			}
		}
		postings = stemmerIndex.get(term);
		if (postings != null) {
			if (postings.containsKey(docId)) {
				return new Tuple(postings.get(docId), false);
			}
		}
		return null;

	}

	/**
	 * This method gets the collection size
	 * 
	 * @param
	 * @return
	 */

	public int getCollectionSize() {
		return documentNameIdMap.keySet().size();
	}

	/**
	 * This method gets the document Frequency
	 * 
	 * @param
	 * @return
	 */

	// doubt can a term belongs to two different posting list
	public int getDocumentFrequency(String term) {
		if (RF1StemmerIndex.containsKey(term)) {
			return RF1StemmerIndex.get(term).size();
		} else if (RF2StemmerIndex.containsKey(term)) {
			return RF2StemmerIndex.get(term).size();
		} else if (RF3StemmerIndex.containsKey(term)) {
			return RF3StemmerIndex.get(term).size();
		} else if (RF4StemmerIndex.containsKey(term)) {
			return RF4StemmerIndex.get(term).size();
		} else if (RF5StemmerIndex.containsKey(term)) {
			return RF5StemmerIndex.get(term).size();
		} else if (RF6StemmerIndex.containsKey(term)) {
			return RF6StemmerIndex.get(term).size();
		} else if (RF7StemmerIndex.containsKey(term)) {
			return RF7StemmerIndex.get(term).size();
		} else if (RF8StemmerIndex.containsKey(term)) {
			return RF8StemmerIndex.get(term).size();
		} else if (stemmerIndex.containsKey(term)) {
			return stemmerIndex.get(term).size();
		}
		return 0;
	}
}
