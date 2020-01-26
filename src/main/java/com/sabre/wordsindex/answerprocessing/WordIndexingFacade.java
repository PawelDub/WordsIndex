package com.sabre.wordsindex.answerprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WordIndexingFacade {

    private UserInteract userInteractService;

    @Autowired
    public WordIndexingFacade(final UserInteractService userInteractService) {
        this.userInteractService = userInteractService;
    }

    public void wordIndexing() {
        userInteractService.userInteractProcess();
    }

}
