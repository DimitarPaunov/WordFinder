package com.example.interviewproject;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class Main {

    public static Set<String> allWords;

    public static void main(String[] args) throws IOException {

        URL url = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");
        try (InputStreamReader streamReader = new InputStreamReader(url.openStream());
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            allWords = bufferedReader.lines()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        }

        long startTime = System.nanoTime();

        List<String> nineLetterWords = allWords
                .stream()
                .filter(word -> word.length() == 9 && (word.contains("a") || word.contains("i")))
                .toList();

        Set<String> reducibleWords = new HashSet<>();

        for (String nineLetterWord : nineLetterWords) {
            if (canReduceToOneLetterWord(nineLetterWord)) {
                reducibleWords.add(nineLetterWord);
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        System.out.println(reducibleWords.size());
        reducibleWords.forEach(System.out::println);
        System.out.println("Total time taken: " + duration + " milliseconds");
    }

    private static boolean canReduceToOneLetterWord(String word) {

        if (!word.contains("a") && !word.contains("i")) {
            return false;
        }

        if (word.length() == 2) {
            return allWords.contains(word);
        }

        for (int i = 0; i < word.length(); i++) {
            String modifiedWord = word.substring(0, i) + word.substring(i + 1);
            if (allWords.contains(modifiedWord) && canReduceToOneLetterWord(modifiedWord)) {
                return true;
            }
        }

        return false;
    }
}
