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
//		String wordOfTheDay = getRandomWord.getRandomElement(wordSet);
		String wordOfTheDay = "mured";

		HashSet<Character> englishWords = new HashSet<Character>();
		for (int i = 0; i < 26; i++) {

			englishWords.add((char) (i + 97));
		}

		Character[] answersofar = new Character[5];
		for (int i = 0; i < 5; i++) {
			answersofar[i] = '*';
		}
//		System.out.println("Today's word is " + wordOfTheDay);
		int count = 0;

		WordleSolver wSolver = new WordleSolver(WORD_LENGTH);
		Hashtable<Integer, String[]> hints = new Hashtable<Integer, String[]>();

		String prevGuess = null;
		
		while (count < 6) {
			String predictedWord = "salet";
			if (count != 0) {
				predictedWord = wSolver.recommendWordMeanSum(hints, prevGuess,false);
			}
			System.out.println("The next recommended guess is: " + predictedWord + "\n");
			System.out.println("Enter your word (Attempt " + (count + 1) + "): ");

			String userGuess = scn.nextLine();
			prevGuess = userGuess;
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
			if (wordOfTheDay.equals(userGuess)) {
				System.out.println("That's correct. You won in " + count + " attempts!");
				break;
			} else {
				for (int letterInWordIndex = 0; letterInWordIndex < wordOfTheDay.length(); letterInWordIndex++) {

					Boolean[] visited = new Boolean[WORD_LENGTH];
					Arrays.fill(visited, Boolean.FALSE);

					// Checking if letters are at the right position
					if (wordOfTheDay.charAt(letterInWordIndex) == userGuess.charAt(letterInWordIndex)) { // if it is
																											// green
						System.out.print(ANSI_GREEN + userGuess.charAt(letterInWordIndex));
						System.out.println(" at correct position " + (letterInWordIndex + 1) + ANSI_RESET);
						visited[letterInWordIndex] = true;
						hints.put(letterInWordIndex,
								new String[] { "" + userGuess.charAt(letterInWordIndex), "PRESENT_AT_RIGHT_POSITION" });
					} else {
						int checkLetterPresence = 0;
						for (int i = 0; i < wordOfTheDay.length(); i++) {
							if (visited[i] == false && userGuess.charAt(i) != wordOfTheDay.charAt(i)) {
								if (userGuess.charAt(letterInWordIndex) == wordOfTheDay.charAt(i)) {
									checkLetterPresence++;
									System.out.print(ANSI_YELLOW + userGuess.charAt(letterInWordIndex));
									System.out.println(" letter present at incorrect position "
											+ (letterInWordIndex + 1) + ANSI_RESET);
									visited[i] = true;
									hints.put(letterInWordIndex, new String[] {
											"" + userGuess.charAt(letterInWordIndex), "PRESENT_AT_WRONG_POSITION" });
								}
							}
						}
						if (checkLetterPresence == 0) {
							System.out.print(userGuess.charAt(letterInWordIndex));
							System.out.println(" letter not present " + (letterInWordIndex + 1));

							hints.put(letterInWordIndex,
									new String[] { "" + userGuess.charAt(letterInWordIndex), "NOT_PRESENT" });
						}

					}

				}

			}

		}

		System.out.println(ANSI_BOLD + ANSI_GREEN + "Today's word is " + wordOfTheDay + ANSI_RESET);
		System.out.println(ANSI_BOLD + "Game over\n" + ANSI_RESET);

	}

	public static testInfo automatedWordlePlayer(WordleSolver wSolver, String word) throws IOException {

		Hashtable<Integer, String[]> hints = new Hashtable<Integer, String[]>();
		HashSet<String> fiveLetterWordSet = wSolver.getWordSet();



		String wordOfTheDay = getRandomWord.getRandomElement(fiveLetterWordSet);

		if(word!=null){
			wordOfTheDay=word;
		}

		System.out.println("Today's word is " + wordOfTheDay);
		int count = 0;
		String prevGuess = null;
		while (count < 6) {

			String predictedWord = "salet";
			if (count != 0) {
				predictedWord = wSolver.recommendWordMeanSum(hints, prevGuess,true); // change this to false if new alg is not being used
			}
			String userGuess = predictedWord;
			prevGuess = userGuess;
			count++;
			if (wordOfTheDay.equals(userGuess)) {
				break;
			} else {
				for (int letterInWordIndex = 0; letterInWordIndex < wordOfTheDay.length(); letterInWordIndex++) {

					Boolean[] visited = new Boolean[WORD_LENGTH];
					Arrays.fill(visited, Boolean.FALSE);

					// Checking if letters are at the right position
					if (wordOfTheDay.charAt(letterInWordIndex) == userGuess.charAt(letterInWordIndex)) { // if it is
						visited[letterInWordIndex] = true;
						hints.put(letterInWordIndex,
								new String[] { "" + userGuess.charAt(letterInWordIndex), "PRESENT_AT_RIGHT_POSITION" });
					} else {
						int checkLetterPresence = 0;
						for (int i = 0; i < wordOfTheDay.length(); i++) {
							if (visited[i] == false && userGuess.charAt(i) != wordOfTheDay.charAt(i)) {
								if (userGuess.charAt(letterInWordIndex) == wordOfTheDay.charAt(i)) {
									checkLetterPresence++;
									visited[i] = true;
									hints.put(letterInWordIndex, new String[] {
											"" + userGuess.charAt(letterInWordIndex), "PRESENT_AT_WRONG_POSITION" });
								}
							}
						}
						if (checkLetterPresence == 0) {
							hints.put(letterInWordIndex,
									new String[] { "" + userGuess.charAt(letterInWordIndex), "NOT_PRESENT" });
						}

					}

				}

			}

		}
		return new testInfo(wordOfTheDay, count);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		playWordle();
	}
}
