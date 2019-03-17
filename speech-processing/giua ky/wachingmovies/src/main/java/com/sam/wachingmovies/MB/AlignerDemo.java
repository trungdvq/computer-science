/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sam.wachingmovies.MB;

import edu.cmu.sphinx.alignment.LongTextAligner;
import edu.cmu.sphinx.api.SpeechAligner;
import edu.cmu.sphinx.result.WordResult;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author SamFisher
 */
public class AlignerDemo {
    private static final String ACOUSTIC_MODEL_PATH =
            "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH =
            "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String TEXT = "one zero zero zero one nine oh two "
            + "one oh zero one eight zero three";

    public static void main(String args[]) throws Exception {
        URL audioUrl;
        String transcript;
        if (args.length > 1) {
            audioUrl = new File(args[0]).toURI().toURL();
            Scanner scanner = new Scanner(new File(args[1]));  
            scanner.useDelimiter("\\Z");  
            transcript = scanner.next();
            scanner.close();
        } else {
            audioUrl = new URL("https://www.uni-due.de/IERC/LOU_Drogheda_U2_M_18.wav");
            transcript = TEXT;
        }
        String acousticModelPath =
                (args.length > 2) ? args[2] : ACOUSTIC_MODEL_PATH;
        String dictionaryPath = (args.length > 3) ? args[3] : DICTIONARY_PATH;
        String g2pPath = (args.length > 4) ? args[4] : null;
        SpeechAligner aligner =
                new SpeechAligner(acousticModelPath, dictionaryPath, g2pPath);

        List<WordResult> results = aligner.align(audioUrl, transcript);
        List<String> stringResults = new ArrayList<String>();
        for (WordResult wr : results) {
            stringResults.add(wr.getWord().getSpelling());
            System.out.println("workL: + "+ wr.getWord().toString());
        }
        
        LongTextAligner textAligner =
                new LongTextAligner(stringResults, 2);
        List<String> sentences = aligner.getTokenizer().expand(transcript);
        List<String> words = aligner.sentenceToWords(sentences);
        
        int[] aid = textAligner.align(words);
        
        int lastId = -1;
        for (int i = 0; i < aid.length; ++i) {
            if (aid[i] == -1) {
                System.out.format("- %s\n", words.get(i));
            } else {
                if (aid[i] - lastId > 1) {
                    for (WordResult result : results.subList(lastId + 1,
                            aid[i])) {
                        System.out.format("+ %-25s [%s]\n", result.getWord()
                                .getSpelling(), result.getTimeFrame());
                    }
                }
                System.out.format("  %-25s [%s]\n", results.get(aid[i])
                        .getWord().getSpelling(), results.get(aid[i])
                        .getTimeFrame());
                lastId = aid[i];
            }
        }

        if (lastId >= 0 && results.size() - lastId > 1) {
            for (WordResult result : results.subList(lastId + 1,
                    results.size())) {
                System.out.format("+ %-25s [%s]\n", result.getWord()
                        .getSpelling(), result.getTimeFrame());
            }
        }
    }
}
