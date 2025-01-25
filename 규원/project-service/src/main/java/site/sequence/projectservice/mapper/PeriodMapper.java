package site.sequence.projectservice.mapper;

import site.sequence.projectservice.enums.Period;

import java.util.HashMap;
import java.util.Map;

public class PeriodMapper{
    private static Map<String, Period> KeywordToPeriod= new HashMap<>();

    static {
        KeywordToPeriod.put("1개월 이하", Period.OneMonthLess);
        KeywordToPeriod.put("1개월 ~ 3개월", Period.OneToThreeMonth);
        KeywordToPeriod.put("3개월 ~ 6개월", Period.ThreeToSixMonth);
        KeywordToPeriod.put("6개월 ~ 1년", Period.SixToOneYear);
        KeywordToPeriod.put("1년 이상", Period.OverOneYear);
    }

    //유효하지 않은 키워드가 들어올 경우 예외처리
    public static Period PeriodCheck(String keyword){
        Period period = KeywordToPeriod.get(keyword);
        if(period == null){
            throw new IllegalArgumentException("잘못된 키워드입니다: " + keyword);
        }
        return period;
    }

}
