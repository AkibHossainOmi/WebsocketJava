package com.tb.common;

public class StringUtil {
    public class Parser{
        public static String getFirstOccuranceOfParamValueByIndexAndTerminatingStr(String str, String paramIdentifier,
                                                                                   String terminatingStr){
            String substrAfterParam=getStrAfterFirstOccuranceOf(str,paramIdentifier);
            if(substrAfterParam.contains(terminatingStr)){
                return substrAfterParam.split(terminatingStr)[0];
            }
            else return substrAfterParam;
        }
        public static String getStrAfterFirstOccuranceOf(String str, String param){
            int substrStart = str.indexOf(param) + param.length();
            return str.substring(substrStart);
        }
    }
}
