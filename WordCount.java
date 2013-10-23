package assignment6;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WordCount {

	public static int mCount = 0;

	/**
	 * All operations are performed on a single main thread since there is only
	 * one single input file. One suggestion for improving performance would be
	 * to split the huge input file it multiple files, create multiple
	 * readers(threads), parsing it the same root node to update. this is
	 * captured in the method {@link #multiMain(String[]) multiMain(String)}
	 * 
	 * @param args
	 *            First argument should be the path to the file. If no argument
	 *            is supplied, the program would use a default file named
	 *            Input.txt which should be located in the package folder of
	 *            this program
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mFile;
		mFile.su
		if (args.length > 0) {
			mFile = args[0];
		} else {
			mFile = "Input.txt";
		}
		WordCount mWordCount = new WordCount();
		ParseReader mReader = mWordCount.new ParseReader(mFile);
		Trie mRootNode = mWordCount.new Trie(' ');
		mReader.readFile(mRootNode);
		PrintWriter mWriter;
		try {
			mWriter = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
			mRootNode.printAll(new StringBuilder(), mWriter);
			mWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Alternative to the main. This method improves performance by reading from
	 * multiple files pass as arguments at the same time but updating the same
	 * Trie(it should be noted that to improver performance using this method,
	 * synchronization would have to be applied to the Parse methods updating
	 * the same tree
	 * 
	 * @param args
	 *            The filenames to be parsed.
	 */
	public void multiMain(String[] args) {
		if (args.length <= 0)
			args = new String[] { "Input.txt" };
		final WordCount mWordCount = new WordCount();
		final Trie mRootNode = mWordCount.new Trie(' ');
		ArrayList<Thread> mThreadReaders = new ArrayList<Thread>();
		// Creates a thread for each file but assigns the words from the
		// different file to the same tree.
		for (final String fileName : args) {
			Thread mThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					WordCount aWordCount = mWordCount;
					ParseReader mReader = aWordCount.new ParseReader(fileName);
					Trie mNode = mRootNode;
					mReader.readFile(mNode);
				}
			}, fileName);
			mThreadReaders.add(mThread);
			mThread.start();
		}

		for (Thread mThread : mThreadReaders) {
			try {
				mThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		PrintWriter mWriter;
		try {
			mWriter = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
			mRootNode.printAll(new StringBuilder(), mWriter);
			mWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * This class outlines the data structure and methods for storing the words
	 * and their count. The words are stored in a Trie format similar to a Tree
	 * where each node is identified by a character. The root node value is
	 * null, the first character of a word forms the child node of the root, the
	 * next character becomes the child of the child node and so on until the
	 * last node becomes the leaf of that branch. Words with similar initial
	 * characters in the same order would be found on the same path, but branch
	 * off where they differ. eg. the words 'that' and 'than' would have the
	 * nodes 't', 'h' and 'a' in common before branches are formed off for 't'
	 * as in that and 'n' as in than. Frequencies of the words are stored in the
	 * last node of that word
	 * 
	 */
	public class Trie {
		private char mNodeValue;
		private ArrayList<Integer> mOccurencies;
		private int mFrequency;
		private ArrayList<Trie> mChildren;

		/**
		 * @return the mNodeValue
		 */
		public char getmNodeValue() {
			return mNodeValue;
		}

		/**
		 * @return the mOccurencies
		 */
		public ArrayList<Integer> getmOccurencies() {
			return mOccurencies;
		}

		public Trie(char mNodeValue) {
			super();
			this.mNodeValue = mNodeValue;
			mFrequency = 0;
			this.mChildren = new ArrayList<Trie>();
			this.mOccurencies = new ArrayList<Integer>();
		}

		/**
		 * @param mOccurencies
		 *            the mOccurencies to set
		 */
		public void setmOccurencies(int mOccurencies) {
			if (this.mOccurencies == null)
				this.mOccurencies = new ArrayList<Integer>();
			this.mOccurencies.add(new Integer(mOccurencies));
		}

		/**
		 * @return the mFrequency
		 */
		public int getmFrequency() {
			return mFrequency;
		}

		/**
		 * Increases current Frequency by 1
		 */
		public void setmFrequency() {
			this.mFrequency++;
		}

		/**
		 * @return the mChildren List
		 */
		public ArrayList<Trie> getmChildrenList() {
			return mChildren;
		}

		/**
		 * Returns the child node of the current node identified by 'mKey'
		 * 
		 * @param mKey
		 *            the key of the child node
		 * @return the child node if its found, else null
		 */
		public Trie getChild(char mKey) {
			for (Trie mChild : mChildren) {
				if (mChild.getmNodeValue() == mKey) {
					return mChild;
				}
			}
			return null;
		}

		/**
		 * @param mChild
		 *            the child Trie or node to add
		 */
		public void addmChild(Trie mChild) {
			this.mChildren.add(mChild);
		}

		/**
		 * Recursive method which would traverse through the Trie and add new
		 * words or update occurences of existing words
		 * 
		 * @param aWord
		 *            The word to be added to the Trie
		 * @param index
		 *            The index of the character in the word which is to be used
		 *            as key
		 * @param occurence
		 *            The sentence number which this word was found.
		 */
		public void addWord(String aWord, int index, int occurence) {
			// initial check, especially to catch empty strings
			if (index >= aWord.length())
				return;
			char mKey = aWord.charAt(index);
			Trie mChild = getChild(mKey);
			if (mChild == null) {
				mChild = new Trie(mKey);
				addmChild(mChild);
			}
			/**
			 * Check for end of word. If end of word, update the frequency at
			 * the leaf node and include its occurence in the occurence list.
			 * Else, move to the next character in the string.
			 */
			if (++index >= aWord.length()) {
				mChild.setmFrequency();
				mChild.setmOccurencies(occurence);
				return;
			} else {
				mChild.addWord(aWord, index, occurence);
			}
		}

		/**
		 * A recursive method that traverses the whole Trie, starting from the
		 * current node, printing out the node value of Trie and its occurences
		 * in alphabetical order. It identifies the end of a word by looking at
		 * the value of its frequence, since the frequency of a word is stored
		 * in the last node of that word
		 * 
		 * @param mString The stringbuilder used to form the words as the tree is traversed
		 */
		public void printAll(StringBuilder mString, PrintWriter mWriter) {
			mString.append(mNodeValue);
			if (mFrequency > 0) {
				mWriter.print(mCount + ". " + mString.toString() + "\t {"
						+ mFrequency + ": ");
				for (int i = 0; i < mFrequency; i++) {
					mWriter.print(mOccurencies.get(i) + ", ");
				}
				mWriter.println("}");
				mWriter.flush();
				mCount++;
			}
			if (!mChildren.isEmpty()) {
				sort();
				for (Trie mChild : mChildren)
					mChild.printAll(mString, mWriter);
			}
			mString.deleteCharAt(mString.length() - 1);
		}

		/**
		 * A sort method which is based on the
		 * {@link java.util.Collections.sort} method. The custom defined compare
		 * method would take two Trie nodes, return 1 if the ASCII value of the
		 * character in the first node is higher than that of the second node,
		 * else return -1 if the first node value is less, else return a 0 if
		 * they are equal. This method would sort the nodes in ascending order
		 */
		public void sort() {
			Collections.sort(mChildren, new Comparator<Trie>() {

				@Override
				public int compare(Trie o1, Trie o2) {
					// TODO Auto-generated method stub
					if (o1.getmNodeValue() > o2.getmNodeValue())
						return 1;
					else if (o1.getmNodeValue() < o2.getmNodeValue())
						return -1;
					else
						return 0;
				}
			});
		}
	}

	/**
	 * This hadles the reading of the data into files and parsing the data into
	 * sentences and then further into words
	 * 
	 */
	public class ParseReader {
		BufferedReader mReader;
		String mLine;
		String[] mSentences;
		String[] mWords;
		int mCount = 0;

		public ParseReader(String filePath) {
			try {
				mReader = new BufferedReader(new FileReader(filePath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("The file " + filePath
						+ " could not be located");
			}
		}

		/**
		 * This is a basic way of splitting sentences. Actual splitting of
		 * sentences or determining the beginning and end of a sentence is a NLP
		 * problem which can best be solved using statistics and heuristics. The
		 * sentences are further devided into words which are parsed by the
		 * {@link #parse(String) parse()} method.
		 * 
		 * @param mRootNode
		 */
		public void readFile(Trie mRootNode) {
			try {
				while ((mLine = mReader.readLine()) != null) {
					/**
					 * 
					 */
					mSentences = mLine.split("(?<=[.?!])\\s+(?=[A-Z])");
					System.out.println("The number of sentences are "
							+ mSentences.length);

					for (String aSentence : mSentences) {
						// System.out.println(aSentence);
						aSentence = aSentence.toLowerCase();
						mCount++;
						mWords = aSentence.split(" ");
						for (String aWord : mWords) {
							mRootNode.addWord(parse(aWord), 0, mCount);
						}

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("The file could not be read");
			}

		}

		/**
		 * This crude method for parsing would remove any non alpha numeric
		 * character in the word.
		 * 
		 * @param aWord
		 *            the word to be parsed
		 * @return formatted word
		 */
		public String parse(String aWord) {
			return aWord.replaceAll("[^A-Za-z0-9]", "");
		}

	}

}
