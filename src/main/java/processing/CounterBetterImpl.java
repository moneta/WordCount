package processing;

import io.FileHandle;

import java.util.*;

/**
 * Created by Vietlc on 6/4/16.
 */
public class CounterBetterImpl implements Counter {
    private Map<String, Long> map;

    private FileHandle fileHandle;
    private Tokenizer tokenizer;

    public CounterBetterImpl(FileHandle fileHandle, Tokenizer tokenizer) {
        this.fileHandle = fileHandle;
        this.tokenizer = tokenizer;

        this.map = new HashMap<>();
    }

    @Override
    public void count(List<String> lines) {
        this.map = partialCount(lines);
    }

    @Override
    public void write() {
        List<String> lines = new ArrayList<>();

        for(Map.Entry<String, Long> entry: this.map.entrySet()) {
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

            Long freq = this.map.get(key);
            this.map.put(key, freq == null ? val: freq + val);
        }
    }
}
