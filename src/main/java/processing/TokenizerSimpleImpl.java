package processing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vietlc on 6/5/16.
 */
public class TokenizerSimpleImpl implements Tokenizer {
    /*
     *  This implementation uses space and other special character to seperate the words in a sentence
     */

    private static String pattern = "[\\s,:;\\.\\?!\\[\\]\\(\\)\\{\\}\\+-/%=\\|~`&\"\\^<>]+|\\d+";
    /* NOTE:
        1. Words are separated by:
            space, tab, comma, colon, semicolon, full stop, question mark, exclaimation
            brackets ( ) [ ] { }
            sign + - * / % = | ~ ` & " ^ > <
        2. The following are not considered a word
            digit (they are numbers)
        3. Words are counted in the case insensitive fashion
    */

    @Override
    public List<String> tokenize(String line) {
        List<String> normalizedWords = new ArrayList<>();

        String[] words = line.split(pattern);
        for(int i = 0; i < words.length ; i++) {
            String word = words[i].toLowerCase();
            if(!word.isEmpty()) {
                normalizedWords.add(normalize(word));
            }
        }

        return normalizedWords;
    }

    @Override
    public String normalize(String word) {
        /*
         * Temporarily do nothing.
         */

        if(true) {
            return word;
        } else {
            return "__error__";
        }
    }


}
