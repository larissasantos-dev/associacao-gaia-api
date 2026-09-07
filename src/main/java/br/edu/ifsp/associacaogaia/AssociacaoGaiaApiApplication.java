package br.edu.ifsp.associacaogaia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class AssociacaoGaiaApiApplication {

// vou utilizar enquanto o banco de dados n está conectado
@SpringBootApplication(exclude = {org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration.class})
public class AssociacaoGaiaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssociacaoGaiaApiApplication.class, args);
    }

}
