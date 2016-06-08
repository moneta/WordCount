package main;

import Utils.MyDictionary;
import Utils.MyDictionarySimpleImpl;
import io.FileHandle;
import io.FileHandlerBetterImpl;
import io.FileHandlerConcurencyImpl;
import io.FileHandlerSimpleImpl;
import processing.*;

import java.io.IOException;
import java.lang.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Vietlc on 6/3/16.
 */
public class WordCount {

    public static void main(String[] arg) {
        List<String> lines;
        Long start, end;
        String filename, charset, dictionaryFile = "";

        if(arg.length == 0) {
            System.out.print("Usage: WordCount <filename> [charset=UTF-8] <>dictionary_file_name");
            return;
        } else if(arg.length == 1) {
            filename = arg[0];
            charset = "UTF-8";
        } else if(arg.length == 2) {
            filename = arg[0];
            charset = arg[1];
        } else {
            filename = arg[0];
            charset = arg[1];
            dictionaryFile = arg[2];
        }


        System.out.println("/**************-----------------------------------------**************/");
        System.out.println("Let's assume the file is small, let's do it in one shot");
        try {
            FileHandle fileHandle;
            fileHandle = new FileHandlerSimpleImpl(filename, charset);

            Tokenizer tokenizer = new TokenizerSimpleImpl();

            // Inject FileHandlerSimple to the Counter
            Counter counter = new CounterSimpleImpl(fileHandle, tokenizer);

            start = System.currentTimeMillis();

            lines = fileHandle.ReadAllLines();
            counter.count(lines);
            counter.write();

            end = System.currentTimeMillis();
            System.out.println("Runtime: " + (end - start));

        } catch (Exception e) {
            e.printStackTrace();;
        }


        System.out.println("/**************-----------------------------------------**************/");
        System.out.println("Now if file sizes go larger, let's use the BufferWrtrer. Note: the CounterSimpleImpl is used still");
        try {
            FileHandle betterFileHandler;    // Using read/write buffer to improve the IO handling
            betterFileHandler = new FileHandlerBetterImpl(filename, filename + ".counted.2", charset);

            Tokenizer tokenizer = new TokenizerSimpleImpl();

            Counter counter = new CounterSimpleImpl(betterFileHandler, tokenizer);

            start = System.currentTimeMillis();

            lines = betterFileHandler.ReadAllLines();
            counter.count(lines);
            counter.write();

            end = System.currentTimeMillis();
            System.out.println("Runtime: " + (end - start) );

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("/**************-----------------------------------------**************/");
        System.out.println("What if file size grows to tens thousand lines? >> Read all lines in one shot will not be a good idea");
        try {
            FileHandle betterFileHandler;    // Using read/write buffer to improve the IO handling
            betterFileHandler = new FileHandlerBetterImpl(filename, filename + ".counted.3", charset);

            Tokenizer tokenizer = new TokenizerSimpleImpl();

            Counter counter = new CounterBetterImpl(betterFileHandler, tokenizer);

            start = System.currentTimeMillis();

            int N = 3;  // Number of lines to read for every processing. This can be adjust to get better performance for
                        // both IOreading and counter processing

            while( (lines = betterFileHandler.ReadNextNLines(N)) != null) {
                Map<String, Long> partial = counter.partialCount(lines);

                counter.update(partial);
            }

            counter.write();

            end = System.currentTimeMillis();
            System.out.println("Runtime: " + (end - start) );

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("/**************-----------------------------------------**************/");
        System.out.println("What id there is error in the text file, ex: typo errors. Let's try to use dictionary");
        try {
            if(!dictionaryFile.isEmpty()) {
                FileHandle dictFileHandler;    // Using read/write buffer to improve the IO handling
                dictFileHandler = new FileHandlerBetterImpl(dictionaryFile, dictionaryFile + ".result", charset);

                MyDictionary myDictionary = new MyDictionarySimpleImpl(dictFileHandler);

                // Let's build the dictionary first
                myDictionary.buildDictFromWordList();

                // Let's print the dictionary onto the console
                myDictionary.printDictionary("FILE", myDictionary.getRootNode());

                // Let's just count the words which are in the dictionary
                FileHandle betterFileHandler;    // Using read/write buffer to improve the IO handling
                betterFileHandler = new FileHandlerBetterImpl(filename, filename + ".counted.4", charset);

                // Using the Dictionary Tokenizer
                Tokenizer tokenizer = new TokenizerDictImpl(myDictionary);

                Counter counter = new CounterBetterImpl(betterFileHandler, tokenizer);

                start = System.currentTimeMillis();

                int N = 3;  // Number of lines to read for every processing. This can be adjust to get better performance for
                            // both IOreading and counter processing

                while( (lines = betterFileHandler.ReadNextNLines(N)) != null) {
                    Map<String, Long> partial = counter.partialCount(lines);

                    counter.update(partial);
                }

                counter.write();

                end = System.currentTimeMillis();
                System.out.println("Runtime: " + (end - start) );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("/**************-----------------------------------------**************/");
        System.out.println("What if file got tens million of lines? >> Waiting for each blocks of lines to be processed in order will not be a good idea");
        //if(false)
        try {
            FileHandle concurentFileHandler;    // Using read/write buffer to improve the IO handling
            concurentFileHandler = new FileHandlerConcurencyImpl(filename, filename + ".counted.5", charset);

            WordCountConcurently wordCounter = new WordCountConcurently(concurentFileHandler);

            wordCounter.count();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
