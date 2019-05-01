package com.eurofins.amrut.springbootplay.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.eurofins.amrut.springbootplay.entity.Article;
import com.eurofins.amrut.springbootplay.entity.Response;
import com.eurofins.amrut.springbootplay.entity.Source;
import com.eurofins.amrut.springbootplay.entity.SourceResponse;

@Component
public class NewsServiceImpl {
	
	static List<Article> articleList = null;
	static List<Article> techArticleList = null;
	static Map<String, List<Article>> cache = new HashMap<>();
	static long lastRefreshTime = 0;
	
	@Value("${news.api.key}")
	private String newsApiKey;

	@Value("${user.categories}")
	private String userCategories;

	@Value("${news.refresh.interval}")
	private double refreshInterval;

	@Value("${news.technology.categories}")
	private String technologySources;

	private static List<Source> newsSourceList = new ArrayList<>();

	public List<Source> getNewsSources() {
		final String uri = "https://newsapi.org/v2/sources?language=en&apiKey=" + newsApiKey;
		if (newsSourceList.isEmpty()) {
			newsSourceList = doRestCallForSources(uri);
		}
		return newsSourceList;
	}

	private List<Source> doRestCallForSources(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		SourceResponse result = restTemplate.getForObject(uri, SourceResponse.class);
		return result.getSources();

	}
	
	private List<Article> doRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		Response result = restTemplate.getForObject(uri, Response.class);
		return result.getArticles();

	}

	public List<Article> getSmartNews() {
		List<Article> latestArticles = doRestCall("https://newsapi.org/v2/top-headlines?language=en&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f&e");
		for (Article article : latestArticles) {
			if(technologySources.toLowerCase().contains(article.getSourceName().toLowerCase()) || technologySources.toLowerCase().contains(article.getSourceId().toLowerCase())) {
				article.setWeight(article.getWeight()+1);
				System.out.println(article.getSource());
			}
		}
		Collections.sort(latestArticles, new Comparator<Article>() {
			@Override
			public int compare(Article o1, Article o2) {
				return o2.getWeight().compareTo(o1.getWeight());
			}
		});
		return latestArticles;
	}
	
	public static void main(String[] args) {
		NewsServiceImpl newsServiceImpl = new NewsServiceImpl();
		
		newsServiceImpl.getSmartNews();
	}


	
	public List<Article> getLatestNews() {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey="+newsApiKey+"&sources=bbc-news,cnn,business-insider,the-verge";
		Date now = new Date();
		long nowMs = now.getTime();
		if (cache.get("headlines") == null || (nowMs - lastRefreshTime)> refreshInterval ) {
			//articleList = doRestCall(uri);
			articleList = getSmartNews();
			cache.put("headlines", articleList);
			lastRefreshTime = nowMs;
		}
		return cache.get("headlines");
	}

	public List<Article> getNewsFromSource(String sourceName) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey=" + newsApiKey + "&sources="
				+ sourceName;
		if(sourceName==null) {
			return null;
		}
		Date now = new Date();
		long nowMs = now.getTime();
		if (cache.get(sourceName) == null || (nowMs - lastRefreshTime)> refreshInterval ) {
			articleList = doRestCall(uri);
			cache.put(sourceName, articleList);
			lastRefreshTime = nowMs;
		}
		return cache.get(sourceName);
	}

	public List<Article> getLatestNewsForSearchText( String searchText) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey=" + newsApiKey + "&q=" + searchText;
		return doRestCall(uri);
	}

	public List<Article> getAllNewsForSearchText(String searchText) {
		final String uri = "https://newsapi.org/v2/everything?language=en&apiKey=" + newsApiKey + "&q=" + searchText;
		return doRestCall(uri);
	}

	public List<Article> getNewsForCategory(String category, boolean extendedSearch) {
		String country = "";
		List<String> indianCategoryList = new ArrayList<>(Arrays.asList("politics","automobile","ipl"));
		if(indianCategoryList.contains(category)) {
			country = "in";
		}
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=" + country + "&apiKey="+newsApiKey+"&category="
				+ category;
		Date now = new Date();
		long nowMs = now.getTime();
		if (cache.get(category) == null || (lastRefreshTime - nowMs)> refreshInterval ) {
			List<Article> articles = doRestCall(uri);
			if (articles.isEmpty()) {
				articles = getLatestNewsForSearchText(category);
			}
			if (extendedSearch && articles.isEmpty()) {
				articles = getAllNewsForSearchText(category);
			}
			cache.put(category, articles);
			lastRefreshTime = nowMs;
		}
		return cache.get(category);
	}

	@RequestMapping(value = "/news/myFeed")
	@ResponseBody
	public List<Article> getLatestFeed() {
		Date now = new Date();
		long nowMs = now.getTime();
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&country=us&apiKey=d7ae5b652f4d481d8cb7ded898d9d43f";
		if (cache.get("myFeed") == null || (lastRefreshTime - nowMs)> refreshInterval ) {
			List<Article> myFeed = new ArrayList<>();
			for (String category : userCategories.split(",")) {
				List<Article> temp = getNewsForCategory(category.split(":")[1], false);
				if (!temp.isEmpty())
					myFeed.add(temp.get(0));
			}
			cache.put("myFeed", myFeed);
			Date lastRefreshTimedate = new  Date();
			lastRefreshTimedate.setTime(lastRefreshTime);
			System.out.println("Last refresh time: "+ lastRefreshTimedate);
			lastRefreshTime = nowMs;
		}
		return cache.get("myFeed");

	}
}
