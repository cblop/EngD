// Punch agent

/* Initial beliefs and rules */

// remember:
// sulky == annoyed, angry == furious, alert == vigilant == excited, vicious == malicious

leftOf(offstageLeft, offstageLeft).
leftOf(offstageLeft, stageLeft).

rightOf(stageLeft, offstageLeft).
leftOf(stageLeft, stageCentre).

rightOf(stageCentre, stageLeft).
leftOf(stageCentre, stageRight).

rightOf(stageRight, stageCentre).
leftOf(stageRight, offstageRight).

rightOf(offstageRight, stageRight).
rightOf(offstageRight, offstageRight).


feeling(0, -1, annoyed).
feeling(0, 0, alert).
feeling(0, 1, vigilant).
feeling(-1, -1, sulky).
feeling(-1, 0, angry).
feeling(-1, 1, furious).
feeling(1, -1, vicious).
feeling(1, 0, malicious).
feeling(1, 1, excited).

direction("RIGHT").
energy(5).

interruption.

valence(0).
arousal(0).
dominance(1).

/* Initial goals */
!changeMood.
!moveTo(stageLeft).
!say_hi.
!dominate.


/* Plans */

// change: different moods

+otherPos(X) : true
	<- .print("Punch thinks Judy is at ", X).

+!boast : true
	<- .print("Punch is boasting");
	   say(happy).

+!dominate : ~interruption
	<- .wait(2000);
	    !boast;
	   .wait(2000);
	   !increaseValence;
	   // goal achieved?
	   !dominate.

+!dominate : interruption
	<- .wait(2000);
	   !silenceOther;
	   !decreaseValence;
	   !dominate.
	
+!silenceOther : emotion(sulky) | emotion(annoyed)
	<- !changeDirection; // want to do this with a probability
	   //say(sulky).
	   say(angry).

+!silenceOther : emotion(angry) | emotion(furious)
	<- !chase; // chase
	   say(angry).

+!silenceOther : emotion(alert) | emotion(vigilant) | emotion(excited)
	<- !pace; // probability
	   //say(excited).
	   say(happy).

+!silenceOther : emotion(vicious) | emotion(malicious)
	<- !pace;
	   //say(vicious).
	   say(angry).
	   
+!increaseValence : valence(X) & X < 1
	<- -valence(X);
		+valence(X + 1).

+!increaseValence : valence(X) & X >= 1
	<- pass.

+!decreaseValence : valence(X) & X > -1
	<- ?valence(X);
		-valence(X);
		+valence(X - 1).

+!decreaseValence : valence(X) & X <= -1
	<- pass.

+!increaseArousal : arousal(X) & X < 1
	<- ?arousal(X);
		-arousal(X);
		+arousal(X + 1).

+!decreaseArousal : arousal(X) & X > -1
	<- ?arousal(X);
		-arousal(X);
		+arousal(X - 1).

+!increaseArousal : arousal(X) & X >= 1
	<- pass.

+!decreaseArousal : arousal(X) & X <= -1
	<- pass.
	   
+!pace : true
	<- !changeDirection; // randomly
	   !moveForward; // randomly
	   !increaseArousal. // randomly
	   
+!chase : pos(X) & otherPos(Y) & not (X == Y)
	<- !increaseArousal;
		!moveTowardsOther.

+!chase : pos(X) & otherPos(Y) & X == Y
	<- !increaseArousal;
		!hitOther.
		
+!chase : true
	<- !increaseArousal;
	   !moveTowardsOther.
	
+!changeDirection : direction(X) & X == "RIGHT"
	<- anim(turn);
	   -direction("RIGHT");
	   +direction("LEFT").

+!changeDirection : direction(X) & X == "LEFT"
	<- anim(turn);
	   -direction("LEFT");
	   +direction("RIGHT").
	
// need to compare positions to determine whether to turn
+!moveTowardsOther : pos(X) & otherPos(Y) & rightOf(X, Y) & direction(Z) & Z == "RIGHT"
	<- !changeDirection;
	   !moveForward.
	
+!moveTowardsOther : pos(X) & otherPos(Y) & rightOf(Y, X) & direction(Z) & Z == "LEFT"
	<- !changeDirection;
	   !moveForward.

+!moveTowardsOther : pos(X) & otherPos(Y) & rightOf(X, Y) & direction(Z) & Z == "RIGHT"
	<- !moveForward.

+!moveTowardsOther : pos(X) & otherPos(Y) & rightOf(Y, X) & direction(Z) & Z == "LEFT"
	<- !moveForward.
	   

+!moveForward : direction(X) & X == "LEFT"
	<-  ?pos(Y);
		?leftOf(Z, Y);
	   .print(Z);
	   !moveTo(Z).

+!moveForward : direction(X) & X == "RIGHT"
	<- ?pos(Y);
	   ?rightOf(Z, Y);
	   .print(Z);
	   !moveTo(Z).
	   

// check the other isn't dead
+!hitOther : true
	<- hit.

+!moveTo(X) : true
	<- +pos(X);
	   move(X).

+!moveTo(X) : pos(Y)
	<- -pos(Y);
	   +pos(X);
	   move(X).
	   
+!changeMood : valence(X) & arousal(Y) & emotion(W)
	<- ?feeling(X, Y, Z);
	   -emotion(W);
	   .print("Punch is feeling ", Z);
	   +emotion(Z);
	   !setSpeed.

+!changeMood : valence(X) & arousal(Y)
	<- ?feeling(X, Y, Z);
	   +emotion(Z);
	   !setSpeed.
	   
+!setSpeed : emotion(sulky) | emotion(annoyed) 
	<- speed(slow).

+!setSpeed : emotion(alert) | emotion(vigilant) | emotion(excited)
	<- speed(medium).

+!setSpeed : emotion(angry) | emotion(furious) | emotion(vicious) | emotion(malicious)
	<- speed(fast).
	
+!setSpeed : true
	<- pass.

+valence(X) : true
	<- !changeMood.

+arousal(X) : true
	<- !changeMood.

/*
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
	   */


+!say_hi : true
	<- say(greeting);
	   .wait(3000).

+!speak(X) : speaking
	<- .wait(100);
		!speak(X).

+!speak(X) : ~speaking
	<- say(X).
	
