package com.playlist;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaylistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaylistApiApplication.class, args);
	}
}
