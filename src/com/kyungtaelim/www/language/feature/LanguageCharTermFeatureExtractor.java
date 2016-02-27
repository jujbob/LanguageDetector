package com.kyungtaelim.www.language.feature;

import com.kyungtaelim.www.language.Configurations;
import java.util.*;

/**
 * Created by user1 on 2016-01-09
 *
 * Feature extraction from Wikipedia plain texts for each language,
 * The result about features can be some characters(a,ù,é,가,施) and some chunks(and, der, in, Obama, revenge,. etc) with term frequency,
 * It is extracted by some procedures about Inverted Language Frequency and Ranking automatically..
 * @author kyungtaelim@kisti.re.kr
 */
public class LanguageCharTermFeatureExtractor {

    /**
     * Top n char extract from each language
     *
     * @param text the text, in this case from Wikipedia plain text
     * @param language the language, string type language ex) Korean.
     * @return the ArrayList for Chars which has Chars frequency in the text and language information.
     */
    public ArrayList<TermVo> topNCharExtract(String text, String language){

        //Extract words and frequencies from text by white space
        HashMap<String, Integer> termFreqHash = new HashMap<>(); // hash for term and frequency
        StringTokenizer tokens	= new StringTokenizer(text, " ,\n");
        String singToken = "";
            while (tokens.hasMoreElements()){
                singToken = StopWordCharUtill.eliminateStopChar(tokens.nextToken()).trim();
                for(int l = 0; l < singToken.length(); l++){
                    termFreqHash = addToHash(termFreqHash, ""+singToken.charAt(l)); //add to hash with term frequency
                }
            }

        //Sort for ranking
        Map<ArrayList,Integer> sortedTerms = this.sortingHash(termFreqHash);
        //Extract Top N with percent
        int termListSize = sortedTerms.size(); double topN =  (termListSize* Configurations.percentOfTopNFeature); //The character feature candidate should be delete almost about the lowest 20% of character because trashes can be powerful feature
        float test = Math.round(topN); String tmpp = (""+test).replaceAll(".0", "");  int topNPercent = Integer.parseInt(tmpp);
        ArrayList<TermVo> topXTerms = this.extractTopTerms(sortedTerms, topNPercent, language);
        return topXTerms;

    }

    /**
     * Top n word extract.
     *
     * @param text the text, in this case from Wikipedia plain text
     * @param language the language, string type language ex) Korean.
     * @return the ArrayList for terms which has term frequency and language information.
     */
    public ArrayList<TermVo> topNWordExtract(String text, String language){

        //Extract words and frequencies from text by white space
        HashMap<String, Integer> termFreqHash = new HashMap<>(); // hash for term and frequency
        StringTokenizer tokens	= new StringTokenizer(text, " ,\n");
        String singToken = "";
        while (tokens.hasMoreElements()){
            singToken = tokens.nextToken().toLowerCase().replace(",", "").trim();
            if(singToken.length() >= 2){
                termFreqHash = addToHash(termFreqHash, singToken); //add to hash with term frequency
            }
        }

        //Sort for ranking
        Map<ArrayList,Integer> sortedTerms = this.sortingHash(termFreqHash);
        //Extract Top N
        ArrayList<TermVo> topXTerms = this.extractTopTerms(sortedTerms, Configurations.topNTermFeature, language);
        //Set the Language
        for(TermVo term : topXTerms){ term.setLanguage(language); }

        return topXTerms;

    }

    /**
     * Add to hash.
     *
     * @param hashTermNum the hash term nnum
     * @param arrayToken the array token
     * @return the HashMap
     */
    public HashMap<String, Integer> addToHash(HashMap<String, Integer> hashTermNum, String arrayToken) {

        if(hashTermNum.containsKey(arrayToken)){
            int newNum = hashTermNum.get(arrayToken) + 1 ; //increase term frequency
            hashTermNum.put(arrayToken, newNum);
        }else{
            hashTermNum.put(arrayToken, 1);
        }
        return hashTermNum;
    }

    /**
     * Sorting hash by term frequency
     *
     * @param termNum the term num
     * @return the map
     */
    public Map<ArrayList,Integer> sortingHash(HashMap<String, Integer> termNum) {

        Map<String,Integer> mp= termNum;
        List<Integer> list= new ArrayList<>();
        for(Map.Entry<String, Integer> m:mp.entrySet()){
            list.add(m.getValue());
        }
        Collections.sort(list);
        Map<ArrayList,Integer> sortedmp= new HashMap<>();
        for(Map.Entry<String, Integer> m:mp.entrySet()){
            int tempage=m.getValue();
            List<String> tempname= new ArrayList<>();
            for(Map.Entry<String, Integer> m1:mp.entrySet()){
                if(m1.getValue()==tempage)
                    tempname.add(m1.getKey());
            }
            if(!sortedmp.containsKey(tempage)){
                Collections.sort(tempname);
                sortedmp.put((ArrayList) tempname,tempage);
            }
        }

        return sortedmp;
    }

    /**
     * Extracting Top N terms from HashMap
     *
     * @param topNum standard number of Top N
     * @return the Map
     */
    public ArrayList<TermVo> extractTopTerms(Map<ArrayList, Integer> sortedTerms, int topNum, String language) {

        ArrayList<TermVo> result = new ArrayList<>();
        int numberOfArr = 0;
        Integer[] freqArray = new Integer[sortedTerms.size()];
        for(Map.Entry<ArrayList,Integer> m:sortedTerms.entrySet()){
//		        System.out.println("key--->"+m.getKey()+" value--->"+m.getValue());
            freqArray[numberOfArr] = m.getValue();
            numberOfArr++;
        }

        int tmp = 0;
        for(int i = 0; i < freqArray.length ; i++){
            for(int j=i+1; j < freqArray.length ; j++){
                if(freqArray[i] < freqArray[j]){
                    tmp = freqArray[i];
                    freqArray[i] = freqArray[j];
                    freqArray[j] = tmp;
                }else{
                }
            }
        }

        for(int k=0 ; k < freqArray.length; k ++){
            if(result.size() < topNum){
                for(Map.Entry<ArrayList,Integer> m:sortedTerms.entrySet()){
                    if(freqArray[k] == m.getValue() || freqArray[k].equals(m.getValue()) ){ //I don't know sometimes freqArray does not act so I added
                        for(Object s : m.getKey()){
                            result.add(new TermVo((String) s, freqArray[k], language));
                            //break;
                        }
                    }
                }
            }else{
                break;
            }
        }

        return result;
    }

    /**
     * Char feature extractor. From the input Termlist which have several characters from many languages,
     * it is extract lowest char which is not duplicated from other language's characters. so the result can be unique character for each language.
     *
     * @param languageCharFeatureList the language char feature list
     * @return the array list
     */
    public ArrayList<TermVo> charFeatureExtractor(ArrayList<TermVo> languageCharFeatureList) {

        ArrayList<TermVo> results = new ArrayList<>();
        HashMap<String, ArrayList<TermVo>> termAndVoHash = new HashMap();

        for(TermVo termAndLanguage : languageCharFeatureList){
            if(termAndVoHash.containsKey(termAndLanguage.getTermName())){
                termAndVoHash.get(termAndLanguage.getTermName()).add(termAndLanguage);
            }else{
                ArrayList<TermVo> tmp = new ArrayList<>();
                tmp.add(termAndLanguage);
                termAndVoHash.put(termAndLanguage.getTermName(), tmp);
            }
        }

        for(Map.Entry<String, ArrayList<TermVo>> m:termAndVoHash.entrySet()){
//            System.out.println(m.getKey()+"-----"+m.getValue().size());
            if(m.getValue().size() < Configurations.numOfCharFeature){
                results.add(m.getValue().get(0));
            }
        }

        return results;
    }
}
