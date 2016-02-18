package dataMining;

import java.io.*;
import java.util.*;
public class GenerateScores_PMI {


/**
 * this generates the scores for dates by reading all the document scores on each date
 * @author raghavender sahdev
 *
 */

	
	/**
	 * this converts the date to numerical formal
	 * @param date
	 * @return
	 */
	public int getDate(String month)
	{
		int a[] = {1,2,3};
		//17 Feb 2011 00:00:00
		
		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("Jan", 1);
		map.put("Feb", 2);
		map.put("Mar", 3);
		map.put("Apr", 4);
		map.put("May", 5);
		map.put("Jun", 6);
		map.put("Jul", 7);
		map.put("Aug", 8);
		map.put("Sep", 9);
		map.put("Oct", 10);
		map.put("Nov", 11);
		map.put("Dec", 12);
		
		int mon = (int) map.get(month);
		return mon;
	}
	
	public static String violence_trend_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/violence_trend_scores.txt";
	public static String scores_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/scores_312k.txt";
	/**
	 * this reads the violence scores of each document and plots the trend of violence for each day of the month
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
		//String scores_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/scores.txt";
		BufferedReader br = new BufferedReader(new FileReader(scores_path));
		
		String temp="";
		
		double score;
		int day, yr;
		String month;
		ArrayList<Dates> dates = new ArrayList<Dates>();
		
		while((temp=br.readLine()) != null)
		{
			StringTokenizer tkn = new StringTokenizer(temp," ");
			
			score = Double.parseDouble(tkn.nextToken());
			day = Integer.parseInt(tkn.nextToken());
			month = tkn.nextToken();
			yr = Integer.parseInt(tkn.nextToken());
			tkn.nextToken();
			
			dates.add(new Dates(day,month,yr,score));
			
		}		
		int days = 31;
		double scores[] = new double[days];
		int scores_cnt[] = new int[days];
		for(int i =0 ; i<scores.length ; i++)
		{
			scores[i] = 0.0d;
			scores_cnt[i] = 0;
		}
		
		for(int i=0 ; i<days ; i++)
		{
			for(int j=0 ; j<dates.size() ; j++)
			{
				if(dates.get(j).day == (i+1) && dates.get(j).month.equalsIgnoreCase("Jul") && dates.get(j).year == 2013)
				{
					scores[i] = scores[i] + dates.get(j).score;
					scores_cnt[i]++;
				}
			}
			// now we divide the scores sum by the number of documents on that specific day!
			scores[i] = scores[i] / scores_cnt[i];
		}
		br.close();
		int sum = 0;
		for(int i=0 ; i<scores_cnt.length ; i++)
		{
			System.out.println("day "+(i+1)+"  "+scores_cnt[i]);
		}
		System.out.println(sum);;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(violence_trend_path));
		for(int i=0 ; i<scores.length ; i++)
		{
			bw.write("day   "+(i+1)+"   " +scores[i]+"  \t "+ scores_cnt[i]);
			bw.newLine();
		}
		bw.close();
	}
}

