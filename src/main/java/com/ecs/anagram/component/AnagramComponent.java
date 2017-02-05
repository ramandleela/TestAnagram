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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;


// This Service

@Component
public class AnagramComponent {
	private static final Logger logger = LoggerFactory.getLogger(AnagramComponent.class);
	//Dictionary words can be an externalized configuration property or in redis cache
	//For now it is stored in a file packaged with this service.
	//file name specified in application.properties
	
	@Value("${dictionaryFileName}")
	protected String fileName;
	protected static String dictionaryWords;
	protected BaseRecord baseRecord;
	
	@Autowired
	public AnagramComponent (BaseRecord baseRecord){
		this.baseRecord = baseRecord;		
	}
	
	// Intiliazes dictionary list at server startup
	public void initialize() {
		logger.info("fileName: " + fileName);
		
		// Build Dictionary Words from cached file based
		dictionaryWords = readDictionary(fileName);
		logger.info("Dictionary Words retrieved...");
		//logger.info("Dictionary Words are:" + dictionaryWords);
	}
	
	public BaseRecord findAnagrams(String searchWord) {
		
		logger.info("findAnagrams() searchWord " + searchWord);
		long startTime = System.currentTimeMillis();
		
		// input validations
		if(!validateSearchWord(searchWord)) {
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
			return baseRecord;
		}
		
		// Get dictionary words with length same as searchWord
		List<String> filteredList = getFilteredList(searchWord);		
		if(filteredList.isEmpty()) {
			logger.info("filteredList is empty");
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
			return baseRecord;
		}
		//logger.info("filteredList " + filteredList);
		
		// Find anagrams in the filtered list
		List<String> resultList = findAnagramsInFilteredList(searchWord, filteredList);
		if(resultList.isEmpty()) {
			logger.info("Couldn't find word " + searchWord);
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
		} else {
			logger.info("Anagrams found in Dictionary are:" + resultList);
			this.baseRecord = new AnagramRecord(searchWord, resultList.toString());
		}
		logger.info("Time taken: " + (System.currentTimeMillis() - startTime) + " milliseconds");	
		return this.baseRecord;
	}
	
	// Utility method to find dictionary words of a given length
	protected List<String> getFilteredList(String str) {
		List<String> filteredList = new ArrayList<>();
		List<String> listOfStrings = new ArrayList<>();		
		listOfStrings = Arrays.asList(dictionaryWords.split(","));
		int strLength = str.length();
		for (String s : listOfStrings) {
          if ((s.length() == strLength) && (0 != str.compareToIgnoreCase(s))) {
        	  filteredList.add(s);
          }
      }
      return filteredList;
	}
	
	protected List<String> findAnagramsInFilteredList(String searchWord, List<String> filteredList){
		List<String> foundList = new ArrayList<>();
		HashMap<Character,Integer> map = new HashMap<Character,Integer>();          
		for(int i = 0; i < searchWord.length(); i++){
		   char c = searchWord.charAt(i);
		   Integer val = map.get(new Character(c));
		   if(val != null){
		     map.put(c, new Integer(val + 1));
		   }else{
		     map.put(c,1);
		   }
		}
		//logger.info("findAnagramsInFilteredList() input: " + searchWord);
		//logger.info(map.toString());
		
        for(String filtered : filteredList) {
			boolean found = true;
			for(Map.Entry<Character,Integer> entry : map.entrySet()) {
				Character key = entry.getKey();
				Integer value = entry.getValue();
				//logger.info(key.charValue() + " - " + value.intValue());
				if (value.intValue() != countOccurrences(filtered.toLowerCase(), key.charValue())) {
					found = false;
					//logger.info(filtered + " not found");
					break;
				}
			}
			if(found) {
				//logger.info("Adding " + filtered + " to foundList");
				foundList.add(filtered);
			}
        }
			
        //logger.info(foundList.toString());
		return foundList;
	}
	
	// Utility method to count occurrences of a char in a given string
	protected int countOccurrences(String str, char c)
	{
	    int count = 0;
	    for(int i = 0; i < str.length(); i++)
	    {
	        if(str.charAt(i) == c) count++;
	    }
	    return count;
	}
	
	// Optimizations to stop Search Words here to avoid calling recursive method 
	// This versions uses Regex for alphabets for to validate
	protected boolean validateSearchWord(String searchWord) {		
		Pattern pattern = Pattern.compile("[A-Za-z]*");
		Matcher matcher = pattern.matcher(searchWord);
		return  matcher.matches() ? true: false;
	}
		
	// Utility method to read dictionary words from a file
	protected String readDictionary(String fileName) {
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
		
	/*
	// This is the service method to find valid anagrams
	public BaseRecord findAnagrams(String searchWord) {
		// Use TreeMap to achieve sorting
		TreeSet <String> result = new TreeSet <String>();
		List<String> listOfStrings = new ArrayList<>();		
		listOfStrings = Arrays.asList(dictionaryWords.split(","));
		Collections.sort(listOfStrings);
		int maxLength = getMaxLength(listOfStrings);
		
		if(searchWord.length() > maxLength){
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
			return baseRecord;
		}	
		// input validations
		if(!validateSearchWord(searchWord)) {
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
			return baseRecord;
		}
		
		if(searchWord == null || searchWord.isEmpty()) {
			this.baseRecord = new BaseRecord();
		}
		
		Set<String> anagrams = getAnagrams(searchWord);
		logger.info("Anagrams are: " + anagrams);
		
		// Find anagram words in Dictionary
		for (String thisString: anagrams) {
			String found = findInDictionary(listOfStrings, thisString);
			if((found != "Not Found") && (!(found.equals(searchWord)))) {
				result.add(found);
			}
		}
		logger.info("Anagrams found in Dictionary are:" + result);
		if(result.isEmpty()) {
			this.baseRecord = new MessageRecord("Couldn't find word " + searchWord);
		} else {
			this.baseRecord = new AnagramRecord(searchWord, result.toString());
		}
			
		return this.baseRecord;
	}
	
	
	// Optimizations to stop Search Words here to avoid calling recursive method 
	// This versions uses Regex for alphabets for to validate
	protected boolean validateSearchWord(String searchWord) {		
		Pattern pattern = Pattern.compile("[A-Za-z]*");
		Matcher matcher = pattern.matcher(searchWord);
		if(matcher.matches()) {
			logger.info((searchWord + " matches"));
			return true;		
		} else {
			logger.info((searchWord + " does not match"));
			return false;
		}				
	}
	 
	/*
	// This method returns all possible anagrams or null
	// This is a recursive method
	protected Set<String> getAnagrams(String str) {
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
	
	// Utility method to generate anagrams
	protected String addChar(String str, char c, int j) {
		String first = str.substring(0, j);
		String last = str.substring(j);
		return first + c + last;
	}
		
	// Utility method to search anagrams in the dictionary 
	protected String findInDictionary(List <String> dictionaryWords, String word) {
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
	
	// Utility method to find the length of the LongestString in an Array
	protected int getMaxLength(List<String> listOfStrings) {
      int maxLength = 0;
      for (String s : listOfStrings) {
          if (s.length() > maxLength) {
              maxLength = s.length();
          }
      }
      return maxLength;
	}
	*/
	

}


