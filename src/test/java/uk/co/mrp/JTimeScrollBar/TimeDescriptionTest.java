package uk.co.mrp.JTimeScrollBar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TimeDescriptionTest 
{
	@Test public void
	canDescribe4Days()
	{
		double secs = 86400 * 4;
		String desc = TimeDescription.getShortDescriptionOfDuration(secs);
		
		assertThat( desc, equalTo("4 days"));
	}
	
	@Test public void
	canDescribe4andaThirdDays()
	{
		double secs = 86400 * 4.33333;
		String desc = TimeDescription.getShortDescriptionOfDuration(secs);
		
		assertThat( desc, equalTo("4.3 days"));
	}
	
}
