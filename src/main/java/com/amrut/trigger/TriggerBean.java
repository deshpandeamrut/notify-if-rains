package com.amrut.trigger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amrut.springbootplay.service.WeatherServiceImpl;



@Component
public class TriggerBean {
	@Autowired
	WeatherServiceImpl weatherServiceImpl;
	/*
	@Scheduled(cron="* * * * *")
	public void triggerWeatherApi() {
		System.out.println("Triggered api");
		weatherServiceImpl.getForecast();
	}*/
}
