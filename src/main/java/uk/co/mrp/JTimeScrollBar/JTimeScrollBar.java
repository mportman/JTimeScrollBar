package uk.co.mrp.JTimeScrollBar;


import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This is an extension of JScrollBar, where each of the
 * values, range, limits and so on are supplied and returned using 
 * dates, which are internally managed as doubles (seconds since epoch).
 * 
 * these are the value,extent  and min and max.
 * 
 * but the scrollbar has a limited span (Int.min -> Int.max) 
 * so these values (sb in the text) have to be converted to
 * times, and vice versa.
 * 
 * 
 * @author mrp
 *
 */
@SuppressWarnings("serial")
public class JTimeScrollBar extends JScrollBar 
{
	private static Logger log = LoggerFactory.getLogger( JTimeScrollBar.class );

	
	BoundedTimeRangeModel timeRangeModel;
	
	// Used in timing repaint operations.
	long lastRenderTime = System.currentTimeMillis();
	
	boolean first = true;

	int previousValue = 0;
	
	
	public JTimeScrollBar(int orientation)
	{
		super( orientation );
		setOrientation(orientation);  // check orientation is acceptable.
		
		
		long epochSecond = Instant.now().getEpochSecond();
		
		Instant min = Instant.ofEpochSecond(epochSecond);
		Instant max = Instant.ofEpochSecond(epochSecond + 1000 );

		Instant value = Instant.ofEpochSecond(epochSecond+500);
		Duration extent = Duration.ofSeconds(100);

		timeRangeModel = new BoundedTimeRangeModel(value, extent, min, max);
		
		
		setModel(timeRangeModel);
		
	}
	
	
	public JTimeScrollBar(int orientation, Calendar value, Duration extent, Calendar min, Calendar max)
	{
		super( orientation );
		setOrientation(orientation);  // check orientation is acceptable.

		Instant minI = Instant.ofEpochMilli( min.getTimeInMillis() );
		
		Instant maxI=  Instant.ofEpochMilli(max.getTimeInMillis() );
		
		Instant valueI = Instant.ofEpochSecond( value.getTimeInMillis() );
	

		timeRangeModel = new BoundedTimeRangeModel(valueI, extent, minI, maxI);
		
		
		setModel(timeRangeModel);
		
	}
	
	
	public JTimeScrollBar()
	{
		super( JScrollBar.HORIZONTAL);
		
	
		
		long epochSecond = Instant.now().getEpochSecond();
		
		Instant min = Instant.ofEpochSecond(epochSecond);
		Instant max = Instant.ofEpochSecond(epochSecond + 1000 );

		Instant value = Instant.ofEpochSecond(epochSecond+500);
		Duration extent = Duration.ofSeconds(100);

		timeRangeModel = new BoundedTimeRangeModel(value, extent, min, max);
		
		
		setModel(timeRangeModel);

		
		
		
		// repaint ourselves while adjusting.  this draws on the 
		// current time over the scroll bar.
		this.addAdjustmentListener(new AdjustmentListener() {		
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) 
			{
				repaint();
				
				long thisTime = System.currentTimeMillis();
				long duration = thisTime - lastRenderTime;
//				System.out.println("" + 1000.0 / (double)duration + " fps.");
				
				System.out.println( timeRangeModel.toString() );
				
				lastRenderTime = thisTime;				
				
				if( first )
				{
					previousValue = JTimeScrollBar.this.getValue();
					first = false;
				}
				
				if( ! e.getValueIsAdjusting() )  // we've finished
				{
//					Command scrollTo = new ScrollTo( TimeScrollBar.this, previousValue, e.getValue() );
//					CommandExecutive.instance().execute(scrollTo);

					first = true;
				}
				
			}
		});
		
		
		 final JPopupMenu popup = new JPopupMenu("");

		 popup.add(  mkDurationMenuItem( "1 Day", 24 * 60 * 60 ));
		 popup.add(  mkDurationMenuItem( "1 Hour", 60 * 60 ));
		 popup.add(  mkDurationMenuItem( "1 Minute", 60 ));
		 
		 
			 
		 this.addMouseListener(new MouseAdapter() {
			 public void mousePressed(MouseEvent e) {
			        maybeShowPopup(e);
			    }

			    public void mouseReleased(MouseEvent e) {
			        maybeShowPopup(e);
			    }

			    private void maybeShowPopup(MouseEvent e) {
			        if (e.isPopupTrigger()) {
			            popup.show(e.getComponent(),
			                       e.getX(), e.getY());
			        }
			    }
			 
		});
		this.add(popup);
		
		
	}
	

	
	/**
	 * We can only do horizontal bars at present.
	 */
	@Override
	public int getOrientation()
	{
		return Adjustable.HORIZONTAL;
	}
	

	@Override
    public void setOrientation(int orientation)
    {
		if( orientation != Adjustable.HORIZONTAL)
			throw new IllegalArgumentException("Can only have HORIZONTAL time scroll bar at present.");
    }
    

	
	
	private JMenuItem mkDurationMenuItem(String label, final int seconds) 
	{
		 JMenuItem item = new JMenuItem(label);
			
		 item.addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					Duration d = Duration.of(seconds, ChronoUnit.SECONDS);
					
					setTimeExtent(  d );
				}

				
			});	
	return item;
	}


		
	public Instant getTimeMin()
	{
		return timeRangeModel.getTimeMinimum();
	}
	
	
	public void setTimeValues( Instant value,Duration extent,Instant min , Instant max )
	{
		setTimeRange(min, max);
		setTimeValue(value);
		setTimeExtent( extent );
	}
	
 
	public void setTimeRange( Instant minI , Instant maxI )
	{
		log.debug("setTimeRange( " + minI.toString() + "," + maxI.toString() +")"); 
		
		timeRangeModel.setTimeMinimum( minI );
		timeRangeModel.setTimeMaximum( maxI );
		
		
		repaint();
		
		//timeRangeModel.f
		
	//TOD	((FireableModel)this.getModel()).fire();
	}
	
	
	
	public void setTimeValue( Instant val)
	{
		log.debug("setTimeValue( " + val.toString() + ")");
		
		timeRangeModel.setTimeValue( val );

		repaint();

	}
	
	public void setTimeExtent( Duration d )
	{

		log.debug("setTimeExtent("+d.toString()+")");
		timeRangeModel.setTimeExtent(d);
		
		repaint();
		
	}
	

	
	
	
	
	public Instant getTimeValue()
	{
		return timeRangeModel.getTimeValue();
	}
	
	public Instant getTimeValuePlusExtent()
	{
		Instant t = getTimeValue();
		
		Duration dur = timeRangeModel.getTimeExtent();
	
		return t.plusSeconds(dur.getSeconds()).plusNanos(dur.getNano());
	}
	
	
	public Duration getExtentDuration()
	{
		return timeRangeModel.getTimeExtent();
	}
	

	
	
	
	//===================================================================
	
	
	
	
	/**
	 * Paint the component, drawing time data on top of it.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
						
		g.setColor(Color.black);
		
		
		// strings to render are 
		String left = TimeDescription.formatTime( getTimeValue());
		String right = TimeDescription.formatTime( getTimeValuePlusExtent() );
		
		String duration = TimeDescription.getShortDescriptionOfDuration(  getExtentDuration());
		
		
		int usableWidth = this.getWidth() - 20*2;  
		// 20 is width of < > on scroll bar, ish.
		
		// get size of text in pixels.

		Graphics2D g2d = (Graphics2D)g;
		
		Rectangle2D rLeft  = g2d.getFontMetrics().getStringBounds(left,  g2d);
		Rectangle2D rRight = g2d.getFontMetrics().getStringBounds(right, g2d);
		Rectangle2D rMid   = g2d.getFontMetrics().getStringBounds(duration, g2d);

		if( usableWidth >= (rLeft.getWidth()+rMid.getWidth()+rRight.getWidth()))
		{
			g.drawString( left,
					20,
					this.getHeight()/2  +4);
					

			g.drawString( duration,
					(int)(this.getWidth() / 2 - rMid.getWidth() /2),
					this.getHeight()/2  +4);

			g.drawString( right, 
					this.getWidth() - 20 - (int)(rRight.getWidth()),
					this.getHeight()/2  +4);
		
		}
		else if( usableWidth >= (rLeft.getWidth()+rRight.getWidth()))
		{

			g.drawString( left,
					20,
					this.getHeight()/2  +4);
					
			g.drawString( right, 
					this.getWidth() - 20 - (int)(rRight.getWidth()),
					this.getHeight()/2  +4);
		}
		else
		{
			// it won't fit!  lets just plot the starting time.
			
			if( usableWidth >= rLeft.getWidth())
			{
				g.drawString( left,
						20,
						this.getHeight()/2  +4);
			}
			else
			{
				// blimey! not much room.

				// lets plot it anyway, the scroll bar clipping will 
				// prevent it overrunning.
				
				g.drawString( left,
						20,
						this.getHeight()/2  +4);
				
			}
		}	
	}
	
	//===
	
	
	
	
	 
}
