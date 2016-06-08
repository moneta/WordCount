The problem is to create a Word Count program. The basic version would take a file, count the words, output a file with one word/count pair per line

The solution is coded in JAVA as follows:

1. The WordCount is the main program.

2. The FileHandle, Counter and Tokenizer,,. are loose coupling.

3. 1st, WordCount couples with FileHandlerSimpleImpl, TokenizerSimpleImpl and CounterSimpleImpl to run the word counting.
    - FileHandlerSimpleImpl: read the entire file content in one shot and then process them all.
    - TokenizerSimpleImpl: separate words in a line simply by the space and the special characters like , . ; : ....
    - CounterSimpleImpl: process the entire data to count all the words in one shot.

4. 2nd: WordCount couples with FileHandlerBetterImpl. It uses bufferedReader to improve the IO reading performance.

5. 3nd: WordCount now couples with CounterBetterImpl to read and process a file content in blocks. This helps when the file size gets larger.

6. 4nd: WordCount now couples with TokenizerDictImpl to improve the tokenization. It uses dictionary to make sure that a word must be in the dictionary. The dictionary has a utility to build and print a dictionary from/to text files.

7. 5nd: WordCount now couples with WordCountConcurently to create multi-thread processing of the word counting. This should help improve performance in case the file size get very large.


And so, here you go. Hope someone might find this useful.
