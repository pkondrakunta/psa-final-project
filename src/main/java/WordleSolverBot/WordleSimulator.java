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

	public static void main(String[] args) throws IOException {

		Scanner scn = new Scanner(System.in);
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		HashSet<String> wordSet = new HashSet<String>();
		HashMap<Character, Integer> letterfrequency = new HashMap<Character, Integer>();
		HashMap<Character, Integer> firstletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> secondletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> thirdletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> fourthletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> fifthletter = new HashMap<Character, Integer>();

		while ((st = br.readLine()) != null) {
			wordSet.add(st);

			for (int i = 0; i < st.length(); i++) {
				if (i == 0) {
					storefrequency(firstletter, st.charAt(i));
				} else if (i == 1) {
					storefrequency(secondletter, st.charAt(i));
				} else if (i == 2) {
					storefrequency(thirdletter, st.charAt(i));
				} else if (i == 3) {
					storefrequency(fourthletter, st.charAt(i));
				} else {
					storefrequency(fifthletter, st.charAt(i));
				}

				storefrequency(letterfrequency, st.charAt(i));
			}
		}

//		System.out.println(letterfrequency);
//		System.out.println(firstletter);
//		System.out.println(secondletter);
//		System.out.println(thirdletter);
//		System.out.println(fourthletter);
//		System.out.println(fifthletter);

		
		System.out.println(ANSI_BOLD + "Wordle Simulator" + ANSI_RESET);
//		getRandomWord gtr = new getRandomWord();
		String wordOfTheDay = getRandomWord.getRandomElement(wordSet);
		// String wordOfTheDay="jiffy";

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
		// String result="tears";

		WordleSolver wSolver = new WordleSolver(WORD_LENGTH);
		Hashtable<Integer, String[]> hints = new Hashtable<Integer, String[]>();

		while (count < 6) {
			System.out.println("Enter your " + (count + 1) + " word:");
			String userGuess = scn.nextLine();

			if (userGuess.length() != 5) {
				System.out.println("Oops, that's invalid. Your word is of length " + userGuess.length()
						+ ". We need a 5-letter word.");
				continue;
			}
			if (!wordSet.contains(userGuess)) {
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
					Arrays.fill(visited, Boolean. FALSE);
					
					// Checking if letters are at the right position
					if (wordOfTheDay.charAt(letterInWordIndex) == userGuess.charAt(letterInWordIndex)) { // if it is
																											// green
						System.out.print(ANSI_GREEN + userGuess.charAt(letterInWordIndex));
						System.out.println(" at correct position " + (letterInWordIndex + 1) + ANSI_RESET);
						visited[letterInWordIndex] = true;
//						answersofar[charvisit] = userGuess.charAt(charvisit);
//						removeDictionarywords(copyofWordSet, userGuess.charAt(charvisit), charvisit, "correct");
						hints.put(letterInWordIndex,
								new String[] { "" + userGuess.charAt(letterInWordIndex), "PRESENT_AT_RIGHT_POSITION" });
					} else {
						int checkLetterPresence = 0;
						for (int i = 0; i < wordOfTheDay.length(); i++) {
							if (visited[i] == false
									&& userGuess.charAt(i) != wordOfTheDay.charAt(i)) {
								if (userGuess.charAt(letterInWordIndex) == wordOfTheDay.charAt(i)) {
									checkLetterPresence++;
									System.out.print(ANSI_YELLOW + userGuess.charAt(letterInWordIndex));
									System.out.println(
											" letter present at incorrect position " + (letterInWordIndex + 1) + ANSI_RESET);
									visited[i] = true;
									hints.put(letterInWordIndex, new String[] { "" + userGuess.charAt(letterInWordIndex),
											"PRESENT_AT_WRONG_POSITION" });
								}
							}
						}
						if (checkLetterPresence == 0) {
							System.out.print(userGuess.charAt(letterInWordIndex));
							System.out.println(" letter not present " + (letterInWordIndex + 1));
							
							hints.put(letterInWordIndex, new String[] { "" + userGuess.charAt(letterInWordIndex),
							"NOT_PRESENT" });
						}

					}

				}


				String predictedWord = wSolver.recommendWordMeanSum(hints);
				System.out.println("The next recommended guess is: " + predictedWord);
			}

		}
		System.out.println("Today's word is " + wordOfTheDay);
		System.out.println("Game finish");

	}

	private static void storefrequency(HashMap<Character, Integer> frequencystore, char currentChar) {
		if (frequencystore.containsKey(currentChar)) {
			frequencystore.put(currentChar, frequencystore.get(currentChar) + 1);
		} else {
			frequencystore.put(currentChar, 1);
		}
	}
}
