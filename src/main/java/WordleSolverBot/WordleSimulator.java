package WordleSolverBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

public class WordleSimulator {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BOLD = "\u001B[1m";

	public static final int WORD_LENGTH = 5;
	private static HashSet<String> nytWordSet = new HashSet<String>();

	/**
	 * 
	 * @param args
	 * @return
	 * @throws IOException
	 */

	private static void playWordle() throws IOException {

		@SuppressWarnings("resource")
		Scanner scn = new Scanner(System.in);
		System.out.println(ANSI_BOLD + "Simulating Wordle" + ANSI_RESET);		
		String wordOfTheDay = "torso";
		int count = 0;

		WordleSolver wSolver = new WordleSolver(WORD_LENGTH);
		Hashtable<Integer, String[]> hints = new Hashtable<Integer, String[]>();
		String prevGuess = null;

		while (count < 6) {
			String predictedWord = "salet";
			if (count != 0) {
				predictedWord = wSolver.recommendWord(hints, prevGuess, 2);
			}
			System.out.println("The next recommended guess is: " + predictedWord + "\n");
			System.out.println("Enter your word (Attempt " + (count + 1) + "): ");

			String userGuess = scn.nextLine();
			prevGuess = userGuess;

			// Checking validity
			if (userGuess.length() != 5) {
				System.out.println("Oops, that's invalid. Your word is of length " + userGuess.length()
						+ ". We need a 5-letter word.");
				continue;
			}
			if (!wSolver.getWordSet().contains(userGuess)) {
				System.out.println("Oops, that's invalid. It's not a word. Try again!");
				continue;
			}

			count++;

			// Check win
			if (wordOfTheDay.equals(userGuess)) {
				System.out.println("\nCongratulations! That's correct. You won in " + count + " attempts.");
				break;
			} else {
				System.out.println("");
				HashMap<Character, Integer> refWordFreq = getLetterFreqInWord(wordOfTheDay);

				for (int letterInWordIndex = 0; letterInWordIndex < wordOfTheDay.length(); letterInWordIndex++) {

					char letterInCheck = userGuess.charAt(letterInWordIndex);

					// Checking if letters are at the right position
					if (wordOfTheDay.charAt(letterInWordIndex) == letterInCheck) {
						System.out.print(ANSI_BOLD + ANSI_GREEN + letterInCheck + ANSI_RESET + " ");
						hints.put(letterInWordIndex, new String[] { "" + letterInCheck, "PRESENT_AT_RIGHT_POSITION" });
						refWordFreq.put(letterInCheck, refWordFreq.get(letterInCheck) - 1);

					} else {
						// Checking if letters are present
						if (refWordFreq.containsKey(letterInCheck) && refWordFreq.get(letterInCheck) > 0) {
							System.out.print(
									ANSI_BOLD + ANSI_YELLOW + userGuess.charAt(letterInWordIndex) + ANSI_RESET + " ");
							hints.put(letterInWordIndex, new String[] { "" + userGuess.charAt(letterInWordIndex),
									"PRESENT_AT_WRONG_POSITION" });
							refWordFreq.put(letterInCheck, refWordFreq.get(letterInCheck) - 1);
						}

						// Checking if letters are not present
						else {
							System.out.print(ANSI_BOLD + userGuess.charAt(letterInWordIndex) + " " + ANSI_RESET);
							hints.put(letterInWordIndex,
									new String[] { "" + userGuess.charAt(letterInWordIndex), "NOT_PRESENT" });
						}

					}

				}
				System.out.println("\n");
			}

		}

		System.out.println(ANSI_BOLD + ANSI_GREEN + "Today's word is " + wordOfTheDay + ANSI_RESET);
		System.out.println(ANSI_BOLD + "Game over\n" + ANSI_RESET);

	}

	public static testInfo automatedWordlePlayer(WordleSolver wSolver, String word) throws IOException {

		Hashtable<Integer, String[]> hints = new Hashtable<Integer, String[]>();
		String wordOfTheDay = getWordOfTheDay();

		if (word != null) {
			wordOfTheDay = word;
		}

		int count = 0;
		String prevGuess = null;

		while (count < 6) {

			String predictedWord = "salet";
			if (count != 0) {
				predictedWord = wSolver.recommendWord(hints, prevGuess, 1); // change this to 1 if new alg is not
																			// being used
			}
			String userGuess = predictedWord;
			prevGuess = userGuess;
			count++;
			if (wordOfTheDay.equals(userGuess)) {
				break;
			} else {
				HashMap<Character, Integer> refWordFreq = getLetterFreqInWord(wordOfTheDay);

				for (int letterInWordIndex = 0; letterInWordIndex < wordOfTheDay.length(); letterInWordIndex++) {
					char letterInCheck = userGuess.charAt(letterInWordIndex);

					// Checking if letters are at the right position
					if (wordOfTheDay.charAt(letterInWordIndex) == letterInCheck) {
						System.out.print(ANSI_BOLD + ANSI_GREEN + letterInCheck + ANSI_RESET + " ");
						hints.put(letterInWordIndex, new String[] { "" + letterInCheck, "PRESENT_AT_RIGHT_POSITION" });
						refWordFreq.put(letterInCheck, refWordFreq.get(letterInCheck) - 1);

					} else {
						// Checking if letters are present
						if (refWordFreq.containsKey(letterInCheck) && refWordFreq.get(letterInCheck) > 0) {
							System.out.print(
									ANSI_BOLD + ANSI_YELLOW + userGuess.charAt(letterInWordIndex) + ANSI_RESET + " ");
							hints.put(letterInWordIndex, new String[] { "" + userGuess.charAt(letterInWordIndex),
									"PRESENT_AT_WRONG_POSITION" });
							refWordFreq.put(letterInCheck, refWordFreq.get(letterInCheck) - 1);
						}

						// Checking if letters are not present
						else {
							System.out.print(ANSI_BOLD + userGuess.charAt(letterInWordIndex) + " " + ANSI_RESET);
							hints.put(letterInWordIndex,
									new String[] { "" + userGuess.charAt(letterInWordIndex), "NOT_PRESENT" });
						}

					}
				}

			}

		}
		return new testInfo(wordOfTheDay, count);
	}

	private static void addCharFrequency(HashMap<Character, Integer> mp, char letter) {
		if (mp.containsKey(letter)) {
			mp.put(letter, mp.get(letter) + 1);
		} else {
			mp.put(letter, 1);
		}
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

	private static String getWordOfTheDay() throws IOException {

		if (nytWordSet.isEmpty()) {
			File file = new File("wordlist/possible-wordle-words.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String w;

			while ((w = br.readLine()) != null) {
				nytWordSet.add(w);
			}
		}

		String word = getRandomWord.getRandomElement(nytWordSet);
		System.out.println("Today's word is " + word);

		return word;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		playWordle();
	}
}
