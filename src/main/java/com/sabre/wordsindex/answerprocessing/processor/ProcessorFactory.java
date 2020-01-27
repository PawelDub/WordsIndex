package com.sabre.wordsindex.answerprocessing.processor;

import org.springframework.stereotype.Service;

@Service
public class ProcessorFactory {

    public enum WordProcessing {
        WORD, WORD_DIGITAL, WORD_DIGITAL_DATA_TIMES,
    }

    public AnswerProcessor createProcessor(final WordProcessing wordProcessing) {
        AnswerProcessor answerProcessor = null;

        switch (wordProcessing) {
            case WORD: answerProcessor = new AnswerWordProcessor();
            break;
        }
        return answerProcessor;
    }

}
