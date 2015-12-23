package dataMining;

import java.io.*;
import java.util.*;
/**
 * 
 * @author raghavender sahdev
 *
 */
public class PMI_new
{
	public static ArrayList<String> seedWords = new ArrayList<String>();
	public static String seedWordsPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
	public static final double threshold_PMI = 0.0;
	/**
	 * default constructor, does not do much here
	 */
	public PMI_new()
	{
		
	}
	/**
	 * this generates the seed words by reading from the seed words file
	 * @param filePath the path of the file containing the seed words
	 * @throws IOException
	 */
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
	 * computes the PMI for a 2 given words a seed word and the document word in the specified file path
	 * @param seed this is the seed WOrd
	 * @param doc_word this is the document word
	 * @param filePath this is the specified document/article/blog file path
	 * @return the function returns the PMI value between the given seed and the document word
	 * @throws IOException 
	 */
	public double computePMI(String seed, String doc_word, String filePath) throws IOException
	{
		int word_window = 20; // this specifies the window size for computing the PMI
		float p_x = 0; // for seed word
		float p_y = 0; // for doc_word
		float p_xy = 0; // for both the seed and the doc word
		int wrd_cnt = 0;
		int seed_cnt = 0;
		int doc_word_cnt = 0;
		int together_cnt = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String temp = "";
		ArrayList<String> arr = new ArrayList<String>();
		
		while((temp = br.readLine()) != null)
		{
			arr.add(temp);
		}
		// WRITE ADDITIONAL CODE FOR COMPUTING P_XY HERE BY USING WINDOW_SIZE CONCEPT
		for(int i=0; i<arr.size() ; i++)
		{
			if(arr.get(i).equalsIgnoreCase(doc_word))
			{
				doc_word_cnt++;
			}
			if(arr.get(i).equalsIgnoreCase(seed))
			{
				seed_cnt++;
			}
			wrd_cnt++;
		}
		p_x = seed_cnt / wrd_cnt;
		p_y = doc_word_cnt / wrd_cnt;
		
		br.close();
		
		
		double pmi_score = Math.log(p_xy)/(Math.log(p_x)*Math.log(p_y));
		return  pmi_score;
	}
	public ArrayList<String> generateVocabulary()
	{
		return null;
	}
	/**
	 * computes the PMI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IOException
	{
		
		PMI_new pmiObject = new PMI_new();
		pmiObject.generateSeedWords(seedWordsPath);
		
		
		ArrayList<String> vocabulary = new ArrayList<String>();
		vocabulary = pmiObject.generateVocabulary();
		int seed_word_size = 78;
		int vocabulary_size = 28916;
		for(int i=0 ; i<seed_word_size ; i++)
		{
			for(int j=0; j<vocabulary_size ; j++)
			{
				double score = computePMI(pmiObject.seedWords.get(i),vocabulary.get(j));
			}
		}			
	}
	public static double computePMI(String seed_word, String vocab_word) throws IOException
	{
		String corpusPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/output_file.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(corpusPath));
		String temp="";
		StringTokenizer tkn;
		
		int px = 0;
		int py = 0;
		int pxy = 0;
		while( (temp=br.readLine()) != null)
		{
			tkn = new StringTokenizer(temp," ");
			String word="";
			boolean flag1 = true, flag2 = true;
			while(tkn.hasMoreTokens())
			{
				word = tkn.nextToken();
				if(flag1 == true && seed_word.equalsIgnoreCase(word))
				{
					px++;
					flag1 = false;
				}
				if(flag2 == true && vocab_word.equalsIgnoreCase(word))
				{
					py++;
					flag2 = false;
				}
				if(flag1 == false && flag2 == false)
				{
					pxy++;
					break;
				}
			}
		}
		
		double pmi_score = Math.log(pxy/(px*py));
		br.close();	
		return pmi_score;
	}
}
