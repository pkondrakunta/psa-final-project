package WordleSolverBot;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class WordleTestRuns {

	public static final int WORD_LENGTH = 5;

	public static void main(String[] args) throws IOException, InterruptedException {
		testInfo[] testData = new testInfo[100];

		int testRuns = 100;

		WordleSolver w = new WordleSolver(WORD_LENGTH);

		WordleSimulator sim = new WordleSimulator();
		System.out.println("Running tests.. ");
		for (int tests = 0; tests < testRuns; tests++) {
			testData[tests] = sim.automatedWordlePlayer(w,null);
			
			w.resetWordSet();
		}

		System.out.println("Test Results");

		for (int i = 0; i < testRuns; i++) {
			System.out.println("Word: " + testData[i].getWord() + ", Attempts: " + testData[i].getAttempts());
		}

		try {
			// Change name of file based on the algorithm - case1.csv, case2.csv
            FileOutputStream fis = new FileOutputStream("results/case1.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 50;
            for (testInfo i : testData) {
                String content = i.getWord() + "," + i.getAttempts()+ "\n";
                bw.write(content);
                bw.flush();
            }
            bw.close();
            System.out.println("File created");
		} catch (IOException e) {
            e.printStackTrace();
        }

		// playWordle();
	
}
}
