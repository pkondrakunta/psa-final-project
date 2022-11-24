package WordleSolverBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class main {

	public static void main(String[] args) throws IOException {
		
		
		Scanner scn=new Scanner(System.in);
		File file = new File("wordlist/valid-wordle-words.txt");
		BufferedReader br= new BufferedReader(new FileReader(file));
		String st;
		HashSet<String> wordSet = new HashSet<String>();
		HashMap<Character, Integer> letterfrequency = new HashMap<Character, Integer>();
		HashMap<Character, Integer> firstletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> secondletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> thirdletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> fourthletter = new HashMap<Character, Integer>();
		HashMap<Character, Integer> fifthletter = new HashMap<Character, Integer>();
		
		while ((st = br.readLine()) != null )
		{
			wordSet.add(st);
			
			for(int i=0;i<st.length();i++) {
				if(i==0) {
					storefrequency(firstletter,st.charAt(i));
				}
				else if(i==1) {
					storefrequency(secondletter,st.charAt(i));
				}
				else if(i==2) {
					storefrequency(thirdletter,st.charAt(i));
				}
				else if(i==3) {
					storefrequency(fourthletter,st.charAt(i));
				}
				else {
					storefrequency(fifthletter,st.charAt(i));
				}
				
				storefrequency(letterfrequency,st.charAt(i));
			}
		}
		HashSet<String> copyofWordSet = new HashSet<String>();
		copyofWordSet = (HashSet<String>) wordSet.clone();
		
		System.out.println(letterfrequency);
		System.out.println(firstletter);
		System.out.println(secondletter);
		System.out.println(thirdletter);
		System.out.println(fourthletter);
		System.out.println(fifthletter);
		
		
		
		getRandomword gtr=new getRandomword();
		String random=getRandomword.getRandomElement(wordSet);
		//String random="jiffy";
		
		HashSet<Character> englishWords=new HashSet<Character>();
		for(int i=0;i<26;i++) {
			
			englishWords.add((char)(i+97));
		}
		
		Character[] answersofar=new Character[5];
		for(int i=0;i<5;i++) {
			answersofar[i]='*';
		}
		System.out.println("Today word is "+random);
		int count=0;
		//String result="tears";
		while(count<6) {
			System.out.println("Enter your "+ (count+1) + " word of 5 letters");
			String userguess=scn.nextLine();
			if(userguess.length()!=5) {
				System.out.println("Your word is of length "+userguess.length()+" we need only 5 letter words");
				continue;
			}
			if(!wordSet.contains(userguess)) {
				System.out.println("Input guess is not present. Retry!!!");
				continue;
			}
			count++;
			if(random.equals(userguess)) {
				System.out.println("You win in "+count +" chances");
				break;
			}
			else {
				
				for(int charvisit=0;charvisit<random.length();charvisit++) {
					boolean[] visited=new boolean[random.length()];
					
					if(random.charAt(charvisit)==userguess.charAt(charvisit)) { //if it is green
						System.out.print(userguess.charAt(charvisit));
						System.out.println(" letter at correct position "+ (charvisit+1));
						visited[charvisit]=true;
						answersofar[charvisit]=userguess.charAt(charvisit);
						removeDictionarywords(copyofWordSet,userguess.charAt(charvisit),charvisit,"correct");
					}
					else {
						int checkcharpresent=0;
						for(int charvisitrandom=0;charvisitrandom<random.length();charvisitrandom++) {
							if(visited[charvisitrandom]==false && userguess.charAt(charvisitrandom)!=random.charAt(charvisitrandom)) {
								if(userguess.charAt(charvisit)==random.charAt(charvisitrandom)) {
									checkcharpresent++;
									System.out.print(userguess.charAt(charvisit));
									System.out.println(" letter present at incorrect position "+ (charvisit+1));
									visited[charvisit]=true;
//									removeDictionarywords(copyofWordSet,userguess.charAt(charvisit),charvisit,"incorrect");
//									break;
								}
							}
						}
						if(checkcharpresent==0) {
							System.out.print(userguess.charAt(charvisit));
							System.out.println(" letter not present "+ (charvisit+1));
							
							englishWords.remove(userguess.charAt(charvisit));
							
							//remove from dictionary of words
							
							removeDictionarywords(copyofWordSet,userguess.charAt(charvisit),-1,"notpresent");
						}
						else {
							//removeDictionarywords(copyofWordSet,userguess.charAt(charvisit),charvisit,"incorrect");
						}
						
					}
					
				}
				System.out.println("Set of alphabets left "+ englishWords);
				for(int i=0;i<5;i++) {
					System.out.print(answersofar[i]);
				}
				System.out.println(copyofWordSet);
				System.out.println("");
			}
			

		}
		System.out.println("Today word is "+random);
		System.out.println("Game finish");
		
		
	}



	private static void removeDictionarywords(HashSet<String> copyofWordSet, char charAt,int charvisit,String method) {
	
		System.out.println("copy of wordset size before  ---- " +copyofWordSet.size());
		Iterator<String> iterator = copyofWordSet.iterator();
		while (iterator.hasNext()) {
		    String element = iterator.next();
		    if(method.equals("notpresent") || method.equals("correct")) {
		    if(element.indexOf(charAt)!=charvisit) {
			iterator.remove();
		    }
		    }
		    else {
		    	if(element.indexOf(charAt)==charvisit) {
					iterator.remove();
				    }	
		    }
		    
		}
		
		 System.out.println("copy of wordset size after  ---- " +copyofWordSet.size());
//rectify the copyofwordset as it is taking the refernece of main wordset		 
//if the letter is not present in today answer then its index should not be equal to 1
// if the letter is present at correct position then we would remove all which do not have at correct position
//		 

	}

	private static void storefrequency(HashMap<Character, Integer> frequencystore, char currentChar) {
		if(frequencystore.containsKey(currentChar)) {
			frequencystore.put(currentChar,frequencystore.get(currentChar)+1);
		}
		else {
			frequencystore.put(currentChar,1);
		}
		
	}


}


