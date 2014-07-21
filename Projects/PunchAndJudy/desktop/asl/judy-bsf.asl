// Judy agent

/* Initial beliefs and rules */

health(5).
energy(5).
~speaking.

emotions([[sad, depressed, afraid], [tired, pessimistic, scared], [peaceful, compassionate, empathetic]]).

emotion(pessimistic).

valence(1).
arousal(1).
dominance(0).

/* Initial goals */
// Taunt Punch. Must first greet him and ask questions.
!greet(punch).
!question(punch).
!moveTo(stageRight).
//!makeConfess(punch).

+!moveTo(X) : true
	<- move(X).

/* Plans */

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
	
	



	
