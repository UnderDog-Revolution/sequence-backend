package site.sequence.projectservice.enums;

public enum Period {
    OneMonthLess("1개월 이하"),
    OneToThreeMonth("1개월 ~ 3개월"),
    ThreeToSixMonth("3개월 ~ 6개월"),
    SixToOneYear("6개월 ~ 1년"),
    OverOneYear("1년 이상");

    public final String koreanName; // 필드를 public으로 선언

    Period(String koreanName) {
        this.koreanName = koreanName;
    }
}
