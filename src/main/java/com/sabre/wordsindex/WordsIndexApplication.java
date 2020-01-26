package com.sabre.wordsindex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordsIndexApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(WordsIndexApplication.class);
        app.run(args);

    }

}
