package dataMining;
import java.io.*;
import java.util.*;
/**
 * this file computes the similarity between the seed words and the document words
 * @author RaghavenderSahdev
 *
 */
public class Similarities 
{
	public static void main(String args[]) throws IOException
	{
		String word2vec_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/word2vec.txt";
		BufferedReader br = new BufferedReader(new FileReader(word2vec_path));
		
		String temp = "";
		ArrayList<String> word = new ArrayList<String>();
		ArrayList<ArrayList<Double>> vec = new ArrayList<ArrayList<Double>>();
		
		int col_cnt = 101;
		int row_cnt = 28916;
		ArrayList<Double> temp_vec = new ArrayList<Double>();
		for(int i=0; i<col_cnt; i++)
		{
			for(int j=0 ; j<row_cnt ; j++)
			{
				
			}
			if(i%col_cnt == 0)
			{
				word.add(temp);
			}
			else
			{
				temp_vec.add((i%col_cnt),Double.parseDouble(temp));
			}
		}		
		br.close();
	}

}
