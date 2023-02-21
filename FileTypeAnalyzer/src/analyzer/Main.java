package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

class Algorithm{
    private AlgorithmStrategy strategy;

    public Algorithm(AlgorithmStrategy strategy) {
        this.strategy = strategy;
    }
    public boolean search(String text, String pattern){
        return this.strategy.hasPattern(text, pattern);
    }
}

interface AlgorithmStrategy{
    boolean hasPattern(String text, String pattern);
}

class Naive implements AlgorithmStrategy{
    @Override
    public boolean hasPattern(String text, String pattern) {
        int textLength = text.length();
        int patternLength = pattern.length();
        for (int i = 0; i<textLength - patternLength + 1; i++) {
            for (int j = 0; j< patternLength; j++) {
                if (!(text.charAt(i+j) == pattern.charAt(j))) {
                    break;
                }
                else if (j == patternLength -1) return true;
            }
        }
        return false;
    }
}

class KMP implements AlgorithmStrategy{
    public Integer[] calculatePrefixFunction(String pattern){
        Integer[] result = new Integer[pattern.length()];
        result[0] = 0;
        for(int i = 1; i < pattern.length(); i++){
            int j = result[i - 1];
            if(pattern.charAt(i) == pattern.charAt(j)){
                result[i] = j + 1;
            } else {
                while(!(pattern.charAt(i) == pattern.charAt(j)) && j != 0){
                    j = result[j - 1];
                }
                if(j == 0) {
                    result[i] = 0;
                } else if(pattern.charAt(i) == pattern.charAt(j)){
                    result[i] = j + 1;
                }
            }
        }
        return result;
    }
    @Override
    public boolean hasPattern(String text, String pattern) {
        int M = pattern.length();
        int N = text.length();
        Integer[] lps = new Integer[M];
        int j = 0;
        lps = calculatePrefixFunction(pattern);
        int i = 0;
        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                return true;
            }

            // mismatch after j matches
            else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return false;
    }
}



public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        String directoryPath = args[0];
        ArrayList<ArrayList<String>> patternsDefinitions = new ArrayList<>();
        for(String patternDefinition : Files.readAllLines(Path.of(args[1]))){
            ArrayList<String> split = new ArrayList<>(List.of(patternDefinition.split(";")));
            String value = split.get(0);
            String pattern = split.get(1).replaceAll("\"", "");
            String response = split.get(2).replaceAll("\"", "");
            patternsDefinitions.add(new ArrayList<>(List.of(value, pattern, response)));
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        File directory = new File(directoryPath);
        if(directory.exists() && directory.isDirectory()){
            for(File file: directory.listFiles()){
                if(file.isFile()){
                    executorService.submit(() -> {
                        ArrayList<Integer> matched = new ArrayList<>();
                        for(int i = 0; i < patternsDefinitions.size(); i++){
                            try {
                                if(new KMP().hasPattern(Files.readString(Path.of(file.getPath())), patternsDefinitions.get(i).get(1))){
                                    matched.add(i);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(matched.isEmpty()){
                            System.out.println(file.getName() + ": Unknown file type");
                        } else {
                            Integer biggestValue = Integer.MIN_VALUE;
                            int index = 0;
                            for(int i = 0; i < matched.size(); i++){
                                Integer value = Integer.parseInt(patternsDefinitions.get(matched.get(i)).get(0));
                                if(value > biggestValue){
                                    index = i;
                                }
                            }
                            System.out.println(file.getName() + ": " + patternsDefinitions.get(matched.get(index)).get(2));
                        }
                    });
                }
            }
        }
        executorService.shutdown();
        executorService.awaitTermination(1000L, TimeUnit.SECONDS);
    }
}
