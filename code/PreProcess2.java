package dataMining;

import java.io.*;
import java.util.*;
/**
 * this file concatinates all the preprocessed files for being used next to compute the Word Embeddings of each word in the vocabulary
 * @author raghavender sahdev
 *
 */
public class PreProcess2 
{	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static String corpusPath = "/home/sahdev/Desktop/Fall2015/nov";
	public static String output_file ="/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/output_content_312k.txt";
	public static void main(String args[]) throws IOException
	{	
		long time1 = System.currentTimeMillis();
		
		
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		int NUM_FILES = listOfFiles.length;
		//int NUM_FILES = 7914;
		
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			if(listOfFiles[i].isFile())
			{
				//System.out.println("File: "+listOfFiles[i].getName());
				filePaths.add(corpusPath+"/"+listOfFiles[i].getName());
			}
		}
		
		
		String temp="";
		String whole_content="";
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(output_file));
		
		for(int i=0; i<filePaths.size() ; i++)
		{
			String temp_path = filePaths.get(i);
			BufferedReader br = new BufferedReader(new FileReader(temp_path));
			
			while((temp = br.readLine()) != null)
			{
				whole_content = whole_content+temp+" ";
			}
			whole_content = whole_content+"\n";
			bw.write(whole_content);
			whole_content = "";
			br.close();
		}
		
		
		bw.close();
		
		long time2 = System.currentTimeMillis();
		System.out.println(time2-time1);
	}
}
