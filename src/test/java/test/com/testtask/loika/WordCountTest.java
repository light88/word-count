package test.com.testtask.loika;

import com.testtask.lohika.WordCount;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Map;

public class WordCountTest {

  @Rule
  public ExpectedException thrown= ExpectedException.none();

  @Test
  public void emptyRun() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    WordCount wordCount = new WordCount(new String[] {});
  }

  @Test
  public void runWithoutAllArgument() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    WordCount wordCount = new WordCount(new String[] {"1.txt"});
  }

  @Test
  public void runWithIncorrectFilePath() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    WordCount wordCount = new WordCount(new String[] {"1.txt", "5"});
  }

  @Test
  public void runWithIncorrectNumberValue() throws IOException {
    thrown.expect(IllegalArgumentException.class);
    WordCount wordCount = new WordCount(new String[] {"1.txt", "YYY"});
  }

  @Test
  public void runSimpleWordCount() throws IOException {
    String path = "src/test/resources/Test1.txt";
    String number = "10";
    WordCount wordCount = new WordCount(new String[] {path, number});
    Map<String, Integer> process = wordCount.process();

    Assert.assertEquals(1, process.size());
    Assert.assertEquals(new Integer(4), process.get("test"));
  }

  @Test
  public void runZeroWordCount() throws IOException {
    String path = "src/test/resources/Test2.txt";
    String number = "10";
    WordCount wordCount = new WordCount(new String[] {path, number});
    Map<String, Integer> process = wordCount.process();

    Assert.assertEquals(0, process.size());
  }

  @Test
  public void runOrder() throws IOException {
    String path = "src/test/resources/Test3.txt";
    String number = "10";
    WordCount wordCount = new WordCount(new String[] {path, number});
    Map<String, Integer> process = wordCount.process();

    Object[] objects = process.keySet().toArray();

    Assert.assertEquals(2, objects.length);
    Assert.assertEquals("b:a", String.format("%s:%s", objects));
  }

  @Test
  public void runOrderEqualsCount() throws IOException {
    String path = "src/test/resources/Test4.txt";
    String number = "10";
    WordCount wordCount = new WordCount(new String[] {path, number});
    Map<String, Integer> process = wordCount.process();

    Object[] objects = process.keySet().toArray();

    Assert.assertEquals(5, objects.length);
    Assert.assertEquals("a:c:x:y:z", String.format("%s:%s:%s:%s:%s", objects));
  }

  @Test
  public void runLimit() throws IOException {
    String path = "src/test/resources/Test4.txt";
    String number = "2";
    WordCount wordCount = new WordCount(new String[] {path, number});
    Map<String, Integer> process = wordCount.process();
    Assert.assertEquals(Integer.valueOf(number), Integer.valueOf(process.size()));
  }
}