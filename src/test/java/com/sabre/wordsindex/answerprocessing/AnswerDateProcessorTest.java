package com.sabre.wordsindex.answerprocessing;

import com.sabre.wordsindex.answerprocessing.processor.AnswerDateProcessor;
import com.sabre.wordsindex.answerprocessing.processor.AnswerProcessor;
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
@DisplayName("Test of processing dates from answer text")
public class AnswerDateProcessorTest {

    @Spy
    private final AnswerProcessor answerDateProcessor = new AnswerDateProcessor();

    @DisplayName("Should throw exception if text is null")
    @Test
    void throwExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerDateProcessor.collectWordsFrom(null));
    }

    @DisplayName("Indexing builder should throw exception if text is null")
    @Test
    void indexingBuilderShouldThrowExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerDateProcessor.buildIndexedWordsCollection(null));
    }

    @DisplayName("Should returns sorted dates collection")
    @Test
    void shouldReturnSortedDatesCollection() {
        String text = "\t\t\t////:-31.12.2019 ąśń13.02.2018 30.02.2019 32.12.2018 31.13.2017 31-04-2017 13.12.1955---///...A16.07.2056la-2 " +
                "\t\t22 222/565 2:566,,,,,,,,,, 25.26 --has a25.26.2006 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}##29.09.1659$$$cat6;devel11.14.11.2018ops11.04.2019.2019** 01/01/2000/\" \ta----12/12/12/2012----2cat:in19-03.1658 " +
                "~[\tJava]/?||\\ \taccording12*12 to&31/04/2017 (java14.10.1655-guide)_=+`14.10.1655`~~ " +
                "during to fly ,with44/1out ,,aviatąóCion424.06.2017all13.11.20156and working in B2B work syst546456565em " +
                "kąty będzie żreć źdźbło ä ésdfsdfé14.08.2016ósdfsdó31.12.2000 ÁffffsdÁ ŐŐgsdgsfŐŐ  őhrtryrhő  ĎlsdfĎ niańczyć ŇkasofŇ ÝosdfsdfÝ" +
                "5sdasd/ ads /56:2233werwer 5-5*pd6d/6  16:15:59  16:15:5 --01/01/2000/.- 31-01-2000///.- 31/12/2000  12.02.20229-31-04-2017";

        Set<String> splitAnswer = answerDateProcessor.collectWordsFrom(text);
        assertThat(splitAnswer).isEqualTo(new TreeSet<>(Arrays.asList( "1655-10-14", "1659-09-29", "1955-12-13", "2000-01-01",
                "2000-01-31", "2000-01-31", "2000-12-31", "2016-08-14", "2018-02-13", "2019-12-31", "2056-07-16")));

        Mockito.verify(answerDateProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should build indexed collection of dates correctly")
    @Test
    void shouldBuildIndexedCollectionOfDatesCorrectly() {
        String text = "\t\t\t////:-31.12.2019 ąśń13.02.2018 30.02.2019 32.12.2018 31.13.2017 31-04-2017 13.12.1955---///...A16.07.2056la-2 " +
                "\t\t22 222/565 2:566,,,,,,,,,, 25.26 --has a25.26.2006 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}##29.09.1659$$$cat6;devel11.14.11.2018ops11.04.2019.2019** 01/01/2000/\" \ta----12/12/12/2012----2cat:in19-03.1658 " +
                "~[\tJava]/?||\\ \taccording12*12 to&31/04/2017 (java14.10.1655-guide)_=+`14.10.1655`~~ " +
                "during to fly ,with44/1out ,,aviatąóCion424.06.2017all13.11.20156and working in B2B work syst546456565em " +
                "kąty będzie żreć źdźbło ä ésdfsdfé14.08.2016ósdfsdó31.12.2000 ÁffffsdÁ ŐŐgsdgsfŐŐ  őhrtryrhő  ĎlsdfĎ niańczyć ŇkasofŇ ÝosdfsdfÝ" +
                "5sdasd/ ads /56:2233werwer 5-5*pd6d/6  16:15:59  16:15:5 --01/01/2000/.- 31-01-2000///.- 31/12/2000  12.02.20229-31-04-2017";

        Map<String, Set<String>> splitAnswer = answerDateProcessor.buildIndexedWordsCollection(text);
        Map<String, Set<String>> expectedIndexedCollection = buildExpectedIndexedCollection();

        assertThat(splitAnswer).isEqualTo(expectedIndexedCollection);
        Mockito.verify(answerDateProcessor, times(1)).collectWordsFrom(text);
        Mockito.verify(answerDateProcessor, times(1)).processAnswer(text);
    }

    private Map<String, Set<String>> buildExpectedIndexedCollection() {
        Map<String, Set<String>> expectedIndexedCollection = new TreeMap<>();
        expectedIndexedCollection.put("0",new TreeSet<>(Arrays.asList("1655-10-14", "1659-09-29", "2000-01-01", "2000-01-31",
                "2000-12-31", "2016-08-14", "2018-02-13", "2019-12-31", "2056-07-16")));
        expectedIndexedCollection.put("1",new TreeSet<>(Arrays.asList("1655-10-14", "1659-09-29", "1955-12-13", "2000-01-01",
                "2000-01-31", "2000-12-31", "2016-08-14", "2018-02-13", "2019-12-31", "2056-07-16")));
        expectedIndexedCollection.put("2",new TreeSet<>(Arrays.asList("1659-09-29", "1955-12-13", "2000-01-01", "2000-01-31",
                "2000-12-31", "2016-08-14", "2018-02-13", "2019-12-31", "2056-07-16")));
        expectedIndexedCollection.put("3",new TreeSet<>(Arrays.asList("1955-12-13", "2000-01-31", "2000-12-31", "2018-02-13", "2019-12-31")));
        expectedIndexedCollection.put("4",new TreeSet<>(Arrays.asList("1655-10-14", "2016-08-14")));
        expectedIndexedCollection.put("5",new TreeSet<>(Arrays.asList("1655-10-14", "1659-09-29", "1955-12-13", "2056-07-16")));
        expectedIndexedCollection.put("6",new TreeSet<>(Arrays.asList("1655-10-14", "1659-09-29", "2016-08-14", "2056-07-16")));
        expectedIndexedCollection.put("7",new TreeSet<>(Arrays.asList("2056-07-16")));
        expectedIndexedCollection.put("8",new TreeSet<>(Arrays.asList("2016-08-14", "2018-02-13")));
        expectedIndexedCollection.put("9",new TreeSet<>(Arrays.asList("1659-09-29", "1955-12-13", "2019-12-31")));

        return expectedIndexedCollection;
    }
}
