package main;

import io.FileHandle;
import processing.CounterConcurenceImpl;
import processing.Tokenizer;
import processing.TokenizerSimpleImpl;

import java.util.List;

/**
 * Created by Vietlc on 6/4/16.
 */
public class WordCountConcurently {
    private static int threadMax = 3;   // Number of thread to run the counting. This can be adjusted according to the hardware concurency
                                        // to get better performance

    private static Thread[] threads = new Thread[threadMax];
    public static Object lock = new Object();

    private FileHandle concurentFileHandler;

    public WordCountConcurently(FileHandle fileHandle) {
        this.concurentFileHandler = fileHandle;
    }

    public void count() {
        List<String> lines;
        Long start, end;

        try {

            start = System.currentTimeMillis();

            int N = 3;          // Number of lines to read for every processing. This can be adjusted to get better performance for
                                // both IOreading and counter processing

            int threadId = -1;
            while( (lines = concurentFileHandler.ReadNextNLines(N)) != null) {

                while( (threadId = getAvailableThread()) == -1) {
                    try {
                        // We don't want to have so many threads, so wait for the thread pool available
                        synchronized (lock) {
                            lock.wait();
                        }

                    } catch (InterruptedException e) { }

                }
                Tokenizer tokenizer = new TokenizerSimpleImpl();

                threads[threadId] = new Thread(new CounterConcurenceImpl(concurentFileHandler, tokenizer, lines));

                threads[threadId].start();
            }

            for(int i = 0; i < threadMax; i++) {
                if(threads[i] != null) threads[i].join();
            }

            CounterConcurenceImpl.Save();

            end = System.currentTimeMillis();
            System.out.println("Runtime: " + (end - start) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAvailableThread() {
        for (int i = 0; i < threadMax; i++) {
            if(threads[i] == null || !threads[i].isAlive()) {
                return i;
            }
        }

        return -1;
    }
}
