package com.kyungtaelim.www.language.feature;

/**
 * Created by usertop on 2016-01-10.
 * @author kyungtaelim@kisti.re.kr
 */
public class StopWordCharUtill {

    public static String eliminateStopChar(String s) {

        String result=s;
        result = result.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
        result = result.replaceAll("[0-9]", "");
        result = result.replaceAll("'", "");
        result = result.replaceAll("&", "");
        result = result.replaceAll("\\(", "");
        result = result.replaceAll("\\)", "");
        result = result.replaceAll("\"", "");
        result = result.replaceAll("[-]", "");
        result = result.replaceAll("[:]", "");
        result = result.replaceAll("[;]", "");
        result = result.replaceAll("[?]", "");
        result = result.replaceAll("[/]", "");
        result = result.replaceAll("\\\\", "");
        result = result.replaceAll("[.]", "");
        result = result.replaceAll("\\[", "");
        result = result.replaceAll("\\]", "");
        result = result.replaceAll("[,]", "");
//      String body  =  body.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");
//      result = result.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");

        return result;
    }
}
