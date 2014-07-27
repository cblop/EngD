// Judy agent
{ include("movement.asl") }  
{ include("emotions.asl") }  

/* Initial beliefs and rules */

name(judy).
direction(left).

/*
locations(offstageLeft, stageLeft, stageCentre, stageRight, offstageRight).

neighbours(X, Y) :- locations(X, Y, _, _, _) | locations(_, X, Y, _, _)
	| locations(_, _, X, Y, _) | locations(_, _, _, X, Y).
	
immLeft(X, Y) :- neighbours(X, Y).
immRight(X, Y) :- neighbours(Y, X).
neighbour(X, Y) :- immLeft(X, Y) | immRight(X, Y).

leftOf(X, Y) :- immLeft(X, Y) |
				locations(X, _, _, _, _, _) | locations(_, X, _, Y, _) |
				locations(_, _, _, _, _, Y).
*/

speed(medium).

waitTime(slow, 3000).
waitTime(medium, 2000).
waitTime(fast, 1000).

health(5).
energy(5).
~speaking.

valence(0).
arousal(0).
dominance(-1).

feeling(0, -1, tired, slow).
feeling(0, 0, pessimistic, slow).
feeling(0, 1, scared, fast).
feeling(-1, -1, sad, slow).
feeling(-1, 0, depressed, slow).
feeling(-1, 1, afraid, fast).
feeling(1, -1, peaceful, medium).
feeling(1, 0, compassionate, medium).
feeling(1, 1, empathetic, medium).

speech(X, happy) :- X == peaceful | X == compassionate | X == empathetic.
speech(X, worried) :- X == tired | X == pessimistic | X == depressed | X == sad.
speech(X, distressed) :- X == scared | X == afraid.

pos(offStageRight).
otherPos(offStageLeft).

/* Initial goals */
// Taunt Punch. Must first greet him and ask questions.
!changeMood.
!moveTo(stageRight).
!greet(punch).
!question(punch).
//!makeConfess(punch).

/* Plans */


// check emotion here
+otherPos(X) : pos(Y) & neighbour(X, Y) 
	<- !evade.

+otherPos(X) : pos(Y) & at(X, Y) 
	<- !evade.

/*
+!evade : pos(X) & otherPos(Y) & at(X, Y)
	<- !moveForward; // randomly
	   .random(R);
	   !increaseArousal(R). // randomly
*/

+!evade : true
	<- !moveForward; // randomly
	   .random(R);
	   !increaseArousal(R). // randomly
	

+!question(punch) : health(X) & X <= 0
	<- !die.
	
	
+!die
	<- .print("Judy is dead.");
	+dead;
	die("judy").
	//.send(narrative, achieve, endScene).


+!speak(X) : speaking
	<- .wait(100);
		!speak(X).

+!speak(X) : ~speaking
	<- ?speech(X, Y);
		say(Y).
	
+!question(punch) : health(X) & X > 0
	<- .print("Judy asks Punch a question.");
	.send(punch, achieve, question(judy));
	?emotion(E);
	!speak(E);
	?speed(S);
	anim(S);
	?waitTime(S, T);
	.wait(T);
	!question(punch).
	
+!greet(punch)
	<- .print("Hi, Punch");
	.send(punch, tell, greeting(judy));
	.wait(3000);
	!speak(greeting).
	

+!take_damage : health(X) & X <= 0
	<- !die.

+!take_damage
	<- ?health(X);
	.print("Judy's health is ", X, ".");
	.send(punch, tell, ouch(judy));
	-+health(X - 1).
	
	



	
