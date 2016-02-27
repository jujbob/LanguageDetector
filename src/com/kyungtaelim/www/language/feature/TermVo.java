package com.kyungtaelim.www.language.feature;

import java.util.ArrayList;

/**
 * Created by user1 on 2016-01-10.
 * @author kyungtaelim@kisti.re.kr
 */
public class TermVo {

    private String termName;
    private int termFreq;
    private ArrayList<Integer> termSequence;
    private String language;


    public TermVo(String termName, int termFreq, String language){
        this.termName = termName;
        this.termFreq = termFreq;
        this.language = language;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public int getTermFreq() {
        return termFreq;
    }

    public void setTermFreq(int termFreq) {
        this.termFreq = termFreq;
    }

    public ArrayList<Integer> getTermSequence() {
        return termSequence;
    }

    public void setTermSequence(ArrayList<Integer> termSequence) {
        this.termSequence = termSequence;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
