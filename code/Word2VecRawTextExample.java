package dataMining;


import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.UimaSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * computes the word embedding for each candidate word in the corpus
 * @author raghavender sahdev modified code from internet
 *
 */
public class Word2VecRawTextExample 
{

    private static Logger log = LoggerFactory.getLogger(Word2VecRawTextExample.class);

    public static void main(String[] args) throws Exception 
    {

    	long time1 = System.currentTimeMillis();
    	//String filePath = new ClassPathResource("/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/sample000001.txt").getFile().getAbsolutePath();
    	String filePath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/output_file.txt";
        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = UimaSentenceIterator.createWithPath(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        InMemoryLookupCache cache = new InMemoryLookupCache();
        WeightLookupTable table = new InMemoryLookupTable.Builder()
                .vectorLength(100)
                .useAdaGrad(false)
                .cache(cache)
                .lr(0.025f).build();

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5).iterations(1)
                .layerSize(100).lookupTable(table)
                .stopWords(new ArrayList<String>())
                .vocabCache(cache).seed(42)
                .windowSize(5).iterate(iter).tokenizerFactory(t).build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");
        // Write word
        WordVectorSerializer.writeWordVectors(vec, "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/word2vec.txt");

        long time2 = System.currentTimeMillis();
        System.out.println(time2-time1);
        
        log.info("Closest Words:");
        Collection<String> lst = vec.wordsNearest("violence", 100);
        System.out.println(lst);
        
        String meta_data_path = "";
        BufferedWriter br = new BufferedWriter(new FileWriter(meta_data_path));
        
        
        br.close();
        
        
        long time3 = System.currentTimeMillis();
        System.out.println(time3-time1);
    }    
    
    /* following are the top 100 similar words to violence lind, guilty, kidnapping, meade, pleaded, 
    manning, rohingya, denise, suu, murder, convicted, conspiring, raped, systematic, remanded, aiding, 
    genocide, rape, charges, accused, allegations, muslim, crimes, robbery, plead, bail, parole, bodily, 
    perpetrators, colonel, charged, antigovernment, boko, commit, kuala, innocent, assault, courtmartial, 
    alqaeda, offenses, kyi, killings, assassination, sectarian, adultery, probe, intent, srebrenica, 
    conspiracy, massacre, premeditated, attacks, mubarak, haram, defendant, mexico, murdering, abetting, 
    militant, counts, sexual, unrest, verdict, torture, allegedly, brar, laden, pinterest, prosecutors, 
    assaulted, stoning, alqaida, gang, lumpur, false, kyis, osama, protesting, kurds, verdicts, traitor, 
    prostitution, manslaughter, victims, raping, espionage, tortured, killing, offences, abused, acquitted, 
    theft, murdered, antisecrecy, ladens, motivated, offence, illegal, precedent, wikileaks
    */
}
