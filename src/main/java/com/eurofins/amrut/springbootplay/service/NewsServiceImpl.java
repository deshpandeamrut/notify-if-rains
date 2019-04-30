package com.eurofins.amrut.springbootplay.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eurofins.amrut.springbootplay.entity.Article;
import com.eurofins.amrut.springbootplay.entity.Response;
import com.eurofins.amrut.springbootplay.entity.Source;
import com.eurofins.amrut.springbootplay.entity.SourceResponse;

@Component
public class NewsServiceImpl {
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
			newsSourceList = doRestCall(uri);
		}
		return newsSourceList;
	}

	private List<Source> doRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		SourceResponse result = restTemplate.getForObject(uri, SourceResponse.class);
		return result.getSources();

	}
}
