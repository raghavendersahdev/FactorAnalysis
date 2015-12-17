import java.io.*;
import java.util.*;

public class PreProcess 
{
	//the variable stopWords should be initialized only once
	public static ArrayList<String> stopWords = new ArrayList<String>();
	
	// the following function populates the stopWords arrayList
	public void generateStopWords(String filePath)throws FileNotFoundException,IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String str = "";
		while((str = br.readLine()) != null)
		{
			if(str.equals("") || str.equals("\n"))
				continue;
			stopWords.add(str);
		}
		br.close();	
	}
	
	public void preprocess(String filePath)throws FileNotFoundException,IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String str = "";
		
		while((str = br.readLine()) != null)
		{
			if(str.equals("") || str.equals("\n"))
				continue;
			stopWords.add(str);
		}
		
		br.close();
		
		
		
	}
	
	public static void main(String args[])throws IOException
	{
		PreProcess tester = new PreProcess();
		String corpus_Path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/2013-07/en_2013-07-18_0b2be55af46644a7d56fcd8d8d3e7929ce24b2.xml";

		// generate stop words
		String stopWords_FilePath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/stopWords.txt";
		tester.generateStopWords(stopWords_FilePath);
		System.out.println(stopWords.size());
		
	}
	
	
	
	
	public static void checkStopWord(String word,String filePath) throws FileNotFoundException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		
	}
	public static void stemming(String filePath)
	{
		
	}
}
