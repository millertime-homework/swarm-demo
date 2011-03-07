/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

public class BugAI extends AI {
    static Random prng = new Random();
    
    static double nextSignedDouble() {
	return 2.0 * prng.nextDouble() - 1.0;
    }

    public void control(MeView me, AgentView[] agents, ThingView[] things) {
	if (things != null)
	    throw new Error("internal error: no things yet");
	System.out.println(me.a);
	me.a = 5;
	double dest = Math.atan(.5-me.y /.5-me.x);
	if (dest > me.t) 
	    me.vt -= .3;
	else if (dest < me.t)
	    me.vt += .3;
	//me.vt += Math.PI * nextSignedDouble() * dt;
    }
}
