package loxia.utils;

import java.util.List;

public class StringUtil{

    public static final String DELIM = ",";

    public static String join(List<? extends Object> list){
        return join(list, DELIM);
    }

    public static String join(Object[] arr){
        return join(arr, DELIM);
    }

    public static String join(List<? extends Object> list,String seperator){
        if (list == null || list.size() == 0)
            return "";
        Object[] t = new Object[0];
        return join(list.toArray(t), seperator);
    }

    public static String join(Object[] arr,String seperator){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++){
            sb.append(seperator + arr[i]);
        }
        if (sb.length() > 0){
            sb.delete(0, seperator.length());
        }
        return sb.toString();
    }
}
