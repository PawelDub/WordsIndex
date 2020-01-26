package com.sabre.wordsindex.answerprocessing;

import org.springframework.stereotype.Service;

@Service
class ProcessorFactory {

    enum WordProcessing {
        WORD, WORD_DIGITAL, WORD_DIGITAL_DATA_TIMES,
    }

    AnswerProcessor createProcessor(final WordProcessing wordProcessing) {
        AnswerProcessor answerProcessor = null;

        switch (wordProcessing) {
            case WORD: answerProcessor = new AnswerWordProcessor();
            break;
        }
        return answerProcessor;
    }

}
