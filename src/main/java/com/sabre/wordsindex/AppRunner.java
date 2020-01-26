package com.sabre.wordsindex;

import com.sabre.wordsindex.answerprocessing.WordIndexingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import static java.lang.System.exit;

@Service
public class AppRunner implements CommandLineRunner {

    private WordIndexingFacade wordIndexingFacade;

    @Autowired
    public AppRunner(final WordIndexingFacade wordIndexingFacade) {
        this.wordIndexingFacade = wordIndexingFacade;
    }

    @Override
    public void run(String... args) {
        System.out.println("\n|----------------------------------------|");
        System.out.println("|Welcome in words indexing application.  |");
        System.out.println("|----------------------------------------|\n");

        wordIndexingFacade.wordIndexing();

        exit(0);
    }

}
