package WordleSolverBot;

import java.io.IOException;

public class WordleTestRuns {

	public static final int WORD_LENGTH = 5;

	public static void main(String[] args) throws IOException, InterruptedException {
		testInfo[] testData = new testInfo[50];

		int testRuns = 50;

		WordleSolver w = new WordleSolver(WORD_LENGTH);

		WordleSimulator sim = new WordleSimulator();
		System.out.println("Running tests.. ");
		for (int tests = 0; tests < testRuns; tests++) {
			System.out.println("Running Test " + tests);

			testData[tests] = sim.automatedWordlePlayer(w);
			
			Thread.sleep(1000);
			w.resetWordSet();
		}

		System.out.println("Test Results");

		for (int i = 0; i < testRuns; i++) {
			System.out.println("Word: " + testData[i].getWord() + ", Attempts: " + testData[i].getAttempts());
		}
		

		// playWordle();
	}
}
