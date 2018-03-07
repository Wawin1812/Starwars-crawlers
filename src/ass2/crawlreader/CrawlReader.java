package ass2.crawlreader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: abx
 * Date: 26/04/2015
 * Time: 11:18 PM
 * Created for ass2 in package ass2.crawlreader
 * 
 * <p>CrawlReader is the model class of the StarWarsCrawler program.
 * It reads in the content of the crawl text, separates</p>
 * <ul>
 * <li>title
 * <li>subtitle</li>
 * <li>prefix</li>
 * <li>main part</li>
 * </ul>
 * @author abx
 * @author (your name and id)
 * @see ass2.StarWarsCrawler
 */

public class CrawlReader {

    String title;
    String subtitle;
    String prefix;
    String mainLine= "";
    String mainPart;

    /* rawLines is the main part of the crawl; its format is not
     * changed (raw) and can be rendered into verses
     * by using method makeVerses*/

    private List<String> rawLines;
    private Object allLines;

    public List<String> makeVersedLines() {

        return this.rawLines;
    }

    /** convert rawLines, if it's formatted as one paragraph
     *  with long lines, into a verse-style
     *  @param lineLength is the max length of lines in versed form
     *  @param minSentenceNumber is minimal number of sentences in a verse
     *  @param maxSentenceNumber is the maximum number of sentences in a versed
     *  */
    public void makeVerses(int lineLength,
                           int minSentenceNumber,
                           int maxSentenceNumber) {
        if(rawLines !=null && lineLength > 0) {
            //this.title = rawLines.split("\\s*:\\s*")[0];
            
        }
       
        // TODO provide the body of the method here (Task 3)
    }


    /**
     * creates a crawl reader object by reading the content of
     * text file; separates crawl title, subtitle, prefix and the main part
     *
     * @param fileName is the name of the crawl file
     * @throws java.io.IOException if fileName cannot be open
     * */
    public CrawlReader(String fileName) throws IOException {
        String line;
        String prefixLine = "";
        List<String> allLines = Files.readAllLines(Paths.get(fileName));
        line = allLines.get(0);
        if (line != null && line.length() > 0) {
            this.title = line.split("\\s*:\\s*")[0];
            this.subtitle = line.split("\\s*:\\s*")[1];
            allLines.remove(0);
        }
        line = allLines.get(0);

        while (line != null && line.length() == 0) {
            allLines.remove(0);
            line = allLines.get(0);
        }

        while (line != null && line.length() > 0) {
            prefixLine += line + "\n";
            allLines.remove(0);
            line = allLines.get(0);
        }
        
        this.prefix = prefixLine;
        
        line = allLines.get(0);
    
        for(int i= 0;i<allLines.size();i++)
        {
            line=allLines.get(i);
            mainLine += line + "\n";
            //line = allLines.get(0);
        }
        this.mainPart = mainLine;
    }
    

    public String getTitle() {
        return this.title;
    }

    public String getSubTitle() {
        return this.subtitle;
    }

    public String getPrefix() {
        return this.prefix;
    }
    
    public String getmainPart() {
        return this.mainPart;
    }

    /* this is for self testing only */
    public static void main(String args[]) throws IOException {
        CrawlReader crawlReader = new CrawlReader(args[0]);
        System.out.printf("%s%n", crawlReader.title);
        System.out.printf("%s%n", crawlReader.subtitle);
        System.out.printf("%s%n", crawlReader.prefix);
        System.out.printf("%s%n", crawlReader.mainPart);
    }
}