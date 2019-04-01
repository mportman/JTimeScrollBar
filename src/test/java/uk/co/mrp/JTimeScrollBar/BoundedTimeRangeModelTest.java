package uk.co.mrp.JTimeScrollBar;

import java.time.Duration;
import java.time.Instant;

import org.junit.Test;
 
public class BoundedTimeRangeModelTest 
{

	@Test public void
	testToString()
	{
		//BoundedRangeModel bm = new DefaultBoundedRangeModel(50,10,0,100);
		//System.out.println( bm.toString() );

		long epochSecond = Instant.now().getEpochSecond();
		
		Instant min = Instant.ofEpochSecond(epochSecond);
		Instant max = Instant.ofEpochSecond(epochSecond + 1000 );

		Instant value = Instant.ofEpochSecond(epochSecond+500);
		Duration extent = Duration.ofSeconds(100);
		
		
		BoundedTimeRangeModel bm = new BoundedTimeRangeModel(value, extent, min, max);

		System.out.println( bm.toString());
		
	}
}
