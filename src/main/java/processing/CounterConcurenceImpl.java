package processing;

import io.FileHandle;
import main.WordCountConcurently;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vietlc on 6/4/16.
 */
public class CounterConcurenceImpl  implements Counter, Runnable {
    private static Map<String, Long> map = new ConcurrentHashMap<>();  // This is shared data and have to be thread-safe


    private static FileHandle fileHandle = null;
    private Tokenizer tokenizer;

    private List<String> lines;

    public CounterConcurenceImpl(FileHandle fileHandle, Tokenizer tokenizer, List<String> lines) {
        if(this.fileHandle == null) this.fileHandle = fileHandle;
        this.tokenizer = tokenizer;
        this.lines = lines;
    }

    @Override
    public void count(List<String> lines) {
        this.map = partialCount(lines);
    }

    @Override
    public void write() {
        List<String> lines = new ArrayList<>();

        for(Map.Entry<String, Long> entry: map.entrySet()) {
            lines.add(entry.getKey() + " " + entry.getValue());
        }

        this.fileHandle.WriteAllLines(lines);
    }

    @Override
    public Map<String, Long> partialCount(List<String> lines) {
        Map<String, Long> partial = new HashMap<>();

        for(String line : lines) {
            List<String> words = tokenizer.tokenize(line);

            for(ListIterator<String> iterator = words.listIterator(); iterator.hasNext();) {
                String word = iterator.next();

                Long freq = this.map.get(word);
                this.map.put(word, freq == null ? 1 : freq + 1);
            }
        }

        return partial;
    }

    @Override
    public void update(Map<String, Long> partial) {
        for (Map.Entry<String, Long> entry: partial.entrySet()) {
            String key = entry.getKey();
            Long val = entry.getValue();

            synchronized (this) {
                Long freq = this.map.get(key);
                this.map.put(key, freq == null ? val : freq + val);
            }
        }
    }

    @Override
    public void run() {
        Map<String, Long> partial = this.partialCount(lines);

        this.update(partial);

        synchronized (this) {
            notifyAll();
        }
    }

    public synchronized static void Save() {
        List<String> lines = new ArrayList<>();

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            lines.add(entry.getKey() + " " + entry.getValue());
        }

        fileHandle.WriteAllLines(lines);
    }

}

