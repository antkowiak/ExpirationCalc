/**
 * Copyright (c) 2016 Ryan Antkowiak (antkowiak@gmail.com).
 * All rights reserved.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, contact antkowiak@gmail.com.
 * 
 * @author	Ryan Antkowiak (antkowiak@gmail.com)
 * 
 */

package com.ryanantkowiak.ExpirationCalc;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @brief	Main entry point for the program.
 * 
 * This program calculates the upcomming options expiration dates.
 * It also displays how many calendar days are left until expiration for each date.
 * In addition, it distinguishes between normal expiration dates and weeklies.
 * 
 * @author	Ryan Antkowiak (antkowiak@gmail.com)
 */
public class Main
{
	
	/** The number of milliseconds in a day **/
	private static final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;
	
	
	/**
	 * @brief	Calculate the number of days until the next Friday, given a week day
	 * 
	 * @param	currentWeekDay - The current day of the week (Sunday = 1, Monday = 2, etc.)
	 * @return	int - The number of days until the next friday
	 * @throws	Exception - If the currentWeekDay is invalid, this method will throw
	 */
	private static int calculateDaysUntilNextFriday(int currentWeekDay) throws Exception
	{
		/** I'm sure this could be done with some clever arithmetic, but this works fine. **/
		switch (currentWeekDay)
		{
			case 1: { return 5; }
			case 2: { return 4; }
			case 3: { return 3; }
			case 4: { return 2; }
			case 5: { return 1; }
			case 6: { return 0; }
			case 7: { return 6; }
		}
		
		throw new Exception("Invalid currentWeekDay: " + currentWeekDay);
	}
	
	
	/**
	 * @brief	Returns true if the expiration day of the month is a monthly expiration
	 * @param	dayOfMonth - integer for the day of the month
	 * @return	boolean - true if this is a monthly expiration
	 */
	private static boolean isMonthlyExpiration(int dayOfMonth)
	{
		return (dayOfMonth >= 15 && dayOfMonth <= 20);
	}
	
	
	/**
	 * @brief	Returns true if the expiration day of the month is a weekly expiration
	 * @param	dayOfMonth - integer for the day of the month
	 * @return	boolean - true if this is a weekly expiration
	 */
	private static boolean isWeeklyExpiration(int dayOfMonth)
	{
		return (!isMonthlyExpiration(dayOfMonth));
	}
	
	/**
	 * @brief	Returns true if the given date (today) is an expiration date 
	 * @param	today - The calendar date for today
	 * @param	cal - The calendar date for the options expiration date in question
	 * @return	boolean - true if today is an expiration date
	 */
	private static boolean isExpirationToday(Calendar today, Calendar cal)
	{
		return (calculateDayDifference(today, cal) == 0);
	}
	
	
	/**
	 * @brief	Calculate the number of calendar days difference between two dates
	 * @param	today - The calendar date for today
	 * @param	cal - The calendar date to calculate the delta of days until
	 * @return	int - The number of days between today and cal
	 */
	private static int calculateDayDifference(Calendar today, Calendar cal)
	{
		long deltaMS = cal.getTimeInMillis() - today.getTimeInMillis();
		int deltaDays = (int) ( deltaMS / MS_IN_A_DAY);
		return deltaDays;
	}
	
	
	/**
	 * @brief	Return a string for the number with zero padding (for date displays)
	 * @param	number - The number to pad with a leading zero, if necessary
	 * @return	String - The padded string
	 */
	private static String padNumber(int number)
	{
		if (number >= 10)
			return ("" + number);
		
		return ("0" + number);
	}
	
	
	/**
	 * @brief	Print out the options expiration date, the days until expiration,
	 * 			whether or not the options expiration date is weekly option, and
	 * 			whether or not the expiration date is today
	 * @param	today - Today's date
	 * @param	cal - The option expiration date
	 */
	private static void printExpiration(Calendar today, Calendar cal)
	{
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		StringBuffer text = new StringBuffer(50);
		text.append(year);
		text.append("-");
		text.append(padNumber(month));
		text.append("-");
		text.append(padNumber(day));
		text.append("\t DTE = ");
		text.append(calculateDayDifference(today, cal));
		text.append("\t");

		if (isWeeklyExpiration(day))
			text.append(" [W]");
		
		if (isExpirationToday(today, cal))
			text.append(" [TODAY]");
		
		System.out.println(text.toString());	
	}
	
	
	/**
	 * @brief	Program entry point.  Prints out the upcoming options expirations.
	 * @param	args - unused
	 */
	public static void main(String[] args)
	{
		try
		{
			final Calendar today = new GregorianCalendar();
			final int daysTillNextFriday = calculateDaysUntilNextFriday(today.get(Calendar.DAY_OF_WEEK));
			
			Calendar expirationDate = new GregorianCalendar();
			expirationDate.add(Calendar.DAY_OF_MONTH, daysTillNextFriday);
		
			/** Print the next 20 options expiration dates **/
			for (int i = 0 ; i < 20 ; ++i)
			{
				printExpiration(today, expirationDate);
				expirationDate.add(Calendar.DAY_OF_MONTH, 7);
			}
		}
		catch (Exception e)
		{
			System.out.print("Exception: " + e.getMessage());
			System.exit(0);
		}
	}
}
