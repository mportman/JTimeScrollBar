package uk.co.mrp.JTimeScrollBar;


import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


/** This is a panel which contains a JTimeScrollBar
 * and some other buttons to help control it.
 * 
 * These are zoom in and out buttons, and a 
 * play,stop,pause buttons.
 * 
 * Initially these are hidden, and an opener/closer
 * toggle button is visible.
 * 
 * Also, many methods here simply delegate to the JTimeScrollBar.
 * This enables the client to have just one of these (JTimeScrollBarPanel)
 * an interact with that, without bothering with the JTimeScrollBar itself.
 * 
 * @author mrp
 *
 */
@SuppressWarnings("serial")
public class JTimeScrollBarPanel extends JPanel
{
	final JTimeScrollBar sb  = new JTimeScrollBar();

	
	
	
	ImageIcon zoomInIcon = createImageIcon("toolbarButtonGraphics/general/ZoomIn16.gif",
            "a pretty but meaningless splat");

	ImageIcon zoomOutIcon = createImageIcon("toolbarButtonGraphics/general/ZoomOut16.gif",
            "a pretty but meaningless splat");

	final JButton zoomIn = new JButton(zoomInIcon);
	final JButton zoomOut = new JButton(zoomOutIcon);
	
	
	
	ImageIcon playIcon = createImageIcon("toolbarButtonGraphics/media/Play16.gif",
            "a pretty but meaningless splat");

	ImageIcon stopIcon = createImageIcon("toolbarButtonGraphics/media/Stop16.gif",
            "a pretty but meaningless splat");

	ImageIcon pauseIcon = createImageIcon("toolbarButtonGraphics/media/Pause16.gif",
            "a pretty but meaningless splat");

	
	
	
	final JButton play = new JButton(playIcon);
	final JButton stop = new JButton(stopIcon);
	final JButton pause = new JButton(pauseIcon);

	
	ImageIcon showIcon = createImageIcon("toolbarButtonGraphics/navigation/Back16.gif",
            "a pretty but meaningless splat");
	ImageIcon hideIcon = createImageIcon("toolbarButtonGraphics/navigation/Forward16.gif",
            "a pretty but meaningless splat");

	
	final JButton show = new JButton( showIcon );
	final JButton hide = new JButton( hideIcon );
  
	
	
	
	boolean paused = false;
	
	
	int factor = 0;
	public JTimeScrollBarPanel()
	{
		super();
		
		show.setMargin(new Insets(2,0,2,0));
		hide.setMargin(new Insets(2,0,2,0));
		
		// prevent buttons taking focus, so key events stay routed to Wall.
		show.setFocusable(false);
		hide.setFocusable(false);
		zoomIn.setFocusable(false);
		zoomOut.setFocusable(false);
		play.setFocusable(false);
		stop.setFocusable(false);
		pause.setFocusable(false);
		
		show.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			

				hide.setVisible(true);
				show.setVisible(false);
				zoomIn.setVisible( true );
				zoomOut.setVisible( true );
				play.setVisible( true );
				stop.setVisible( true );
				pause.setVisible( true );			
			}} );
		hide.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				hide.setVisible(false);
				show.setVisible(true);
//				zoomIn.setVisible( false );
//				zoomOut.setVisible( false );
				play.setVisible( false );
				stop.setVisible( false );
				pause.setVisible( false );			
			}} );

		
		
		zoomIn.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				
				// currently we have         s  dur=10
				//                           |----------|
				//
				// and we want                  s  5
				//                              |-----|
				
				
				
				Duration new_extent = sb.getExtentDuration().dividedBy(2);		
				Duration margin = sb.getExtentDuration().dividedBy(4);		
				
				Instant new_start = sb.getTimeValue().plus(  margin ); 
				
				
				if( new_start.isBefore( sb.getTimeMin() ) )
					return;
				
				sb.setTimeValue( new_start );
				sb.setTimeExtent( new_extent );						
			}} );

		
		zoomOut.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {

				// currently we have            s  5
				//                              |-----|
				//
				// and we want               s    10
				//                           |----------|

				Duration new_extent = sb.getExtentDuration().multipliedBy(2);		
				Duration margin = sb.getExtentDuration().dividedBy(4);		

				
				Instant new_start = sb.getTimeValue().minus(  margin ); 
				
				// if start or extent are too far out, TimeScrollBar will
				// limit them.

				sb.setTimeValue( new_start);
				sb.setTimeExtent( new_extent );		

			}} );


		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paused = false;
				pause.setText( "" );
				if( factor==0) factor=1;
				factor=factor*2;
				play.setText( "x" + factor);
			}
		});

		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				factor = 0;
				play.setText( "");		
			}
		});
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paused  = !paused;
				pause.setText( paused? "Paused":"");
			}
		});

		
		
		setLayout( new BoxLayout(this, BoxLayout.X_AXIS ) );
		add(sb);
		 
		
		
		 
		add(zoomIn);
		add(zoomOut);
		add(play);
		add(pause);
		add(stop);

		add( show );
		add( hide );
		
		hide.setVisible(false);
		show.setVisible(true);
		
		zoomIn.setVisible( true);
		zoomOut.setVisible( true );
		
		play.setVisible( false );
		stop.setVisible( false );
		pause.setVisible( false );			

		new Thread(){
			@Override
			public void run() {
				while(true)
				{
					try
					{
						Thread.sleep(1000);
					}
					catch( InterruptedException e)
					{
					}

					if( factor > 0 && !paused )
					{
						Instant v = sb.getTimeValue();
						Instant newv = v.plusMillis( factor * 1000);
						
						sb.setTimeValue(newv);
					}
				}
			}
		}.start();

	}


	
	
	
	public void setTimeRange(Calendar start_time, Calendar end_time) 
	{
		sb.setTimeRange( TimeConversions.calToInstant(start_time), TimeConversions.calToInstant(end_time) );		
	}
	
	
	public void setTimeExtent( double e )
	{
		sb.setTimeExtent( TimeConversions.secondsToDuration(e) );		
	}


	public double getValue()
	{
		return TimeConversions.toDouble( sb.getTimeValue() );
	}

	public double getValuePlusExtent()
	{
		return TimeConversions.toDouble( sb.getTimeValuePlusExtent( ));	
	}
 
	public BoundedRangeModel getModel() 
	{
		return sb.getModel();
	}


	
	public double getVisibleMillis() 
	{
		double d = TimeConversions.toDouble( sb.getExtentDuration() );
		
		return d * 1000;
	}
	
	

	protected ImageIcon createImageIcon(String path,
			String description) {
		java.net.URL imgURL = 		ClassLoader.getSystemClassLoader().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

}
