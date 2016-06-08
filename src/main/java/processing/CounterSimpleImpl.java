package processing;

import io.FileHandle;

import java.util.*;

/**
 * Created by Vietlc on 6/3/16.
 */
public class CounterSimpleImpl implements Counter {
    private Map<String, Long> map;

    private FileHandle fileHandle;
    private Tokenizer tokenizer;

    public CounterSimpleImpl(FileHandle fileHandle, Tokenizer tokenizer) {
        this.fileHandle = fileHandle;
        this.tokenizer = tokenizer;

        this.map = new HashMap<>();
    }

    @Override
    public void count(List<String> lines) {
        for(String line : lines) {
            List<String> words = tokenizer.tokenize(line);

            for(ListIterator<String> iterator = words.listIterator(); iterator.hasNext();) {
                String word = iterator.next();

                Long freq = this.map.get(word);
                this.map.put(word, freq == null ? 1 : freq + 1);
            }
        }
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
        return null;
    }

    @Override
    public void update(Map<String, Long> partial) {}


}
