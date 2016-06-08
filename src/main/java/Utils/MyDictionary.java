package Utils;

import dto.DictTree;

/**
 * Created by Vietlc on 6/5/16.
 */
public interface MyDictionary {
    public DictTree<Character>.DictNode getRootNode();

    public String buildCOmpletedDataFromChildNode(DictTree<Character>.DictNode node);

    public void buildDictFromWordList();

    public void printDictionary(String media, DictTree<Character>.DictNode node);
}
