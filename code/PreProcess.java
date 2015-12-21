import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 * Contains the code to pre-process xml files
 */
public class PreProcess 
{
	//the variable stopWords should be initialized only once
	public static ArrayList<String> stopWords = new ArrayList<String>();
	public static ArrayList<Character> punctuations = new ArrayList<Character>();
	public String stopWordsFile = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/stopWords.txt";
	/**
	 * constructor to initialize the punctuation array list
	 */
	public PreProcess()
	{
		punctuations.add(',');
		punctuations.add('.');
		punctuations.add(';');
		punctuations.add(':');
		punctuations.add('\\');
		punctuations.add('/');
		punctuations.add('!');	
		punctuations.add('\'');	
		punctuations.add('\"');
		punctuations.add('"');
		punctuations.add('\n');
		punctuations.add('*');
		punctuations.add('(');
		punctuations.add(')');
		punctuations.add('1');
		punctuations.add('2');
		punctuations.add('3');
		punctuations.add('4');
		punctuations.add('5');
		punctuations.add('6');
		punctuations.add('7');
		punctuations.add('8');
		punctuations.add('9');
		punctuations.add('0');	
	}
	
	/**
	 *  the following function populates the stopWords arrayList
	 * @param filePath specify the file Path containing the stopWords
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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
	/** removes the punctuation like ,.:;" etc from the given string
	 * 
	 * @param text input from which the punctuation need to be removed
	 * @return returns the pre-processed after removing punctuation 
	 */
	public String removePunctuation(String text)
	{
		text = text.toLowerCase();
		String temp = text;
		for(int i=0 ; i<text.length() ; i++)
		{
			if(punctuations.contains(text.charAt(i)))
			{
				if(text.charAt(i) == '\n')
				{
					temp = temp.replace(text.charAt(i)+"", " ");
					continue;
				}	
				temp = temp.replace(text.charAt(i)+"","");//.charAt(i) = "";				
			}
		}
		return temp;
	}
	/**
	 * this returns the processed String after removing the stop words, it replaces the stop words with an empty string
	 * @param text the input string to be pre-processed
	 * @return the processed string
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public String removeStopWords(String text) throws FileNotFoundException, IOException
	{
		generateStopWords(stopWordsFile);
		//System.out.println(stopWords);
		StringTokenizer tkn = new StringTokenizer(text," ");
		String processed = "";
		while(tkn.hasMoreTokens())
		{
			String temp_word = tkn.nextToken();
			if(stopWords.contains(temp_word))
			{
				text = text.replace(temp_word, "");
			}
			else
			{
				processed = processed + " " + temp_word;
			}
		}
		return processed;
	}
	/**
	 * the function removes punctuation and stop words
	 * @param text input processed string
	 * @return the pre-processed string
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String preprocess(String text)throws FileNotFoundException,IOException
	{
		String text1 = removePunctuation(text);
		String text2 = removeStopWords(text1);
		return text2;		
	}
	/**
	 * this function checks if the entered language is in english or not based on simple ascii value!
	 * @param text
	 * @return
	 */
	public boolean checkLanguage(String text)
	{
		for(int i=0 ; i<text.length() ; i++)
		{
			if((int)text.charAt(i) > 1000)
				return false;
		}
		return true;
	}
	/**
	 * write code for stemming here
	 * @param text
	 */
	public void stemming(String text)
	{
		
	}
	/**
	 * reads the files and pre-processes them
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[])throws IOException
	{
		long time1 = System.currentTimeMillis();
		
		PreProcess tester = new PreProcess();
		String corpus_Path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/2013-07/en_2013-07-18_0b2be55af46644a7d56fcd8d8d3e7929ce24b2.xml";
			
		String corpusPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/2013-07";
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		int NUM_FILES = 10000;
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			if(listOfFiles[i].isFile())
			{
				//System.out.println("File: "+listOfFiles[i].getName());
				filePaths.add(corpusPath+"/"+listOfFiles[i].getName());
			}
		}
		try 
		{
			for(int i=0 ; i<NUM_FILES; i++)
			{
				File fXmlFile = new File(filePaths.get(i));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				  
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();				
				//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());							
				NodeList nList = doc.getChildNodes();			  
				//doc.getElementsByTagName(Id);
				//System.out.println("----------------------------"+nList.getLength());					  
				Node nNode = nList.item(0);						
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());						
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{			
					Element eElement = (Element) nNode;					   
					String processThis = eElement.getElementsByTagName("Text").item(0).getTextContent();
					String date = eElement.getElementsByTagName("PublicationDateTime").item(0).getTextContent();
					
					if(tester.checkLanguage(processThis))
					{
						String processed2 = tester.preprocess(processThis);
						//System.out.println(processed2+" \n "+date);
						String file_number = String.format("%08d", i+1);
						FileWriter fp = new FileWriter("/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/tester"+file_number+".txt");
						fp.write(date + "\n" + processed2);
						fp.close();					
					}
					
				}	
			} // end of catch block
		} // end of try
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		
		long time2 = System.currentTimeMillis();
		System.out.println("Time elapsed: "+(time2-time1));
		// example of file having chinese language
		//BufferedReader file2 = new BufferedReader(new FileReader("/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/2013-07/en_2013-07-18_a1399b7e49d5dc9d19dba2fe30d9eaa6ff622ff7.xml"));
		
		
		
		/*String text = "Raghavender.sahdev ! how are yuou! ., : fdfd;";
		String processed = tester.preprocess(text);
		System.out.println(processed);		
		System.out.println(text);	
		*/	
	}// end of main function
}