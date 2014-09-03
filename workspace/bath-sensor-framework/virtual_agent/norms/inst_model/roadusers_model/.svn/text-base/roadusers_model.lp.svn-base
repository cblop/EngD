%
% Domain declarations for queue
%
type(normal).
type(disable).
agent(centralMember1).
agent(centralMember2).
agent(centralMember3).
agent(centralMember4).
%
% -------------------------------PART 1-------------------------------
%
%
% Standard prelude for queue
%
% instant ordering
% removed for MDV 20130117
%%% before(I1,I2):- next(I1,I2),instant(I1), instant(I2).
%%% before(I1,I3):- before(I1,I2),before(I2,I3),
%%%    instant(I1),instant(I2),instant(I3).
% fluent rules
holdsat(P,In,J):- holdsat(P,In,I),not terminated(P,In,I),
    next(I,J),fluent(P, In),instant(I),instant(J), inst(In).
holdsat(P,In,J):- initiated(P,In,I),next(I,J),
    ifluent(P, In),instant(I),instant(J), inst(In).
holdsat(P,In,J):- initiated(P,In,I),next(I,J), 
    oblfluent(P, In),instant(I),instant(J), inst(In).
holdsat(P,In,J):- initiated(P,In,I),next(I,J), 
    nifluent(P, In),instant(I),instant(J), inst(In).
% all observed events occur
occurred(E,In,I):- evtype(E,In,ex),observed(E,In,I),instant(I), inst(In).
% produces null for unknown events 
occurred(null,In,I) :- not evtype(E,In,ex), observed(E,In,I), 
    instant(I), inst(In). 
% produces gap warning for unknown events 
unknown(E, In, I) :- not evtype(E,In,ex), observed(E,In,I), 
    instant(I), inst(In). 
warninggap(In, I) :- unknown(E,In,I), inst(In), instant(I). 
% a violation occurs for each non-permitted action 
occurred(viol(E),In,I):-
    occurred(E,In,I),
    evtype(E,In,ex),
    not holdsat(perm(E),In,I),
    holdsat(live(In),In,I),evinst(E,In),
    event(E),instant(I),event(viol(E)),inst(In).
occurred(viol(E),In,I):-
    occurred(E,In,I),
    evtype(E,In,inst),
    not holdsat(perm(E),In,I),
    event(E),instant(I),event(viol(E)), inst(In).
% needed until I tidy up some of the constraint generation 
true.
%
% Rules for Institution queue
%
  ifluent(live(queue), queue).
  fluent(live(queue), queue).
  inst(queue).
%
% Constraints for obserable events depending on mode option
%
%%  mode DEFAULT is chosen:
{observed(E,In,J)}:- evtype(E,In,ex),instant(J), not final(J), inst(In).
:- observed(E,In,J),observed(F,In,J),instant(J),evtype(E,In,ex),
evtype(F,In,ex), E!=F,inst(In). 
obs(In,I):- observed(E,In,I),evtype(E,In,ex),instant(I),inst(In).
         :- not obs(In,I), not final(I), instant(I), inst(In).

%
% The following types were declared:
%
% Type
% TargetAgent
% Agent
%
% Exogenous events
% Event: flashLights (type: ex)
  event(flashLights(Agent0)) :- agent(Agent0).
  evtype(flashLights(Agent0),queue,ex) :- agent(Agent0).
  evinst(flashLights(Agent0),queue) :- agent(Agent0).
  ifluent(perm(flashLights(Agent0)), queue) :- agent(Agent0).
  fluent(perm(flashLights(Agent0)), queue) :- agent(Agent0).
  event(viol(flashLights(Agent0))) :- agent(Agent0).
  evtype(viol(flashLights(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(flashLights(Agent0)),queue) :- agent(Agent0).
% Event: newArrival (type: ex)
  event(newArrival(Agent0,Type1)) :- agent(Agent0),type(Type1).
  evtype(newArrival(Agent0,Type1),queue,ex) :- agent(Agent0),type(Type1).
  evinst(newArrival(Agent0,Type1),queue) :- agent(Agent0),type(Type1).
  ifluent(perm(newArrival(Agent0,Type1)), queue) :- agent(Agent0),type(Type1).
  fluent(perm(newArrival(Agent0,Type1)), queue) :- agent(Agent0),type(Type1).
  event(viol(newArrival(Agent0,Type1))) :- agent(Agent0),type(Type1).
  evtype(viol(newArrival(Agent0,Type1)),queue,viol) :- agent(Agent0),type(Type1).
  evinst(viol(newArrival(Agent0,Type1)),queue) :- agent(Agent0),type(Type1).
% Event: queueLast (type: ex)
  event(queueLast(Agent0)) :- agent(Agent0).
  evtype(queueLast(Agent0),queue,ex) :- agent(Agent0).
  evinst(queueLast(Agent0),queue) :- agent(Agent0).
  ifluent(perm(queueLast(Agent0)), queue) :- agent(Agent0).
  fluent(perm(queueLast(Agent0)), queue) :- agent(Agent0).
  event(viol(queueLast(Agent0))) :- agent(Agent0).
  evtype(viol(queueLast(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(queueLast(Agent0)),queue) :- agent(Agent0).
% Event: yield (type: ex)
  event(yield(Agent0)) :- agent(Agent0).
  evtype(yield(Agent0),queue,ex) :- agent(Agent0).
  evinst(yield(Agent0),queue) :- agent(Agent0).
  ifluent(perm(yield(Agent0)), queue) :- agent(Agent0).
  fluent(perm(yield(Agent0)), queue) :- agent(Agent0).
  event(viol(yield(Agent0))) :- agent(Agent0).
  evtype(viol(yield(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(yield(Agent0)),queue) :- agent(Agent0).
% Event: changeLane (type: ex)
  event(changeLane(Agent0)) :- agent(Agent0).
  evtype(changeLane(Agent0),queue,ex) :- agent(Agent0).
  evinst(changeLane(Agent0),queue) :- agent(Agent0).
  ifluent(perm(changeLane(Agent0)), queue) :- agent(Agent0).
  fluent(perm(changeLane(Agent0)), queue) :- agent(Agent0).
  event(viol(changeLane(Agent0))) :- agent(Agent0).
  evtype(viol(changeLane(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(changeLane(Agent0)),queue) :- agent(Agent0).
% Event: detectDisable (type: ex)
  event(detectDisable(Agent0)) :- agent(Agent0).
  evtype(detectDisable(Agent0),queue,ex) :- agent(Agent0).
  evinst(detectDisable(Agent0),queue) :- agent(Agent0).
  ifluent(perm(detectDisable(Agent0)), queue) :- agent(Agent0).
  fluent(perm(detectDisable(Agent0)), queue) :- agent(Agent0).
  event(viol(detectDisable(Agent0))) :- agent(Agent0).
  evtype(viol(detectDisable(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(detectDisable(Agent0)),queue) :- agent(Agent0).
% Event: deadline (type: ex)
  event(deadline) :- true.
  evtype(deadline,queue,ex) :- true.
  evinst(deadline,queue) :- true.
  ifluent(perm(deadline), queue) :- true.
  fluent(perm(deadline), queue) :- true.
  event(viol(deadline)) :- true.
  evtype(viol(deadline),queue,viol) :- true.
  evinst(viol(deadline),queue) :- true.
%
% null event for unknown events
% Event: null (type: ex)
  event(null).
  evtype(null,queue,ex).
  evinst(null,queue).
  ifluent(perm(null), queue).
  fluent(perm(null), queue).
  event(viol(null)).
  evtype(viol(null),queue,viol).
  evinst(viol(null),queue).
% Institutional events
% Event: iniOblChangeLane (type: in)
  event(iniOblChangeLane(Agent0)) :- agent(Agent0).
  evtype(iniOblChangeLane(Agent0),queue,inst) :- agent(Agent0).
  evinst(iniOblChangeLane(Agent0),queue) :- agent(Agent0).
  ifluent(pow(queue,iniOblChangeLane(Agent0)),queue) :- agent(Agent0).
  ifluent(perm(iniOblChangeLane(Agent0)),queue) :- agent(Agent0).
  fluent(pow(queue,iniOblChangeLane(Agent0)),queue) :- agent(Agent0).
  fluent(perm(iniOblChangeLane(Agent0)),queue) :- agent(Agent0).
  event(viol(iniOblChangeLane(Agent0))) :- agent(Agent0).
  evtype(viol(iniOblChangeLane(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(iniOblChangeLane(Agent0)),queue) :- agent(Agent0).
% Event: iniOblQueueLast (type: in)
  event(iniOblQueueLast(Agent0,Type1)) :- agent(Agent0),type(Type1).
  evtype(iniOblQueueLast(Agent0,Type1),queue,inst) :- agent(Agent0),type(Type1).
  evinst(iniOblQueueLast(Agent0,Type1),queue) :- agent(Agent0),type(Type1).
  ifluent(pow(queue,iniOblQueueLast(Agent0,Type1)),queue) :- agent(Agent0),type(Type1).
  ifluent(perm(iniOblQueueLast(Agent0,Type1)),queue) :- agent(Agent0),type(Type1).
  fluent(pow(queue,iniOblQueueLast(Agent0,Type1)),queue) :- agent(Agent0),type(Type1).
  fluent(perm(iniOblQueueLast(Agent0,Type1)),queue) :- agent(Agent0),type(Type1).
  event(viol(iniOblQueueLast(Agent0,Type1))) :- agent(Agent0),type(Type1).
  evtype(viol(iniOblQueueLast(Agent0,Type1)),queue,viol) :- agent(Agent0),type(Type1).
  evinst(viol(iniOblQueueLast(Agent0,Type1)),queue) :- agent(Agent0),type(Type1).
% Event: iniOblYield (type: in)
  event(iniOblYield(Agent0)) :- agent(Agent0).
  evtype(iniOblYield(Agent0),queue,inst) :- agent(Agent0).
  evinst(iniOblYield(Agent0),queue) :- agent(Agent0).
  ifluent(pow(queue,iniOblYield(Agent0)),queue) :- agent(Agent0).
  ifluent(perm(iniOblYield(Agent0)),queue) :- agent(Agent0).
  fluent(pow(queue,iniOblYield(Agent0)),queue) :- agent(Agent0).
  fluent(perm(iniOblYield(Agent0)),queue) :- agent(Agent0).
  event(viol(iniOblYield(Agent0))) :- agent(Agent0).
  evtype(viol(iniOblYield(Agent0)),queue,viol) :- agent(Agent0).
  evinst(viol(iniOblYield(Agent0)),queue) :- agent(Agent0).
%
% Violation events
%
% Event: vioYield (type: in)
  event(vioYield(Agent0)) :- agent(Agent0).
  evtype(vioYield(Agent0),queue,viol) :- agent(Agent0).
  evinst(vioYield(Agent0),queue) :- agent(Agent0).
% Event: vioQueue (type: in)
  event(vioQueue(Agent0)) :- agent(Agent0).
  evtype(vioQueue(Agent0),queue,viol) :- agent(Agent0).
  evinst(vioQueue(Agent0),queue) :- agent(Agent0).
%
% Creation events
%
% Event: createQueue (type: ex)
  event(createQueue) :- true.
  evtype(createQueue,queue,ex) :- true.
  evinst(createQueue,queue) :- true.
  ifluent(perm(createQueue),queue) :- true.
  fluent(perm(createQueue),queue) :- true.
  event(viol(createQueue)) :- true.
  evtype(viol(createQueue),queue,viol) :- true.
  evinst(viol(createQueue),queue) :- true.
%
% Dissolution events
%
%
% dissolve events
%
%
% inertial fluents
%
%
% noninertial fluents
%
%
% violation fluents (to be implemented)

%
% obligation fluents
%
oblfluent(obl(queueLast(Agent0),deadline,vioQueue(Agent1)), queue) :-
   event(queueLast(Agent0)),
   event(deadline),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
fluent(obl(queueLast(Agent0),deadline,vioQueue(Agent1)), queue) :-
   event(queueLast(Agent0)),
   event(deadline),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
terminated(obl(queueLast(Agent0),deadline,vioQueue(Agent1)),queue,I) :-
   event(queueLast(Agent0)), occurred(queueLast(Agent0),queue,I),
   event(deadline),
   holdsat(obl(queueLast(Agent0),deadline,vioQueue(Agent1)),queue,I),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
terminated(obl(queueLast(Agent0),deadline,vioQueue(Agent1)),queue,I) :-
   event(queueLast(Agent0)), 
   event(deadline), occurred(deadline,queue,I),
   holdsat(obl(queueLast(Agent0),deadline,vioQueue(Agent1)),queue,I),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
occurred(vioQueue(Agent1),queue,I) :-
   event(queueLast(Agent0)), 
   event(deadline), occurred(deadline,queue,I),
   holdsat(obl(queueLast(Agent0),deadline,vioQueue(Agent1)),queue,I),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
oblfluent(obl(changeLane(Agent0),deadline,vioQueue(Agent1)), queue) :-
   event(changeLane(Agent0)),
   event(deadline),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
fluent(obl(changeLane(Agent0),deadline,vioQueue(Agent1)), queue) :-
   event(changeLane(Agent0)),
   event(deadline),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
terminated(obl(changeLane(Agent0),deadline,vioQueue(Agent1)),queue,I) :-
   event(changeLane(Agent0)), occurred(changeLane(Agent0),queue,I),
   event(deadline),
   holdsat(obl(changeLane(Agent0),deadline,vioQueue(Agent1)),queue,I),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
terminated(obl(changeLane(Agent0),deadline,vioQueue(Agent1)),queue,I) :-
   event(changeLane(Agent0)), 
   event(deadline), occurred(deadline,queue,I),
   holdsat(obl(changeLane(Agent0),deadline,vioQueue(Agent1)),queue,I),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
occurred(vioQueue(Agent1),queue,I) :-
   event(changeLane(Agent0)), 
   event(deadline), occurred(deadline,queue,I),
   holdsat(obl(changeLane(Agent0),deadline,vioQueue(Agent1)),queue,I),
   event(vioQueue(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
oblfluent(obl(yield(Agent0),deadline,vioYield(Agent1)), queue) :-
   event(yield(Agent0)),
   event(deadline),
   event(vioYield(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
fluent(obl(yield(Agent0),deadline,vioYield(Agent1)), queue) :-
   event(yield(Agent0)),
   event(deadline),
   event(vioYield(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
terminated(obl(yield(Agent0),deadline,vioYield(Agent1)),queue,I) :-
   event(yield(Agent0)), occurred(yield(Agent0),queue,I),
   event(deadline),
   holdsat(obl(yield(Agent0),deadline,vioYield(Agent1)),queue,I),
   event(vioYield(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
terminated(obl(yield(Agent0),deadline,vioYield(Agent1)),queue,I) :-
   event(yield(Agent0)), 
   event(deadline), occurred(deadline,queue,I),
   holdsat(obl(yield(Agent0),deadline,vioYield(Agent1)),queue,I),
   event(vioYield(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
occurred(vioYield(Agent1),queue,I) :-
   event(yield(Agent0)), 
   event(deadline), occurred(deadline,queue,I),
   holdsat(obl(yield(Agent0),deadline,vioYield(Agent1)),queue,I),
   event(vioYield(Agent1)), agent(Agent0),true,agent(Agent1),inst(queue).
%
% -------------------------------PART 2-------------------------------
%
% generates: [[['detectDisable', ['Agent']], [['iniOblYield', ['Agent']]], [], []], [['flashLights', ['Agent']], [['iniOblChangeLane', ['Agent']]], [], []], [['newArrival', ['Agent', 'Type']], [['iniOblQueueLast', ['Agent', 'Type']]], [], []]]
%
% generate rules
%
%
% Translation of detectDisable(Agent) generates iniOblYield(Agent) if [] in  
occurred(iniOblYield(Agent),queue,I) :- occurred(detectDisable(Agent),queue,I),
   holdsat(pow(queue,iniOblYield(Agent)),queue,I),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of flashLights(Agent) generates iniOblChangeLane(Agent) if [] in  
occurred(iniOblChangeLane(Agent),queue,I) :- occurred(flashLights(Agent),queue,I),
   holdsat(pow(queue,iniOblChangeLane(Agent)),queue,I),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of newArrival(Agent,Type) generates iniOblQueueLast(Agent,Type) if [] in  
occurred(iniOblQueueLast(Agent,Type),queue,I) :- occurred(newArrival(Agent,Type),queue,I),
   holdsat(pow(queue,iniOblQueueLast(Agent,Type)),queue,I),
   type(Type),
   agent(Agent),
   inst(queue), instant(I).
%
% initiate rules
%
%
% Translation of iniOblYield(Agent) initiates ['perm', ['yield', ['Agent']]] if []
%
initiated(perm(yield(Agent)),queue,I) :-
   occurred(iniOblYield(Agent),queue,I),
   holdsat(live(queue),queue,I), inst(queue),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of iniOblYield(Agent) initiates ['obl', [['yield', ['Agent']], ['deadline', []], ['vioYield', ['Agent']]]] if []
%
initiated(obl(yield(Agent),deadline,vioYield(Agent)),queue,I) :-
   occurred(iniOblYield(Agent),queue,I),
   holdsat(live(queue),queue,I), inst(queue),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of iniOblChangeLane(Agent) initiates ['perm', ['changeLane', ['Agent']]] if []
%
initiated(perm(changeLane(Agent)),queue,I) :-
   occurred(iniOblChangeLane(Agent),queue,I),
   holdsat(live(queue),queue,I), inst(queue),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of iniOblChangeLane(Agent) initiates ['obl', [['changeLane', ['Agent']], ['deadline', []], ['vioQueue', ['Agent']]]] if []
%
initiated(obl(changeLane(Agent),deadline,vioQueue(Agent)),queue,I) :-
   occurred(iniOblChangeLane(Agent),queue,I),
   holdsat(live(queue),queue,I), inst(queue),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of iniOblQueueLast(Agent,Type) initiates ['perm', ['queueLast', ['Agent']]] if []
%
initiated(perm(queueLast(Agent)),queue,I) :-
   occurred(iniOblQueueLast(Agent,Type),queue,I),
   holdsat(live(queue),queue,I), inst(queue),
   type(Type),
   agent(Agent),
   inst(queue), instant(I).
%
% Translation of iniOblQueueLast(Agent,Type) initiates ['obl', [['queueLast', ['Agent']], ['deadline', []], ['vioQueue', ['Agent']]]] if []
%
initiated(obl(queueLast(Agent),deadline,vioQueue(Agent)),queue,I) :-
   occurred(iniOblQueueLast(Agent,Type),queue,I),
   holdsat(live(queue),queue,I), inst(queue),
   type(Type),
   agent(Agent),
   inst(queue), instant(I).
%
% terminate rules
%
%
% noninertial rules
%
%
% -------------------------------PART 3-------------------------------
%
%
% initially
%
% at least one create event
%
% clear state to allow for re-creation
%
terminated(F,queue,I) :-
   occurred(createQueue,queue,I),
   not holdsat(live(queue),queue,I),
   holdsat(F,queue,I),
   instant(I),inst(queue).
initiated(live(queue),queue,I) :-
   occurred(createQueue,queue,I),
   not holdsat(live(queue),queue,I),
   instant(I),inst(queue).
initiated(perm(null)),queue,I) :-
   occurred(createQueue,queue,I),
   not holdsat(live(queue),queue,I),
   instant(I),inst(queue).
%
% set up initial state
%
% initially: [['perm', ['deadline', []]], []]
initiated(['perm', ['deadline', []]],queue,I) :-
   occurred(createQueue,queue,I),
   not holdsat(live(queue),queue,I), inst(queue),
