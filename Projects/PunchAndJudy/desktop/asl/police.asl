// Police agent
{ include("movement.asl") }  
{ include("emotions.asl") }  
{ include("dialogue.asl") }  

/* Initial beliefs and rules */

name(police).
direction(left).
skit(free).

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

valence(1).
arousal(1).
dominance(1).

feeling(0, -1, annoyed, slow).
feeling(0, 0, alert, slow).
feeling(0, 1, vigilant, medium).
feeling(-1, -1, sulky, medium).
feeling(-1, 0, angry, fast).
feeling(-1, 1, furious, fast).
feeling(1, -1, vicious, fast).
feeling(1, 0, malicious, fast).
feeling(1, 1, excited, medium).

speech(greeting, greeting).
speech(search, search).
speech(X, happy) :- X == alert | X == vigilant | X == excited.
speech(X, annoyed) :- X == sulky | X == annoyed.
speech(X, angry) :- X == angry | X == furious | X == vicious | X == malicious.

pos(offStageRight).
otherPos(offStageLeft).

/* Initial goals */
// Taunt Punch. Must first greet him and ask questions.
//!makeConfess(punch).

/* Plans */
+scene(X) : _
	<- -+currentScene(X).

+!resetScene : direction(left)
	<- -+valence(1);
	   -+arousal(1);
	   -+dominance(-1);
	   -+health(5);
	   -+skit(free);
	   anim(rest);
	   !changeMood.

+!resetScene : direction(right)
	<- -+valence(1);
	   -+arousal(1);
	   -+dominance(-1);
	   -+health(5);
	   -+skit(free);
	   !changeDirection;
	   anim(rest);
	   !changeMood.

+currentScene(police) : _
	<- !resetScene;
        !moveTo(stageCentre);
        .wait(2000);
        -+currentSkit(search).

// could generalise this to X
// check the emotion
+currentSkit(search) : _
	<- !lookForPunch.
		
-!g[.print("Fail plan triggered")].

+currentSkit(search) : audienceYes
	<-  anim(front);
		!speak(search);
		.wait(2000);
		-audienceYes;
		!lookForPunch.
		
+!noiseDetected : _
	<- _.

		
+input(_) : _
	<- -+audienceYes;
		.print("AUDIENCE SAYS YES").
		
+!lookForPunch : canSeeOther
	<- _.
		
+!lookForPunch : not canSeeOther
	<-  anim(front);
		!speak(search);
		.wait("+audienceYes", 5000);
		anim(rest);
		!speak(search);
		.wait(2000);
		?opposite(X);
		!moveTo(X);
		.wait(2000);
		!changeDirection;
		.wait(3000);
		.random(R);
		.random(S);
		!decreaseValence(R);
		!increaseArousal(S);
		!lookForPunch.

-!lookForPunch : not emotion(sulky)
	<- .random(R);
		.random(S);
		!decreaseValence(0.1);
		!decreaseArousal(0.1);
		anim(rest);
		.wait(2000);
		!changeDirection;
		.wait(2000);
		!lookForPunch.
		
-!lookForPunch : emotion(sulky)
	<- !moveTo(offstageRight).


-currentScene(_) : _
	<- !moveTo(offstageRight);
		.wait(2000);
		!resetScene.


// check emotion here

+otherPos(X) : true
	<- !evade.
	
+otherAction(X) : X == hit
	<- .print("Police is getting hit");
		!takeDamage.


/*
+!evade : pos(X) & otherPos(Y) & at(X, Y)
	<- !moveForward; // randomly
	   .random(R);
	   !increaseArousal(R). // randomly
*/

+!evade : pos(X) & otherPos(Y) & neighbour(X, Y)
	<- !moveForward; // randomly
	   .random(R);
	   !decreaseValence(R);
	   !increaseArousal(R). // randomly

+!evade : pos(X) & otherPos(Y) & at(X, Y)
	<- !moveForward; // randomly
	   .random(R);
	   !decreaseValence(R). // randomly
	   
+!evade : pos(X) & otherPos(Y) & not neighbour(X, Y)
	<- pass.

+!evade : pos(X) & otherPos(Y) & not at(X, Y)
	<- pass.
	

+!question(punch) : health(X) & X <= 0
	<- !die.
	
	
+!die
	<- .print("Police is dead.");
	+dead;
	anim(dead);
	.wait(2000);
	-currentScene(_).
	//.send(narrative, achieve, endScene).


+!speak(X) : speaking
	<- .wait(300);
		//.print("Punch is speaking");
		!speak(X).

+!speak(X) : not speaking
	<- ?speech(X, Y);
		say(Y).
	
+!question(punch) : health(X) & X > 0
	<- !evade;
	?emotion(E);
	!speak(E);
	?speed(S);
	anim(S);
	?waitTime(S, T);
	.wait(T);
	!question(punch).
	
+!greet(punch)
	<- .print("Hi, Punch");
	.wait(3000);
	!speak(greeting).
	

+!takeDamage : health(X) & X <= 0
	<- !die.

+!takeDamage : health(X) & X > 0
	<- ?health(X);
	.print("Police's health is ", X, ".");
	-+health(X - 1).
	
	



	
