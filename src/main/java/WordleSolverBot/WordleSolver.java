package WordleSolverBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
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
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String word;

		while ((word = br.readLine()) != null) {
			wordSet.add(word);
		}

	}

	public String recommendWord(Hashtable<Integer, String[]> hints) throws IOException {
		System.out.println("Recommending Words... ");

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
		System.out.println(wordSet);
		return resultWord;
	}

	private static void deduceHintsUpdateWords(Hashtable<Integer, String[]> hints) {

		System.out.println("Deducing hints.. Received " + hints.size() + " hints.");

		Enumeration<Integer> e = hints.keys();
		while (e.hasMoreElements()) {
			// Getting the key of a particular entry
			int key = e.nextElement();
			String letter = hints.get(key)[0];
			LetterHint letterInfo = LetterHint.valueOf(hints.get(key)[1]);

			if (letterInfo.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)
					|| letterInfo.equals(LetterHint.PRESENT_AT_WRONG_POSITION)) {
				updateWordSet(wordSet, letter.charAt(0), key, letterInfo);
			}

			else if (letterInfo.equals(LetterHint.NOT_PRESENT)) {
				LetterHint checkDuplicateLetterInfo = checkDuplicates(hints, key, letter);

				if (checkDuplicateLetterInfo.equals(LetterHint.NOT_PRESENT)) {
					updateWordSet(wordSet, letter.charAt(0), key, LetterHint.NOT_PRESENT);
				} else if (checkDuplicateLetterInfo.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)) {
					updateWordSet(wordSet, letter.charAt(0), key, LetterHint.PRESENT_AT_RIGHT_POSITION);
				} else if (checkDuplicateLetterInfo.equals(LetterHint.PRESENT_AT_WRONG_POSITION)) {
					updateWordSet(wordSet, letter.charAt(0), key, LetterHint.PRESENT_AT_WRONG_POSITION);
				} else {
					System.out.println("Entered else!! ");
				}
			}
		}

//		Set<Integer> setOfKeys = hints.keySet();
//		// Iterating through the Hashtable
//		for (Integer i : setOfKeys) {
//			// Print and display the Rank and Name
//			System.out.println("Rank : " + i + "\t\t Name : " + hints.get(i)[0] + " " + hints.get(i)[1]);
//		}

	}

	private static LetterHint checkDuplicates(Hashtable<Integer, String[]> hints, Integer checkLetterIndex,
			String letter) {
		System.out.println("Checking duplicates for " + letter);

		Set<Integer> duplicateKeys = hints.keySet();
		for (Integer dKey : duplicateKeys) {

			if (checkLetterIndex != dKey) {
				String newLetter = hints.get(dKey)[0];
				LetterHint newLetterInfo = LetterHint.valueOf(hints.get(dKey)[1]);

				if (newLetter == letter && newLetterInfo.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)) {
					return LetterHint.PRESENT_AT_RIGHT_POSITION;
				} else if (newLetter == letter && newLetterInfo.equals(LetterHint.PRESENT_AT_WRONG_POSITION)) {
					return LetterHint.PRESENT_AT_WRONG_POSITION;
				}
			}
		}
		return LetterHint.NOT_PRESENT;
	}

	private static void updateWordSet(HashSet<String> wordSet, char chartocheck, int charposition, LetterHint method) {

//		System.out.println("Updating words with hint for " + chartocheck);
//		System.out.println("Before, word set size: \t" + wordSet.size());
		
		Iterator<String> iterator = wordSet.iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			if (method.equals(LetterHint.PRESENT_AT_RIGHT_POSITION)) {
				if (element.charAt(charposition) != chartocheck || element.indexOf(chartocheck) == -1) {
					iterator.remove();
				}
			} else if (method.equals(LetterHint.PRESENT_AT_WRONG_POSITION)) {
				if (element.charAt(charposition) == chartocheck || element.indexOf(chartocheck) == -1) {
					iterator.remove();
				}
			} else {
				if (element.indexOf(chartocheck) != -1) {
					iterator.remove();
				}
			}
		}

//		System.out.println("After, word set size: \t" + wordSet.size());
	}

	public static void main(String[] args) throws IOException {
		WordleSolver wSolver = new WordleSolver(5);

		Hashtable<Integer, String[]> hints_hashtable = new Hashtable<Integer, String[]>();
		hints_hashtable.put(1, new String[] { "p", "PRESENT_AT_RIGHT_POSITION" });
		hints_hashtable.put(2, new String[] { "r", "PRESENT_AT_RIGHT_POSITION" });
		hints_hashtable.put(3, new String[] { "i", "NOT_PRESENT" });
		hints_hashtable.put(4, new String[] { "z", "NOT_PRESENT" });
		hints_hashtable.put(5, new String[] { "e", "NOT_PRESENT" });
		
		String word = wSolver.recommendWord(hints_hashtable);
		
		System.out.println(word);
	}

	public static Integer getWordLength() {
		return wordLength;
	}

	public static void setWordLength(Integer wordLength) {
		WordleSolver.wordLength = wordLength;
	}

}
