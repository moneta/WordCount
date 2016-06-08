package processing;

import java.util.List;

/**
 * Created by Vietlc on 6/5/16.
 */
public interface Tokenizer {
    public List<String> tokenize(String line);

    public String normalize(String word);
}
