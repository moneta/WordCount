package Utils;

import dto.DictTree;
import io.FileHandle;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by Vietlc on 6/5/16.
 */
public class MyDictionarySimpleImpl implements MyDictionary {
    private FileHandle fileHandle;
    public DictTree<Character> dictTree;

    public MyDictionarySimpleImpl(FileHandle fileHandle) {
        this.fileHandle = fileHandle;
        this.dictTree = new DictTree('_');
    }

    @Override
    public DictTree<Character>.DictNode getRootNode() {
        return this.dictTree.root;
    }

    @Override
    public String buildCOmpletedDataFromChildNode(DictTree<Character>.DictNode node) {
        if(node.parent != null) {
            return buildCOmpletedDataFromChildNode(node.parent) + node.data;
        } else {
            return "";
        }
    }

    @Override
    public void buildDictFromWordList() {
        /*
         * Dictionary is defined as list of words in the dictionary that are seperated by a space
         */

        List<String> lines;
        Integer N = 100; // Process 100 lines every round

        while((lines = fileHandle.ReadNextNLines(N)) != null) {
            for(ListIterator<String> iterator = lines.listIterator(); iterator.hasNext();) {
                String line = iterator.next();

                // Uncomment the below to test with non-standard dictionary text file as being defined above
                String[] words = line.split("[\\s,:;\\.\\?!\\[\\]\\(\\)\\{\\}\\+-/%=\\|~`&\"\\^<>\r\n]+|\\d+");
                //String[] words = line.split(" ");

                // For each word in the line:
                for (int i = 0; i < words.length; i++) {
                    String word = words[i].toLowerCase();

                    if(!word.isEmpty()) {
                        DictTree<Character>.DictNode node = this.dictTree.root;

                        // For each letter in a word:
                        for (int j = 0; j < word.length(); j++) {
                            DictTree<Character>.DictNode nextNode = node.searchDirectChildren(word.charAt(j), node);

                            if (nextNode.equals(node)) {
                                // NOT FOUND. Let's add this word into the dictionary
                                DictTree.DictNode newChildNode = node.addChild(word.charAt(j));

                                node = newChildNode;
                            } else {
                                // Keep on searching in the next matched node
                                node = nextNode;
                            }
                        }

                        // Mark the end of word with 0x00 to differentiate between "the" and "these"
                        if (node.searchDirectChildren((char) 0, node).equals(node)) {
                            node.addChild((char) 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void printDictionary(String media, DictTree<Character>.DictNode node) {
        for (int i = 0; i < node.children.size(); i++) {
            DictTree<Character>.DictNode childNode = node.children.get(i);

            if(!childNode.isTokenizable()) {
                printDictionary(media, childNode);
            } else {
                if (media.equals("CONSOLE")) {
                    // Let's just print the dict to the console first
                    System.out.println(buildCOmpletedDataFromChildNode(childNode));

                } else if (media.equals("FILE")) {
                    // Let's save the dictionary to the file
                    fileHandle.writeSingleLine(buildCOmpletedDataFromChildNode(childNode));
                }
            }
        }

    }
}
