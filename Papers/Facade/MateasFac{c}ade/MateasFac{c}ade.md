# Fa\c{c}ade : An Experiment in Building a Fully-Realized Interactive Drama
## Mateas, Michael and Stern, Andrew and Tech, Georgia

In the intro, they say how we don't interact with game characters with _natural language_, and state that this would be desirable.

They also say it would be great if games could understand the dialogues and interactions between human players.

NPCs also need a richer repertoire of behaviours, especially when it comes to conversation.

Drama manager: Bates 1992, Weyrauch 1997

"Create a system that tries to shape open-ended play into narrative structures."

Requires moving beyond finite state machines and scripting languages.

"What is needed is a character authoring language that combines tight authorial control with the real-time capability to dynamically combine multiple behaviors and pursue multiple goals."

Their system consists of:

- NLP
- novel character authoring language
- novel drama manager

## Facade

- offers a new framework for authors to create structured hierarchies of behaviours, which allow for a theatrically dramatic experience to occur when modulated by player interaction
- not general NLP: listens for a large variety of word patters and phrases focused on the context of the dramatic situation
- this feeds into discourse management system

## Motivation

Previous approaches take two forms:

- Graph approach: hand-craft a structure of nodes (graph, flowchart, etc), where each node is a plot event, location, character info, etc. Connections between nodes are paths/links. The player traverses the graph and the resulting sequence of nodes is the story.
Examples: adventure games, hypertext fiction, some IF, choose-your-own-adventure.

- Procedural simulation: open-ended virtual world containing a collection of NPCs/autonomous agents. The player is another element. Many things happen at the same time. So the player experiences a sequence of events, which is a narrative of sorts (emergent narrative).
Examples: FPS, sim games, VR, etc.

Facade tries to find a middle ground between structured narrative and simulation, tries to get the best of both worlds.

So the balance needs to be between drama/experience and freedom/autonomy.

## Working

Simulation constantly updates with feedback on what the player is doing. The unit of simulation is a _story beat_, the smallest unit of dramatic action as defined by McKee 1997 (need this book).

Beats have preconditions and effects on the story state, so the drama manager knows when they make sense to use.

The "beats" have a higher granularity than that of typical sim games, with ~200 beats updating every minute or so. Very context-dependant.

They say that the architecture offers direct support for coordinated activity between the autonomous agents in the simulation using _joint goals and behaviours_. This creates more sophisticated, lifelike coordinated behaviour between dramatic characters than strongly autonomous agent architectures usually provide (I guess in most cases they are too individual). 2000 paper talks about this.

## Architecture

Similar to what I propose, actually. 3 layer:

- story level interaction (drama management)
- believable agents
- shallow NLP

They developed several authoring languages for it:

- A Behaviour Language (ABL): look at their paper for this! Based on Hap (Loyall and Bates 1991)
- Natural Language Understanding (NLU) Template Language: Forward-chaining template rule language. Maps player-typed surface text into discourse acts.
- Reaction Decider Language: forward-chaining rule language that decides reactions to story events
- Beat Sequencing Language: specializes in drama management.

## Animation
They used a combo of procedural animation and layered keyframe animation data. Made a special scripting language for this.

## ABL
An activity is represented as a goal, which can have one or more behaviours as a means of accomplishing its task. If a behaviour fails, the goal tries to use other ones, or fails totally if none are left.

ABL maintains an _active behaviour tree_ (ABT) to keep track of all the active goals and behaviours, subgoal relationships, etc.

The important takeaway is that it's multi-threaded, so allows many concurrent events.

The API is worth a look, seems to roughly correspond to the events I want to put into my system.

### Player ABL
The player ABL doesn't control the player's character (the player does that), but instead observes what they do, infers meaning from it, and supplies this info to other agents in the system (Grace, Trip).

## Beats
= a context-sensitive collection of ABL behaviours. Only one beat is active at a time.

The behaviours advance the narrative, but still allow interaction to change to other beats.

5 types of beat goals:

- transition-in: characters express their intentions
- body: a dramatic question/situation is posed to the player
- local/global mix-in: react to the player before end of beat
- wait-with-timeout: wait for the player's reaction
- transition-out: final reaction to the player's action in the beat

Beat goal is series of steps in a sequential behaviour. Each step has a _joint parallel behaviour_ (joint, because both Grace and Trip have them).

Each joint behaviour has one or more of these subgoal behaviours in parallel:

- staging (where to walk to, face)
-  dialogue to speak
- where and how to gaze
- arm gestures to perform
- facial expression to perform
- head and face gestures to perform
- small arm and head emphasis motions triggered by dialogue (head nods, hand flourishes)

There is also some discussion of context, which is important.


