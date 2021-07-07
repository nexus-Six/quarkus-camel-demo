package org.acme;

import org.apache.camel.builder.RouteBuilder;

// Creates a Camel Route that connects to an Rest webservice to get current location for IP address, then passes it to another
// webservices for current temperature and writes it to a file

public class CamelRoute extends RouteBuilder{
    @Override
    public void configure() throws Exception {
                                        
        from("timer:foo?period=5000")
        .streamCaching()
        .to("https://api.ipgeolocation.io/ipgeo?apiKey={{api.key.geolocation}}")
        .log("Response : ${body}")
        .setHeader("longtitude")
        .jsonpath("$.longitude")
        .setHeader("latitude")
        .jsonpath("$.latitude")
        .log("Long : ${header.longtitude} Lat : ${header.latitude} ")
        .toD("https://api.openweathermap.org/data/2.5/weather?lat=${header.latitude}&lon=${header.longtitude}&appid={{api.key.openweathermap}}&units=metric")
        .log("Response : ${body}")
        .setHeader("temp")
        .jsonpath("$.main.temp")
        .log("My temperature --->  ${header.temp}")
        .setBody(simple("On ${date:now:yyyyMMdd} the temperature was ${header.temp}\\n" ))
        .to("file:?fileName=report.txt&fileExist=Append")
        ; 

    }

    
}

