package edu.kh.project.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
	
	public static void main(String[] args) throws ParseException {
		
		// Date : 날짜용 객체
		// Calendar : Date가 오래된 기능이라 지원이 안되는게 많아서 만들어진 Date 업그레이드 객체
		// SimpleDateFormat : 날짜를 원하는 형태의 문자열로 변환 / toChar 같은 거래.
		
		Date a = new Date(); // 현재 시간
		Date b = new Date(1669087647475L); // Java에서 시간 계산 시 기준 시간
		// 기준시간 : 1970년 1월 1일 09시 0분 0초
		// new Date(ms) : 기준 시간 + ms 만큼 지난 시간

		Calendar cal = Calendar.getInstance();
		cal.add(cal.DATE, 1); // 날짜에 1 추가
		//	단위(년/월/일) , 추가할 값
		// 여기서 년월일만 남기고 다 지우면 년월일 0:0:0이래
		
		// SimpleDateFormat을 이용해서 cal 날짜 중 시,분,초를 0:0:0 바꿈
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date temp = new Date( cal.getTimeInMillis() );
		// 하루 증가한 내일 날짜의 ms 값을 이용해서 Date 객체 생성
		
//		System.out.println(sdf.format(temp));
		Date c = sdf.parse(sdf.format(temp));
			// 날짜 형식 String -> Date로 변환
		
		System.out.println(a);
		System.out.println(temp);
		System.out.println(c);

		// 날짜 끼리는 빼기가 안된데
		// 내일 자정 ms - 현재 시간 ms
		
		long diff = c.getTime() - a.getTime();
		System.out.println((diff/1000)/1000); // 23:59:59까지 남은 시간(s)
		
	}

}
