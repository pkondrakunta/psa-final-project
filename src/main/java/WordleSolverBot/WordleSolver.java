package WordleSolverBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WordleSolver {

//	class variables
	private static HashSet<String> wordSet = new HashSet<String>();;
	private static Integer wordLength;

	public static enum LetterHint {
		NOT_PRESENT, PRESENT_AT_RIGHT_POSITION, PRESENT_AT_WRONG_POSITION
	}

	// Constructor
	public WordleSolver(int length) throws IOException {

		setWordLength(length);
		// Creating the Wordle words list
		computeWordSet();
	}

	public void resetWordSet() throws IOException {
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String word;

		while ((word = br.readLine()) != null) {
			wordSet.add(word);
		}
	}

	public HashSet<String> getWordSet() {
		return wordSet;
	}

	public String recommendWordMeanSum(Hashtable<Integer, String[]> hints) throws IOException {
//		System.out.println("Recommending Words... ");
		String resultWord = new String();
		deduceHintsUpdateWords(hints);
		int wordsetindex = 1;
		for (String s : wordSet) {
			if (wordsetindex == 1) {
				resultWord = s;
				break;
			}
		}
		if (wordSet.isEmpty()) {
			System.out.println("I am out of words! What?!?!");
		}
		//System.out.println(wordSet);
		return resultWord;
	}

	private static void deduceHintsUpdateWords(Hashtable<Integer, String[]> hints) {
//		System.out.println("Deducing hints.. Received " + hints.size() + " hints.");

		Enumeration<Integer> e = hints.keys();
		while (e.hasMoreElements()) {
			// Getting the key of a particular entry
			int key = e.nextElement();

			String letter = hints.get(key)[0];
			//System.out.println("letter+ "+letter);
			LetterHint letterInfo = LetterHint.valueOf(hints.get(key)[1]);
			//System.out.println("letterinfo "+letterInfo);

			if (letterInfo.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)
					|| letterInfo.equals(LetterHint.PRESENT_AT_WRONG_POSITION)) {
				updateWordSet(letter.charAt(0), key, letterInfo);
			}

			else if (letterInfo.equals(LetterHint.NOT_PRESENT)) {
				//System.out.println(checkIfDuplicatesExist(hints, key, letter));
				boolean dupe=checkIfDuplicatesExist(hints, key, letter);
				//System.out.println(dupe);

				if (!dupe) {

					updateWordSet(letter.charAt(0), key, LetterHint.NOT_PRESENT);
				}
			}
		}

	}

	private static boolean checkIfDuplicatesExist(Hashtable<Integer, String[]> hints, Integer checkLetterIndex,
			String letter) {
		//System.out.println("Checking if duplicates exist for " + letter);

		Set<Integer> duplicateKeys = hints.keySet();
		for (Integer dKey : duplicateKeys) {
			//System.out.println("dkey: "+dKey+" cgeckLetterIndex "+checkLetterIndex);
			//System.out.println("letter "+letter);
			//System.out.println("hints.get(dkey) "+ hints.get(dKey)[0]);

			if ((!checkLetterIndex.equals(dKey)) && letter.equals(hints.get(dKey)[0])){
				//System.out.println("duplicate exist");
				return true;
			}
		}
		return false;
	}

	private static void updateWordSet(char guessedChar, int guessedCharPosition, LetterHint receivedCharHint) {
		// System.out.println("Deducing hint for " + guessedChar + " " + wordSet);
		Iterator<String> iterator = wordSet.iterator();

		while (iterator.hasNext()) {
			String word = iterator.next();
			// System.out.println(word + " index: " +charPresenceInWord);

			boolean charPresentInWord = (word.indexOf(guessedChar) == -1) ? false : true;

			if (receivedCharHint.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)
					&& (word.charAt(guessedCharPosition) != guessedChar || !charPresentInWord)) {
				iterator.remove();
			} else if (receivedCharHint.equals(LetterHint.PRESENT_AT_WRONG_POSITION) && (word.charAt(guessedCharPosition) == guessedChar || !charPresentInWord)) {
				iterator.remove();
			} else if (receivedCharHint.equals(LetterHint.NOT_PRESENT) && charPresentInWord) {
				iterator.remove();
			}
		}
	}

	public static void main(String[] args) throws IOException {
//		WordleSolver wSolver = new WordleSolver(5);
//
//		updateWordSet('a', 2, LetterHint.NOT_PRESENT);

	}

	public static Integer getWordLength() {
		return wordLength;
	}

	public static void setWordLength(Integer wordLength) {
		WordleSolver.wordLength = wordLength;
	}

	private static void computeWordSet() throws IOException {
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String word;

//		HashSet<String> wordSet = new HashSet<String>();
		HashMap<Character, Integer> letterFrequency = new HashMap<Character, Integer>();
		HashMap<Character, Integer> firstletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> secondletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> thirdletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> fourthletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> fifthletter = new HashMap<Character, Integer>();

		while ((word = br.readLine()) != null) {
			wordSet.add(word);

			for (int i = 0; i < word.length(); i++) {
				if (i == 0) {
					storefrequency(firstletter, word.charAt(i));
				} else if (i == 1) {
					storefrequency(secondletter, word.charAt(i));
				} else if (i == 2) {
					storefrequency(thirdletter, word.charAt(i));
				} else if (i == 3) {
					storefrequency(fourthletter, word.charAt(i));
				} else {
					storefrequency(fifthletter, word.charAt(i));
				}
				storefrequency(letterFrequency, word.charAt(i));
			}
		}
	}

	private static void storefrequency(HashMap<Character, Integer> frequencystore, char currentChar) {
		if (frequencystore.containsKey(currentChar)) {
			frequencystore.put(currentChar, frequencystore.get(currentChar) + 1);
		} else {
			frequencystore.put(currentChar, 1);
		}
	}
}
