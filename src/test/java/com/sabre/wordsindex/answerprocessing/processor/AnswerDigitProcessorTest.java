package com.sabre.wordsindex.answerprocessing.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = AnswerWordProcessor.class)
@DisplayName("Test of processing digits from answer text")
public class AnswerDigitProcessorTest {

    @Spy
    private final AnswerProcessor answerDigitProcessor = new AnswerDigitProcessor();

    @DisplayName("Should throw exception if text is null")
    @Test
    void throwExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerDigitProcessor.collectWordsFrom(null));
    }

    @DisplayName("Indexing builder should throw exception if text is null")
    @Test
    void indexingBuilderShouldthrowExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerDigitProcessor.buildIndexedWordsCollection(null));
    }

    @DisplayName("Should returns sorted digits collection")
    @Test
    void shouldReturnSortedDigitsCollection() {
        String text = "\t\t\tAla-2 \t\t22 222/565 2:566,,,,,,,,,, 25.26 --has a25.26.2006 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}## $$$cat6;develops**  \ta--------2cat:in ~[\tJava]/?||\\ \taccording12*12 to& (java-guide)_=+``~~ " +
                "during to fly ,with44/1out ,,aviation4all and working in B2B work syst546456565em" +
                "5sdasd/ ads /56:2233werwer 5-5*pd6d/6  16:15:59  16:15:5 --01/01/2000/.- 31/01/2000///.- 31/12/2000  12.02.20229-";

        Set<String> splitAnswer = answerDigitProcessor.collectWordsFrom(text);
        assertThat(splitAnswer).isEqualTo(new TreeSet<>(Arrays.asList( "01", "02", "1", "12", "15", "16", "2", "2000", "2006", "20229", "22", "222", "2233", "23", "25", "26", "31", "4", "44", "5", "546456565", "56", "565", "566", "59", "6")));

        Mockito.verify(answerDigitProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should build indexed collection of digits correctly")
    @Test
    void shouldBuildIndexedCollectionOfDigitsCorrectly() {
        String text = "\t\t\tAla-2 \t\t22 222/565 2:566,,,,,,,,,, 25.26 --has a25.26.2006 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}## $$$cat6;develops**  \ta--------2cat:in ~[\tJava]/?||\\ \taccording12*12 to& (java-guide)_=+``~~ " +
                "during to fly ,with44/1out ,,aviation4all and working in B2B work syst546456565em 000056 0056 056 000000055 " +
                "5sdasd/ ads /56:2233werwer 5-5*pd6d/6  16:15:59  16:15:5 --01/01/2000/.- 31/01/2000///.- 31/12/2000  12.02.20229-";

        Map<String, Set<String>> splitAnswer = answerDigitProcessor.buildIndexedWordsCollection(text);
        Map<String, Set<String>> expectedIndexedCollection = buildExpectedIndexedCollection();

        assertThat(splitAnswer).isEqualTo(expectedIndexedCollection);
        Mockito.verify(answerDigitProcessor, times(1)).collectWordsFrom(text);
        Mockito.verify(answerDigitProcessor, times(1)).processAnswer(text);
    }

    private Map<String, Set<String>> buildExpectedIndexedCollection() {
        Map<String, Set<String>> expectedIndexedCollection = new TreeMap<>();
        expectedIndexedCollection.put("0",new TreeSet<>(Arrays.asList("01", "02", "000000055", "056", "000056", "0056", "2000", "2006", "20229")));
        expectedIndexedCollection.put("1",new TreeSet<>(Arrays.asList("01", "1", "12", "15", "16", "31")));
        expectedIndexedCollection.put("2",new TreeSet<>(Arrays.asList("02", "2", "12", "22", "23", "25", "26", "222", "2000", "2006", "2233", "20229")));
        expectedIndexedCollection.put("3",new TreeSet<>(Arrays.asList("23", "31", "2233")));
        expectedIndexedCollection.put("4",new TreeSet<>(Arrays.asList("4", "44", "546456565")));
        expectedIndexedCollection.put("5",new TreeSet<>(Arrays.asList("5", "15", "25", "000000055", "056", "56", "000056", "0056", "59", "565", "566", "546456565")));
        expectedIndexedCollection.put("6",new TreeSet<>(Arrays.asList("6", "16", "26", "056", "56", "56", "000056", "0056", "565", "566", "2006", "546456565")));
        expectedIndexedCollection.put("9",new TreeSet<>(Arrays.asList("59", "20229")));
        return expectedIndexedCollection;
    }

}
