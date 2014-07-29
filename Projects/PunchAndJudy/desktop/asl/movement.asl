locations(stageLeft, stageCentre, stageRight).

immLeft(X, Y) :- locations(X, Y, _) | locations(_, X, Y).
immRight(X, Y) :- locations(Y, X, _) | locations(_, Y, X).

neighbour(X, Y) :- immLeft(X, Y) | immRight(X, Y).

leftOf(X, Y) :- immLeft (X, Y) | locations(X, _, Y).
rightOf(X, Y) :- immRight (X, Y) | locations(Y, _, X).
				
at(X, Y) :- X == Y.


rightOfOther :- pos(X) & otherPos(Y) & rightOf(X, Y).
leftOfOther :- pos(X) & otherPos(Y) & leftOf(X, Y).

otherBehind :- rightOfOther & direction(D) & D == right.
otherBehind :- leftOfOther & direction(D) & D == left.


+otherSpeaking(S) : true
	<- +speaking;
	   ?name(X).
	   //.print(X, ": Speaking percept added").

-otherSpeaking(_) : true
	<- -speaking;
	   ?name(X).
	   //.print(X, ": Speaking percept removed").
	   


+otherMoved(X) : true
	<-  -speaking;
		-+otherPos(X).

+!moveTo(X) : pos(Y)
	<- -+pos(X);
	   move(X).

+!moveForward : direction(left) & pos(stageLeft)
	<- !changeDirection.

+!moveForward : direction(right) & pos(stageRight)
	<- ?name(Y).

+!moveForward : direction(left) & pos(X) & not (X == stageLeft)
	<-  ?immLeft(Y, X);
	   !moveTo(Y).

+!moveForward : direction(right) & pos(X) & not (X == stageRight)
	<- ?immRight(Y, X);
	   !moveTo(Y).
	   
+!changeDirection : direction(X) & X == right
	<- anim(turn);
	   -+direction(left).

+!changeDirection : direction(X) & X == left
	<- anim(turn);
	   -+direction(right).
	
+!moveTowardsOther : otherBehind
	<- !changeDirection;
	   !moveForward.
	
+!moveTowardsOther : not otherBehind
	<- !moveForward.