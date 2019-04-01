package uk.co.mrp.JTimeScrollBar;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * A BoundedRangeModel, but we add a few extra methods to allow
 * the range to be specified by two Instants,
 * and the current range to be specified by an Instant and a Duration.
 * 
 * @author mrp
 */
@SuppressWarnings("serial")
public class BoundedTimeRangeModel extends DefaultBoundedRangeModel 
{

	private static Logger log = LoggerFactory.getLogger( BoundedTimeRangeModel.class );

	   
	// the Super class holds all the values as Integers.
	// We hold our values as Instants and Durations,
	// and translate between the two.
	
	Instant minimum;
	Instant maximum;
	
	// the min / max of the superclass are 0 and Integer.MAX
	// we set them in our constructor.
	
	// we need 2 functions that map an Instant in our range to the
	// Integer range, and vice versa.
	
	// the amount of time a single int (ie. 1) in the Integer range
	// represents.  This gets updated any time our range changes.
	Duration timePerInt;
	
	/** Update our internal idea of the amount of time 1 
	 * in the Integer range represents.
	 */
	private void updateTimePerInt()
	{
		long range = getRangeInSuperClass();
		
		timePerInt = getTimeRange().dividedBy( range );
	}
	
	
	private long getRangeInSuperClass()
	{
		long range = (long)super.getMaximum() - (long)super.getMinimum();
		return range;		
	}
	
	
		
	

	
	public BoundedTimeRangeModel()
	{
		super( 0,100,  0,Integer.MAX_VALUE);
		
		super.addChangeListener(cl);
	}
	
	public BoundedTimeRangeModel(Instant value,
            Duration extent,
            Instant min,
            Instant max)
	{
		super( 0,100,0,Integer.MAX_VALUE);
		
		
		this.minimum = min;
		this.maximum = max;

		updateTimePerInt();

		setTimeExtent( extent );
		
		setTimeValue( value );

		super.addChangeListener(cl);
	}
	
	private ChangeListener cl = new ChangeListener(){

		@Override
		public void stateChanged(ChangeEvent arg0) {
			
			
		}};
	
	
	
	
	
	public Duration getTimeRange()
	{
		return Duration.between(minimum, maximum);		
	}
	
	
		
	
	
	public void	setTimeMaximum(Instant n)
	{
		log.debug("setTimeMaximum("+n.toString() + ")");
		this.maximum = n;
		updateTimePerInt();
		
		if( getTimeValue().compareTo(this.maximum)  == -1 )
			setTimeValue(  this.maximum.minusSeconds(10));
		
		
		this.fireStateChanged();
	}
	
	public Instant	getTimeMaximum()
	{
		return maximum;
	}
	
	
	public void	setTimeMinimum(Instant n)
	{
		log.debug("setTimeMinimum("+n.toString() + ")");
		this.minimum = n;
		updateTimePerInt();

		this.fireStateChanged();
	}
	
	public Instant	getTimeMinimum()
	{
		return minimum;
	}
	
	
	public void	setTimeValue(Instant n)
	{
		log.debug("setTimeValue( " + n.toString() + ")" );
		
		long i = convertTimeToInt(n);
		super.setValue( (int)i);
	}

	public Instant getTimeValue()
	{
		int v = getValue();
		
		return convertIntToTime(v);		
	}
	

	public void setTimeExtent( Duration n )
	{
		log.debug("setTimeExtent(" + n.toString() + ")" );
		
		long i = convertDurationToInt(n);
		super.setExtent((int)i);
	}
	
	public Duration getTimeExtent()
	{
		int ext = super.getExtent();
		Duration duration = convertIntToDuration(ext);
		return duration;
	}

	
//	@Override
//	public void addChangeListener( ChangeListener cl )
//	{	
//	}
//	
//	@Override
//	public void removeChangeListener( ChangeListener cl)
//	{		
//	}
	
	
	@Override
	public String	toString()
	{
		//javax.swing.DefaultBoundedRangeModel[value=50, extent=10, min=0, max=100, adj=false]

		String valueString = getTimeValue().toString();
		String extentString = getTimeExtent().toString();
		String minString = minimum.toString();
		String maxString = maximum.toString();
		
		return "BoundedTimeRangeModel[value="+valueString+", extent="+extentString+", min="+minString+", max="+maxString+", adj="+getValueIsAdjusting() +"]";
	}

	//=======================================================================
	// internal conversions
	
	private int convertTimeToInt( Instant t )
	{
		Duration dur = Duration.between(minimum, t);
		return convertDurationToInt(dur);	
	}
	
	private Instant convertIntToTime( int i )
	{	
		Duration t = timePerInt.multipliedBy(i);
		
		Instant tv = minimum.plus(t);

		return tv;
		
	}

	

	private int convertDurationToInt( Duration dur )
	{
		int unit_ns = timePerInt.getNano();
		long unit_s = timePerInt.get(ChronoUnit.SECONDS);
		
		double unit = unit_s + ((double)unit_ns / 1000000000.0);
		
		
		int dur_ns = dur.getNano();
		long dur_s = dur.get(ChronoUnit.SECONDS);
		
		double durS = dur_s + ((double)dur_ns / 1000000000.0);
		
		
		
		double x = durS / unit;
		
		int d  = (int)x;
		
		// don't allow a duration of 0 ints
		if( d == 0 ) d = 1;
		
		return d;
		
	}

	private Duration convertIntToDuration( int i)
	{
		Duration dur = timePerInt.multipliedBy(i);
		return dur;
	}


	
	
}
