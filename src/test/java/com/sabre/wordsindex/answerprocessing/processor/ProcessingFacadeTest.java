package com.sabre.wordsindex.answerprocessing.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AnswerWordProcessor.class)
@DisplayName("Test of processing words from answer text")
public class ProcessingFacadeTest {

    private final static String INPUT_TEXT = "\\t\\t11:16:60\\t11:6:56//1:14:18//:...A16.07.2056la-2 23:60 24:18 00:00 00:01 19:18 19:18 19:17:17 19:17:17 " +
            "\\t\\t22 222/565 2:566,,,19:18,,,, 25.26 23::26 --has 25.26.2006:19:18 02:23  ,,cat,^ <2a>" +
            " {:''\\\"\\\"@!}##29.09.1659$$;develop11.14.11.2018   11.04.2019.2019** 01/01/2000/\\\" ---12/12/12/2012----2cat:in19-03.1658 " +
            "~[\\tJava]/?||\\\\12:15:5 \\taccording12*12 to&31/04/2017 (java14.10.1655-guide)_=+`14.10.1655`~~ during to fl11:6y " +
            ",with44/1out 1:06,,424.06.2017all13.11.20156and working in B2B work syst546456565em kąty będzie żreć źdźbło ä " +
            "ésdfé14.08.2016ósdó31.12.2000 ÁdÁ ŐŐsfŐŐ  őhő  ĎlĎ ŇkŇ ÝoÝ5 /56:22 5-5*6d/6  16:15:59  16:15:5 --///.-";

    @Mock
    private ProcessorFactory processorFactory;
    @InjectMocks
    private ProcessingFacade processingFacade;

    @BeforeEach
    public void setUp() {
        when(processorFactory.createProcessor(ProcessingType.WORD)).thenReturn(new AnswerWordProcessor());
        when(processorFactory.createProcessor(ProcessingType.DATE)).thenReturn(new AnswerDateProcessor());
        when(processorFactory.createProcessor(ProcessingType.DIGIT)).thenReturn(new AnswerDigitProcessor());
        when(processorFactory.createProcessor(ProcessingType.TIME)).thenReturn(new AnswerTimeProcessor());
    }


    @DisplayName("Processing should throw exception when text is not given")
    @Test
    void processingShouldThrowExceptionWhenTextIsNotGiven() {
        assertThrows(NullPointerException.class, () ->
                processingFacade.processAnswerFrom(null, Collections.singletonList(ProcessingType.WORD)));
    }

    @DisplayName("Processing should throw exception when processing type is not given")
    @Test
    void processingShouldThrowExceptionWhenProcessingTypeIsNotGiven() {
        assertThrows(NullPointerException.class, () ->
                processingFacade.processAnswerFrom("sample text for process", null));
    }

    @DisplayName("Should returns sorted all collection")
    @Test
    void shouldReturnSortedAllCollection() {
        Map<String, Set<String>> splitAnswer = processingFacade.processAnswerFrom(INPUT_TEXT, Arrays.asList(ProcessingType.WORD,
                ProcessingType.DIGIT, ProcessingType.TIME, ProcessingType.DATE));
        assertThat(splitAnswer).isEqualTo(buildWordsDigitsTimesDates());

    }

    @DisplayName("Should returns sorted digits with times and dates collection")
    @Test
    void shouldReturnSortedDigitsWithTimesAndDatesCollection() {

        Map<String, Set<String>> splitAnswer = processingFacade.processAnswerFrom(INPUT_TEXT, Arrays.asList(ProcessingType.DIGIT, ProcessingType.TIME, ProcessingType.DATE));
        assertThat(splitAnswer).isEqualTo(buildDigitsTimesDates());

    }

    private Map<String, Set<String>> buildWordsDigitsTimesDates() {
        Map<String, Set<String>> expectedIndexedCollection = new LinkedHashMap<>();
        expectedIndexedCollection.put("a",new LinkedHashSet<>(Arrays.asList("a", "according", "all", "and", "cat", "has", "java", "la")));
        expectedIndexedCollection.put("á",new LinkedHashSet<>(Arrays.asList("ádá")));
        expectedIndexedCollection.put("ä",new LinkedHashSet<>(Arrays.asList("ä")));
        expectedIndexedCollection.put("ą",new LinkedHashSet<>(Arrays.asList("kąty")));
        expectedIndexedCollection.put("b",new LinkedHashSet<>(Arrays.asList("b", "będzie", "źdźbło")));
        expectedIndexedCollection.put("c",new LinkedHashSet<>(Arrays.asList("according", "cat")));
        expectedIndexedCollection.put("ć",new LinkedHashSet<>(Arrays.asList("żreć")));
        expectedIndexedCollection.put("d",new LinkedHashSet<>(Arrays.asList("according", "ádá", "and", "będzie", "d", "develop", "during", "ésdfé", "guide", "ósdó", "źdźbło")));
        expectedIndexedCollection.put("ď",new LinkedHashSet<>(Arrays.asList("ďlď")));
        expectedIndexedCollection.put("e",new LinkedHashSet<>(Arrays.asList("będzie", "develop", "em", "guide", "żreć")));
        expectedIndexedCollection.put("é",new LinkedHashSet<>(Arrays.asList("ésdfé")));
        expectedIndexedCollection.put("ę",new LinkedHashSet<>(Arrays.asList("będzie")));
        expectedIndexedCollection.put("f",new LinkedHashSet<>(Arrays.asList("ésdfé", "fl", "őősfőő")));
        expectedIndexedCollection.put("g",new LinkedHashSet<>(Arrays.asList("according", "during", "guide", "working")));
        expectedIndexedCollection.put("h",new LinkedHashSet<>(Arrays.asList("has", "őhő", "with")));
        expectedIndexedCollection.put("i",new LinkedHashSet<>(Arrays.asList("according", "będzie", "during", "guide", "in", "with", "working")));
        expectedIndexedCollection.put("j",new LinkedHashSet<>(Arrays.asList("java")));
        expectedIndexedCollection.put("k",new LinkedHashSet<>(Arrays.asList("kąty", "ňkň", "work", "working")));
        expectedIndexedCollection.put("l",new LinkedHashSet<>(Arrays.asList("all", "develop", "ďlď", "fl", "la")));
        expectedIndexedCollection.put("ł",new LinkedHashSet<>(Arrays.asList("źdźbło")));
        expectedIndexedCollection.put("m",new LinkedHashSet<>(Arrays.asList("em")));
        expectedIndexedCollection.put("m",new LinkedHashSet<>(Arrays.asList("em")));
        expectedIndexedCollection.put("n",new LinkedHashSet<>(Arrays.asList("according", "and", "during", "in", "working")));
        expectedIndexedCollection.put("ň",new LinkedHashSet<>(Arrays.asList("ňkň")));
        expectedIndexedCollection.put("o",new LinkedHashSet<>(Arrays.asList("according", "develop", "out", "to", "work", "working", "ýoý", "źdźbło")));
        expectedIndexedCollection.put("ő",new LinkedHashSet<>(Arrays.asList("őhő", "őősfőő")));
        expectedIndexedCollection.put("ó",new LinkedHashSet<>(Arrays.asList("ósdó")));
        expectedIndexedCollection.put("ó",new LinkedHashSet<>(Arrays.asList("ósdó")));
        expectedIndexedCollection.put("p",new LinkedHashSet<>(Arrays.asList("develop")));
        expectedIndexedCollection.put("r",new LinkedHashSet<>(Arrays.asList("according", "during", "work", "working", "żreć")));
        expectedIndexedCollection.put("s",new LinkedHashSet<>(Arrays.asList("ésdfé", "has", "őősfőő", "ósdó", "syst")));
        expectedIndexedCollection.put("s",new LinkedHashSet<>(Arrays.asList("ésdfé", "has", "őősfőő", "ósdó", "syst")));
        expectedIndexedCollection.put("t",new LinkedHashSet<>(Arrays.asList("cat", "kąty", "out", "syst", "to", "with")));
        expectedIndexedCollection.put("u",new LinkedHashSet<>(Arrays.asList("during", "guide", "out")));
        expectedIndexedCollection.put("v",new LinkedHashSet<>(Arrays.asList("develop", "java")));
        expectedIndexedCollection.put("w",new LinkedHashSet<>(Arrays.asList("with", "work", "working")));
        expectedIndexedCollection.put("y",new LinkedHashSet<>(Arrays.asList("kąty", "syst", "y")));
        expectedIndexedCollection.put("ý",new LinkedHashSet<>(Arrays.asList("ýoý")));
        expectedIndexedCollection.put("z",new LinkedHashSet<>(Arrays.asList("będzie")));
        expectedIndexedCollection.put("ź",new LinkedHashSet<>(Arrays.asList("źdźbło")));
        expectedIndexedCollection.put("ż",new LinkedHashSet<>(Arrays.asList("żreć")));
        expectedIndexedCollection.put("0",new LinkedHashSet<>(Arrays.asList("00", "01", "02", "03", "04", "06", "07", "08", "09", "10", "60", "2000", "2006", "2012", "2016", "2017", "2018", "2019", "2056", "20156", "00:00", "00:01", "02:23", "1655-10-14", "1659-09-29", "2000-12-31","2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("1",new LinkedHashSet<>(Arrays.asList("01", "1", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "31", "1655", "1658", "1659", "2012", "2016", "2017", "2018", "2019", "20156", "00:01", "16:15:59", "19:17:17", "19:18", "1655-10-14", "1659-09-29", "2000-12-31", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("2",new LinkedHashSet<>(Arrays.asList("02", "2", "12", "22", "23", "24", "25", "26", "29", "222", "424", "2000", "2006", "2012", "2016", "2017", "2018", "2019", "2056", "20156", "02:23", "1659-09-29", "2000-12-31", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("3",new LinkedHashSet<>(Arrays.asList("03", "13", "23", "31", "02:23", "2000-12-31")));
        expectedIndexedCollection.put("4",new LinkedHashSet<>(Arrays.asList("04", "14", "24", "44", "424", "546456565", "1655-10-14", "2016-08-14")));
        expectedIndexedCollection.put("5",new LinkedHashSet<>(Arrays.asList("5", "15", "25", "56", "59", "565", "566", "1655", "1658", "1659", "2056", "20156", "546456565", "16:15:59", "1655-10-14", "1659-09-29", "2056-07-16")));
        expectedIndexedCollection.put("6",new LinkedHashSet<>(Arrays.asList("06", "6", "16", "26", "56", "60", "565", "566", "1655", "1658", "1659", "2006", "2016", "2056", "20156", "546456565", "16:15:59", "1655-10-14", "1659-09-29", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("7",new LinkedHashSet<>(Arrays.asList("07", "17", "2017", "19:17:17", "2056-07-16")));
        expectedIndexedCollection.put("8",new LinkedHashSet<>(Arrays.asList("08", "18", "1658", "2018", "19:18", "2016-08-14")));
        expectedIndexedCollection.put("9",new LinkedHashSet<>(Arrays.asList("09", "19", "29", "59", "1659", "2019", "16:15:59", "19:17:17", "19:18", "1659-09-29")));


        return expectedIndexedCollection;
    }

    private Map<String, Set<String>> buildDigitsTimesDates() {
        Map<String, Set<String>> expectedIndexedCollection = new LinkedHashMap<>();
        expectedIndexedCollection.put("0",new LinkedHashSet<>(Arrays.asList("00", "01", "02", "03", "04", "06", "07", "08", "09", "10", "60", "2000", "2006", "2012", "2016", "2017", "2018", "2019", "2056", "20156", "00:00", "00:01", "02:23", "1655-10-14", "1659-09-29", "2000-12-31","2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("1",new LinkedHashSet<>(Arrays.asList("01", "1", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "31", "1655", "1658", "1659", "2012", "2016", "2017", "2018", "2019", "20156", "00:01", "16:15:59", "19:17:17", "19:18", "1655-10-14", "1659-09-29", "2000-12-31", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("2",new LinkedHashSet<>(Arrays.asList("02", "2", "12", "22", "23", "24", "25", "26", "29", "222", "424", "2000", "2006", "2012", "2016", "2017", "2018", "2019", "2056", "20156", "02:23", "1659-09-29", "2000-12-31", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("3",new LinkedHashSet<>(Arrays.asList("03", "13", "23", "31", "02:23", "2000-12-31")));
        expectedIndexedCollection.put("4",new LinkedHashSet<>(Arrays.asList("04", "14", "24", "44", "424", "546456565", "1655-10-14", "2016-08-14")));
        expectedIndexedCollection.put("5",new LinkedHashSet<>(Arrays.asList("5", "15", "25", "56", "59", "565", "566", "1655", "1658", "1659", "2056", "20156", "546456565", "16:15:59", "1655-10-14", "1659-09-29", "2056-07-16")));
        expectedIndexedCollection.put("6",new LinkedHashSet<>(Arrays.asList("06", "6", "16", "26", "56", "60", "565", "566", "1655", "1658", "1659", "2006", "2016", "2056", "20156", "546456565", "16:15:59", "1655-10-14", "1659-09-29", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("7",new LinkedHashSet<>(Arrays.asList("07", "17", "2017", "19:17:17", "2056-07-16")));
        expectedIndexedCollection.put("8",new LinkedHashSet<>(Arrays.asList("08", "18", "1658", "2018", "19:18", "2016-08-14")));
        expectedIndexedCollection.put("9",new LinkedHashSet<>(Arrays.asList("09", "19", "29", "59", "1659", "2019", "16:15:59", "19:17:17", "19:18", "1659-09-29")));


        return expectedIndexedCollection;
    }
}
