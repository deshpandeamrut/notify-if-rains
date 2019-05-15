package com.amrut.springbootplay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amrut.springbootplay.entity.AccuWeatherResponse;
import com.amrut.springbootplay.entity.DarkySkyApiResponse;
import com.amrut.springbootplay.entity.PredictionResponse;


@RestController
public class WeatherService {

	@Autowired
	WeatherServiceImpl weatherServiceImpl;
	
	@RequestMapping(value = "/weather/forecast/accu")
	@ResponseBody
	public AccuWeatherResponse getForecast() {
		return weatherServiceImpl.getAccuForecast();
	}
	
	@RequestMapping(value = "/weather/forecast/darksky")
	@ResponseBody
	public DarkySkyApiResponse getdarkSkyForecast() {
		return weatherServiceImpl.getDarkSkyForecast();
	}
	
	@RequestMapping(value = "/weather/predict")
	@ResponseBody
	public PredictionResponse predict() {
		return weatherServiceImpl.predictRain();
	}
	
	@RequestMapping(value = "/weather/sendPushNotification")
	@ResponseBody
	public String sendPushNotification() {
		return weatherServiceImpl.sendPushNotification();
	}
	
	@RequestMapping(value = "/weather/sendTodaysForecast")
	@ResponseBody
	public String sendTodaysForecast() {
		return weatherServiceImpl.sendTodaysForecast();
	}
}
