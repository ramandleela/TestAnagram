package TestAnagram;

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
import com.ecs.anagram.component.BaseRecord;



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
		int wordLength = 6;
		
		logger.info("Test Service: postive test cases");
		testInput("glare");
		testInput("ezons");
		testInput("spool");
		
		logger.info("Test Service: random alpha strings");
		for (int i = 0; i < 10; i++) {
			testInput(generateRandomChars(alphaChars,wordLength));
		}
		
		logger.info("Test Service: random alphanumeric strings");
		for (int i = 0; i < 10; i++) {
			testInput(generateRandomChars(alphanumericChars,wordLength));
		}
		logger.info("End of Test Service");
	}
	
 
	protected void testInput(String input) {
 		logger.info("Input: " + input);
 		BaseRecord baseRecord = anagramComponent.findAnagrams(input);
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

	
