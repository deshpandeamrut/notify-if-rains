## Notify If It Rains
Application that alerts with push notifications to sunscribed users about rain forecast for the day.

## Motivation
Drenched myslef on the way back to home and was waiting for rain to stop, which made me to think of this app, although it could have been achieved with IFTT, considering issues with IFTT had to implement this.

## Tech/framework used
-Spring Boot
-APIs from Accuweather and DarkSky 
-OneSignal for Push Notifications

<b>Built with</b>
- [Eclipse](https://eclispe.org)

## Features
-Sends out the alert in the morning if the forecast is positive for rain
-Light weight as no installation as such


## Installation
-Clone the repo
-Run the app locally with **mvn spring-boot:run**

## API Reference
https://accuweather.com & 
https://darksky.net/




## How to use?
Visit 
http://nammabagalkot.in/rainalerts 
and subscribe to push notifictaions, you will start getting alerts everyday.
Note: Currently Bangalore,IN is set as default location in application.properties file.
