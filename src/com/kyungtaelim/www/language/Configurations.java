package com.kyungtaelim.www.language;

/**
 * Created by user1 on 2016-01-11.
 * @author kyungtaelim@kisti.re.kr
 */
public class Configurations {


    /**
     * Minimum number of feature for generating training corpus
     */
    public static int minNumberOfFeatureTrain = 4;

    /**
     * Minimum number of feature for generating test corpus
     */
    public static int minNumberOfFeatureTest = 2;

    /**
     * The feature of character candidate should be delete almost about the lowest 20% of character because trashes can be powerful feature
     */
    public static double percentOfTopNFeature = 0.75;

    /**
     * The constant topNTermFeature.
     */
    public static int topNTermFeature = 50;

    /**
     * The constant, selecting char feature less than x among the languages
     */
    public static int numOfCharFeature = 2;

}
