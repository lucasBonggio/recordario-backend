package com.recordario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.recordario.usuarios.autenticacion.JwtServicio;

@SpringBootApplication
@EnableConfigurationProperties(JwtServicio.class)
public class RecordarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordarioApplication.class, args);
	}

}
