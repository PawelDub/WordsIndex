package com.sabre.wordsindex.answerprocessing.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ProcessorFactory.class)
@DisplayName("Test of factoring of the processor")
public class ProcessorFactoryTest {

    @Autowired
    private ProcessorFactory processorFactory;

    @DisplayName("Should return processor for collecting words")
    @Test
    void shouldReturWordProcessor() {
        AnswerProcessor answerProcessor = processorFactory.createProcessor(ProcessingType.WORD);
        assertThat(answerProcessor).isInstanceOf(AnswerWordProcessor.class);
    }

    @DisplayName("Should throw exception when word processing type is not signed")
    @Test
    void shouldThrowExceptionForNotSignedType() {
        assertThrows(NullPointerException.class, () -> processorFactory.createProcessor(null));
    }

    @DisplayName("Should throw exception when type not match")
    @Test
    void shouldThrowExceptionWhenTypeNotMatch() {
        assertThrows(IllegalArgumentException.class, () -> processorFactory.createProcessor(ProcessingType.valueOf("NOT_MATCH")));
    }
}
