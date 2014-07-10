# The event calculus explained
## Shanahan, Murray

A logic-based formalism for representing actions and their effects.

Event calculus infers what's true when given:

- what happens when: a narrative of events
- what actions do: effects of actions

E.g: giving that eating makes me happy and that I eat at 12:00, the event calculus licenses the conclusion that I'm happy at 12:05.

Based on first-order predicate calculus

## First-order logic
Predicate: function outputs true or false

A logical foundation for a number of reasoning tasks, categorised as:

- _deductive tasks_: "what happens when", "what actions do" -> "what's true when"
- _abductive tasks_: "what actions do", "what's true when" -> "what happens when"
- _inductive tasks_: "what's true when", "what happens when" -> "what actions do"

Deductive tasks include temporal _projection_ or _prediction_. Examples: projection, prediction (outcome of a known sequence of actions is sought)

Abductive: "what actions do" and "what's true when" are supplied, and "what happens when" is required. Examples: explanation, postdiction, diagnosis, planning (sequence of actions is sought that leads to a given outcome)

Inductive: "what's true when" and "what happens when" are supplied. Examples: learning, scientific discovery, theory formation (set of general rules on the effects of actions is sought to account for observed data)

## Ontology
Basic ontology comprises:

- actions/events
- fluents: anything whose value is subject to change over time (num: "temp in the room"/bool: "it is raining"). Mostly propositional (the latter)
- time points

### Predicates

- Ititiates (_alpha_, _beta_, _tau_): fluent _beta_ starts to hold after action _alpha_ at time _tau_
- Terminates (_alpha_, _beta_, _tau_): fluent _beta_ ceases to hold after action _alpha_ at time _tau_
- Initially rho(_beta_): fluent _beta_ holds from time 0
- _tau1_ < _tau2_: time point _tau1_ is before time point _tau2_
- Happens (_alpha_, _tau_): action _alpha_ occurs at time _tau_
- HoldsAt (_beta_, _tau_): fluent _beta_ holds at time _tau_
- Clipped (_tau1_, _beta_, _tau2_): fluent _beta_ is terminated between times _tau1_ and _tau2_

Fluents are _reified_ (first-class objects which can be quantified over and can appear as the arguments to predicates). Unreified is also possible.

## Axioms

- a fluent holds at a time t if it held at time 0 and hasn't been terminated between 0 and t.
- a fluent holds at time t if it was initiated at some time before t and hasn't been terminated between then and t

## The Frame Problem
The Frame Problem: how do we use logic to represent the effects of actions without having to explicitly represent all their non-effects?

### Example: the Yale Shooting Scenario
3 types of action: Load, Sneeze, Shoot
3 fluents: Loaded, Alive, Dead

Load -> Loaded hold
Shoot -> (Dead hold, Alive not hold) if Loaded holds
Sneeze -> no effects

- Initiates(Load, Loaded, t)
- Initiates(Shoot, Dead, t) - HoldsAt(Loaded, t)
- Terminates(Shoot, Alive, t) - HoldsAt(Loaded, t)

Yale shooting scenario is Load -> Sneeze -> Shoot:

- Initially p(Alive)
- Happens(Load, T1)
- Happens(Sneeze, T2)
- Happens(Shoot, T3)
- T1 < T2
- T2 < T3
- T3 < T4

If _Sigma_ is line 65-67 and _Delta_ is 71-77, then:

_Sigma_ & _Delta_ & SC _entails_ HoldsAt(Dead, T4)

But this sequent is not valid because the non-effects of actions have not been explicitly described, e.g that the sneeze action doesn't unload the gun.

You solve this kind of thing by supplying the _completions_ of the Initiates, Terminates and Happens predicates. Too much detail to go into here.

Also by _circumscription_, which minimises the extensions of certain named predicates.

## The Full Event Calculus

Has three new axiom's that model the basic EC's ones, but also describe when a fluent does _not_ hold. Also adds a three-argument version of Happens, which allows actions with duration and compound actions.

It also adds a new prodicate, "Releases", used to disable the law of inertia (?)

- Releases (_alpha_, _beta_, _tau_): fluent _beta_ is not subject to inertia aftor action _alpha_ at time _tau_
- Initially _N_(_beta)_: fluent _beta_ does not hold from time 0
- Happens (_alpha_, _tau1_, _tau2_): Action _alpha_ starts at time _tau1_ and ends at time _tau2_
- Declipped (_tau1_, _beta_, _tau2_): fluent _beta_ is initiated between times _tau1_ and _tau2_

### Using the full EC
Since we can reason about fluents that do not hold, we only need the Alive fluent, not the Dead one. An example of this is given in an extended version of the Yale shooting problem in the paper.

## State constraints
The _ramification problem_ is like the frame problem, but for actions with indirect effects.

A way to represent actions with indirect effects is through _state constraints_, which are logical relationships that have to hold between fluents at all times.

## Non-deterministic actions
Common sense law of inertia: an action can be assumed to make no other changes than the ones specified.

_Determining fluents_ are used to represent actions with non-deterministic effects. A determining fluent is one which is not subject to the common sense law of inertia, but its value determines whether or not some other fluent is initiated or terminated by an event.

E.g an action Toss could non-deterministically result in either Heads holding or not holding.

## Compound actions
(actions which are composed of other actions)

It's best if you can work out the effects of an action based on the effects of its sub-actions. 

## Summary

The event calculus is capable of representing:

- actions with indirect effects
- actions with non-deterministic effects
- compound actions
- concurrent actions
- continuous change with triggered events

