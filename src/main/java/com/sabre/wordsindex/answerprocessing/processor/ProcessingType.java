package com.sabre.wordsindex.answerprocessing.processor;

public enum ProcessingType {
    WORD("<Maximum length for single word: 250 chars>"), DIGIT("<Maximum value: 9223372036854775807>"), TIME(""), DATE("");

    private String message;

    ProcessingType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
