package psafinal;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.openqa.selenium.remote.DesiredCapabilities;
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
		
		ChromeOptions co=new ChromeOptions();
		co.addArguments("−−incognito");
		
		WebDriver driver =WebDriverManager.chromedriver().capabilities(co).create();
		
	      driver.get("https://www.nytimes.com/games/wordle/index.html");
	      
	      //waiting for popup to show
	      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	      
	      // to close the pop-up page
	      WebElement web=driver.findElement(By.className("Modal-module_closeIcon__b4z74"));
	      web.click();
	      
	      
	      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	      Actions act=new Actions(driver);
	      String word="prize";
	      
	      for(int outerdiv=1;outerdiv<=2;outerdiv++) {
	    	  
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
	    	  if(txt.equals("correct")|| txt.equals("present")) {
	    		  
	    		  removeDictionarywords(wordSet,word.charAt(innerdiv-1),innerdiv-1,txt);
	    	  }
	    	  else if(txt.equals("absent")){
	    		  //absent
	    		  //int common=0;
	    		  HashSet<String> checkduplicates = new HashSet<String>();
	    		  System.out.println(txt);
	    		  for(int checkanyotherword=1;checkanyotherword<=WORD_LENGTH;checkanyotherword++) {
	    			 
	    			  if(checkanyotherword!=innerdiv) {
	    				  if(word.charAt(checkanyotherword-1)==word.charAt(innerdiv-1)) {
	    					  WebElement checkduplicate=driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div["+outerdiv+"]/div["+checkanyotherword+"]/div"));
	    					  System.out.println("checkduplicate"+word.charAt(checkanyotherword-1));
	    					  String checkduplicatetxt=checkduplicate.getAttribute("outerHTML".valueOf("data-state")).split(" ").clone()[0];
	    					  if(checkduplicatetxt=="correct") {
	    						  checkduplicates.add("correct");
	    						  break;
	    					  }
	    					  else if(checkduplicatetxt=="present"){
	    						  checkduplicates.add("present");
	    						  break;
	    					  }
	    					  else {
	    						 
	    					  }
	    				  }
	    				  
	    			  }
	    		  }
	    		  if(checkduplicates.contains("correct")|| checkduplicates.contains("present")) {
	    			  System.out.println("present at some other location");
	    			  removeDictionarywords(wordSet,word.charAt(innerdiv-1),innerdiv-1,"present");
	    		  }
	    		  else {
	    			  System.out.println("absent everywhere");
	    			  removeDictionarywords(wordSet,word.charAt(innerdiv-1),innerdiv-1,"absent");
	    		  }
	    	  }
	    
	      }
	      
	      driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
	     // System.out.println("------------------");
	      if(outerdiv==1) {
	    	  word="drive";
	      }
	      
	      }
	      
	      for(String s:wordSet) {
	    	  System.out.println(s);
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
		    else if(method.equals("present")){
		    	if(element.charAt(charposition)==chartocheck) {
					iterator.remove();
				    }	
		    }
		    else {
		    	if(element.indexOf(chartocheck)!=-1) {
		    		System.out.println("hereee");
		    		iterator.remove();
		    	}
		    }
		}
		
		 System.out.println("wordset size after  ---- " +wordSet.size());
		
	}
}

    