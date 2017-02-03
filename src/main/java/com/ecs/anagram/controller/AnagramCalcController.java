package com.ecs.anagram.controller;

// Java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// Spring
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// This Service
import com.ecs.anagram.component.AnagramComponent;
import com.ecs.anagram.component.AnagramRecord;
import com.ecs.anagram.component.BaseRecord;

@RestController
public class AnagramCalcController {
	private static final Logger logger = LoggerFactory.getLogger(AnagramCalcController.class);
	public static String host = "http://localhost:8010";
	//public final String hostAlt= "http://localhost:8010";
	AnagramComponent anagramComponent;
	
	
	@Autowired
	AnagramCalcController(AnagramComponent anagramComponent){
		this.anagramComponent = anagramComponent;
	}

	/*
	@RequestMapping(method = RequestMethod.GET, value = "/word/{searchWord}")
	BaseRecord getAnagrams(@PathVariable String searchWord) {
		logger.info("in getAnagrams(()");
		return this.anagramComponent.findAnagrams(searchWord);
	}
	*/
	@RequestMapping(method = RequestMethod.GET, path = "/word/{searchWord}")
	public ResponseEntity<BaseRecord> getAnagrams(@PathVariable String searchWord) {
		logger.info("in getAnagrams(()");
		if(searchWord == null || searchWord.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //return 404, with null body
		 }
		BaseRecord baseRecord = anagramComponent.findAnagrams(searchWord);
		return ResponseEntity.ok(baseRecord); // // return 200, with json
	}
/*
 	@RequestMapping(value="/", method = RequestMethod.GET)
 	public String getAllBooks(Model model){
 		logger.info("in getAllBooks()");
 		return getBooks(model);
 	}

	@RequestMapping("/{id}")
	String getBook(@PathVariable Integer id, Model model){
		try {
			logger.info("in getBook()");
			String path = "/book/" + id.toString();
			ResponseEntity<Map> entity = this.restTemplate.getForEntity(host + path, Map.class);
			if(entity != null && entity.getStatusCode() == HttpStatus.OK) {
				logger.info("Entity All " + entity.toString());
				logger.info("Entity Headers" + entity.getHeaders());
				
				String body = entity.getBody().toString();
				logger.info("Entity Body " + body);
				BookRecord bookRecord = frontOfficeComponent.convertJsontoBookRecord(body);
				bookRecord.setId(id);
				logger.info("Book " + bookRecord.toString());
				model.addAttribute("book", bookRecord);
			} else {
				logger.warn("GET request for book failed");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "bookshow";
	}
	
	@RequestMapping(value="/create")
	public String createBook(Model model){
		logger.info("in createBook()");
		model.addAttribute("book", new BookRecord());
		return "bookform";
	}
	
	@RequestMapping("/edit/{id}")
	public String editBook(@PathVariable Integer id, Model model){
		try {
			logger.info("in editBook()");
			String path = "/book/" + id.toString();
			ResponseEntity<Map> entity = this.restTemplate.getForEntity(host + path, Map.class);
			if(entity != null && entity.getStatusCode() == HttpStatus.OK) {
				logger.info("Entity All " + entity.toString());
				logger.info("Entity Headers" + entity.getHeaders());
				
				String body = entity.getBody().toString();
				logger.info("Entity Body " + body);
				BookRecord bookRecord = frontOfficeComponent.convertJsontoBookRecord(body);
				bookRecord.setId(id);
				logger.info("Book " + bookRecord.toString());
				model.addAttribute("book", bookRecord);
			} else {
				logger.warn("GET request for book failed");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		return "bookform";
	}

	@RequestMapping(value="book", method = RequestMethod.POST)
	public String saveBook(BookRecord book, Model model){
		try {
			logger.info("in saveBook(()");
			String path = "/savebook";
			logger.info("Book from Args: " + book.toString());
			String s = book.toString();
			ResponseEntity<Object[]> entity = this.restTemplate.postForEntity(host + path, book, Object[].class);
			if(entity != null && entity.getStatusCode() == HttpStatus.OK) {
				logger.info("Saved Book");
			} else {
				logger.warn("POST request for book failed");
			} 
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "redirect:/books";
    }
	
	@RequestMapping(value = "/delete/{id}")
    public String deleteBook(@PathVariable Integer id){
		try {
			logger.info("in deleteBook() Id: " + id);
			return deleteBookRemote(id);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "redirect:/books";
	
    }
	
	@RequestMapping(value="id", method = RequestMethod.POST)
    public String deleteBookRemote(Integer id){
		try {
			logger.info("in deleteBookRemote() Id: " + id);
			String path = "/deletebook";
			ResponseEntity<Object[]> entity = this.restTemplate.postForEntity(host + path, id, Object[].class);
			if(entity != null && entity.getStatusCode() == HttpStatus.OK) {
				logger.info("Deleted Book");
			} else {
				logger.warn("POST request for delete book failed");
			} 
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "redirect:/books";	
    }
	
	
	
	@RequestMapping(value="books", method = RequestMethod.GET)
	public String getBooks(Model model){
		try {
			logger.info("in getBooks()");
			String path = "/books";
			List<BookRecord> books = new ArrayList<>();
			ResponseEntity<Object[]> entity = this.restTemplate.getForEntity(host + path, Object[].class);
			if(entity != null && entity.getStatusCode() == HttpStatus.OK) {
				Object[] objects = entity.getBody();
				List<Object> listOfStrings = new ArrayList<>();
				listOfStrings = Arrays.asList(objects);
				//logger.info("Entity All " + entity.toString());
				//logger.info("Entity Headers" + entity.getHeaders());
				logger.info("Entity Body " + listOfStrings);
				for (Object i : listOfStrings) {
					logger.info("Entity Body " + i.toString());
					BookRecord bookRecord = frontOfficeComponent.convertJsontoBookRecord(i.toString());
					books.add(bookRecord);
				}
				logger.info("Array size " + books.size());
				model.addAttribute("books", books);
			} else {
				logger.warn("GET request for books failed");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
		return "books";
    }
    */
}
