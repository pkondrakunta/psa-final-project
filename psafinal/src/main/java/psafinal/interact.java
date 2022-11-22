package psafinal;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	    public static final int ATTEMPTS = 5;
	    public static final int SUGGESTION_COUNT = 10;
	public static void main(String[] args ) throws IOException, InterruptedException {
		
		
		WebDriver driver =WebDriverManager.chromedriver().create();
	      driver.get("https://www.nytimes.com/games/wordle/index.html");
	      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	      
	      // to close the pop-up page
	      WebElement web=driver.findElement(By.className("Modal-module_closeIcon__b4z74"));
	      web.click();
	      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	      Actions act=new Actions(driver);
	      String word="";
	      
	      for(int outerdiv=1;outerdiv<=2;outerdiv++) {
	      for(int innerdiv=1;innerdiv<=5;innerdiv++) {
	    	  web=driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div["+outerdiv+"]/div["+innerdiv+"]/div"));
	    	  Thread.sleep(2000);
	    	  act.sendKeys(word.charAt(innerdiv-1)+"").perform();
	      System.out.println(web.getLocation()+" | ");
	      }
	      act.sendKeys(Keys.ENTER).perform();
	      Thread.sleep(5000);
	      for(int innerdiv=1;innerdiv<=5;innerdiv++) {
	    	  web=driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div/div["+outerdiv+"]/div["+innerdiv+"]/div"));
	    	  System.out.print(web.getAttribute("outerHTML")+" ");
	    	 
	    	  //web.sendKeys("c");
	      System.out.println(web.getLocation()+" | ");
	      }
	     
	      System.out.println("------------------");
	      }


	        System.out.println("Page title is: " + driver.getTitle());
	        System.out.println("url" + driver.getCurrentUrl());
	        //driver.quit();
	        
	}
}

    