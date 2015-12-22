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
	public ArrayList<String> vocabulary = new ArrayList<String>();
	public ArrayList<String> seed_words = new ArrayList<String>();
	
	public Similarities()
	{
		
	}
	
	/**
	 * 
	 * @return this returns the seed words from the seed_words file which is glossary.txt here
	 * @throws IOException
	 */
	public ArrayList<String> getSeedWords() throws IOException
	{
		String seed_words_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/glossary.txt";
		BufferedReader br2 = new BufferedReader(new FileReader(seed_words_path));
		
		String temp2="";
		while((temp2 = br2.readLine()) != null)
		{
			seed_words.add(temp2);
		}		
		br2.close();
		
		return seed_words;
	}

	/**
	 * this generates the vectorized form of the words in the vocabulary
	 * @param row_cnt represents the number of words in the vocabulary
	 * @param col_cnt represents the dimensionality of each word in the vocabulary
	 * @return returns the 2D array representing the word encodings of the words
	 * @throws IOException
	 */
	public double[][] getVectors(int row_cnt,int col_cnt) throws IOException
	{
		ArrayList<String> words = getDocumentWords(row_cnt);	
		double vectors[][] = new double[row_cnt][col_cnt];
		// here words.size = row_cnt
		for(int i=0; i<words.size() ; i++)
		{
			
			StringTokenizer str = new StringTokenizer(words.get(i)," ");
			this.vocabulary.add(str.nextToken());
			
			int j=0;
			while(str.hasMoreTokens())
			{
				vectors[i][j] = Double.parseDouble(str.nextToken());
				j++;
			}
		}
		
		String vocabularyPath="/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/vocabulary.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(vocabularyPath));
		for(int i=0 ; i<vocabulary.size() ; i++)
		{
			bw.write(vocabulary.get(i));
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
	public ArrayList<String> getDocumentWords(int row_cnt) throws IOException
	{
		String word2vec_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/word2vec.txt";
		BufferedReader br = new BufferedReader(new FileReader(word2vec_path));
		
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

	public double[][] getSimilarities(int seed_words_size, int vocabulary_size, double vectors[][],Similarities tester) throws IOException
	{
		String similarities_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/similarity_array2D.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(similarities_file));		
		double similarities[][] = new double[seed_words_size][vocabulary_size];		
		for(int i=0 ; i<seed_words_size ; i++)
		{
			for(int j=0 ; j<vocabulary_size ; j++)
			{
				// find the index of the seed word
				String str = tester.seed_words.get(i);
				int index_seed = tester.vocabulary.indexOf(str);
				
				
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
		
		Similarities tester = new Similarities();		
		int col_cnt = 100;   // currently hard coded
		int row_cnt = 28916;  // currently hard coded
		
		double vectors[][] = new double[row_cnt][col_cnt];
		vectors = tester.getVectors(row_cnt, col_cnt);	
		tester.seed_words = tester.getSeedWords();
		/* till here we have 
		 * vectors[][] - the vector representations of words stored in the 2d array
		 * seed_words - ArrayList<String> has all the seed words read from the glossary
		 * vocabulary - ArrayList<String> has the vocabulary generated from the word2vec file path
		 *  
		 *  now we need to compute the similarities between seed words and document words
		 */				
		// Start computing the seed words similarity
		
		int vocabulary_size = tester.vocabulary.size();
		int seed_words_size = tester.seed_words.size();
		
		
		double similarities[][] = new double[seed_words_size][vocabulary_size];
		// $$$$$$$$ uncomment the following function call FOR FINAL TESTING
		similarities = tester.getSimilarities(seed_words_size, vocabulary_size, vectors, tester);
		
		String similarities_file = "";
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
		
		String corpusPath = "/home/sahdev/Desktop/Fall2015/samples";
		File folder = new File(corpusPath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> filePaths = new ArrayList<String>();
		//int NUM_FILES = listOfFiles.length;
		int NUM_FILES = 10000;
		
		for(int i=0 ; i<NUM_FILES ; i++)
		{
			if(listOfFiles[i].isFile())
			{
				filePaths.add(corpusPath+"/"+listOfFiles[i].getName());
			}
		}	
		
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

		StringTokenizer tkn;
		double score[] = new double[NUM_FILES];
		for(int i=0 ; i<NUM_FILES ;i++)
		{
			score[i] = 0.0;
		}
		
		
		for(int i=0 ; i<1 ; i++)
		{		
			int temp_wrd_cnt=0;
			for(int j=0 ; j<seed_words_size ; j++)
			{
				tkn = new StringTokenizer(content.get(i));				
				while(tkn.hasMoreTokens())
				{
					String doc_word = tkn.nextToken();
					
					int word_index = tester.vocabulary.indexOf(doc_word);
					int seed_index = tester.seed_words.indexOf(tester.seed_words.get(j));
					if(word_index <0 || seed_index<0)
						continue;
					
					score[i] = score[i] + similarities[seed_index][word_index];
				}
				temp_wrd_cnt = tkn.countTokens();
			}
			score[i] = score[i] / temp_wrd_cnt;
		}
		
		String score_file = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/scores.txt";
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(score_file));
		for(int i=0 ; i<score.length ; i++)
			bw2.write(score[i]+" ");
		bw2.close();
		long time2 = System.currentTimeMillis();
		System.out.println(time2-time1);
		
		
		/*
		String temp_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/word2vec_test.txt";
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(temp_path));
		for(int i=0 ; i<row_cnt ; i++)
		{
			for(int j=0 ; j<col_cnt ; j++)
			{
				bw2.write(""+vectors[i][j]);
			}
			bw2.newLine();
		}
		bw2.close();
		*/
	}
	
}
