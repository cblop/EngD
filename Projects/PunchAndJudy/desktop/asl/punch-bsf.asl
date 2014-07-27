// Punch agent

/* Initial beliefs and rules */

// remember:
// sulky == annoyed, angry == furious, alert == vigilant == excited, vicious == malicious

direction("RIGHT").


locations(offstageLeft, stageLeft, stageCentre, stageRight, offstageRight).

neighbours(X, Y) :- locations(X, Y, _, _, _) | locations(_, X, Y, _, _)
	| locations(_, _, X, Y, _) | locations(_, _, _, X, Y).
	
immLeft(X, Y) :- neighbours(X, Y).
immRight(X, Y) :- neighbours(Y, X).

leftOf(X, Y) :- immLeft(X, Y) |
				locations(X, _, _, _, _, _) | locations(_, X, _, Y, _) |
				locations(_, _, _, _, _, Y).
				
at(X, Y) :- X == Y.

rightOf(X, Y) :- leftOf(Y, X).

rightOfOther :- pos(X) & otherPos(Y) & rightOf(X, Y).
leftOfOther :- pos(X) & otherPos(Y) & leftOf(X, Y).

otherBehind :- rightOfOther & direction("RIGHT").
otherBehind :- leftOfOther & direction("LEFT").

feeling(0, -1, annoyed).
feeling(0, 0, alert).
feeling(0, 1, vigilant).
feeling(-1, -1, sulky).
feeling(-1, 0, angry).
feeling(-1, 1, furious).
feeling(1, -1, vicious).
feeling(1, 0, malicious).
feeling(1, 1, excited).

emotion(alert).

/*
speed(slow) :- emotion(sulky).
speed(slow) :- emotion(annoyed).
speed(medium) :- emotion(alert).
speed(medium) :- emotion(vigilant).
speed(medium) :- emotion(excited).
speed(fast) :- emotion(angry).
speed(fast) :- emotion(furious).
speed(fast) :- emotion(vicious).
speed(fast) :- emotion(malicious).
*/

speed(fast).

waitTime(slow, 3000).
waitTime(medium, 2000).
waitTime(fast, 1000).

energy(5).

interruption.

pos(offStageLeft).
otherPos(offStageRight).


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
	<- .print("Punch thinks Judy is at ", X);
	   ?leftOf(Y, Z);
	   .print("Alignment: ", Y, ", ", Z).
	   
+otherMoved(X) : true
	<- ?otherPos(Y);
	   -otherPos(Y);
	   +otherPos(X).


+!boast : true
	<- .print("Punch is boasting");
	   say(happy).

+!dominate : ~interruption
	<- ?speed(X);
		anim(X);
		?waitTime(X, Y);
		.wait(Y);
	    !boast;
	   .wait(Y);
	   !increaseValence;
	   // goal achieved?
	   !dominate.

+!dominate : interruption
	<- ?speed(X);
		anim(X);
		?waitTime(X, Y);
		.print("Wait time: ", Y);
	   .wait(Y);
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
		
	
+!changeDirection : direction(X) & X == "RIGHT"
	<- anim(turn);
	   -direction("RIGHT");
	   +direction("LEFT").

+!changeDirection : direction(X) & X == "LEFT"
	<- anim(turn);
	   -direction("LEFT");
	   +direction("RIGHT").
	
// need to compare positions to determine whether to turn
+!moveTowardsOther : otherBehind
	<- .print("behind");
	   !changeDirection;
	   !moveForward.
	
+!moveTowardsOther : not otherBehind
	<- .print("not behind");
	!moveForward.
	   
	   
+!moveForward : direction("LEFT") & pos(offstageLeft)
	<- !changeDirection.

+!moveForward : direction("RIGHT") & pos(offstageRight)
	<- !changeDirection.

+!moveForward : direction("LEFT")
	<-  ?pos(Y);
		?immLeft(Z, Y);
	   .print(Z);
	   !moveTo(Z).

+!moveForward : direction("RIGHT")
	<- ?pos(Y);
	   ?immRight(Z, Y);
	   !moveTo(Z).
	   

// check the other isn't dead
+!hitOther : true
	<- hit.


+!moveTo(X) : pos(Y)
	<- -pos(Y);
	   +pos(X);
	   move(X).
	   
+!changeMood : valence(X) & arousal(Y) & emotion(W)
	<- ?feeling(X, Y, Z);
	   -emotion(W);
	   .print("Punch is feeling ", Z);
	   +emotion(Z).

+!changeMood : valence(X) & arousal(Y)
	<- ?feeling(X, Y, Z);
	   +emotion(Z).
	   

+valence(X) : true
	<- !changeMood.

+arousal(X) : true
	<- !changeMood.



+!say_hi : true
	<- say(greeting);
		?speed(X);
		?waitTime(X, Y);
	   .wait(Y).

+!speak(X) : speaking
	<- .wait(100);
		!speak(X).

+!speak(X) : ~speaking
	<- say(X).
	
