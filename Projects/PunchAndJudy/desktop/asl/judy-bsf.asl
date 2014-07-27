// Judy agent

/* Initial beliefs and rules */

direction("LEFT").

locations(offstageLeft, stageLeft, stageCentre, stageRight, offstageRight).

neighbours(X, Y) :- locations(X, Y, _, _, _) | locations(_, X, Y, _, _)
	| locations(_, _, X, Y, _) | locations(_, _, _, X, Y).
	
immLeft(X, Y) :- neighbours(X, Y).
immRight(X, Y) :- neighbours(Y, X).
neighbour(X, Y) :- immLeft(X, Y) | immRight(X, Y).

leftOf(X, Y) :- immLeft(X, Y) |
				locations(X, _, _, _, _, _) | locations(_, X, _, Y, _) |
				locations(_, _, _, _, _, Y).
				
at(X, Y) :- X == Y.

rightOf(X, Y) :- leftOf(Y, X).

rightOfOther :- pos(X) & otherPos(Y) & rightOf(X, Y).
leftOfOther :- pos(X) & otherPos(Y) & leftOf(X, Y).

otherBehind :- rightOfOther & direction("RIGHT").
otherBehind :- leftOfOther & direction("LEFT").

health(5).
energy(5).
~speaking.

valence(0).
arousal(0).
dominance(-1).

feeling(0, -1, tired).
feeling(0, 0, pessimistic).
feeling(0, 1, scared).
feeling(-1, -1, sad).
feeling(-1, 0, depressed).
feeling(-1, 1, afraid).
feeling(1, -1, peaceful).
feeling(1, 0, compassionate).
feeling(1, 1, empathetic).

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

+!moveTo(X) : pos(Y)
	<- -pos(Y);
	   +pos(X);
	   move(X).

+otherMoved(X) : true
	<- ?otherPos(Y);
	   -otherPos(Y);
	   +otherPos(X).
	   
// check emotion here
+otherPos(X) : pos(Y) & neighbour(X, Y) 
	<- !evade.

+otherPos(X) : pos(Y) & at(X, Y) 
	<- !evade.

+!evade : pos(X) & otherPos(Y) & at(X, Y)
	<- !moveForward; // randomly
	   !increaseArousal. // randomly

+!evade : otherBehind
	<- !moveForward; // randomly
	   !increaseArousal. // randomly
	
+!evade : not otherBehind
	<- !changeDirection; // randomly
	   !moveForward; // randomly
	   !increaseArousal. // randomly
	 
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


+!changeDirection : direction(X) & X == "RIGHT"
	<- anim(turn);
	   -direction("RIGHT");
	   +direction("LEFT").

+!changeDirection : direction(X) & X == "LEFT"
	<- anim(turn);
	   -direction("LEFT");
	   +direction("RIGHT").

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

+!changeMood : valence(X) & arousal(Y) & emotion(W)
	<- ?feeling(X, Y, Z);
	   -emotion(W);
	   .print("Judy is feeling ", Z);
	   +emotion(Z).

+!changeMood : valence(X) & arousal(Y)
	<- ?feeling(X, Y, Z);
	   +emotion(Z).

+valence(X) : true
	<- !changeMood.

+arousal(X) : true
	<- !changeMood.


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
	<- say(X).
	
+!question(punch) : health(X) & X > 0
	<- .print("Judy asks Punch a question.");
	.send(punch, achieve, question(judy));
	!speak(happy);
	.wait(3000);
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
	-health(X);
	+health(X - 1).
	
	



	
