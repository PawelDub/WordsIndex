package com.sabre.wordsindex.answerprocessing;

import com.sabre.wordsindex.answerprocessing.processor.AnswerProcessor;
import com.sabre.wordsindex.answerprocessing.processor.AnswerTimeProcessor;
import com.sabre.wordsindex.answerprocessing.processor.AnswerWordProcessor;
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
@DisplayName("Test of processing words from answer text")
public class AnswerTimeProcessorTest {

    @Spy
    private final AnswerProcessor answerTimeProcessor = new AnswerTimeProcessor();

    @DisplayName("Should throw exception if text is null")
    @Test
    void throwExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerTimeProcessor.collectWordsFrom(null));
    }

    @DisplayName("Indexing builder should throw exception if text is null")
    @Test
    void indexingBuilderShouldThrowExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerTimeProcessor.buildIndexedWordsCollection(null));
    }

    @DisplayName("Should returns sorted time collection")
    @Test
    void shouldReturnSortedTimeCollection() {
        String text = "\t\t11:16:60\t11:6:56//1:14:18//: ąśń---///...A16.07.2056la-2 23:60 24:18 00:00 00:01 19:18 19:18 19:17:17 19:17:17 " +
                "\t\t22 222/565 2:566,,,,,,19:18,,,, 25.26 23::26 --has a25.26.2006:19:18 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}##29.09.1659$$$cat6;devel11.14.11.2018ops11.04.2019.2019** 01/01/2000/\" \ta----12/12/12/2012----2cat:in19-03.1658 " +
                "~[\tJava]/?||\\12:15:5 \taccording12*12 to&31/04/2017 (java14.10.1655-guide)_=+`14.10.1655`~~ " +
                "during to fl11:6y ,with44/1out 1:06,,aviatąóCion424.06.2017all13.11.20156and working in B2B work syst546456565em " +
                "kąty będzie żreć źdźbło ä ésdfsdfé14.08.2016ósdfsdó31.12.2000 ÁffffsdÁ ŐŐgsdgsfŐŐ  őhrtryrhő  ĎlsdfĎ niańczyć ŇkasofŇ ÝosdfsdfÝ" +
                "5sdasd/ ads /56:2233werwer 5-5*pd6d/6  16:15:59  16:15:5 --///.-";

        Set<String> splitAnswer = answerTimeProcessor.collectWordsFrom(text);
        assertThat(splitAnswer).isEqualTo(new TreeSet<>(Arrays.asList( "00:00", "00:01", "02:23", "16:15:59", "19:17:17", "19:18")));

        Mockito.verify(answerTimeProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should build indexed collection of dates correctly")
    @Test
    void shouldBuildIndexedCollectionOfDatesCorrectly() {
        String text =  "\t\t11:16:60\t11:6:56//1:14:18//: ąśń---///...A16.07.2056la-2 23:60 24:18 00:00 00:01 19:18 19:18 19:17:17 19:17:17 " +
                "\t\t22 222/565 2:566,,,,,,19:18,,,, 25.26 23::26 --has a25.26.2006:19:18 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}##29.09.1659$$$cat6;devel11.14.11.2018ops11.04.2019.2019** 01/01/2000/\" \ta----12/12/12/2012----2cat:in19-03.1658 " +
                "~[\tJava]/?||\\12:15:5 \taccording12*12 to&31/04/2017 (java14.10.1655-guide)_=+`14.10.1655`~~ " +
                "during to fl11:6y ,with44/1out 1:06,,aviatąóCion424.06.2017all13.11.20156and working in B2B work syst546456565em " +
                "kąty będzie żreć źdźbło ä ésdfsdfé14.08.2016ósdfsdó31.12.2000 ÁffffsdÁ ŐŐgsdgsfŐŐ  őhrtryrhő  ĎlsdfĎ niańczyć ŇkasofŇ ÝosdfsdfÝ" +
                "5sdasd/ ads /56:2233werwer 5-5*pd6d/6  16:15:59  16:15:5 --///.-";

        Map<String, Set<String>> splitAnswer = answerTimeProcessor.buildIndexedWordsCollection(text);
        Map<String, Set<String>> expectedIndexedCollection = buildExpectedIndexedCollection();

        assertThat(splitAnswer).isEqualTo(expectedIndexedCollection);
        Mockito.verify(answerTimeProcessor, times(1)).collectWordsFrom(text);
        Mockito.verify(answerTimeProcessor, times(1)).processAnswer(text);
    }

    private Map<String, Set<String>> buildExpectedIndexedCollection() {
        Map<String, Set<String>> expectedIndexedCollection = new TreeMap<>();
        expectedIndexedCollection.put("0",new TreeSet<>(Arrays.asList("00:00", "00:01", "02:23")));
        expectedIndexedCollection.put("1",new TreeSet<>(Arrays.asList("00:01", "16:15:59", "19:17:17", "19:18")));
        expectedIndexedCollection.put("2",new TreeSet<>(Arrays.asList("02:23")));
        expectedIndexedCollection.put("3",new TreeSet<>(Arrays.asList("02:23")));
        expectedIndexedCollection.put("5",new TreeSet<>(Arrays.asList("16:15:59")));
        expectedIndexedCollection.put("6",new TreeSet<>(Arrays.asList("16:15:59")));
        expectedIndexedCollection.put("7",new TreeSet<>(Arrays.asList("19:17:17")));
        expectedIndexedCollection.put("8",new TreeSet<>(Arrays.asList("19:18")));
        expectedIndexedCollection.put("9",new TreeSet<>(Arrays.asList("16:15:59", "19:17:17", "19:18")));

        return expectedIndexedCollection;
    }
}
