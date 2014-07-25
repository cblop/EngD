// Punch agent

/* Initial beliefs and rules */

// remember:
// sulky == annoyed, angry == furious, alert == vigilant == excited, vicious == malicious

rightOf(_, offstageLeft).
rightOf(stageCentre, stageLeft).
rightOf(stageRight, stageCentre).
rightOf(offstageRight, _).

direction("RIGHT").
energy(5).

~interruption.

valence(0).
arousal(0).
dominance(1).

/* Initial goals */
!say_hi.
!getEmotion.
!dominate.
!moveTo(stageLeft).


/* Plans */

// change: different moods
+!boast : true
	<- say(happy).

+!dominate : ~interruption
	<- !boast;
	   .wait(2000);
	   // goal achieved?
	   !dominate.

+!dominate : interruption
	<- .wait(2000);
	   !silenceOther.
	
+!silenceOther : emotion(sulky) | emotion(annoyed)
	<- move(turn); // want to do this with a probability
	   say(sulky).

+!silenceOther : emotion(angry) | emotion(furious)
	<- !chase; // chase
	   say(angry).

+!silenceOther : emotion(alert) | emotion(vigilant) | emotion(excited)
	<- !pace; // probability
	   say(excited).

+!silenceOther : emotion(vicious) | emotion(malicious)
	<- !pace;
	   say(vicious).
	   
+!pace : true
	<- !changeDirection; // randomly
	   !moveForward. // randomly
	   
+!chase : pos(X) & otherPos(Y) & not X == Y
	<- !moveTowardsOther.

+!chase : pos(X) & otherPos(Y) & X == Y
	<- !hitOther.
	
+!changeDirection : direction(X) & X == "RIGHT"
	<- move(turn);
	   -direction("RIGHT");
	   +direction("LEFT").

+!changeDirection : direction(X) & X == "LEFT"
	<- move(turn);
	   -direction("LEFT");
	   +direction("RIGHT").
	
// need to compare positions to determine whether to turn
+!moveTowardsOther : rightOf(pos(X), otherPos(Y)) & direction(Z) & Z == "RIGHT"
	<- !changeDirection;
	   !moveForward.
	
+!moveTowardsOther : rightOf(otherPos(Y), pos(X)) & direction(Z) & Z == "LEFT"
	<- !changeDirection;
	   !moveForward.

+!moveForward : direction(X) == "RIGHT"
	<- ?rightOf(pos(Y), Z);
	   move(Z).

+!moveForward : direction(X) == "LEFT"
	<- ?rightOf(Z, pos(Y));
	   move(Z).

// check the other isn't dead
+!hitOther : true
	<- hit.

+!moveTo(X) : true
	<- +pos(X);
	   move(X).


+valence(X) : true
	<- !getEmotion.

+arousal(X) : true
	<- !getEmotion.

+!getEmotion : valence(X) & X == -1 & arousal(Y) & Y == -1
	<- +emotion(sulky);
	   setSpeed(slow).

+!getEmotion : valence(X) & X == 0 & arousal(Y) & Y == -1
	<- +emotion(annoyed);
	   setSpeed(slow).

+!getEmotion : valence(X) & X == -1 & arousal(Y) & Y == 0
	<- +emotion(angry);
	   setSpeed(fast).

+!getEmotion : valence(X) & X == -1 & arousal(Y) & Y == 1
	<- +emotion(furious);
	   setSpeed(fast).

+!getEmotion : valence(X) & X == 0 & arousal(Y) & Y == 0
	<- +emotion(alert);
	   setSpeed(medium).

+!getEmotion : valence(X) & X == 0 & arousal(Y) & Y == 1
	<- +emotion(vigilant);
	   setSpeed(medium).
	

+!getEmotion : valence(X) & X == 1 & arousal(Y) & Y == 1
	<- +emotion(excited);
	   setSpeed(medium).
	
+!getEmotion : valence(X) & X == 1 & arousal(Y) & Y == -1
	<- +emotion(vicious);
	   setSpeed(fast).

+!getEmotion : valence(X) & X == 1 & arousal(Y) & Y == 0
	<- +emotion(malicious);
	   setSpeed(fast).


+!say_hi : true
	<- say(happy);
		.wait(2000);
		!say_hi.

+!speak(X) : speaking
	<- .wait(100);
		!speak(X).

+!speak(X) : ~speaking
	<- say(X).
	
