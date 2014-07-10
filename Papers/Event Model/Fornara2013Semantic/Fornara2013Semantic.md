# Semantic technologies for open interaction systems
## Fornara, Nicoletta and Tampitsikas, Charalampos

Open interaction systems: software devised for enabling autonomous agents to interact, negotiate, collaborate, coordinate actions
        
Introduces Artificial Institutions (AIs) for the integration of open interaction systems into museum spaces
        
## Open interaction systems
_Dynamic distributed event-based systems_ having these components:
        
- A state that evolves due to events that happen and actions performed by agents.
- No assumptions can be made on interactin agents' behaviour, so _norms_ are a fundamental part of open systems. Norms are used to express restrictions on agent behaviour.
        
Not sure how useful this paper is. Might have to look at it if I want to use agents.

## 2nd read

Open interaction systems are _dynamic distributed event-based systems_ with these components:

- a state that evolves due to the _events_ that happen and the _actions_ (events with an actor) performed by the interacting agents. Events and actions are described by means of their _preconditions_, which need to be satisfied for the successful performance of the events or actions, and their _effects_ on the state of the system. Events occur due to the _elapsing of time_ (or the change of value of some properties).
- _norms_ are a fundamental part of open systems. They express obligations, prohibitions, permissions, etc that regulate how the agents interact. At design time, norms ar eexpressed in an abstract form in terms of _roles_ and may contain some.
- interacting agents and open interaction system are running on _different platforms_, so standard mechanisms and rules for the agents to _perceive_ the state of the system and for _acting_ within the system need to be devised.

Open distributed systems can be modeled as a set of _institutional spaces_ of interaction where _objects_ and _agents_ are situated [1].

_Space_ is similar to GOLEM[2]'s _container_ or CArtAgO's _workspace_.

They say that it's fundamental to be able to create and destroy spaces of interaction at run-time, so they create them using _Artificial Institutions_ (AIs).

_Spaces of interaction_ are how stuff is modeled [1].

An environment for multi-agent systems (MAS) is composed by multiple _spaces_ where _objects_ and _agents_ are situated.

_Spaces_ help to model _limited observability_. A space represents the _boundaries_ for the effects and perception of the events/actions that happen in a space (perceived only by the agents inside that space).
A space is also in charge of _mediating_ the events and the actions that happen inside the space.

They adopt and extend the GOLEM framework [2] for their prototype.

Spaces of interaction are created and destroyed at run-time. This happens by using the OWL-API to access the content of the ontology where the model of the relevant AI is stored and that manipulates the content of the _State Ontology_, which is used for representing the state of the existing spaces of interaction. It adds a new individual that represents the new space.

## Institutional actions
Look up institutional actions [3]. They are a special type of action whose effects change the value of institutional properties.

For example, the action of opening or closing an auction, creating a space of interaction or assigning a role to an agent.

An _institutional action_ is successfully performed if and only if the actor of the action has the _institutional power_ to perform such an action and if other application dependant contextual conditions are satisfied [4].

## Artificial institutions
An _artificial institution_ (AI) is characterized by:

- a set of concepts and properties introduced by the specific AI
- a set of actions available for the agents and defined by the AI
- a set of roles, which are labels defined in a given AI for abstracting from the specific agents that will take part at run-time to an interaction
- a set of norms for expressing at design time the obligations, permissions, prohibitions and institutional powers of the agents that will play a given role

A specific _institutional space_ is characterised by:

- the AI or AIs used to create the space
- the set of sub-institutional spaces dynamically created inside a given space
- the list of agents that are inside the space at a given instant of time
- the list of roles defined in the space that is generated from the list of roles of the AIs used to create the space
- the list of objects that the agents can manipulate in the space

### Roles

An AI may have different roles. So, in an auction AI we may have the roles of auctioneer and participant

### Norms
Norms:

- are used to define at design time the _obligations_, _prohibitions_, _permissions_, and _institutional powers_. They are all defined in terms of roles.
- regulate the performance of _actions_ and they are _active_ during a period of time that can be expressed through _activation_ and _deactivation_ evens. 
- they specify _sanctions_ for the attempt to perform an institutional action without having the right institutional power or when specific preconditions are not satisfied.

Norms have _activations_, _contents_, _ends_ and _durations_.

## Conclusion

There is no conclusion! Overall, this paper is not so useful.

## Papers

[1] Tampitsikas et al. 2012
[2] Bromuri and Stathis 2009
[3] Fornara et al. 2007
[4] Searle 1995
