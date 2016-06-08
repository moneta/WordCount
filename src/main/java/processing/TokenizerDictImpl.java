package processing;

import Utils.MyDictionary;
import dto.DictTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vietlc on 6/5/16.
 */
public class TokenizerDictImpl implements Tokenizer {
    private MyDictionary dictionary;

    public TokenizerDictImpl(MyDictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public List<String> tokenize(String line) {

        List<String> normalizedWords = new ArrayList<>();

        DictTree.DictNode root = dictionary.getRootNode();
        DictTree.DictNode node = root;

        String match = "";
        int startPosition = 0, matchPosition = -1;
        for(int i = startPosition; i < line.length() ; i++) {
            Character c = line.charAt(i);

            DictTree.DictNode nextNode = node.searchDirectChildren(c, node);
            if(!nextNode.equals(node)) {
                if(nextNode.isTokenizable()) {
                    // First match of the word. Let's keep this word to save it if the followings do not match
                    match = dictionary.buildCOmpletedDataFromChildNode(nextNode);
                    matchPosition = i;
                }

                // Found the next match in the dictionary tree >> keep matching the next character
                node = nextNode;

            } else {
                if(matchPosition != -1) {
                    normalizedWords.add(match);

                    // Restart the for loop at the last matched position
                    i = startPosition = matchPosition + 1;

                    matchPosition = -1;

                    node = root;

                } else {
                    // This word is not in the dictionary, start over with the next position
                    startPosition++;

                    i = startPosition;

                    node = root;
                }
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
