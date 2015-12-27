package dataMining;
import java.io.*;
import java.util.*;
/**
 * this file computes the similarity between the seed words and the document words
 * @author raghavender sahdev
 *
 */
public class Similarities 
{
	public static ArrayList<String> vocabulary = new ArrayList<String>();
	public static ArrayList<String> seed_words = new ArrayList<String>();
	public static HashMap<String,Integer> vocabulary_map = new HashMap<String,Integer>();
	public static HashMap<String,Integer> seed_map = new HashMap<String,Integer>();
	
	public static String corpusPath = "/home/sahdev/Desktop/Fall2015/july";
	public static String word2vec_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/word2vec_35k.txt";
	public static String score_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/scores_35k.txt";
	
	public static String vocab_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/vocabulary35k.txt";
	public static String similarity_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/similarity_array2D.txt";
	public static String vectors_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/vector_array2D.txt";	
	public static String similarities_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/cosineSimilarities.txt";
	
	public static String temp_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/temp_file.txt";
	public static String seed_words_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
	public static int dimensionality = 100; // this is the dimensionality of the word vector
	
	public Similarities()
	{
		
	}
	public static void computeVocabularyMapping()
	{
		for(int i=0 ; i<vocabulary.size() ; i++)
		{
			vocabulary_map.put(vocabulary.get(i), i);
		}
	}
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
	 * this generates the vectorized form of the words in the vocabulary
	 * @param row_cnt represents the number of words in the vocabulary
	 * @param col_cnt represents the dimensionality of each word in the vocabulary
	 * @return returns the 2D array representing the word encodings of the words
	 * @throws IOException
	 */
	public static double[][] getVectors(int row_cnt,int col_cnt) throws IOException
	{
		ArrayList<String> words = getDocumentWords(row_cnt);	
		double vectors[][] = new double[row_cnt][col_cnt];
		// here words.size = row_cnt
		for(int i=0; i<words.size() ; i++)
		{
			
			StringTokenizer str = new StringTokenizer(words.get(i)," ");
			String t = str.nextToken(); // skip the word
			int j=0;
			while(str.hasMoreTokens())
			{
				vectors[i][j] = Double.parseDouble(str.nextToken());
				j++;
			}
		}		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(vectors_path));
		for(int i=0 ; i<row_cnt ; i++)
		{
			for(int j=0 ; j<col_cnt ; j++)
			{
				bw.write(vectors[i][j]+" ");
			}
			bw.newLine();
		}
		bw.close();		
		return vectors;
	}
	/**
	 * this function gets the document words from the word2vec file which was generated in the word embedding stage
	 * @param row_cnt represents the number of words in the dictionary
	 * @return returns the words in the vocabulary with their vectorization
	 * @throws IOException
	 */
	public static ArrayList<String> getDocumentWords(int row_cnt) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(word2vec_path));	
		HashMap<String,String> words_vectors = new HashMap<String,String>(); // maybe code this later
		
		ArrayList<String> words = new ArrayList<String>();				
		
		String temp = "";
		for(int i=0; i<row_cnt; i++)
		{
			temp = br.readLine();
			words.add(temp);					
		}		
		br.close();
		return words;
	}	
	
	public double computeCosineSimilarity(double a1[],double a2[])
	{
		double sum1=0.0;
		double sqr1=0.0;
		double sqr2=0.0;
		for(int i=0 ; i<a1.length ; i++)
		{
			sum1 = sum1 + a1[i]*a2[i];
			sqr1 = sqr1 + a1[i]*a1[i];
			sqr2 = sqr2 + a2[i]*a2[i];
		}
		double numerator = Math.sqrt(sqr1*sqr2);
		double sim = sum1 / numerator;
		
		return sim;
	}

	public static double[][] getSimilarities(int seed_words_size, int vocabulary_size, double vectors[][],Similarities tester,boolean flag) throws IOException
	{
		
		if(flag == true)
		{
			BufferedReader br = new BufferedReader(new FileReader(similarity_path));
			// to write code here
			br.close();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(similarity_path));		
		double similarities[][] = new double[seed_words_size][vocabulary_size];	
		for(int i=0 ; i<seed_words_size ; i++)
		{
			for(int j=0 ; j<vocabulary_size ; j++)
				similarities[i][j] = 0.0d;
		}
		for(int i=0 ; i<seed_words_size ; i++)
		{
			for(int j=0 ; j<vocabulary_size ; j++)
			{
				// find the index of the seed word
				String str = seed_words.get(i);
				if(vocabulary_map.get(str) == null)
				{
					//System.out.println(str + " "+i);
					continue;
				}	
				int index_seed = vocabulary_map.get(str);		
				
				double s1[] = vectors[index_seed];
				double v1[] = vectors[j];
				
				similarities[i][j] = tester.computeCosineSimilarity(s1,v1);
				bw.write(similarities[i][j]+" ");
			}
			//System.out.println(i + " " + tester.seed_words.get(i));
			bw.newLine();
		}
		
		bw.close();
		return similarities;
	}
	
	/**
	 * 
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
		
		Similarities tester = new Similarities();	
		
		int vocabulary_size = vocabulary.size();
		int seed_words_size = seed_words.size();
		int vocab_map = vocabulary_map.size();
		int seed_map2 = seed_map.size();
		
		System.out.println(vocabulary_size);
		System.out.println(seed_words_size);
		System.out.println(vocab_map);
		System.out.println(seed_map2);
		
		int col_cnt = dimensionality;   // currently hard coded
		int row_cnt = vocabulary_size;  // currently hard coded not now its 17454 here
		
		
		
		double vectors[][] = new double[row_cnt][col_cnt];		
		vectors = getVectors(row_cnt, col_cnt);	
		
		/* till here we have 
		 * vectors[][] - the vector representations of words stored in the 2d array
		 * seed_words - ArrayList<String> has all the seed words read from the glossary
		 * vocabulary - ArrayList<String> has the vocabulary generated from the word2vec file path
		 * vocabulary_map
		 * seed_map
		 * now we need to compute the similarities between seed words and document words
		 */				
		// Start computing the seed words similarity
		
		
		
		double similarities[][] = new double[seed_words_size][vocabulary_size];
		// $$$$$$$$ uncomment the following function call FOR FINAL TESTING
		similarities = getSimilarities(seed_words_size, vocabulary_size, vectors, tester, false);
		
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(similarities_file));
		for(int i=0 ; i<seed_words_size ; i++)
		{
			for(int j=0 ; j<vocabulary_size ; j++)
			{
				bw.write(similarities[i][j]+" ");
			}
			bw.newLine();
		}
		
		bw.close();
		
		
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
		
		
		

		StringTokenizer tkn;
		double score[] = new double[NUM_FILES];
		for(int i=0 ; i<NUM_FILES ;i++)
		{
			score[i] = 0.0d;
		}		
		long time2 = System.currentTimeMillis();
		System.out.println("Time taken: "+(time2-time1));
		System.out.println(content.size());
		
		
		
		
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(score_file));
		for(int i=0 ; i<NUM_FILES ; i++)
		{		
			int temp_wrd_cnt=0;

			for(int j=0 ; j<seed_words_size ; j++)
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
					
					score[i] = score[i] + similarities[seed_index][word_index];
				}
			}
			//System.out.println(" "+score[i]);
			// here we divide by temp_wrd_cnt to get the average
			score[i] = score[i] / temp_wrd_cnt;
			bw2.write(score[i]+" "+date2.get(i));
			bw2.newLine();
		}
		
		bw2.close();
		
		long time3 = System.currentTimeMillis();
		System.out.println("Time taken: "+(time3-time1));
		
	}	
}
