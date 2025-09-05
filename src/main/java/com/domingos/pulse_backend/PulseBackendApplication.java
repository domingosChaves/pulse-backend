package com.domingos.pulse_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PulseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PulseBackendApplication.class, args);
	}

}
