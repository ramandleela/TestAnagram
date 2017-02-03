package com.ecs.anagram.component;

import java.util.ArrayList;
// Java
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


// This Service

@Component
public class AnagramComponent {
	private static final Logger logger = LoggerFactory.getLogger(AnagramComponent.class);
	//file name and file location can be part of external configuration
	private static final String fileName = "dictionary.txt";
	private static String dictionaryWords;
	private BaseRecord baseRecord;
	
	@Autowired
	public AnagramComponent (BaseRecord baseRecord){
		this.baseRecord = baseRecord;		
	}
	
	public void initialize() {
		
		/*
		//Retreive dictionary from online
		RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> response = restTemplate.getForEntity("https://users.cs.duke.edu/~ola/ap/linuxwords", String.class);
    	if(response != null && response.getStatusCode() == HttpStatus.OK) {
			String ecsWords = response.getBody().toString();
			logger.info("ecsWords: " + ecsWords);
		} else {
			logger.warn("getting ecsWords failed");
		}
    	*/
		
		// Build Dictionary Words from cached file based
		dictionaryWords = readDictionary(fileName);
		logger.info("Dictionary Words retrieved...");
		logger.info("Dictionary Words are:" + dictionaryWords);
		
	}
	
	public BaseRecord findAnagrams(String searchWord) {
		// Use TreeMap to achieve sorting
		TreeSet <String> result = new TreeSet <String>();
		  
		// input validations
		if(searchWord == null || searchWord.isEmpty()) {
			this.baseRecord = new BaseRecord();
		}
		
		List<String> listOfStrings = new ArrayList<>();		
		listOfStrings = Arrays.asList(dictionaryWords.split(","));
		Collections.sort(listOfStrings);
	
		Set<String> anagrams = getAnagrams(searchWord);
		logger.info("Anagrams are: " + anagrams);
		
		// Find anagram words in Dictionary
		for (String thisString: anagrams) {
			String found = findInDictionary(listOfStrings, thisString);
			if((found != "Not Found") && (!(found.equals(searchWord)))) {
				result.add(found);
			}
		}
		logger.info("Anagram Words are:" + result);
		if(result.isEmpty()) {
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
		} else {
			this.baseRecord = new AnagramRecord(searchWord, result.toString());
		}
			
		return this.baseRecord;
	}
	 
	// This method returns all possible anagrams or null
	private Set<String> getAnagrams(String str) {
		// Use HashSet because don't need sorting and it is faster
		Set<String> Result = new HashSet <String>();	
		if (str == null) {
			//System.out.println("String is null");
			return null;
		} else if (str.length() == 0) {
			//System.out.println("String is empty");
			Result.add("");
			return Result;
		}
 
		char firstChar = str.charAt(0);
		String rem = str.substring(1);
		// First take out the first char from String and permute the remaining chars
		Set<String> words = getAnagrams(rem);
		for (String newString : words) {
			for (int i = 0; i <= newString.length(); i++) {
				Result.add(addChar(newString, firstChar, i));
			}
		}
		return Result;
		
	}
	 
	private String addChar(String str, char c, int j) {
		String first = str.substring(0, j);
		String last = str.substring(j);
		return first + c + last;
	}
		
	private String findInDictionary(List <String> dictionaryWords, String word) {
		int index = 0;		
		// Search as all lowercase
		// Because arg1 is sorted faster binrary search is used
		index = Collections.binarySearch(dictionaryWords, word.toLowerCase());
		//System.out.println("index: " + index);
		if(index < 0){
			// if not found as  first letter capital
			index = Collections.binarySearch(dictionaryWords, word.substring(0,1).toUpperCase() + word.substring(1));
			//System.out.println(word.substring(0,1).toUpperCase() + word.substring(1));
			//System.out.println("index: " + index);
			if (index < 0) {
				return "Not Found";
			}
			//System.out.println("Word: " +listOfStrings.get(index));
			return dictionaryWords.get(index);
		} else {
			//System.out.println("index: " + index);
			return dictionaryWords.get(index);
		} 
	}
		
	private String readDictionary(String fileName) {
		StringBuilder lines = new StringBuilder();
		String line = null;
			
		try {
			BufferedReader bufferedReader = new BufferedReader (new FileReader(fileName));
			while ( (line = bufferedReader.readLine()) != null) {
				lines.append(line);
				lines.append(",");
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			    ex.printStackTrace();
			
		} catch (IOException ex2) {
			   ex2.printStackTrace();
		}

		//System.out.println(lines.toString());
		return lines.toString();
	}
}


