/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;
import java.text.DecimalFormat;

public class BugAI extends AI {
    static Random prng = new Random();
    
    static double nextSignedDouble() {
	return 2.0 * prng.nextDouble() - 1.0;
    }

    public void control(MeView me, AgentView[] agents, ThingView[] things) {
	if (things != null)
	    throw new Error("internal error: no things yet");
	me.a = 0;
	DecimalFormat df = new DecimalFormat("#.000");
	double dest = Math.atan((.5-me.y) /(.5-me.x)) - Math.PI/4;
	System.out.println("x: " + df.format(me.x) + " y: " + df.format(me.y));
	System.out.println("dest: " + df.format(Math.toDegrees(dest)));
	System.out.println("me: " + df.format(Math.toDegrees(me.t)));
	if ((Math.abs(dest - me.t) > .1) && (me.vt > 0))
	    me.vt += .001;
	else {
	    me.a = .5;
	    me.vt = 0;
	}
	//System.out.println("x: " + df.format(me.x) + " y: " + df.format(me.y));
	//System.out.println("dest: " + df.format(dest));
	//System.out.println("me: " + df.format(me.t));
	System.out.println("--");
	//me.vt += Math.PI * nextSignedDouble() * dt;
	System.exit(0);
    }
}
