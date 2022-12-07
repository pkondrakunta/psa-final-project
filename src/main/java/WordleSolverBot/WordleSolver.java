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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WordleSolver {

//	class variables
	private static HashSet<String> wordSet = new HashSet<String>();
	private static HashMap<String, Double> wordFreqScores = new HashMap<String, Double>();
	private static HashMap<String, Double> wordEntropyTable = new HashMap<String, Double>();

	private static Integer wordLength;
	private static Integer numOfWords;

	public static enum LetterHint {
		NOT_PRESENT, PRESENT_AT_RIGHT_POSITION, PRESENT_AT_WRONG_POSITION, REMOVE_REPEATING
	}

	// Constructor
	public WordleSolver(int length) throws IOException {
		numOfWords = 0;
		setWordLength(length);
		// Creating the Wordle words list
		computeWordSet();
		System.out.println(wordSet.size() + " words in the list!");
	}

	public void resetWordSet() throws IOException {
		File file = new File("wordlist/allowed-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String word;

		while ((word = br.readLine()) != null) {
			wordSet.add(word);
		}
	}

	public HashSet<String> getWordSet() {
		return wordSet;
	}

	public String recommendWord(Hashtable<Integer, String[]> hints, String guessedWord, int typeOfRun)
			throws IOException {
//		System.out.println("Recommending Words... ");
		String resultWord = new String();
		deduceHintsUpdateWords(hints, guessedWord);

		switch (typeOfRun) {
		case 1:
			// Case 1: Picking first word in remaining list
			int wordsetindex = 1;
			for (String s : wordSet) {
				if (wordsetindex == 1) {
					resultWord = s;
					break;
				}
			}
			break;
		case 2:
			// Case 2: Picking word with maximum frequency score in remaining list
			resultWord = maxFreqScoreWord();
			break;
		}

		if (wordSet.isEmpty()) {
			System.out.println("I am out of words! What?!?!");
		}
		System.out.println(wordSet.size() + " words remaining: " + wordSet);
		return resultWord;
	}

	private static void deduceHintsUpdateWords(Hashtable<Integer, String[]> hints, String guessedWord) {
//		System.out.println("Deducing hints.. Received " + hints.size() + " hints.");

		Enumeration<Integer> e = hints.keys();
		while (e.hasMoreElements()) {
			// Getting the key of a particular entry
			int key = e.nextElement();

			String letter = hints.get(key)[0];
			// System.out.println("letter+ "+letter);
			LetterHint letterInfo = LetterHint.valueOf(hints.get(key)[1]);
			// System.out.println("letterinfo "+letterInfo);

			if (letterInfo.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)
					|| letterInfo.equals(LetterHint.PRESENT_AT_WRONG_POSITION)) {
				updateWordSet(letter.charAt(0), key, letterInfo);
			}

			else if (letterInfo.equals(LetterHint.NOT_PRESENT)) {
				// System.out.println(checkIfDuplicatesExist(hints, key, letter));
				boolean dupe = checkIfDuplicatesExist(hints, key, letter);
				// System.out.println(dupe);

				removeGuessedWord(guessedWord);

				if (!dupe) {
					updateWordSet(letter.charAt(0), key, LetterHint.NOT_PRESENT);
				}
			}
		}

		updateWordSetForRepeatingCharacters(guessedWord, hints);

	}

	private static void updateWordSetForRepeatingCharacters(String guessedWord, Hashtable<Integer, String[]> ht) {
		HashMap<Character, Integer> letterFreq = getLetterFreqInWord(guessedWord);

		for (Character letter : letterFreq.keySet()) {
			int letterCount = 0;
			if (letterFreq.get(letter) > 1) {
				Enumeration<Integer> e = ht.keys();
				while (e.hasMoreElements()) {
					int key = e.nextElement();
					if (ht.get(key)[0].charAt(0) == letter && (ht.get(key)[1] == "PRESENT_AT_RIGHT_POSITION"
							|| ht.get(key)[1] == "LetterHint.PRESENT_AT_WRONG_POSITION")) {
						letterCount++;
					}
				}
				if (letterFreq.get(letter) != letterCount) {
					updateWordSet(letter, letterCount);

				}
			}
		}

	}

	private static void updateWordSet(Character letter, int letterCount) {

		// System.out.println("Deducing hint for " + guessedChar + " " + wordSet);
		Iterator<String> iterator = wordSet.iterator();

		while (iterator.hasNext()) {
			String word = iterator.next();

			if (getLetterFreqInWord(letter, word)>letterCount){
				iterator.remove();
			} 
		}
	}

	private static int getLetterFreqInWord(Character letter, String word) {
		int freq=0;
		for(int i=0; i<word.length();i++) if(word.charAt(i) == letter) freq++;
		return freq;
	}

	private static HashMap<Character, Integer> getLetterFreqInWord(String word) {
		HashMap<Character, Integer> mp = new HashMap<Character, Integer>();

		for (int i = 0; i < word.length(); i++) {
			char letter = word.charAt(i);
			if (mp.containsKey(letter)) {
				mp.put(letter, mp.get(letter) + 1);
			} else {
				mp.put(letter, 1);
			}
		}
		return mp;
	}
	

	private static void removeGuessedWord(String guessedWord) {
		wordSet.remove(guessedWord);
	}

	private static boolean checkIfDuplicatesExist(Hashtable<Integer, String[]> hints, Integer checkLetterIndex,
			String letter) {
		// System.out.println("Checking if duplicates exist for " + letter);

		Set<Integer> duplicateKeys = hints.keySet();
		for (Integer dKey : duplicateKeys) {
			if ((!checkLetterIndex.equals(dKey)) && letter.equals(hints.get(dKey)[0])) {
				// System.out.println("duplicate exist");
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
			} else if (receivedCharHint.equals(LetterHint.PRESENT_AT_WRONG_POSITION)
					&& (word.charAt(guessedCharPosition) == guessedChar || !charPresentInWord)) {
				iterator.remove();
			} else if (receivedCharHint.equals(LetterHint.NOT_PRESENT) && charPresentInWord) {
				iterator.remove();
			}
		}
	}

	
	public static Integer getWordLength() {
		return wordLength;
	}

	public static void setWordLength(Integer wordLength) {
		WordleSolver.wordLength = wordLength;
	}

	private static void computeWordSet() throws IOException {
		File file = new File("wordlist/allowed-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String word;

		HashMap<Character, Integer> letterFrequency = new HashMap<Character, Integer>();
		HashMap<Character, Integer> letter1 = new HashMap<Character, Integer>();
		HashMap<Character, Integer> letter2 = new HashMap<Character, Integer>();
		HashMap<Character, Integer> letter3 = new HashMap<Character, Integer>();
		HashMap<Character, Integer> letter4 = new HashMap<Character, Integer>();
		HashMap<Character, Integer> letter5 = new HashMap<Character, Integer>();

		while ((word = br.readLine()) != null) {
			wordSet.add(word);
			numOfWords++;

			for (int i = 0; i < word.length(); i++) {
				if (i == 0) {
					storeFrequency(letter1, word.charAt(i));
				} else if (i == 1) {
					storeFrequency(letter2, word.charAt(i));
				} else if (i == 2) {
					storeFrequency(letter3, word.charAt(i));
				} else if (i == 3) {
					storeFrequency(letter4, word.charAt(i));
				} else {
					storeFrequency(letter5, word.charAt(i));
				}
				storeFrequency(letterFrequency, word.charAt(i));
			}
		}
		computeFreqOfAllWords(letter1, letter2, letter3, letter4, letter5);
	}

	private String maxFreqScoreWord() {
		// System.out.println("Deducing hint for " + guessedChar + " " + wordSet);
		Iterator<String> iterator = wordSet.iterator();
		String maxScoreWord = null;
		Double maxScore = null;
		while (iterator.hasNext()) {
			String word = iterator.next();
			Double score = wordFreqScores.get(word);
			if (maxScore == null || maxScore < score) {
				maxScoreWord = word;
				maxScore = score;
			}
		}

//		System.out.println("Max score word: " + maxScoreWord + ", score: " + maxScore);

		return maxScoreWord;

	}

	private static void storeFrequency(HashMap<Character, Integer> freqMap, char currentChar) {
		if (freqMap.containsKey(currentChar)) {
			freqMap.put(currentChar, freqMap.get(currentChar) + 1);
		} else {
			freqMap.put(currentChar, 1);
		}
	}

	private static void computeFreqOfAllWords(HashMap<Character, Integer> letter1, HashMap<Character, Integer> letter2,
			HashMap<Character, Integer> letter3, HashMap<Character, Integer> letter4,
			HashMap<Character, Integer> letter5) {

//		System.out.println("Computing freq ..");
		Iterator<String> iterator = wordSet.iterator();
		while (iterator.hasNext()) {
			String word = iterator.next();
			Double wordFreqScore = 0.0;
			Double score = null;

			for (int letterPosition = 1; letterPosition <= word.length(); letterPosition++) {
				switch (letterPosition) {
				case 1:
					score = letter1.get(word.charAt(letterPosition - 1)) / (double) numOfWords;
				case 2:
					score = letter2.get(word.charAt(letterPosition - 1)) / (double) numOfWords;
				case 3:
					score = letter3.get(word.charAt(letterPosition - 1)) / (double) numOfWords;
				case 4:
					score = letter4.get(word.charAt(letterPosition - 1)) / (double) numOfWords;
				case 5:
					score = letter5.get(word.charAt(letterPosition - 1)) / (double) numOfWords;
				}
				wordFreqScore = wordFreqScore + score;
			}
			wordFreqScores.put(word, wordFreqScore);
		}
//		System.out.println("The probability/frequency scores are: " + wordFreqScores);
	}

	public static void main(String[] args) throws IOException {
		WordleSolver wSolver = new WordleSolver(5);
	}
}
