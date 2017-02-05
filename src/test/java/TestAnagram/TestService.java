package TestAnagram;

import static org.junit.Assert.assertEquals;

// java
import java.util.Random;

// spring
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

// this service
import com.ecs.anagram.Application;
import com.ecs.anagram.component.AnagramComponent;
import com.ecs.anagram.component.AnagramRecord;
import com.ecs.anagram.component.BaseRecord;
import com.ecs.anagram.component.MessageRecord;



@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebAppConfiguration

public class TestService {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private AnagramComponent anagramComponent;
	
	
	@Test
	public void testService() {
		String alphaChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";	
		BaseRecord baseRecord = new BaseRecord();
		int testWordLength = 6;
		
		logger.info("Test Service: postive test cases");
		baseRecord = testInput("glare");
		assertEquals("[Alger, lager, large, regal]", ((AnagramRecord) baseRecord).getAnagrams());
		
		baseRecord = (AnagramRecord)testInput("ezons");
		assertEquals("[zones]", ((AnagramRecord) baseRecord).getAnagrams());
		
		baseRecord = (AnagramRecord)testInput("spool");
		assertEquals("[loops, pools, sloop]", ((AnagramRecord) baseRecord).getAnagrams());
		
		baseRecord = (AnagramRecord)testInput("newsi");
		assertEquals("[sinew, swine, wines]", ((AnagramRecord) baseRecord).getAnagrams());
		
		logger.info("Test Service: negative test cases");
		baseRecord = testInput("blah");
		assertEquals("Couldn't find word blah", ((MessageRecord) baseRecord).getMessage());
		
		baseRecord = testInput("");
		assertEquals("Couldn't find word ", ((MessageRecord) baseRecord).getMessage());
		
		baseRecord = testInput(" ");
		assertEquals("Couldn't find word  ", ((MessageRecord) baseRecord).getMessage());
		
		baseRecord = testInput(new String());
		assertEquals("Couldn't find word ", ((MessageRecord) baseRecord).getMessage());
		
		logger.info("Test Service: random alpha strings");
		// Random alpha strings may or may not find anagrams in dictionary
		// thus cannot be asserted
		for (int i = 0; i < 5; i++) {
			testInput(generateRandomChars(alphaChars,testWordLength));
		}
		
		logger.info("Test Service: random alphanumeric strings");
		// All alphanumeric strings should  fail thus can be asserted
		for (int i = 0; i < 5; i++) {
			String testStr = generateRandomChars(alphanumericChars,testWordLength);
			baseRecord = testInput(testStr);
			assertEquals("Couldn't find word " + testStr, ((MessageRecord) baseRecord).getMessage());
		}
		logger.info("End of Test Service");
	}
	
 
	protected BaseRecord testInput(String input) {
 		logger.info("Input: " + input);
 		BaseRecord baseRecord = anagramComponent.findAnagrams(input);
 		return baseRecord;
 	}
 	
 	protected String generateRandomChars(String candidateChars, int length) {
 		StringBuilder sb = new StringBuilder();
 		Random random = new Random();
 		for (int i = 0; i < length; i++) {
 			sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
 		}
 		return sb.toString();
 	}
}

	
