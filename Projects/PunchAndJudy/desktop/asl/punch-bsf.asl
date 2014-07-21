// Punch agent

/* Initial beliefs and rules */

energy(5).

~speaking.
emotions([[sulky, angry, furious], [annoyed, alert, vigilant], [vicious, malicious, excited]]).

emotion(alert).

valence(1).
arousal(1).
dominance(2).

/* Initial goals */
//!anger(0).
!say_hi.
//!moveTo(stageLeft).
!checkEmotion.

+!moveTo(X) : true
	<- move(X).

emotion(low, neg, def, moveTo(stageLeft)).

+!checkEmotion : true
	<- ?emotion(low, neg, def, Q);
	   !Q.
	   
	    

/* Plans */

+!getEmotion(I, J) : true
	<- .print("argh").

// looks like we'll have to do this the hard way
+!valenceChange(X) : X == 1
	<- ?emotion(Y);
	   //?valence(I);
	   //?arousal(J);
	   ?emotions([_, Z]);
	   -emotion(Y);
	   +emotion(annoyed).

+!say_hi : true
	<- say(happy);
		.wait(2000);
		!say_hi.

+!speak(X) : speaking
	<- .wait(100);
		!speak(X).

+!speak(X) : ~speaking
	<- say(X).
	
