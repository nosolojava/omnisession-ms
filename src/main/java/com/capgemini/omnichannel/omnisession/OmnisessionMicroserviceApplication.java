package com.capgemini.omnichannel.omnisession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({"com.capgemini.omnichannel.omnisession","com.capgemini.omnichannel.common.integration"})
public class OmnisessionMicroserviceApplication {

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		SpringApplication.run(OmnisessionMicroserviceApplication.class, args);
	}
}
