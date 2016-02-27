package com.kyungtaelim.www.machineLearning.SVM;

import com.kyungtaelim.www.language.feature.LanguageResources;
import libsvm.*;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Created by usertop on 2016-01-10.
 * @author kyungtaelim@kisti.re.kr
 */
public class LibSVMHandler {

    // Preparing the SVM parameters
    private svm_parameter param;
    private svm_model model;

    // Preparing the learning corpus and test corpus
    private HashMap<Integer, HashMap<Integer, Double>> featuresTraining;
    private HashMap<Integer, Integer> labelTraining;
    private HashMap<Integer, HashMap<Integer, Double>> featuresTesting;
    private HashSet<Integer> features;

    public LibSVMHandler(){

       this.param = new svm_parameter();
       this.param.probability = 1;
       this.param.gamma = 0.5;
       this.param.nu = 0.5;
       this.param.C = 1;
       this.param.svm_type = svm_parameter.C_SVC;
       this.param.kernel_type = svm_parameter.LINEAR;
       this.param.cache_size = 20000;
       this.param.eps = 0.001;

       featuresTraining = new HashMap<>();
       labelTraining = new HashMap<>();
       featuresTesting = new HashMap<>();
       features = new HashSet<>();

    }

    /**
     * Read training data.
     *
     * @param trainingSet the training set
     */
    public void readTrainingData(String trainingSet){
        //Read in training data
        BufferedReader reader=null;
        try{

            StringTokenizer st = new StringTokenizer(trainingSet,"\n");
            String line=null;
            int lineNum=0;
            while(st.hasMoreElements()){
                line = st.nextToken();
                featuresTraining.put(lineNum, new HashMap<Integer,Double>());
                String[] tokens=line.split("\\s+");
                int label=Integer.parseInt(tokens[0]);
                labelTraining.put(lineNum, label);
                for(int i=1;i<tokens.length;i++){
                    String[] fields=tokens[i].split(":");
                    int featureId=Integer.parseInt(fields[0]);
                    double featureValue=Double.parseDouble(fields[1]);
                    features.add(featureId);
                    featuresTraining.get(lineNum).put(featureId, featureValue);
                }
                lineNum++;
            }

            reader.close();
        }catch (Exception e){

        }
    }

    /**
     * Read test data.
     *
     * @param testSet the test set
     */
    public void readTestData(String testSet){
        //Read in test data
        BufferedReader reader=null;
        try{
            StringTokenizer st = new StringTokenizer(testSet,"\n");
            String line=null;
            int lineNum=0;
            while(st.hasMoreElements()){
                line = st.nextToken();
                featuresTesting.put(lineNum, new HashMap<Integer,Double>());
                String[] tokens=line.split("\\s+");
                for(int i=1; i<tokens.length;i++){
                    String[] fields=tokens[i].split(":");
                    int featureId=Integer.parseInt(fields[0]);
                    double featureValue=Double.parseDouble(fields[1]);
                    featuresTesting.get(lineNum).put(featureId, featureValue);
                }
                lineNum++;
            }
            reader.close();
        }catch (Exception e){

        }
    }

    /**
     * Train from training set
     */
    public void train(){
        //Train the SVM model
        svm_problem prob=new svm_problem();
        int numTrainingInstances=featuresTraining.keySet().size();
        prob.l=numTrainingInstances;
        prob.y=new double[prob.l];
        prob.x=new svm_node[prob.l][];

        for(int i=0;i<numTrainingInstances;i++){
            HashMap<Integer,Double> tmp=featuresTraining.get(i);
            prob.x[i]=new svm_node[tmp.keySet().size()];
            int indx=0;
            for(Integer id:tmp.keySet()){
                svm_node node=new svm_node();
                node.index=id;
                node.value=tmp.get(id);
                prob.x[i][indx]=node;
                indx++;
            }
            prob.y[i]=labelTraining.get(i);
        }
        model=svm.svm_train(prob, param);
    }


    /**
     * Predict string.
     *
     * @return the string
     */
    public String predict(){
        String result="";
        for(Integer testInstance:featuresTesting.keySet()){
            HashMap<Integer, Double> tmp=featuresTesting.get(testInstance);
            int numFeatures=tmp.keySet().size();
            svm_node[] x=new svm_node[numFeatures];
            int featureIndx=0;
            for(Integer feature:tmp.keySet()){
                x[featureIndx]=new svm_node();
                x[featureIndx].index=feature;
                x[featureIndx].value=tmp.get(feature);
                featureIndx++;
            }
            double d=svm.svm_predict(model, x);
            result = result+d+"\n";
//            System.out.println(testInstance+"\t"+d);
        }
        return result;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        LibSVMHandler lsvm = new LibSVMHandler();
        lsvm.readTrainingData(LanguageResources.trainingSet);
        lsvm.train();
        lsvm.readTestData(LanguageResources.testSet);

        System.out.println(lsvm.predict());

    }

}