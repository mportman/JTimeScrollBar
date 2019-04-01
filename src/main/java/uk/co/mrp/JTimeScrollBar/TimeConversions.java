package uk.co.mrp.JTimeScrollBar;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;

public class TimeConversions {
 
	
	public static double toDouble( Duration dur )
	{
		long s = dur.getSeconds();
		int n = dur.getNano();
		
		return toTime(s, n);		
	}

	public static  double toTime( long sec, int nano )
	{
		double s  = (double)sec + (   (double)nano / 1000000000.0 );	
		return s;				
	}
	
	public static Instant secondsToInstant( double seconds)
	{
		long epochSecond = (long) (seconds / 1000);
		int nanoAdjustment = (int) ((seconds - epochSecond)  * 1000000000);
		
		Instant n = Instant.ofEpochSecond(epochSecond, nanoAdjustment);
		return n;
	}
	
	public static  Duration secondsToDuration( double seconds)
	{
		long epochSecond = (long) (seconds);
		int nanoAdjustment = (int) ((seconds - epochSecond)  * 1000000000);
		
		Duration dur  = Duration.ofSeconds(epochSecond, nanoAdjustment);
		return dur;
	}

	public static double toDouble(Instant timeValue) 
	{
		long s = timeValue.getEpochSecond();
		int n = timeValue.getNano();
		
		return toTime(s, n);		
	}

	
	
	public static double calToDouble( Calendar t )
	{
		return (((double)t.getTimeInMillis() ) );
	}
	
	public static Instant calToInstant( Calendar t )
	{
		long epochMilli = t.getTimeInMillis();
		return Instant.ofEpochMilli(epochMilli);
	}
	
	public static Calendar timeToCal( double d )
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(  (long)(d ) );
		return cal;
	}
}
