package dataMining;

import java.io.*;
import java.util.*;

public class RemoveInvalid
{
	public static String corpusPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/replaced_nov/2013-11";
	public static void main(String args[]) throws IOException
	{
		long time1 = System.currentTimeMillis();
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		int NUM_FILES = listOfFiles.length;
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			if(listOfFiles[i].isFile())
			{
				filePaths.add(corpusPath+"/"+listOfFiles[i].getName());
			}
		}
		System.out.println(NUM_FILES);
		System.out.println(NUM_FILES);
		BufferedReader br= null;
		BufferedWriter bw= null;
		String new_one = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/replaced2/f";
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			br = new BufferedReader(new FileReader(filePaths.get(i)));
			bw = new BufferedWriter(new FileWriter(new_one+i+".txt"));
			String temp = "";
			String addthis="";
			while((temp=br.readLine())!=null)
			{
				addthis = addthis + temp;
				
				
			}
			addthis = addthis.replace('&', ' ');
			addthis = addthis.replace('#', ' ');
			bw.write(addthis);
			bw.close();
			br.close();
		}
		
		long time2 = System.currentTimeMillis();
		System.out.println(time2-time1);
	}
}
