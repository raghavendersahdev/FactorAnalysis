package dataMining;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * this class is responsible for pre processing the files and store the preprocessed files in a new folder
 * @author raghavender sahdev
 */
public class PreProcess3 
{
	public static ArrayList<String> stopWords = new ArrayList<String>();
	public static ArrayList<Character> punctuations = new ArrayList<Character>();
	public static String corpusPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/2013-11";
	public static String files_path = "/home/sahdev/Desktop/Fall2015/tester4/f"; 
	/**
	 * constructor to initialize the punctuation array list
	 */
	public static void generatePunctuations()
	{
		punctuations.add(',');		punctuations.add('.');
		punctuations.add(';');		punctuations.add(':');
		punctuations.add('\\');		punctuations.add('/');
		punctuations.add('!');	    punctuations.add('\'');	
		punctuations.add('\"');		punctuations.add('"');
		punctuations.add('\n');		punctuations.add('*');
		punctuations.add('(');		punctuations.add(')');
		punctuations.add('1');		punctuations.add('2');
		punctuations.add('3');		punctuations.add('4');
		punctuations.add('5');		punctuations.add('6');
		punctuations.add('7');		punctuations.add('8');
		punctuations.add('9');		punctuations.add('0');
		punctuations.add(']');		punctuations.add('[');
		punctuations.add('}');		punctuations.add('{');
		punctuations.add('~');		punctuations.add('?');
		punctuations.add('-');		punctuations.add('@');
		punctuations.add('#');		punctuations.add('$');
		punctuations.add('%');		punctuations.add('^');
		punctuations.add('&');		punctuations.add('_');
		punctuations.add('<');		punctuations.add('>');
		punctuations.add('’');		punctuations.add('|');
		punctuations.add('“');		punctuations.add('”');
		punctuations.add('»');
	}
	
	
	/** removes the punctuation like ,.:;" etc from the given string
	 * 
	 * @param text input from which the punctuation need to be removed
	 * @return returns the pre-processed after removing punctuation 
	 */
	public static String removePunctuation(String text)
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
	 * the function removes punctuation and stop words
	 * @param text input processed string
	 * @return the pre-processed string
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String preprocess(String text)throws FileNotFoundException,IOException
	{
		String text1 = removePunctuation(text);
		return text1;		
	}

	/**
	 * reads the files and pre-processes them
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[])throws IOException
	{
		long time1 = System.currentTimeMillis();
		
		generatePunctuations();
		
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		int NUM_FILES = listOfFiles.length;
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
			
			NodeList nList;
			Node nNode;
			Element eElement;
			String processThis;
			String date;
			for(int i=0 ; i<NUM_FILES; i++)
			{
				File fXmlFile = new File(filePaths.get(i));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				  
				doc.getDocumentElement().normalize();				
				nList = doc.getChildNodes();			

				nNode = nList.item(0);				

				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{			
					eElement = (Element) nNode;					   
					processThis = eElement.getElementsByTagName("Text").item(0).getTextContent();
					date = eElement.getElementsByTagName("PublicationDateTime").item(0).getTextContent();					
					
					String processed2 = preprocess(processThis);
					String file_number = String.format("%06d", i+1);
					FileWriter fp = new FileWriter(files_path+file_number+".txt");
					fp.write(date + "\n" + processed2);
					fp.close();					
				}
				
			} // end of for block
			
			
			
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
