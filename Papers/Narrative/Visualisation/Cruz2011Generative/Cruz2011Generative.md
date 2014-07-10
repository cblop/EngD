# Generative Storytelling for Information Visualization
## Cruz, Pedro and Machado, Penousal and Bal, Following Mieke

Fabula: a set of time-ordered events caused or experienced by actors.
Actors: agents that perform actions in the fabula's time span.
Data fabulas: the set of events, agents, actions and chronology extracted from a dataset.
Story: a fabula's presentation layer.
Narrative: a story's structure, defined by fabula's events, actors and actions.
(a narrative's chronology can vary from that of the fabula in its order, pace or rhythm)
Narrator/storyteller: the agent that transmutes the fabula into a story.
        
A story is a fabula's presentation layer. It involves:
- the representation of the fabula's actors
- defining a temporal structure that contains the same events as the fabula, but has a different pace, rhythm or chronology -> narrative
        
Fabula is discrete, but can have a continuous representation.
        
## Conceptual framework
Engine transforms fabula into story. Consists of two models:
- event model: manipulates time of the events, creates story's timeline. Chronology can be altered to change pace, rhythm, etc.
- action model: deals with fabula's actions, implements actors' behaviours. Adapts to its own actions.
        
## Visualising empires decline
Dataset: 110 events of land gain/loss of British, Spanish, French & Portuguese empires.
        
Empires: solid coloured circles
Former colonies: coloured circle outlines
(size of circle = landmass)
        
3 types of actions:
- area increase
- colony detaches, decreases area
- colony moves
        
Since decline is the emphasis, colonies splitting is more dramatic than the circles just enlarging due to growth.
        
Timeline is nonlinear: one year per second, but three times faster when no colonies become independent.
