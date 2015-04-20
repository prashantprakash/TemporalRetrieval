package com.src.ebola;

import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class RfUtility
{

    public static HashMap<Integer, HashSet<String>> RFValueMap = new HashMap<Integer, HashSet<String>>();
    public static PorterStemmer stemmer = new PorterStemmer();

    public static String getStem(String token)
    {
	String stemsPart1 = stemmer.step1(token);
	String stemsPart2 = stemmer.step2(stemsPart1);
	String stemsPart3 = stemmer.step3(stemsPart2);
	String stemsPart4 = stemmer.step4(stemsPart3);
	String stemsPart5 = stemmer.step5(stemsPart4);

	return stemsPart5;
    }

    public static void initialize()
    {
	for (int i = 1; i <= 8; i++)
	{
	    HashSet<String> RFValues = new HashSet<String>();
	    RFValueMap.put(i, RFValues);
	}
    }

    public static void addValuesForRF(String RFN, String stem)
    {
	HashSet<String> RFValues = RFValueMap.get(Integer.parseInt(RFN));
	RFValues.add(stem);

    }

    public static Integer getRFForValue(String token)
    {

	for (Integer rfId : RFValueMap.keySet())
	{

	    HashSet<String> values = RFValueMap.get(rfId);

	    if (values.contains(token))
	    {
		return rfId;
	    }

	}

	return 0;
    }

}
