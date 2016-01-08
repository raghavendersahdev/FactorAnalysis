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
	
	public static String corpusPathFile = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/july_35k/output_content_35k.txt";
	public static String corpusPath = "/home/sahdev/Desktop/Fall2015/july";
	
	public static String vocab_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/july_35k/vocabulary35k.txt";
	public static String seed_words_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
	public static String word2vec_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/july_35k/word2vec_35k.txt";
	
	public static String score_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/pmi_scores_35k.txt";
	
	
	public static String temp_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/temp_file.txt"; // this stores only the non null content introduced to get rid of the null pointer exceptions
	
	
	public static String vocabulary_counts = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/vocabulary_counts.txt";
	public static String pmi_matrix = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/pmi_matrix.txt";
	public static String co_occurrence_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/co_occurrence_matrix.txt";
	public static ArrayList<String> vocabulary = new ArrayList<String>();
	public static ArrayList<String> seed_words = new ArrayList<String>();
	public static HashMap<String,Integer> vocabulary_map = new HashMap<String,Integer>();
	public static HashMap<String,Integer> seed_map = new HashMap<String,Integer>();
	
	
	public static final double threshold_PMI = 0.0;
	/**
	 * default constructor, does not do much here
	 */
	public PMI_new()
	{
		
	}
	
	/**
	 * this function computes the Hash map for vocabulary, this increases the efficiency of the program
	 */
	public static void computeVocabularyMapping()
	{
		for(int i=0 ; i<vocabulary.size() ; i++)
		{
			vocabulary_map.put(vocabulary.get(i), i);
		}
	}
	/**
	 * this computes the mapping for the seed words, increases the efficiency of the program 
	 */
	public static void computeSeedMapping()
	{
		int i;
		for( i=0 ; i<seed_words.size() ; i++)
		{
			seed_map.put(seed_words.get(i), i);
			
		}
	}
	
	/**
	 * 
	 * @return this returns the seed words from the seed_words file which is glossary.txt here
	 * @throws IOException
	 */
	public static void setSeedWords() throws IOException
	{
		//String seed_words_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
		BufferedReader br2 = new BufferedReader(new FileReader(seed_words_path));
		
		String temp2="";
		while((temp2 = br2.readLine()) != null)
		{
			seed_words.add(temp2);
		}		
		br2.close();
		
	}
	/**
	 * this sets the vocabulary for the current corpus by reading the results of the word embedding
	 */
	public static void setVocabulary() throws IOException
	{		
		BufferedReader br = new BufferedReader(new FileReader(word2vec_path));
		BufferedWriter bw = new BufferedWriter(new FileWriter(vocab_path));
		String temp="";
		String vocab_word = "";
		while( (temp=br.readLine()) != null)
		{
			StringTokenizer tkn = new StringTokenizer(temp," ");
			vocab_word = tkn.nextToken();
			bw.write(vocab_word);
			vocabulary.add(vocab_word);
			bw.newLine();			
		}		
		bw.close();;
		br.close();
	}
	/**
	 * this generates the fequency of each word from the vocabulary over the entire corpus
	 * @param vocabulary_size input to this function is the vocabulary size
	 * @return returns the array containing the word counts for each word
	 */
	public static int[] getVocabualryCounts(int vocabulary_size) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(corpusPathFile));
		String temp = "";

		StringTokenizer tkn;
		String word = "";
		int word_occur[] = new int[vocabulary_size];
		
		//find occurrence of each word in vocabulary
		while((temp = br.readLine()) != null)
		{
			tkn = new StringTokenizer(temp," ");
			while(tkn.hasMoreTokens())
			{
				word = tkn.nextToken();
				if(vocabulary_map.get(word) == null)
					continue;
				int tkn_index = vocabulary_map.get(word);
				word_occur[tkn_index]++;
			}
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(vocabulary_counts));
		for (int i=0 ; i<vocabulary_size ; i++)
		{
			bw.write(vocabulary.get(i)+" "+ word_occur[i]);
			bw.newLine();
		}
		
		bw.close();
		br.close();
		return word_occur;
	}
	/**
	 * this function computes the co_occurrence matrix of seed words and vocabulary words
	 * @param seed_word_size this is the number of seed words
	 * @param vocabulary_size this is the number of distinct words in the vocabulary
	 * @return returns the 2D array containing the co_occurrence matrix
	 */
	public static int[][] getCo_occurrenceMatrix(int seed_word_size,int vocabulary_size)throws IOException
	{
		BufferedReader br2 = new BufferedReader(new FileReader(corpusPathFile));
		
		String temp = "";
		StringTokenizer tkn1,tkn2;
		String word1,word2;
		boolean flag1 = false;
		int co_occur[][] = new int[seed_word_size][vocabulary_size];
		//System.out.println(seed_words);
		while((temp = br2.readLine()) != null)
		{
			//System.out.println(temp);
			for(int i=0 ; i<seed_word_size ; i++)
			{
				tkn1 = new StringTokenizer(temp," ");
				tkn2 = new StringTokenizer(temp," ");
				// check if temp line contains seed word if not skip the loop below this one
				while(tkn1.hasMoreElements())
				{
					word1 = tkn1.nextToken();
					if (seed_words.get(i).equalsIgnoreCase(word1))
					{
						flag1 = true;
						break;
					}
				}				
				while(flag1 && tkn2.hasMoreTokens())
				{
					word2 = tkn2.nextToken();
					if(vocabulary_map.get(word2) == null)
						continue;
					int j_index = vocabulary_map.get(word2);
					
					co_occur[i][j_index]++;   // this would increment i,j_index 2 times if the vocabulary word occurs twice in a document
				}
				flag1 = false;
			}//end of for loop
		}//end of while for document iterator	
		
		br2.close();
		
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(co_occurrence_path));
		for(int i=0 ; i<seed_word_size ; i++)
		{
			for (int j=0 ; j<vocabulary_size ; j++)
			{
				bw2.write(co_occur[i][j]+" ");//.get(i)+" "+ word_occur[i]);
			}
			bw2.newLine();
		}
		bw2.close();		
		
		return co_occur;
	}
	/**
	 * this function computes the PMI Matrix based on the formula:
	 * PMI(x,y) = P(x,y) / (P(x)*P(y))
	 * @param vocab_cnt
	 * @param co_occur
	 * @return
	 * @throws IOException
	 */
	public static double[][] getPMIs(int vocab_cnt[], int co_occur[][]) throws IOException
	{
		int seed_word_size = seed_words.size();
		int vocabulary_size = vocab_cnt.length;
		
		double[][] PMIs = new double[seed_word_size][vocabulary_size];
		String word = "";
		String seed = "";
		for(int i=0 ; i<seed_word_size ; i++)
		{
			double PMI=0;
			seed = seed_words.get(i);
			int seed_index_in_vocab = vocabulary_map.get(seed);
			//System.out.println(seed+"  "+seed_index_in_vocab);
			
			for(int j=0; j<vocabulary_size ;j++)
			{				
				if(vocab_cnt[seed_index_in_vocab] == 0 || vocab_cnt[j] == 0 || co_occur[i][j] == 0)
				{
					PMIs[i][j] = 0;
					continue;
				}
				PMI = (double)co_occur[i][j]/(vocab_cnt[seed_index_in_vocab] * vocab_cnt[j]);
				PMI = Math.log(PMI);
				PMIs[i][j] = PMI;
			}
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(pmi_matrix));
		for(int i=0 ; i<seed_word_size ; i++)
		{
			for(int j=0 ; j<vocabulary_size ; j++)
			{
				bw.write(PMIs[i][j]+" ");
			}
			bw.newLine();
		}
		bw.close();
		return PMIs;
	}

	/**
	 * computes the PMI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IOException
	{
		long time1 = System.currentTimeMillis();	
		setSeedWords(); // here seed words = 80 from glossary.txt
		setVocabulary();
		computeVocabularyMapping();
		computeSeedMapping();	
		
		int seed_word_size = seed_words.size();
		int vocabulary_size = vocabulary.size();
		int vocab_map = vocabulary_map.size();
		int seed_map2 = seed_map.size();
		
		System.out.println(vocabulary_size);
		System.out.println(seed_word_size);
		System.out.println(vocab_map);
		System.out.println(seed_map2);
		
		//compute vocabulary counts
		int word_occur[] = getVocabualryCounts(vocabulary_size);
		long time2 = System.currentTimeMillis();
		System.out.println(time2-time1);
		
		//find c0-occurrence of seed word and doc words ones in vocabulary	
		int[][] co_occur = getCo_occurrenceMatrix(seed_word_size,vocabulary_size);
		long time3 = System.currentTimeMillis();
		System.out.println(time3-time1);
		
		
		double PMIs[][] = new double[seed_word_size][vocabulary_size];
		PMIs = getPMIs(word_occur,co_occur);
		
		long time4 = System.currentTimeMillis();
		System.out.println(time4-time1);
		
		// this is a hack to take care of NaN which appears for 7 stop words with very high frequency the,to,of,and
		for(int i=0; i<seed_word_size ; i++)
		{
			for(int j=0 ; j<vocabulary_size ; j++)
			{
				if(Double.isNaN(PMIs[i][j]))
				{
					System.out.println("i:" +i+"  j:"+j);
					PMIs[i][j] = 0;
				}
			}
		}
		
		
		
		// start computing the document scores
		// read file paths below
		//String corpusPath = "/home/sahdev/Desktop/Fall2015/tester1";
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		int NUM_FILES = listOfFiles.length;
		//int NUM_FILES = 1000;
		System.out.println("Number of Files: "+NUM_FILES);
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			if(listOfFiles[i].isFile())
			{
				filePaths.add(corpusPath+"/"+listOfFiles[i].getName());
			}
		}	
		
		// store date and content below
		ArrayList<String> date = new ArrayList<String>();
		ArrayList<String> content = new ArrayList<String>();
		BufferedReader br;
		for(int i=0 ; i<NUM_FILES; i++)
		{
			br = new BufferedReader(new FileReader(filePaths.get(i)));
			date.add(br.readLine());
			content.add(br.readLine());		
			br.close();
		}
		
		
		// remove null files below and re-adjust NUM_FILES
		//String temp_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/temp_file.txt";
		BufferedWriter bw3 = new BufferedWriter(new FileWriter(temp_file));
		int nulls = 0;
		ArrayList<String> content2 = new ArrayList<String>();
		ArrayList<String> date2 = new ArrayList<String>();
		
		for(int i=0; i<NUM_FILES ; i++)
		{
			if(content.get(i) == null)
			{
				nulls++;
				continue;
			}
			content2.add(content.get(i));
			date2.add(date.get(i));
			bw3.write(date.get(i));
			bw3.newLine();
			bw3.write(content.get(i));
			bw3.newLine();
		}
		bw3.close();
		NUM_FILES = NUM_FILES - nulls;

		System.out.println("Non null contents: "+content2.size());
		System.out.println("Non null dates: "+date2.size());
		
		
		

		
		double score[] = new double[NUM_FILES];
		for(int i=0 ; i<NUM_FILES ;i++)
		{
			score[i] = 0.0d;
		}		
		long time5 = System.currentTimeMillis();
		System.out.println("Time taken: "+(time5-time1));
		System.out.println(content.size());
		
		
		StringTokenizer tkn;
		
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(score_file));
		for(int i=0 ; i<NUM_FILES ; i++)
		{		
			int temp_wrd_cnt=0;

			for(int j=0 ; j<seed_word_size ; j++)
			{
				tkn = new StringTokenizer(content2.get(i));	
				temp_wrd_cnt = tkn.countTokens();
				while(tkn.hasMoreTokens())
				{
					String doc_word = tkn.nextToken();
					
					if(vocabulary_map.get(doc_word) == null || seed_map.get(seed_words.get(j)) == null)
						continue;
					int word_index = vocabulary_map.get(doc_word);//vocabulary.indexOf(doc_word);
					int seed_index = seed_map.get(seed_words.get(j));//seed_words.indexOf(seed_words.get(j));
					if(word_index <0 || seed_index<0)
						continue;
				
					score[i] = score[i] + PMIs[j][word_index];
					
				}
				
			}
			//System.out.println(" "+score[i]);
			// here we divide by temp_wrd_cnt to get the average
			score[i] = score[i] / temp_wrd_cnt;
			bw2.write(score[i]+" "+date2.get(i));
			bw2.newLine();
		}
		
		bw2.close();
		
		long time6 = System.currentTimeMillis();
		System.out.println("Time taken: "+(time6-time1));



		
		
		
		
		
		
		
		
		/*
		for(int i=0 ; i<seed_word_size ; i++)
		{
			for(int j=0; j<vocabulary_size ; j++)
			{
				score[i][j] = computePMI(seed_words.get(i),vocabulary.get(j));
				System.out.println(j);
			}
			System.out.println(i);
		}*/
		/*BufferedReader br3 = new BufferedReader(new FileReader(corpusPath));
		String temp2="",word3="";
		StringTokenizer tkn3;
		while( (temp2=br3.readLine()) !=null)
		{
			tkn3 = new StringTokenizer(temp2);
			while(tkn3.hasMoreTokens())
			{
				word3 = tkn3.nextToken();
				if(vocabulary_map.get(word3) == null)
					continue;
				int j_index = vocabulary_map.get(word3);
			}
		}
		
		br3.close();
		
		long time3 = System.currentTimeMillis();
		System.out.println(time3-time1);*/
	}
	
	
	/**
	 * the following is a useless function never use it...lol
	 * @param seed_word
	 * @param vocab_word
	 * @return
	 * @throws IOException
	 */
	public static double computePMI(String seed_word, String vocab_word) throws IOException
	{		
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
		br.close();	
		if (px ==0 || py == 0)
		{
			return 0;
		}
		double pmi_score = Math.log(pxy/(px*py));
		
		return pmi_score;
	}
}
