package com.kyungtaelim.www.main;

import com.kyungtaelim.www.language.LanguageDetectorHandler;
import com.kyungtaelim.www.machineLearning.SVM.LibSVMHandler;
import java.util.StringTokenizer;

/**
 * The type Main.
 * @author kyungtaelim@kisti.re.kr
 * Created by usertop on 2016-01-10.
 */
public class Main {

    public static void main(String[] args) {

        //Feature extraction from Wikipedia plain texts for each language (English, German, Korean, Japanese, French).
        LanguageDetectorHandler ldh = new LanguageDetectorHandler();
        ldh.trainingFeatureExtractor();
        String trainingSet = ldh.trainingSetGenerator();

        //Machine Learning part
        LibSVMHandler libSVM = new LibSVMHandler();
        libSVM.readTrainingData(trainingSet);
        libSVM.train();

        //Test for 5 languages in one document. the machine split the text by "\n,."
        String testSet = ldh.testSetGenerator("조선의 제26대 왕이자 대한제국의 초대 황제 고종의 일곱째 아들이며 어머니는 순헌황귀비 엄씨이다." +
                "Giacometti gehört zu den bedeutendsten Bild-hauern des 20. Jahr-hunderts. " +
                "Le moustique (photo) est l’animal qui cause le plus de morts chez l’être humain." +
                "Dishonored is a 2012 stealth action-adventure video game developed by Arkane Studios and published by Bethesda Softworks. " +
                "天皇賞は、日本中央競馬会（JRA）が春・秋に年2回施行する競馬の重賞で 国際的には最高位のGIに格付けされている ゴート戦争は." +
                "Who is framed for murder and forced to become an assassin, seeking revenge on those who conspired against him.");
        libSVM.readTestData(testSet);
        String results = libSVM.predict();

        StringTokenizer st = new StringTokenizer(results, "\n");
        while (st.hasMoreElements()){
            String token = st.nextToken();
            if(token.equals("1.0")){
                System.out.println(token+": English");
            }else if(token.equals("2.0")){
                System.out.println(token + ": German");
            }else if(token.equals("3.0")){
                System.out.println(token + ": Korean");
            }else if(token.equals("4.0")){
                System.out.println(token + ": Japanese");
            }else if(token.equals("5.0")){
                System.out.println(token+ ": French");
            }else {
            }
        }

        /*
        for more information about features.
        1. feature dictionary with frequency and languages
        2. feature String and feature Number
        HashMap<String, ArrayList<TermVo>> featureList =  ldh.getFeatureDic().featureTermDictionary;
        HashMap<String, Integer> featureNumberList = ldh.getFeatureDic().featureNumberDictionary;
         */

        /*
        Can change training corpus from LanguageResources.class
        LanguageResources.englishText
         */

    }

}

