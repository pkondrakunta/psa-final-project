package psafinal;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
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
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

public class interact {
	  public static final int WORD_LENGTH = 5;
	    public static final int ATTEMPTS = 6;
	    //public static final int SUGGESTION_COUNT = 10;
	    
	public static void main(String[] args ) throws IOException, InterruptedException {
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br= new BufferedReader(new FileReader(file));
		String st;
		HashSet<String> wordSet = new HashSet<String>();

		while ((st = br.readLine()) != null )
		{
			wordSet.add(st);
		}
		
		
				
		
		WebDriver driver =WebDriverManager.chromedriver().create();
	      driver.get("https://www.nytimes.com/games/wordle/index.html");
	      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	      
	      // to close the pop-up page
	      WebElement web=driver.findElement(By.className("Modal-module_closeIcon__b4z74"));
	      web.click();
	      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	      Actions act=new Actions(driver);
	      String word="caste";
	      
	      for(int outerdiv=1;outerdiv<=1;outerdiv++) {
	    	  
	      for(int innerdiv=1;innerdiv<=WORD_LENGTH;innerdiv++) {
	    	  web=driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div["+outerdiv+"]/div["+innerdiv+"]/div"));
	    	  Thread.sleep(2000);
	    	  act.sendKeys(word.charAt(innerdiv-1)+"").perform();
	      
	      }
	      act.sendKeys(Keys.ENTER).perform();
	      Thread.sleep(5000);
	      
	      
	      for(int innerdiv=1;innerdiv<=WORD_LENGTH;innerdiv++) {
	    	  web=driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div["+outerdiv+"]/div["+innerdiv+"]/div"));
	    	  String txt=web.getAttribute("outerHTML".valueOf("data-state")).split(" ").clone()[0];
	    	  System.out.println(txt);
	    	  if(txt.equals("correct")) {
	    		  System.out.println(word.charAt(innerdiv-1));
	    		  removeDictionarywords(wordSet,word.charAt(innerdiv-1),innerdiv-1,txt);
	    	  }
	    	  else if(txt.equals("present")) {
	    		  removeDictionarywords(wordSet,word.charAt(innerdiv-1),innerdiv-1,txt);
	    	  }
	    	  else if(txt.equals("absent")){
	    		  //absent
	    		  removeDictionarywords(wordSet,word.charAt(innerdiv-1),innerdiv-1,txt);
	    		  //System.out.println(word.charAt(innerdiv-1));
	    	  }
	    
	      }
	      
	      driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	     // System.out.println("------------------");
	      
	      }


	        System.out.println("Page title is: " + driver.getTitle());
	        System.out.println("url" + driver.getCurrentUrl());
	        //driver.quit();
	        
	}

	private static void removeDictionarywords(HashSet<String> wordSet, char chartocheck, int charposition, String method) {
		// TODO Auto-generated method stub
		System.out.println("wordset size before  ---- " +wordSet.size());
		Iterator<String> iterator = wordSet.iterator();
		while (iterator.hasNext()) {
		    String element = iterator.next();
		    if(method.equals("correct")) {
		    	if(element.charAt(charposition)!=chartocheck) {
		    	iterator.remove();
		    	}
		    }
		    else if(method.equals("present") || method.equals("absent")){
		    	if(element.charAt(charposition)==chartocheck) {
					iterator.remove();
				    }	
		    }
		    
		}
		for(String s:wordSet) {
			System.out.println(s);
		}
		 System.out.println("wordset size after  ---- " +wordSet.size());
		
	}
}

    