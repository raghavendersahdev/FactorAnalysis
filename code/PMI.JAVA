package dataMining;
import java.io.*;
import java.util.*;
/**
 * 
 * @author raghavender sahdev
 *
 */
public class PMI 
{
	public static ArrayList<String> seedWords = new ArrayList<String>();
	public static String seedWordsPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
	public static final double threshold_PMI = 0.0;
	/**
	 * default constructor, does not do much here
	 */
	public PMI()
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
	/**
	 * computes the PMI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IOException
	{
		String corpusPath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/2013-07";
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		int NUM_FILES = 10;
		PMI pmiObject = new PMI();
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			if(listOfFiles[i].isFile())
			{
				//System.out.println("File: "+listOfFiles[i].getName());
				filePaths.add(corpusPath+"/"+listOfFiles[i].getName());
			}
		}

		pmiObject.generateSeedWords(seedWordsPath);
		double PMI_score =0.0;
		ArrayList<Double> PMI_scores_documents = new ArrayList<Double>();
		String current_filePath="";
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			current_filePath = filePaths.get(i);
			BufferedReader br = new BufferedReader(new FileReader(current_filePath));
			ArrayList<String> doc_words = new ArrayList<String>();
			String temp="";
			while((temp = br.readLine()) != null)
			{
				doc_words.add(temp);
			}
			// iterate over all words in the document to compute PMI between ith seed word and document words
			double PMI_score_file =0.0; 
			int avg_cnt = 0;
			for(int j=0 ; j<doc_words.size() ; j++)
			{
				double temp_PMI = pmiObject.computePMI(seedWords.get(i),doc_words.get(j),current_filePath);
				if(temp_PMI > threshold_PMI)
				{
					PMI_score_file = PMI_score_file + temp_PMI;
					avg_cnt++;
				}					
			}
			
			PMI_score_file = PMI_score_file/avg_cnt;
			PMI_scores_documents.add(PMI_score_file);
			
			
			br.close();
		}
	}
}
