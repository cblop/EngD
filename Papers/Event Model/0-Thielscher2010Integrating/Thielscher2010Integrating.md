# Integrating Action Calculi and AgentSpeak: Closing the Gap.
## Thielscher, Michael

Agentspeak domain signature: _belief predicates_ and _action predicates_. Belief literal is a belief predicate with arguments.

Need concept of TIME

Knows(f, s): Agent knows FLUENT f to be true at TIME s

Poss(a, s, t): execution of ACTION a is possible starting at TIME s & ending at time t.

AgentLP: Agent Logic Programs, combine reasoning about actions with the declarative programming paradigm.

Assume extra fluent, Goals(e), e is a list of triggering events

## Situation Calculus Summary

Branching time structure, elements are of basic sort TIME, called _situations_.

S_0 is the initial situation.

S_0 -> Do(a, s)

Do(a,s) denotes the situation resulting from performing action a in situation s.

Two standard predicates:

- Holds(f, s): fluent f holds in situation s
- Poss(a, s): action a is possible in situation s
