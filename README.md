# FactorAnalysis
This code contains the algorithm for Factor Analysis over a period of time for the Violence Factor.
We use 2 different methods to compute the violence scores and analyse its trend over a period of one month. We then compare it with the ground truth to see which of the 2 methods is more correlated witht he ground truth.

The code used in the paper was much different than the one here. This code is an initial version of the research being done.
download the project paper below

http://ieeexplore.ieee.org/abstract/document/7817051/ 

if you use the code please cite the following paper in your research:

@inproceedings{agrawal2016detecting, <br/>
  title={Detecting the Magnitude of Events from News Articles}, <br/>
  author={Agrawal, Ameeta and Sahdev, Raghavender and Davoudi, Heidar and Khonsari, Forouq and An, Aijun and McGrath, Susan}, <br/>
  booktitle={Web Intelligence (WI), 2016 IEEE/WIC/ACM International Conference on}, <br/>
  pages={177--184},<br/>
  year={2016},<br/>
  organization={IEEE}<br/>
}<br/>
 
 
Agrawal, A., Sahdev, R., Davoudi, H., Khonsari, F., An, A. and McGrath, S., 2016, October. Detecting the Magnitude of Events from News Articles. In Web Intelligence (WI), 2016 IEEE/WIC/ACM International Conference on (pp. 177-184). IEEE. <br/>

Download preliminary project report: <br/>
http://www.raghavendersahdev.com/uploads/3/9/6/2/39623741/factor_analysis_sahdev.pdf <br/>



## Instructions to use the code:

In this project we have written the following files: <br/>
* PreProcess.java – this file pre-processes all the files in a particular folder. This parses the xml data and extracts only the text content of each article and the publication date and time. We remove the punctuations, stop words and convert the corpus to lower case. We then save the pre-processed files in a new folder. We also give the user an option to remove the files whose content is not in English. However we do not remove the foreign languages files in our project.
* PreProcess2.java – this file pre-processes reads all the pre-processed files generated by PreProcess.java and stores them in a single text file. This is required as word2vec program expects a single file to learn the model for the vector representations of the word.
* RemoveInvalid.java – this file removes the invalid xml files which has illegal characters like &#. This program is required to pre-process huge corpuses because sometimes the xml files have illegal character sequences in them, so this program takes care of that.
* Word2VecRawTextExaample – this file has been modified from the code by [1],[10]. This file is responsible for computing the word vector representations from the single text file generated by PreProcess3.java. This files stores the word vectors in a text file word2vec.txt so that it can later be used by other programs. We also generate the vocabulary from this file. 
* Similarities.java – this file reads the word vector representations and computes the cosine similarity of each of the seed words with each of the words in the vocabulary to generate a matrix of size seed_words_size x vocabulary_size. In this file after generating the matrix we compute the violence score for each document based on the method explained in the report.
* GenerateScores.java – this files reads the document scores generated by the Similarities.java file and computes the violence score for each day. It stores the violence scores for each day in a text file from which we plot the graph of violence scores vs. days.
* PMI_new.java – this file computes the Point wise Mutual Information between each of the seed words and the vocabulary words generated by the word2vec code. It then computes the violence scores for each document, these scores are then used by GenerateScores.java file to compute the violence score for each day.


## Descriptions of your System Design 

In this part we explain how to run our program. So one should follow the following steps to execute our code:

* Step 1: Run the PreProcess.java file to pre-process the files. Input to this program is the path to the corpus which contains all the files. Output path for the pre-processed files should be specified by the user.
* Step 2: Run the PreProcess2.java to concatenate all the files generated in step 1 in a single text file.
* Step 3: Run the Word2VecRawTextExample.java to compute the word vector representations.
* Step 4: Run the Similarities.java to compute the scores for documents based on word embedding OR run the PMI_new.java to compute the score based on PMI
* Step 5: Run the GenerateScores.java to compute the violence scores for each day.

## Sample Input and Output
A sample input to our program is the path to the corpus. Sample outputs are: <br/> 
* Vocabulary of words
* Violence scores for each day 
* Plot of violence scores vs. days (shown in the plots earlier in the report)
Limitations of the software <br/>
The amount of RAM usage for 300,000 files is about 12GB while running the Word2VecRawTextExample.java, so this might be a problem for computers having lesser RAM.

