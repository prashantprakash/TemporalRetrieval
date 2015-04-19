package com.src.ebola;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DAO {
	private final String indexFilesDir = "/WEB-INF/classes/";
	public List<String> getQueries() {
		List<String> queries = new ArrayList<String>();
		BufferedReader br = null;
		 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("E:\\UTDHackethon\\UTD_Hack\\UTDHackWorkSpace\\ebola\\"
					+ "corpus\\queries.txt"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				queries.add(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return queries;
	}

}
