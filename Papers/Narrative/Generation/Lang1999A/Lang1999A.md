# A declarative model for simple narratives
## Lang, Raymond

## Summary
Implements a _story grammar_. Good summary of early story generation systems.

Generates Russian folk stories (a la Propp) based on a grammar/set of rules.

## Content
Say that story generation generally takes one of two approaches:

1. declarative (creating text to conform to a generic story structure)
2. procedural (modelling and recreating processes used by human authors)

They implement a story grammar to address the issues encountered by both approaches.

Rumelhart (1975) implements a story grammar which is context-free and has rewrite rules. But this got abandoned due to the tech at the time.

Describes TALESPIN (Meehan 1976)

Lebowitz develops a model of story telling based on an extensible library of plot fragments (Lebowitz 1985). Is oriented around the narrative goals of the author.

## Joseph story generation system
Their system is called "Joseph". It has these components:

- story grammary
- grammar interpreter
- temporal predicates
- world model: instantiations of terminals from their grammar.
- natural language output unit: story grammar and world model define event lists and sequences of events that can be rendered into natural language.

Grammar predicates are:

1. hero(Agent) -> protagonist
2. fact(Fact, World) -> can be added to setting
3. ep_prim(Event, World) -> Given a world state World, Event can initiate an episode.
4. effect(Act, Result, World) Act couses Result to happen if Result is an event
5. consequence(State1, State2, World) -> given World, the consequence of State1 is State2
6. plan(Title, Steps, World) -> a compound act consisting of several steps
7. plan-efct(Plan-title, Effect, World) -> returns the effect of executing a plan in the world
8. emot_reaction(Event, Emotion, World) -> emotion the hero feels when an event happens
9. action_motiv(Emotion, Action, World) -> potention action with someone in a certain emotional state
10. goal_motiv(Emotion, Goal, World) -> in World, Emotion motivetes someone to adopt Goal.
11. intention(Prot, Goal, Action, World) -> in World, Prot believes Action is a means to achieve Goal.

They use Russian folk stories like Propp to generate similar ones.

Limitations:

- single protagonist
- cannot represent beliefs and goals of multiple agents
- no representation of unforeseen circumstances in plans