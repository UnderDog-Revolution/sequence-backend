package site.sequence.projectservice.utils;

import java.util.List;

public class DataConvertor {

    //List로 받은 자료형을 DB에 바로 저장하기 위해 String으로 변환할때 사용
    public static <T> String listToString(List<T> data){
        StringBuilder sb = new StringBuilder("[");
        for (T t : data) {
            sb.append(t);
        }
        sb.append("]");
        return sb.toString();
    }
}
