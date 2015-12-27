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
 * @author raghavender sahdev modified code from net
 *
 */
public class Word2VecRawTextExample 
{

    private static Logger log = LoggerFactory.getLogger(Word2VecRawTextExample.class);

    public static String filePath = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/output_content_312k.txt";
    public static String word2vec_path = "/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/word2vec_312k.txt";
    public static void main(String[] args) throws Exception 
    {

    	long time1 = System.currentTimeMillis();
    	//String filePath = new ClassPathResource("/home/sahdev/Desktop/Fall2015/Data Mining/PROJECT/en/sample000001.txt").getFile().getAbsolutePath();
    	
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
                .minWordFrequency(10).iterations(1)
                .layerSize(100).lookupTable(table)
                .stopWords(new ArrayList<String>())
                .vocabCache(cache).seed(42)
                .windowSize(5).iterate(iter).tokenizerFactory(t).build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");
        // Write word
        WordVectorSerializer.writeWordVectors(vec, word2vec_path);

        long time2 = System.currentTimeMillis();
        System.out.println(time2-time1);
        
        log.info("Closest Words:");
        Collection<String> lst = vec.wordsNearest("violence", 100);
        System.out.println(lst);
        
        String meta_data_path = "";
        //BufferedWriter bw = new BufferedWriter(new FileWriter(meta_data_path));
        
        
       // bw.close();
        
        
        long time3 = System.currentTimeMillis();
        System.out.println(time3-time1);
    }    
    
    
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

/*
min frequency = 10
[sectarian, antigovernment, crimes, killings, brotherhood, incidents, stoning, allegations, muslim, 
religious, gang, popocatepetl, spews, rape, sunni, atrocities, abuses, hamas, adultery, murder, protests, 
militant, morsi, kidnapping, mubarak, torture, mexicos, genocide, kurds, raped, islamist, mursi, tortured, 
haram, rohingya, sinai, inciting, kill, attacks, prostitution, amnesty, massacre, accused, rwandan, boko, 
crackdown, protesting, ousted, imprisonment, bloodshed, deaths, extremists, uprising, sparked, irregularities, 
murders, sexual, cairo, innocent, hutu, hosni, egypts, overthrow, killing, shia, unrest, insurgency, supporters, 
toppled, sexually, offence, militias, alqaida, sex, refuse, trafficking, violent, parade, discrimination, 
volcano, egypt, bombings, homicide, systematic, minorities, assault, rallies, conspiracy, assassination, 
ouster, outrage, rwanda, victim, raid, promorsi, vigils, smuggling, deposed, aggravated, civilians]

*/
/*
 * victims, murder, unrest, inciting, mursi, perspective, crackdown, usled, destruction, criminal, pleaded, 
 * rape, tears, flowed, escalation, saddam, violent, guilty, hussein, collapse, wave, killings, weapon, double,
 *  humanity, killed, coup, morsi, crimes, arguments, paulo, arrest, activity, charges, deposed, terror, 
 *  gunmen, ousted, committing, brotherhood, trafficking, sunni, terrorist, aggression, activists, kurds,
 *  offences, detained, highs, egypts, gang, forgotten, syria, accusations, ambush, possession, harassment, 
 *  conflict, deadly, sexual, mohamed, violation, charged, conviction, prosecutor, facing, muslim, uprising,
 *  accused, trial, gassed, violations, protests, arrests, seized, downfall, inquiry, riots, attacked, cases,
 *  genocide, surrounding, parts, arrested, custody, rebels, iraqs, tributes, signs, raped, invasion, suspects,
 *  denies, reading, demonstrations, conspiracy, prohibited, communal, lebanese, mubarak 
 */
/*
 *meta, hazaras, valentines, moya, wordpressorg, defamatory, killings, shias, newletters, starved, 
 *bodily, violate, shia, brotherhood, inciting, baloch, pornographic, obscene, arson, non, harassment, 
 *clashes, sectarian, genocide, erupted, antigovernment, protesters, terrorism, resulted, rss, harm, 
 *abuses, torture, islamist, bloodshed, disappearances, extrajudicial, brutality, brutally, hazara, 
 *solidarity, trafficking, confrontations, rallies, mobs, crackdowns, exploitation, rohingya, complicity, 
 *looting, grievous, premeditated, civilians, harvesting, rape, morsis, insurgents, marches, testimonies, 
 *violent, sinai, bystanders, sikh, protest, intimidation, ouster, gangrape, uncategorized, shinwatras, 
 *theory, inhuman, escalated, antimuslim, pakistan’s, quetta, syringe, sexual, abductions, civilian, extremist, 
 *rakhine, assault, extrajudicially, militant, fight, discrimination, pinterest, mursi, egyptian, hamas, 
 *insurgent, lashkarejhangvi, morsi, acts, troops, protestors, appoints, oppression, minorities, commit 
 */
/*valentines newletters diarrhoea
 * [valentines, sectarian, shia, genocide, dengue, killings, non, hazara, anti-government, quetta, newletters, 
 * abuses, humanity, protests, sexual, hazaras, clashes, arakan, intimidation, diarrhoea, harassment, anti-muslim,
 *  attacks, rohingya, unrest, moya, crimes, muslims, uncategorized, exploitation, inciting, militant, violent, 
 *  torture, brotherhood, protest, erupted, crackdown, malaria, theory, rakhine, rallies, atrocities, systematic,
 *   pro-democracy, perpetrated, demonstrators, hamas, uprising, discrimination, terrorism, sit-in, insurgents, 
 *   ‘instigators, terror, buddhists, extremists, drone, brutality, sunni, campaign, protesting, woza, persecution, 
 *   genocidal, trafficking, committing, civilians, protesters, brutal, deadly, militants, morsis, incidents, 
 *   violations, kidnapping, aggression, acts, rap, commit, deaths, bombings, cleansing, hosni, repression, 
 *   hezbollah, destruction, overthrow, spate, riots, wave, perpetrators, rebels, insurgency, pro-morsi, rebellion,
 *    albanians, sparked, against, ethnic]

 */
