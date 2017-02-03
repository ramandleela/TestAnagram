package com.ecs.anagram.component;

import org.springframework.stereotype.Component;

@Component
public class MessageRecord extends BaseRecord{
	private String  message;

public MessageRecord() { 
}
	
public MessageRecord(String message) {
	this.message = message;
}

 public String getMessage() {
     return message;
 }

 public void setMessage(String message) {
     this.message = message;
 }
}
 