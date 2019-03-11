package com.synergix;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

public class SysWeekGenerator {
	public static void main(String[] args) {
		List<String> queryList = new ArrayList<>();
		String fileName = "";
		for (Integer wantedYear: SysWeekGenerator.getWantedYear()) {
			fileName = fileName.isEmpty()? fileName + wantedYear : fileName + "_" + wantedYear ;
			Calendar oneDay = SysWeekGenerator.getFirstDateOfYear(wantedYear);
			while (oneDay.getWeekYear() <= wantedYear) {
				WeekOfYear weekOfYear = new WeekOfYear(oneDay);
				oneDay.add(Calendar.DATE, 7);
				if (weekOfYear.getYear_no() != wantedYear) continue;
				queryList.add(weekOfYear.toSqlInsert());
			}
		}
		SysWeekGenerator.writeToFile(queryList, fileName);
	}
	
	private static void writeToFile(List<String> queryList, String fileName) {
		String outputFolderString = Paths.get(".").toAbsolutePath().toString();
		String outputFileName = fileName + "- query.txt";
		Path outputFile = Paths.get(outputFolderString.substring(0, outputFolderString.length() -1) + outputFileName);
		try(BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			for (String query : queryList) {
				bufferedWriter.write(query + System.lineSeparator());
			}
		} catch (IOException e) {
			System.out.println("Error on creating output file");
			e.printStackTrace();
		}
	}
	
	public static Calendar getFirstDateOfYear(int year) {
		Calendar calendar = SysWeekGenerator.getCalendar();
		calendar.set(year, 0, 1);
		SysWeekGenerator.removeTime(calendar);
		return calendar;
	}
	
	public static Calendar getLastDateOfYear(int year) {
		Calendar calendar = SysWeekGenerator.getCalendar();
		calendar.set(year, 0, 6);
		SysWeekGenerator.removeTime(calendar);
		return calendar;
	}
	
	public static Calendar getMidDateOfYear(int year) {
		Calendar calendar = SysWeekGenerator.getCalendar();
		calendar.set(year, 0, 6);
		SysWeekGenerator.removeTime(calendar);
		return calendar;
	}
	
	private static Calendar getCalendar() {
		Calendar calendar = new GregorianCalendar();
		calendar.setMinimalDaysInFirstWeek(7);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		return calendar;
	}
	
	private static void removeTime(Calendar calendar) {
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	}
	
	private static List<Integer> getWantedYear() {
		System.out.println("Enter year to generate (multiple years will be separated by comma): ");
		Scanner scanner = new Scanner(System.in);
		String years = scanner.nextLine();
		scanner.close();
		String[] listOfYearInString = years.split(",");
		List<Integer> yearList = new ArrayList<>();
		for (String year : listOfYearInString) {
			try {
				int yearNumber = Integer.parseInt(year.trim());
				if (!yearList.contains(yearNumber)) {
					yearList.add(yearNumber);
				}
			} catch (NumberFormatException  nfe) {
				System.out.println("Invalid input, ensure that all entered values are number");
			}
		}
		Collections.sort(yearList);
		return yearList;
	}

}
