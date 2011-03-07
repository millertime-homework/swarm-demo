/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

// http://download.oracle.com/javase/tutorial/uiswing/painting/step1.html

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Playfield extends JPanel implements Runnable {
    static final long serialVersionUID = 0;
    private Bug[] bugs;
    private int d;
    private Thread t = null;
    private boolean threadSuspended = true;
    // http://www.rgagnon.com/javadetails/java-0260.html
    public static final BasicStroke stroke = new BasicStroke(2.0f);
    public static final double DT = 0.01;   // Physics time increment
    public static final double DDT = 10;   // Multiple for display time

    public Playfield(int nbugs) {
	bugs = new Bug[nbugs];
	for (int i = 0; i < nbugs; i++)
	    do
		bugs[i] = new Bug(this);
	    while(collision(bugs[i]));
    }

    public Dimension getPreferredSize() {
	return new Dimension(250,250);
    }

    public boolean collision(Bug b) {
	boolean result = false;

	if (b.wallCollision()) {
	    result = true;
	    b.thump();
	}
	for (int i = 0; i < bugs.length; i++) {
	    if (bugs[i] != null && bugs[i] != b &&
		bugs[i].bugCollision(b)) {
		result = true;
		b.thump();
		bugs[i].thump();
	    }
	}
	return result;
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	Graphics2D g2d = (Graphics2D) g;
	g2d.setStroke(stroke);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
	Dimension ds = getSize();
	d = Math.min(ds.width, ds.height) - 5;
	if (d <= 0)
	    return;
	g.clearRect(0, 0, ds.width, ds.height);
	g.translate((ds.width - d) / 2, (ds.height - d) / 2);
	g.setColor(Color.black);
	g.drawRect(0, 0, d, d);
	for (int i = 0; i < bugs.length; i++)
	    bugs[i].paintComponent(g, d);
    }

    // http://profs.etsmtl.ca/mmcguffin/learn/java/06-threads/
    public void run() {
	while(true) {
	    long then = System.currentTimeMillis();
	    for (int j = 0; j < DDT; j++)
		for (int i = 0; i < bugs.length; i++)
		    bugs[i].step();
	    if (threadSuspended) {
		synchronized(this) {
		    while (threadSuspended) {
			try {
			    t.wait();
			} catch(InterruptedException e) {
			    System.exit(1);
			}
		    }
		}
	    }
	    repaint();
	    long interval = (long) Math.floor(1000 * DT * DDT);
	    long now = System.currentTimeMillis();
	    interval -= now - then;
	    if (interval < 0) {
		System.err.println("time overrun");
	    } else {
		try {
		    t.sleep(interval);
		} catch(InterruptedException e) {
		    System.exit(1);
		}
	    }
	}
    }

    public void start() {
	if ( t == null ) {
	    t = new Thread( this );
	    threadSuspended = false;
	    t.start();
	}
	else {
	    if ( threadSuspended ) {
		threadSuspended = false;
		synchronized( this ) {
		    notify();
		}
	    }
	}
    }

    public void stop() {
	threadSuspended = true;
    }
}