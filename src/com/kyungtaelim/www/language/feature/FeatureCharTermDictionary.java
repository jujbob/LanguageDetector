package com.kyungtaelim.www.language.feature;

import java.util.*;

/**
 * Created by user1 on 2016-01-09.
 *
 * A feature Dictionary for generating training corpus and test corpus.
 * the feature can be some characters(a,ù,é,가,施) and some chunks(and, der, in, Obama, revenge,. etc) with term frequency,
 * @author kyungtaelim@kisti.re.kr
 */
public class FeatureCharTermDictionary {


    public static HashMap<String, ArrayList<TermVo>> featureTermDictionary;
    public static HashMap<String, Integer> featureNumberDictionary;
    public static int totalFeatureNumber;

    public FeatureCharTermDictionary(){
        featureTermDictionary = new HashMap<>();
        featureNumberDictionary = new HashMap<>();
        totalFeatureNumber = 0;
    }

    /**
     * Add term list.
     *
     * @param termList the term list
     */
    public void addTermList(ArrayList<TermVo> termList){

        for(TermVo term : termList){
            if(featureNumberDictionary.containsKey(term.getTermName())){ // if the term already exist in the dictionary
                featureTermDictionary.get(term.getTermName()).add(term);
            }else{
                featureNumberDictionary.put(term.getTermName(), totalFeatureNumber++);
                ArrayList<TermVo> tmp = new ArrayList<>();
                tmp.add(term);
                featureTermDictionary.put(term.getTermName(), tmp);
            }
        }
    }

    /**
     * Train set generator by char dictionary.
     *
     * @param text the text
     * @param language the language
     * @param minFeatureNumberInSentence the min feature number in sentence
     * @return the array list
     */
    public ArrayList<String> trainSetGeneratorByCharDictionary(String text, String language, int minFeatureNumberInSentence) {

        ArrayList<String> resultTrainSet = new ArrayList<>();
        String sentence = ""; String term =""; String aTrainingData= "";
        ArrayList<Object> featureList;

        StringTokenizer stDocument = new StringTokenizer(text, "\n,.,;,。,");
        while(stDocument.hasMoreElements()){
            aTrainingData = "";
            sentence = stDocument.nextToken().trim();
            featureList = new ArrayList<>();
            StringTokenizer stSentence = new StringTokenizer(sentence, " \t");
            aTrainingData = aTrainingData + language;

            while(stSentence.hasMoreElements()){
                term = StopWordCharUtill.eliminateStopChar(stSentence.nextToken()).trim();
                String oneChar = "";

                for(int l = 0; l < term.length() ; l++){
                    oneChar = ""+term.charAt(l);
                    if(featureNumberDictionary.containsKey(oneChar)){ // If a char is in the feature dictionary then it will be recoded by a feature.
                        featureList.add(featureNumberDictionary.get(oneChar));
                    }
                }

                if(featureNumberDictionary.containsKey(term)){ // If a term is in the feature dictionary then it will be recoded by a feature.
                    featureList.add(featureNumberDictionary.get(term));
                }
            }

            // for eliminating duplication
            ArrayList<Object> result = new ArrayList<>();
            HashSet<Object> hs = new HashSet<>(featureList);
            Iterator it = hs.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
            featureList = result;

            // for sorting the feature number
            Comparator<Object> comparator = new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    if((Integer)o1 > (Integer)o2){
                        return  1;
                    }else if((Integer)o1 < (Integer)o2){
                        return  -1;
                    }else{
                        return 0;
                    }
                }
            };
            Collections.sort(featureList, comparator);
            if(featureList.size() >= minFeatureNumberInSentence) { // The number of feature should be more than ..
                for(Object num : featureList){
                    aTrainingData = aTrainingData + " " + num + ":1";
                }
                resultTrainSet.add(aTrainingData);
            }
        }
        return resultTrainSet;
    }

}
