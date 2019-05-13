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
	
	private AccuWeatherResponse doRestCall(String uri) {
		System.out.println("Doing rest call... for " + uri);
		RestTemplate restTemplate = new RestTemplate();
		AccuWeatherResponse result = restTemplate.getForObject(uri, AccuWeatherResponse.class);
		return result;

	}

	public AccuWeatherResponse getForecast() {
		return doRestCall(
				"http://dataservice.accuweather.com/forecasts/v1/daily/1day/204108?apikey=5kv7fq5hi6seWxB2u74G7g8Brv0ocA6N");
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

	public String predictRain() {
		String message = "NA";
		AccuWeatherResponse accuWeatherResponse = getForecast();
		if (checkIfItRains(accuWeatherResponse)) {
			String when = checkWhenItRains(accuWeatherResponse);
			if (when.equals("day")) {
				return dayRainMsg;
			} else if (when.equals("night")) {
				return nightRainMsg;
			} else if (when.equals("both")) {
				return bothRainMsg;
			}
		}
		return message;
	}

	public String sendPushNotification() {
		try {
			String message = predictRain();
			System.out.println(message);
			if(message.equalsIgnoreCase(ignoreWord)) {
				return "No Rain Notifications!";
			}
			String jsonResponse;
			
			URL url = new URL("https://onesignal.com/api/v1/notifications");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Authorization", "Basic "+ oneSignalAppKey);
			con.setRequestMethod("POST");

			String strJsonBody = "{" + "\"app_id\": \"" + oneSignalAppId + "\","
					+ "\"included_segments\": [\"All\"]," + "\"data\": {\"foo\": \"bar\"},"
					+ "\"contents\": {\"en\": \"" + message + "\"}" + "}";

			System.out.println("strJsonBody:\n" + strJsonBody);

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
}
