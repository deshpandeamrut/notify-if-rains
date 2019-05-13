package com.amrut.springbootplay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amrut.springbootplay.entity.AccuWeatherResponse;


@RestController
public class WeatherService {

	@Autowired
	WeatherServiceImpl weatherServiceImpl;
	
	@RequestMapping(value = "/weather/forecast")
	@ResponseBody
	public AccuWeatherResponse getForecast() {
		return weatherServiceImpl.getForecast();
	}
	
	@RequestMapping(value = "/weather/predict")
	@ResponseBody
	public String predict() {
		return weatherServiceImpl.predictRain();
	}
	
	@RequestMapping(value = "/weather/sendPushNotification")
	@ResponseBody
	public String sendPushNotification() {
		return weatherServiceImpl.sendPushNotification();
	}
}
