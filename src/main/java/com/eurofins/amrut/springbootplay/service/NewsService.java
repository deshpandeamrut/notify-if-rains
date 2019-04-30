package com.eurofins.amrut.springbootplay.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.eurofins.amrut.springbootplay.entity.Article;
import com.eurofins.amrut.springbootplay.entity.Response;
import com.eurofins.amrut.springbootplay.entity.Source;

@RestController
public class NewsService {
	
	@Autowired
	NewsServiceImpl newsServiceImpl;
	
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

	
	
	@RequestMapping(value = "/news/getLatest")
	@ResponseBody
	public List<Article> getLatestNews() {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey="+newsApiKey+"&sources=bbc-news,cnn,business-insider,the-verge";
		Date now = new Date();
		long nowMs = now.getTime();
		if (cache.get("headlines") == null || (nowMs - lastRefreshTime)> refreshInterval ) {
			articleList = doRestCall(uri);
			cache.put("headlines", articleList);
			lastRefreshTime = nowMs;
		}
		return cache.get("headlines");
	}

	@RequestMapping("/news/source/{sourceName}")
	@ResponseBody
	public List<Article> getNewsFromSource(@PathVariable String sourceName) {
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

	@RequestMapping(value = "/news/search/{searchText}")
	@ResponseBody
	public List<Article> getLatestNewsForSearchText(@PathVariable String searchText) {
		final String uri = "https://newsapi.org/v2/top-headlines?language=en&apiKey=" + newsApiKey + "&q=" + searchText;
		return doRestCall(uri);
	}

	@RequestMapping(value = "/news/all/search/{searchText}")
	@ResponseBody
	public List<Article> getAllNewsForSearchText(@PathVariable String searchText) {
		final String uri = "https://newsapi.org/v2/everything?language=en&apiKey=" + newsApiKey + "&q=" + searchText;
		return doRestCall(uri);
	}

	@RequestMapping(value = "/news/category/{category}")
	@ResponseBody
	public List<Article> getNewsForCategory(@PathVariable String category, boolean extendedSearch) {
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

	private List<Article> doRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		Response result = restTemplate.getForObject(uri, Response.class);
		return result.getArticles();

	}
	
	@RequestMapping(value = "/news/sources")
	@ResponseBody
	public List<Source> getNewsSources() {
		return newsServiceImpl.getNewsSources();
	}
}