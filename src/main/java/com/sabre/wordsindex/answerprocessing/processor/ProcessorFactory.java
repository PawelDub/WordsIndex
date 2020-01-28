package com.sabre.wordsindex.answerprocessing.processor;

import org.springframework.stereotype.Service;

@Service
class ProcessorFactory {

    AnswerProcessor createProcessor(final ProcessingType processingType) {
        AnswerProcessor answerProcessor = null;

        switch (processingType) {
            case WORD: answerProcessor = new AnswerWordProcessor();
            break;
            case DIGIT: answerProcessor = new AnswerDigitProcessor();
            break;
            case TIME: answerProcessor = new AnswerTimeProcessor();
            break;
            case DATE: answerProcessor = new AnswerDateProcessor();
            break;
        }
        return answerProcessor;
    }

}
