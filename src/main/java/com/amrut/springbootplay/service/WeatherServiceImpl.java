package com.amrut.springbootplay.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.amrut.springbootplay.entity.AccuWeatherForecasts;
import com.amrut.springbootplay.entity.AccuWeatherHeadline;
import com.amrut.springbootplay.entity.AccuWeatherResponse;
import com.amrut.springbootplay.entity.CurrentForecast;
import com.amrut.springbootplay.entity.DarkySkyApiResponse;
import com.amrut.springbootplay.entity.DarkySkyApiWeek;
import com.amrut.springbootplay.entity.PredictionResponse;

@Component
public class WeatherServiceImpl {
	@Value("${accu.weather.uri}")
	private String weatherUri;

	@Value("${weather.api.key}")
	private String weatherApiKey;

	@Value("${day.rain.msg}")
	private String dayRainMsg;

	@Value("${night.rain.msg}")
	private String nightRainMsg;

	@Value("${both.rain.msg}")
	private String bothRainMsg;

	@Value("${one.signal.app.id}")
	private String oneSignalAppId;

	@Value("${one.signal.app.key}")
	private String oneSignalAppKey;

	@Value("${push.notification.title}")
	private String pushNotificationTitle;

	@Value("${ignore.push.notification.word}")
	private String ignoreWord;

	@Value("${darksky.api.url}")
	private String darkSkyUrl;

	private AccuWeatherResponse doAccuRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		AccuWeatherResponse result = restTemplate.getForObject(uri, AccuWeatherResponse.class);
		return result;

	}

	private DarkySkyApiResponse doDarkySkyRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		DarkySkyApiResponse result = restTemplate.getForObject(uri, DarkySkyApiResponse.class);
		return result;

	}

	public AccuWeatherResponse getAccuForecast() {
		return doAccuRestCall(
				"http://dataservice.accuweather.com/forecasts/v1/daily/1day/204108?apikey=5kv7fq5hi6seWxB2u74G7g8Brv0ocA6N");
	}

	public DarkySkyApiResponse getDarkSkyForecast() {
		return doDarkySkyRestCall(darkSkyUrl);
	}

	private boolean checkIfItRains(AccuWeatherResponse accuWeatherResponse) {
		boolean raining = false;
		AccuWeatherHeadline headline = accuWeatherResponse.getHeadline();
		if (headline != null) {
			if (headline.getText() != null && headline.getText().toLowerCase().contains("rain")) {
				raining = true;
			}
		}
		return raining;
	}

	private String checkWhenItRains(AccuWeatherResponse accuWeatherResponse) {
		AccuWeatherForecasts forecast = accuWeatherResponse.getForecasts().get(0);
		String when = "";
		if (forecast != null) {
			if (forecast.getDay() != null && forecast.getDay().getPhrase().toLowerCase().contains("rain")) {
				when = "day";
			}
			if (forecast.getNight() != null && forecast.getNight().getPhrase().toLowerCase().contains("rain")) {
				if (when.equalsIgnoreCase("day")) {
					when = "both";
				} else {
					when = "night";
				}
			}
		}
		return when;
	}

	private boolean checkIfItRains(DarkySkyApiResponse darkySkyApiResponse) {
		DarkySkyApiWeek todaysData = darkySkyApiResponse.getDarkySkyApiDaily().getWeekData().get(0);
		boolean raining = false;
		String summary = todaysData.getSummary();
		if (summary != null) {
			if (summary.toLowerCase().contains("rain")) {
				raining = true;
			}
		}
		return raining;
	}

	public PredictionResponse predictRain() {
		PredictionResponse predictionResponse = new PredictionResponse();
		AccuWeatherResponse accuWeatherResponse = getAccuForecast();
		if (checkIfItRains(accuWeatherResponse)) {
			String when = checkWhenItRains(accuWeatherResponse);
			if (when.equals("day")) {
				predictionResponse.setMessage(dayRainMsg);
			} else if (when.equals("night")) {
				predictionResponse.setMessage(nightRainMsg);
			} else if (when.equals("both")) {
				predictionResponse.setMessage(bothRainMsg);
			}
			predictionResponse.setSource("ACCU");
			predictionResponse.setActualMessage(accuWeatherResponse.getHeadline().getText());
		}
		/**
		 * Check with DarkSky API
		 */
		DarkySkyApiResponse darkySkyApiResponse = getDarkSkyForecast();
		if (checkIfItRains(darkySkyApiResponse)) {
			predictionResponse.setMessage(bothRainMsg);
			predictionResponse
					.setActualMessage(darkySkyApiResponse.getDarkySkyApiDaily().getWeekData().get(0).getSummary());
			if (predictionResponse.getSource().equals("")) {
				predictionResponse.setSource("DARK SKY");
			} else {
				predictionResponse.setSource("ALL SOURCES");
			}
		}
		return predictionResponse;
	}

	public String sendPushNotification() {
		PredictionResponse predictionResponse = predictRain();
		if (predictionResponse.getMessage().equalsIgnoreCase(ignoreWord)) {
			return "No Rain Notifications!";
		}

		String strJsonBody = "{" + "\"app_id\": \"" + oneSignalAppId + "\"," + "\"included_segments\": [\"All\"],"
				+ "\"data\": {\"headings\": \"bar\"}," + "\"contents\": {\"en\": \""
				+ predictionResponse.getActualMessage() + "\n" + "<<" + predictionResponse.getSource() + ">>" + "\"},"
				+ "\"headings\": {\"en\": \"" + predictionResponse.getMessage() + "\"}" + "}";

		return pushNotification(strJsonBody);
	}

	private String pushNotification(String strJsonBody) {
		try {
			String jsonResponse;

			URL url = new URL("https://onesignal.com/api/v1/notifications");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Authorization", "Basic " + oneSignalAppKey);
			con.setRequestMethod("POST");

			byte[] sendBytes = strJsonBody.getBytes("UTF-8");
			con.setFixedLengthStreamingMode(sendBytes.length);

			OutputStream outputStream = con.getOutputStream();
			outputStream.write(sendBytes);

			int httpResponse = con.getResponseCode();
			System.out.println("httpResponse: " + httpResponse);

			if (httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
				Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
				jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
				scanner.close();
			} else {
				Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
				jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
				scanner.close();
			}
			return jsonResponse;

		} catch (Throwable t) {
			t.printStackTrace();
		}
		return "Error while sending";
	}

	public String sendTodaysForecast() {
		DarkySkyApiResponse darkySkyApiResponse = getDarkSkyForecast();
		CurrentForecast currentForecast = new CurrentForecast();
		currentForecast.setCurrentSummary(darkySkyApiResponse.getDarkySkyApiCurrently().getSummary());
		currentForecast.setDaySummary(darkySkyApiResponse.getDarkySkyApiHourly().getSummary());
		currentForecast.setWeekSummary(darkySkyApiResponse.getDarkySkyApiDaily().getSummary());

		String notificationContent = "Currently " + currentForecast.getCurrentSummary() + ", "
				+ currentForecast.getDaySummary();

		String strJsonBody = "{" + "\"app_id\": \"" + oneSignalAppId + "\"," + "\"included_segments\": [\"All\"],"
				+ "\"data\": {\"headings\": \"bar\"}," + "\"contents\": {\"en\": \"" + notificationContent + "\n"
				+ "\"}," + "\"headings\": {\"en\": \"  Today's Summary  \"}" + "}";

		return pushNotification(strJsonBody);
	}
}
