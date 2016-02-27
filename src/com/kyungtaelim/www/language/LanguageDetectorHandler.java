package com.kyungtaelim.www.language;

import com.kyungtaelim.www.language.feature.*;
import java.util.ArrayList;

/**
 * Created by user1 on 2016-01-11.
 * Handler. there are several functions
 * @author kyungtaelim@kisti.re.kr
 *
 */
public class LanguageDetectorHandler {

    //prepare for language resources, it contains texts about Wikipedia article(plain text) for each language
    private LanguageResources lr;
    //generate feature dictionary, this dictionary is for features about terms(chunk) and characters which can be distinguished from other languages in Wikipedia.
    //the feature dictionary made by automatically calculator
    private FeatureCharTermDictionary featureDic;

    public LanguageDetectorHandler(){
        lr = new LanguageResources();
        featureDic = new FeatureCharTermDictionary();
    }

    public LanguageResources getLr() {
        return lr;
    }

    public void setLr(LanguageResources lr) {
        this.lr = lr;
    }

    public FeatureCharTermDictionary getFeatureDic() {
        return featureDic;
    }

    public void setFeatureDic(FeatureCharTermDictionary featureDic) {
        this.featureDic = featureDic;
    }


    public void trainingFeatureExtractor(){
        LanguageCharTermFeatureExtractor lfe = new LanguageCharTermFeatureExtractor();

        ArrayList<TermVo> deFeature = lfe.topNWordExtract(lr.germanText, "German");
        ArrayList<TermVo> koFeature = lfe.topNWordExtract(lr.koreanText, "Korean");
        ArrayList<TermVo> enFeature = lfe.topNWordExtract(lr.englishText, "English");
        ArrayList<TermVo> jaFeature = lfe.topNWordExtract(lr.japanText, "Japan");
        ArrayList<TermVo> frFeature = lfe.topNWordExtract(lr.frenchText, "French");

        featureDic.addTermList(deFeature);
        featureDic.addTermList(koFeature);
        featureDic.addTermList(enFeature);
        featureDic.addTermList(jaFeature);
        featureDic.addTermList(frFeature);

        enFeature = lfe.topNCharExtract(lr.englishText, "English");
        deFeature = lfe.topNCharExtract(lr.germanText, "German");
        koFeature = lfe.topNCharExtract(lr.koreanText, "Korean");
        jaFeature = lfe.topNCharExtract(lr.japanText, "Japan");
        frFeature = lfe.topNCharExtract(lr.frenchText, "French");

        ArrayList<TermVo> languageCharFeatureList = new ArrayList<>();
        languageCharFeatureList.addAll(deFeature);
        languageCharFeatureList.addAll(enFeature);
        languageCharFeatureList.addAll(koFeature);
        languageCharFeatureList.addAll(jaFeature);
        languageCharFeatureList.addAll(frFeature);
        languageCharFeatureList = lfe.charFeatureExtractor(languageCharFeatureList);

        featureDic.addTermList(languageCharFeatureList);

    }


    public String trainingSetGenerator(){
        String result="";
        ArrayList<String> engTrainSet = featureDic.trainSetGeneratorByCharDictionary(lr.englishText, "1", Configurations.minNumberOfFeatureTrain);
        ArrayList<String> gerTrainSet = featureDic.trainSetGeneratorByCharDictionary(lr.germanText, "2", Configurations.minNumberOfFeatureTrain);
        ArrayList<String> korTrainSet = featureDic.trainSetGeneratorByCharDictionary(lr.koreanText, "3", Configurations.minNumberOfFeatureTrain);
        ArrayList<String> jaTrainSet = featureDic.trainSetGeneratorByCharDictionary(lr.japanText, "4", Configurations.minNumberOfFeatureTrain);
        ArrayList<String> frTrainSet = featureDic.trainSetGeneratorByCharDictionary(lr.frenchText, "5", Configurations.minNumberOfFeatureTrain);

        int enConut = engTrainSet.size(), gerCount = gerTrainSet.size(), korCount = korTrainSet.size(), frCount = korTrainSet.size(), jaCount = jaTrainSet.size();
        int trainingCount = 0;

        if (enConut < gerCount && enConut < korCount && enConut < jaCount && enConut < frCount) {
            trainingCount = enConut;
        } else if (gerCount < enConut && gerCount < korCount && gerCount < jaCount && gerCount < frCount) {
            trainingCount = gerCount;
        } else if (korCount < enConut && korCount < gerCount && korCount < jaCount && korCount < frCount){
            trainingCount = korCount;
        }else if(jaCount < enConut && jaCount < gerCount && jaCount < korCount && jaCount < frCount){
            trainingCount = jaCount;
        }else{
            trainingCount = frCount;
        }
        for (int i = 0; trainingCount > i; i++) {
            result = result+engTrainSet.get(i) + " \n";
            result = result+gerTrainSet.get(i) + " \n";
            result = result+korTrainSet.get(i) + " \n";
            result = result+jaTrainSet.get(i) + " \n";
            result = result+frTrainSet.get(i) + " \n";
        }
        return result;
    }


    public String testSetGenerator(String s){
        String result="";
        ArrayList<String> testSet = featureDic.trainSetGeneratorByCharDictionary(s, "1", Configurations.minNumberOfFeatureTest);

        for (int i = 0; testSet.size() > i; i++) {
            result = result + testSet.get(i) + " \n";
            System.out.println(testSet.get(i) + " ");
        }

        return result;
    }


    public static void main(String[] args) {
        LanguageDetectorHandler excutor = new LanguageDetectorHandler();
        excutor.trainingFeatureExtractor();
        System.out.print(excutor.trainingSetGenerator());
    }
}

