package me.sangwon;

import org.jooq.conf.Settings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootJooqExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJooqExampleApplication.class, args);
	}

	@Bean
	public Settings settings () {
		return new Settings().withRenderFormatted(true);
	}
}
