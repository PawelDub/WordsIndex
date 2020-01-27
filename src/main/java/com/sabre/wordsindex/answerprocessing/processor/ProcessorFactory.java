package com.sabre.wordsindex.answerprocessing.processor;

import org.springframework.stereotype.Service;

@Service
public class ProcessorFactory {

    public enum WordProcessing {
        WORD, DIGIT, DATE, TIME
    }

    public AnswerProcessor createProcessor(final WordProcessing wordProcessing) {
        AnswerProcessor answerProcessor = null;

        switch (wordProcessing) {
            case WORD: answerProcessor = new AnswerWordProcessor();
            break;
            case DIGIT: answerProcessor = new AnswerDigitProcessor();
            break;
            case DATE: answerProcessor = new AnswerDateProcessor();
            break;
            case TIME: answerProcessor = new AnswerTimeProcessor();
            break;
        }
        return answerProcessor;
    }

}
