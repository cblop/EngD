// Punch agent

{ include("movement.asl") }  
{ include("emotions.asl") }  

/* Initial beliefs and rules */

// remember:
// sulky == annoyed, angry == furious, alert == vigilant == excited, vicious == malicious

name(punch).
direction(right).


/*
locations(offstageLeft, stageLeft, stageCentre, stageRight, offstageRight).

neighbours(X, Y) :- locations(X, Y, _, _, _) | locations(_, X, Y, _, _)
	| locations(_, _, X, Y, _) | locations(_, _, _, X, Y).
	
immLeft(X, Y) :- neighbours(X, Y).
immRight(X, Y) :- neighbours(Y, X).

leftOf(X, Y) :- immLeft(X, Y) |
				locations(X, _, _, _, _, _) | locations(_, X, _, Y, _) |
				locations(_, _, _, _, _, Y).
*/


feeling(0, -1, annoyed, slow).
feeling(0, 0, alert, slow).
feeling(0, 1, vigilant, medium).
feeling(-1, -1, sulky, medium).
feeling(-1, 0, angry, fast).
feeling(-1, 1, furious, fast).
feeling(1, -1, vicious, fast).
feeling(1, 0, malicious, fast).
feeling(1, 1, excited, medium).

emotion(alert).

speed(medium).

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
	   .random(R);
	   !increaseValence(R);
	   // goal achieved?
	   !dominate.

+!dominate : interruption
	<- ?speed(X);
		anim(X);
		?waitTime(X, Y);
		.print("Wait time: ", Y);
	   .wait(Y);
	   !silenceOther;
	   .random(R);
	   !decreaseValence(R);
	   !dominate.
	
+!silenceOther : emotion(sulky) | emotion(annoyed)
	<- !changeDirection; // want to do this with a probability
	   //say(sulky).
	   say(annoyed).

+!silenceOther : emotion(angry) | emotion(furious)
	<- !chase; // chase
	   say(angry).

+!silenceOther : emotion(alert) | emotion(vigilant) | emotion(excited)
	<- .random(R);
	   !pace(R); // probability
	   //say(excited).
	   say(happy).

+!silenceOther : emotion(vicious) | emotion(malicious)
	<- .random(R);
	   !pace(R);
	   //say(vicious).
	   say(angry).
	   
	   
+!pace(R) : R >= 0.5
	<- !changeDirection.

+!pace(R) : R < 0.5
	<- !moveForward; // randomly
	   .random(S);
	   !increaseArousal(S). // randomly
	   
+!chase : pos(X) & otherPos(Y) & not (X == Y)
	<- .random(R);
		!increaseArousal(R);
		!moveTowardsOther.

+!chase : pos(X) & otherPos(Y) & X == Y
	<- .random(R);
		!increaseArousal(R);
		!hitOther.

// check the other isn't dead
+!hitOther : true
	<- hit.

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
	
