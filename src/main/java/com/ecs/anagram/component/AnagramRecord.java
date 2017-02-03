package com.ecs.anagram.component;

//Spring

import org.springframework.stereotype.Component;

@Component
public class AnagramRecord extends BaseRecord{
	private String  word;
	private String  anagrams;

public AnagramRecord() { 
}
	
public AnagramRecord(String word, String anagrams) {
	this.word= word;
	this.anagrams = anagrams;
}

 public String getWord() {
     return word;
 }

 public void setWord(String word) {
     this.word = word;
 }
 public String getAnagrams() {
     return anagrams;
 }

 public void setAnagrams(String anagrams) {
     this.anagrams = anagrams;
 }
 
}
