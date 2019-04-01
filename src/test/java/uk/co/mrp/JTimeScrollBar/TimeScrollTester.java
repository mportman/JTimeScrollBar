package uk.co.mrp.JTimeScrollBar;
import java.awt.BorderLayout;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;

import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Simple class showing the scroll bar working
 *  at the foot of a big label in the panel,
 *  which itself shows the value and extent of the
 *  scrollbar.
 *  
 * @author mrp
 */
public class TimeScrollTester 
{
	public static void main( String[] args )
	{

		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());

		// set initial time range.
		Calendar start_time  = Calendar.getInstance();
		Calendar end_time  = Calendar.getInstance();
		end_time.add(Calendar.DAY_OF_YEAR, 7);

		JTimeScrollBarPanel scroll = new JTimeScrollBarPanel();
		scroll.setTimeRange(start_time, end_time);

		final TimeDisplay display = new TimeDisplay();

		frame.add( display, BorderLayout.CENTER);
		frame.add( scroll, BorderLayout.SOUTH );

		frame.setVisible( true );
		frame.pack();

		
		scroll.getModel().addChangeListener( new ChangeListener(){
			
			public void stateChanged(ChangeEvent changeEvent) {
				Object source = changeEvent.getSource();
				if (source instanceof BoundedRangeModel) {

					BoundedTimeRangeModel bt = (BoundedTimeRangeModel) source;
					
					display.showTimeRange( 
							bt.getTimeValue(),
							bt.getTimeExtent()  
							);
				} 
			}}); 
	}	
}


/** Simple panel to render the selected time (Instant and Duration) in a
 * text label.
 */
@SuppressWarnings("serial")
class TimeDisplay extends JPanel
{
	JLabel timeDisplay = new JLabel();
	
	public TimeDisplay()
	{
		super();
		
		add( timeDisplay );
	}

	public void showTimeRange(Instant time, Duration dur ) 
	{
		String dispString = 
				TimeDescription.formatTime(time) + " " + TimeDescription.getShortDescriptionOfDuration( dur );

		timeDisplay.setText( dispString );		
	}
}
