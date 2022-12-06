package WordleSolverBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import WordleSolverBot.WordleSolver.LetterHint;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WordleConnectorBot {
	public static final int WORD_LENGTH = 5;
	public static final int ATTEMPTS = 6;
	// public static final int SUGGESTION_COUNT = 10;

	public static void main(String[] args) throws IOException, InterruptedException {

//		Creating a set of prospective words
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		HashSet<String> wordSet = new HashSet<String>();

		while ((st = br.readLine()) != null) {
			wordSet.add(st);
		}

//		Opening Chrome and setting up drivers
		ChromeOptions co = new ChromeOptions();
		co.addArguments("−−incognito");

		WebDriver driver = WebDriverManager.chromedriver().capabilities(co).create();

		driver.get("https://www.nytimes.com/games/wordle/index.html");

		// waiting for popup to show
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// to close the pop-up page
		WebElement web = driver.findElement(By.className("Modal-module_closeIcon__b4z74"));
		web.click();

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Actions act = new Actions(driver);
		String word = "salet";

//		Typing up the word
		WordleSolver wSolver = new WordleSolver(WORD_LENGTH);
		Hashtable<Integer, String[]> hints = new Hashtable<Integer, String[]>();

		String prevGuess = null;
		for (int outerdiv = 1; outerdiv <= ATTEMPTS; outerdiv++) {

			if (outerdiv > 1) {
				word = wSolver.recommendWordMeanSum(hints, prevGuess,2); // change to true if new alg is being used
			}

			prevGuess = word;
			System.out.println("Typing " + word);
			

			for (int innerdiv = 1; innerdiv <= WORD_LENGTH; innerdiv++) {
				web = driver.findElement(By.xpath(
						"/html/body/div/div/div[2]/div/div[1]/div/div[" + outerdiv + "]/div[" + innerdiv + "]/div"));
				Thread.sleep(1000);
				act.sendKeys(word.charAt(innerdiv - 1) + "").perform();
			}

			act.sendKeys(Keys.ENTER).perform();
			Thread.sleep(3000);

			for (int innerdiv = 1; innerdiv <= WORD_LENGTH; innerdiv++) {
//				Retrieving hints from the web page
				int letterInWordIndex = innerdiv - 1;

				System.out.println("Retrieving hints");
				web = driver.findElement(By.xpath(
						"/html/body/div/div/div[2]/div/div[1]/div/div[" + outerdiv + "]/div[" + innerdiv + "]/div"));
				String txt = web.getAttribute("outerHTML".valueOf("data-state")).split(" ").clone()[0];

//				Creating hints HastTable
				if (txt.equals("correct")) {
					hints.put(letterInWordIndex,
							new String[] { "" + word.charAt(letterInWordIndex), "PRESENT_AT_RIGHT_POSITION" });
				} else if (txt.equals("present")) {
					hints.put(letterInWordIndex,
							new String[] { "" + word.charAt(letterInWordIndex), "PRESENT_AT_WRONG_POSITION" });
				} else if (txt.equals("absent")) {
					hints.put(letterInWordIndex, new String[] { "" + word.charAt(letterInWordIndex), "NOT_PRESENT" });
				}
				driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
			}
		}
		System.out.println("url" + driver.getCurrentUrl());
		// driver.quit();
	}
}
