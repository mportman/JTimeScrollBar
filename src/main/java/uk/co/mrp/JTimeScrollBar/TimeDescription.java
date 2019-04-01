package uk.co.mrp.JTimeScrollBar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;


/** Class to produce nice String descriptions of time 
 * and durations.
 * 
 * @author mrp
 *
 */
public class TimeDescription 
{
	private static long secsInYear     = 365*24*60*60;
	private static long secsInMonth    =  31*24*60*60;
	private static long secsInDay      =     24*60*60;
	private static long secsInHour     =        60*60;
	private static long secsInMinute   =           60;

	
	private static DecimalFormat df = new DecimalFormat("#.#");
	
	/**
	 * Describe the duration.
	 * 
	 * Will return strings like "3.5 days"
	 * 
	 * @param dur - in seconds
	 * @return the description
	 */
	public static  String getShortDescriptionOfDuration(double dur) 
	{
		if( dur > secsInYear  )	 return df.format( dur / secsInYear)   + " years"; 
		if( dur > secsInMonth )	 return df.format( dur / secsInMonth)  + " months"; 
		if( dur > secsInDay   )	 return df.format( dur / secsInDay )   + " days"; 
		if( dur > secsInHour  )	 return df.format( dur / secsInHour)   + " hours"; 
		if( dur > secsInMinute ) return df.format( dur / secsInMinute) + " mins"; 

		if( dur > 1.0 )  		return df.format( dur ) +"s"; 

		return df.format( dur * 1000 ) +"ms"; 
	}
	
	
	
	private static DateFormat datef = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public static String formatTime( double t )
	{
		Calendar s  = Calendar.getInstance();
		s.setTimeInMillis(  (long)(t*1000));
		return datef.format(s.getTime()); 
	}

	public static String formatTime( Instant t )
	{
		long sec = t.getEpochSecond();
		int nano = t.getNano();

		double d = sec + (nano/1000000000.0);

		return formatTime( d );
	}
	
	public static  String getShortDescriptionOfDuration(Duration dur) 
	{
		long sec = dur.getSeconds();
		int nano = dur.getNano();
		double d = sec + (nano/1000000000.0);
		
		return getShortDescriptionOfDuration(d);
	}

}
