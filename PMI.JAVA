import java.io.*;
import java.util.*;

public class PMI 
{
	public static ArrayList<String> seedWords = new ArrayList<String>();
	public String seedWordsPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
	public PMI()
	{
		
	}
	public void generateSeedWords(String filePath) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String str = "";
		while((str = br.readLine()) != null)
		{
			if(str.equals("") || str.equals("\n"))
				continue;
			seedWords.add(str);
		}
		br.close();			
	}
	/**
	 * computes the PMI for a given string
	 * @param text
	 */
	public void computePMI(String text)
	{
		
	}
	/**
	 * computes the PMI
	 * @param args
	 */
	public static void main(String args[])
	{
		
	}
}
