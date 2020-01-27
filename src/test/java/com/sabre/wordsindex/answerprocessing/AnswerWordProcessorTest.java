package com.sabre.wordsindex.answerprocessing;

import com.sabre.wordsindex.answerprocessing.processor.AnswerProcessor;
import com.sabre.wordsindex.answerprocessing.processor.AnswerWordProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = AnswerWordProcessor.class)
@DisplayName("Test of processing words from answer text")
public class AnswerWordProcessorTest {

    @Spy
    private final AnswerProcessor answerWordProcessor = new AnswerWordProcessor();

    @DisplayName("Should remove all tab, space and comma characters and returns collection of words")
    @ParameterizedTest
    @ValueSource(strings = {
            "Ala has a cat, a cat develops a cat in Java",
            "               Ala                         has a cat, a cat develops a cat in Java                         ",
            "                 Ala has \t\t     a cat, a     cat       develops a     cat    in Java                       ",
            "\t\t\tAla \t has\t\t a cat,\t\ta cat develops  \t\t\t\t\ta cat   in Java\t\t\t\t\t\t\t\t",
            ",,Ala, has,,,,a,cat, a cat,develops a cat in , Java,,,,,,,,,,,,,,,,,,,,,,,,,,,"
    })
    void removeUnacceptedCharactersAndReturnsWordsCollection(String text) {
        Set<String> splitAnswer = answerWordProcessor.collectWordsFrom(text);
        assertThat(splitAnswer).isEqualTo(new TreeSet<>(Arrays.asList( "a", "ala", "cat", "develops", "has", "in", "java")));
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should throw exception if text is null")
    @Test
    void throwExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerWordProcessor.collectWordsFrom(null));
    }

    @DisplayName("Indexing builder should throw exception if text is null")
    @Test
    void IndexingBuilderShouldThrowExceptionWhenTextIsNull() {
        assertThrows(NullPointerException.class, () ->
                answerWordProcessor.collectWordsFrom(null));
    }

    @DisplayName("Should collects single words correctly")
    @Test
    void shouldCollectsSingleWordsCorrectly() {
        String text = "Ala-2 22 222/565 2:566 25.26 --has a25.26.2006 02:23  %%cat,^ <2a> {:''\"\"@!}## $$$cat6;develops**  " +
                "\ta--------2cat:in ~[Java]/?||\\ according12*12 to& (java-guide)_=+``~~ during to fly with44/1out aviation4all " +
                "and working in B2B work syst546456565em";

        Set<String> splitAnswer = answerWordProcessor.collectWordsFrom(text);

        List<String> words = Arrays.asList("a", "according", "ala", "all", "and", "aviation", "b", "cat", "develops",
                "during", "em", "fly", "guide", "has", "in", "java", "out", "syst", "to", "with", "work", "working");

        assertThat(splitAnswer).isEqualTo(new TreeSet<>(words));
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should split joined by <-> phrasal to single words")
    @Test
    void shouldSplitJoinedPhrasalToSingleWords() {
        String text = "java-guide, business-two-business, easily-confused, self-confident";

        Set<String> splitAnswer = answerWordProcessor.collectWordsFrom(text);

        List<String> words = Arrays.asList("business", "confident", "confused", "easily", "guide", "java", "self", "two");

        assertThat(splitAnswer).isEqualTo(new TreeSet<>(words));
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should split phrasal contained the digits to the single words")
    @Test
    void shouldSplitPhrasalWithDigitsToSingleWords() {
        String text = "java4people, smart4aviation, b2b, self3123123123123confident";

        Set<String> splitAnswer = answerWordProcessor.collectWordsFrom(text);

        List<String> words = Arrays.asList("aviation", "b", "confident", "java", "people", "self", "smart");

        assertThat(splitAnswer).isEqualTo(new TreeSet<>(words));
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should split phrasal contained the digits to the single words")
    @Test
    void shouldWords() {
        String text = "    \t\t\t\t       \r\r\r \n\n\n\n\n                      ";

        Set<String> splitAnswer = answerWordProcessor.collectWordsFrom(text);

        List<String> words = Collections.singletonList("");

        assertThat(splitAnswer).isEqualTo(new TreeSet<>(words));
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should sort indexes and words using the alphabet, including local language characteristics")
    @Test
    void shouldSortUsingAlphabetIncludingLocalLanguageCharacteristic() {
        String text = "kąty będzie żreć źdźbło ä ésdfsdfé ósdfsdó ÁffffsdÁ ŐŐgsdgsfŐŐ  őhrtryrhő  ĎlsdfĎ niańczyć ŇkasofŇ ÝosdfsdfÝ";

        Map<String, Set<String>> splitAnswer = answerWordProcessor.buildIndexedWordsCollection(text);
        Map<String, Set<String>> expectedIndexedCollection = buildCollectionIndexedByLocalCharacteristic();

        assertThat(splitAnswer).isEqualTo(expectedIndexedCollection);
        Mockito.verify(answerWordProcessor, times(1)).collectWordsFrom(text);
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    @DisplayName("Should build indexed collection of words correctly")
    @Test
    void shouldBuildIndexedCollectionOfWordsCorrectly() {
        String text = "\t\t\tAla-2 \t\t22 222/565 2:566,,,,,,,,,, 25.26 --has a25.26.2006 02:23  %%,,,,,,,,,,,cat,^ <2a> " +
                "{:''\"\"@!}## $$$cat6;develops**  \ta--------2cat:in ~[\tJava]/?||\\ \taccording12*12 to& (java-guide)_=+``~~ " +
                "during to fly ,with44/1out ,,aviation4all and working in B2B work syst546456565em";

        Map<String, Set<String>> splitAnswer = answerWordProcessor.buildIndexedWordsCollection(text);
        Map<String, Set<String>> expectedIndexedCollection = buildExpectedIndexedCollection();

        assertThat(splitAnswer).isEqualTo(expectedIndexedCollection);
        Mockito.verify(answerWordProcessor, times(1)).collectWordsFrom(text);
        Mockito.verify(answerWordProcessor, times(1)).processAnswer(text);
    }

    private Map<String, Set<String>> buildExpectedIndexedCollection() {
        Map<String, Set<String>> expectedIndexedCollection = new TreeMap<>();
        expectedIndexedCollection.put("a",new TreeSet<>(Arrays.asList("a", "according", "ala", "all", "and", "aviation", "cat", "has", "java")));
        expectedIndexedCollection.put("b",new TreeSet<>(Arrays.asList("b")));
        expectedIndexedCollection.put("c",new TreeSet<>(Arrays.asList("according", "cat")));
        expectedIndexedCollection.put("d",new TreeSet<>(Arrays.asList("according", "and", "develops", "during", "guide")));
        expectedIndexedCollection.put("e",new TreeSet<>(Arrays.asList("develops", "em", "guide")));
        expectedIndexedCollection.put("f",new TreeSet<>(Arrays.asList("fly")));
        expectedIndexedCollection.put("g",new TreeSet<>(Arrays.asList("according", "during", "guide", "working")));
        expectedIndexedCollection.put("h",new TreeSet<>(Arrays.asList("has", "with")));
        expectedIndexedCollection.put("i",new TreeSet<>(Arrays.asList("according", "aviation", "during", "guide", "in", "with", "working")));
        expectedIndexedCollection.put("j",new TreeSet<>(Arrays.asList("java")));
        expectedIndexedCollection.put("k",new TreeSet<>(Arrays.asList("work", "working")));
        expectedIndexedCollection.put("l",new TreeSet<>(Arrays.asList("ala", "all", "develops", "fly")));
        expectedIndexedCollection.put("m",new TreeSet<>(Arrays.asList("em")));
        expectedIndexedCollection.put("n",new TreeSet<>(Arrays.asList("according", "and", "aviation", "during", "in", "working")));
        expectedIndexedCollection.put("o",new TreeSet<>(Arrays.asList("according", "aviation", "develops", "out", "to", "work", "working")));
        expectedIndexedCollection.put("p",new TreeSet<>(Arrays.asList("develops")));
        expectedIndexedCollection.put("r",new TreeSet<>(Arrays.asList("according", "during", "work", "working")));
        expectedIndexedCollection.put("s",new TreeSet<>(Arrays.asList("develops", "has", "syst")));
        expectedIndexedCollection.put("t",new TreeSet<>(Arrays.asList("aviation", "cat", "out", "syst", "to", "with")));
        expectedIndexedCollection.put("u",new TreeSet<>(Arrays.asList("during", "guide", "out")));
        expectedIndexedCollection.put("v",new TreeSet<>(Arrays.asList("aviation", "develops", "java")));
        expectedIndexedCollection.put("w",new TreeSet<>(Arrays.asList("with", "work", "working")));
        expectedIndexedCollection.put("y",new TreeSet<>(Arrays.asList("fly", "syst")));
        return expectedIndexedCollection;
    }

    private Map<String, Set<String>> buildCollectionIndexedByLocalCharacteristic() {
        Map<String, Set<String>> expectedIndexedCollection = new TreeMap<>();
        expectedIndexedCollection.put("a",new TreeSet<>(Arrays.asList("niańczyć", "ňkasofň")));
        expectedIndexedCollection.put("á",new TreeSet<>(Arrays.asList("áffffsdá")));
        expectedIndexedCollection.put("ä",new TreeSet<>(Arrays.asList("ä")));
        expectedIndexedCollection.put("ą",new TreeSet<>(Arrays.asList("kąty")));
        expectedIndexedCollection.put("b",new TreeSet<>(Arrays.asList("będzie", "źdźbło")));
        expectedIndexedCollection.put("c",new TreeSet<>(Arrays.asList("niańczyć")));
        expectedIndexedCollection.put("ć",new TreeSet<>(Arrays.asList("niańczyć", "żreć")));
        expectedIndexedCollection.put("d",new TreeSet<>(Arrays.asList("áffffsdá", "będzie", "ďlsdfď", "ésdfsdfé", "őőgsdgsfőő", "ósdfsdó", "ýosdfsdfý", "źdźbło")));
        expectedIndexedCollection.put("ď",new TreeSet<>(Arrays.asList("ďlsdfď")));
        expectedIndexedCollection.put("e",new TreeSet<>(Arrays.asList("będzie", "żreć")));
        expectedIndexedCollection.put("é",new TreeSet<>(Arrays.asList("ésdfsdfé")));
        expectedIndexedCollection.put("ę",new TreeSet<>(Arrays.asList("będzie")));
        expectedIndexedCollection.put("f",new TreeSet<>(Arrays.asList("áffffsdá", "ďlsdfď", "ésdfsdfé", "ňkasofň", "őőgsdgsfőő", "ósdfsdó", "ýosdfsdfý")));
        expectedIndexedCollection.put("g",new TreeSet<>(Arrays.asList("őőgsdgsfőő")));
        expectedIndexedCollection.put("h",new TreeSet<>(Arrays.asList("őhrtryrhő")));
        expectedIndexedCollection.put("i",new TreeSet<>(Arrays.asList("będzie", "niańczyć")));
        expectedIndexedCollection.put("k",new TreeSet<>(Arrays.asList("kąty", "ňkasofň")));
        expectedIndexedCollection.put("l",new TreeSet<>(Arrays.asList("ďlsdfď")));
        expectedIndexedCollection.put("ł",new TreeSet<>(Arrays.asList("źdźbło")));
        expectedIndexedCollection.put("n",new TreeSet<>(Arrays.asList("niańczyć")));
        expectedIndexedCollection.put("ň",new TreeSet<>(Arrays.asList("ňkasofň")));
        expectedIndexedCollection.put("ń",new TreeSet<>(Arrays.asList("niańczyć")));
        expectedIndexedCollection.put("o",new TreeSet<>(Arrays.asList("ňkasofň", "ýosdfsdfý", "źdźbło")));
        expectedIndexedCollection.put("ő",new TreeSet<>(Arrays.asList("őhrtryrhő", "őőgsdgsfőő")));
        expectedIndexedCollection.put("ó",new TreeSet<>(Arrays.asList("ósdfsdó")));
        expectedIndexedCollection.put("r",new TreeSet<>(Arrays.asList("őhrtryrhő", "żreć")));
        expectedIndexedCollection.put("s",new TreeSet<>(Arrays.asList("áffffsdá", "ďlsdfď", "ésdfsdfé", "ňkasofň", "őőgsdgsfőő", "ósdfsdó", "ýosdfsdfý")));
        expectedIndexedCollection.put("t",new TreeSet<>(Arrays.asList("kąty", "őhrtryrhő")));
        expectedIndexedCollection.put("y",new TreeSet<>(Arrays.asList("kąty", "niańczyć", "őhrtryrhő")));
        expectedIndexedCollection.put("ý",new TreeSet<>(Arrays.asList("ýosdfsdfý")));
        expectedIndexedCollection.put("z",new TreeSet<>(Arrays.asList("będzie", "niańczyć")));
        expectedIndexedCollection.put("ź",new TreeSet<>(Arrays.asList("źdźbło")));
        expectedIndexedCollection.put("ż",new TreeSet<>(Arrays.asList("żreć")));
        return expectedIndexedCollection;
    }

}
