# Dragons, bats and evil knights: A three-layer design approach to character-based creative play
## Bryson, Joanna and Th{\'o}risson, Kristinn R

Character-based approach to constructive narratives.

How would you construct a play environment to support character-based creative play?

3 levels:

1. A high, artistic design level for creating story and characters
2. A middle, behaviour-based design level for creating personality i character agents
3. A low, VR design level for basic capabilities and appearances

This paper mostly focuses on the middle layer, as it's mostly concerned with AI.

Spark of Life (SoL) combines two already established AI architectures: Ymir and Edmund (Edmund by Bryson).

SoL eliminates this problem by changing the top level creative design from a script to a cast of characters.

However, it removes a substantial element of narrative structure: a sequential order of events.

Uses reactive, behaviour-based AI. Good for creating believable characters.

## Behaviour-based AI
Splits AI into specialised modules(behaviours). They control a specific sort of action (e.g. _walking_ or _laughing_)

SoL is based on Edmund, which uses Parallel-rooted, Ordered, Slip-stack Hierarchical (POSH) action selection.

Actually, SoL is based on two systems: _Edmund_ and _Ymir_.

### Edmund

- action pattern: like a game loop
- competence: prioritised set of elements. highest priority element is the goal
- slip-stack hierarchy: recente hight priority elements can be inhibited to allow low prority elements to fire

### Ymir
Idea is to intregrate it with Edmund.

Ymir allows a VR character to engage in real-time dialogue with a human.

Uses context-dependent perception-action loops as short as 100msec to react and plan

Six main elements of Ymir:

1. Perception
2. Decision
3. Action
4. Intermodule communication (Blackboards)
5. Knowledge
6. Organisation

Plan execution is non-deterministic.


## Follow-ups
Combining the concept of a script with the notion of autonomous, reactive characters:

- Hayes-Roth and van Gent, 1997
- Lester and Stone, 1997
- André et al, 1998


