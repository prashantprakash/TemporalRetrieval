package com.src.ebola;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class Query
 */
@WebServlet("/query")
public class Query extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Query() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String choice = request.getParameter("choice");
		DAO dao = new DAO();
		if ("getquery".equalsIgnoreCase(choice)) {
			List<String> queries = dao.getQueries();
			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().println(mapper.writeValueAsString(queries));

		} else if ("getresults".equalsIgnoreCase(choice)) {
			String query = request.getParameter("query");
			dao.indexQuery(query);
			dao.generateRaking();
			Map<Integer, Double> ranking = dao.getRankingByW1();
			Map<String, Integer> docNameMap = dao.getDocumentNameMap();
			Map<Integer, String> urlMap = dao.getUrlMap();
			ArrayList<Result> results = new ArrayList<Result>();
			for (Entry<Integer, Double> entry : ranking.entrySet()) {
				if (entry.getValue() > 0.0d) {
					results.add(new Result(getName(entry.getKey(), docNameMap), Util.getDocContent(getName(
							entry.getKey(), docNameMap)), entry.getValue(), urlMap.get(entry.getKey())));
					System.out.println(entry.getKey());
				}
			}
			// this.noOfResult = this.results.size();
			ObjectMapper mapper = new ObjectMapper();
			response.getWriter().println(mapper.writeValueAsString(results));

		}
	}

	private String getName(int id, Map<String, Integer> docNameMap) {
		for (Map.Entry<String, Integer> entry : docNameMap.entrySet()) {
			if (entry.getValue() == id) {
				System.out.println(entry.getKey());
				return entry.getKey();
			}
		}

		return null;

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
	}

}
