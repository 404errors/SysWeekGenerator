package com.synergix;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeekOfYear {
	private int week_code;
	private int year_no;

	private int week_no;
	private Calendar week_start_date;
	private Calendar week_end_date;
	private static final String timestampPattern = "yyyy-MM-dd HH:mm:ss";
	private static final String sqlSeparator = ", ";
	private static final String sqlLineTerminator = ";";
	private static final String sqlSingleQuote = "'";
	
	public int getYear_no() {
		return year_no;
	}

	public void setYear_no(int year_no) {
		this.year_no = year_no;
	}
	
	public String toSqlInsert() {
		SimpleDateFormat sdf = new SimpleDateFormat(WeekOfYear.timestampPattern);
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO SYS_WEEK (WEEK_CODE, YEAR_NO, WEEK_NO, WEEK_START_DATE, WEEK_END_DATE, OBJECT_VERSION) ");
		sb.append("VALUES (");
		sb.append(this.week_code)
		.append(sqlSeparator).append(this.year_no)
		.append(sqlSeparator).append(this.week_no)
		.append(sqlSeparator).append(sqlSingleQuote).append(sdf.format(this.week_start_date.getTime())).append(sqlSingleQuote)
		.append(sqlSeparator).append(sqlSingleQuote).append(sdf.format(this.week_end_date.getTime())).append(sqlSingleQuote)
		.append(sqlSeparator).append(0)
		.append(")")
		.append(sqlLineTerminator);
		return sb.toString();
	}
	
	public WeekOfYear(Calendar calendar) {
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int firstDayOfWeek = calendar.getFirstDayOfWeek();
		int deltaToFirstDayOfCurrentWeek = 0;
		this.year_no = calendar.getWeekYear();
		this.week_no = calendar.get(Calendar.WEEK_OF_YEAR);
		this.week_code = 100 * this.year_no + this.week_no;
		deltaToFirstDayOfCurrentWeek = dayOfWeek >= firstDayOfWeek ? dayOfWeek - firstDayOfWeek : dayOfWeek + 7 - firstDayOfWeek;
		
		this.week_start_date = (Calendar) calendar.clone(); 
		this.week_start_date.add(Calendar.DATE, -deltaToFirstDayOfCurrentWeek);
		this.week_end_date = (Calendar) this.week_start_date.clone();
		this.week_end_date.add(Calendar.DATE, 6);
	}
	
	public WeekOfYear() {
		
	}
	

}
