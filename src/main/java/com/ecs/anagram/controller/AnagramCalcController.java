package com.ecs.anagram.controller;


// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// This Service
import com.ecs.anagram.component.AnagramComponent;
import com.ecs.anagram.component.BaseRecord;

@RestController
public class AnagramCalcController {
	private static final Logger logger = LoggerFactory.getLogger(AnagramCalcController.class);
	public static String host = "http://localhost:8010";
	//host:port must be a configurable param in an external config server so services can be scaled out and in by admin tools
	AnagramComponent anagramComponent;
	
	
	@Autowired
	AnagramCalcController(AnagramComponent anagramComponent){
		this.anagramComponent = anagramComponent;
	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/word/{searchWord}")
	BaseRecord getAnagrams(@PathVariable String searchWord) {
		logger.info("in getAnagrams(()");
		BaseRecord baseRecord = anagramComponent.findAnagrams(searchWord);
		return baseRecord;
	}
	/*
	@RequestMapping(method = RequestMethod.GET, path = "/word/{searchWord}")
	public ResponseEntity<BaseRecord> getAnagrams(@PathVariable String searchWord) {
		logger.info("in getAnagrams(()");
		if(searchWord == null || searchWord.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
		 }
		BaseRecord baseRecord = anagramComponent.findAnagrams(searchWord);
		return ResponseEntity.ok(baseRecord); 
	}
	*/
	
}
