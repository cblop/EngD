# Virtual agents for the production of linear animations
## Damiano, Rossana and Lombardo, Vincenzo and Nunnari, Fabrizio

Description of character behaviour -> linear (non-interactive) animation
        
Problem of bridging gap between story and anim generation has been addressed by research into virtual agents.
        
Uses "bricks" to make a story.
        
Called "AnimaTricks", their system has:
- production pipeline for authoring animated characters from high level behaviour specifications
- reference architecture for generating the animation from these specifications
- a declarative language for mapping the chaacter's behaviour onto animations
        
Agent architecture:
knowledge of the world + internal state -> reasoning unit (strategy) -> planner (sequence of actions) -> movement coordinator (motor activities) -> DOF manipulator
        
They discuss the possible coupling of the graphic engine (Unity, etc) that performs the two lowest levels with the planner.
        
Say it must support a declarative approach to definition of actions so that it's easy to use for 3D animators. Clearly this is desirable for Sysemia's system, too.
        
## AnimaTricks
3 main components:
        
- planner
- A2A language executer
- animation engine
        
Has an offline authoring pipeline where writer and AI engineer encode a character's behaviours into planner format, and catalogue creation where action primitives are translated into the animatio language.
        
In the online phase, the system generates the animated behaviour of the character giver a specification of the character's high level tasks.
        
### Planner
Planner input is a description of the scene and a set of tasks. They use the JSHOP2 planning system. The description necessarily contains:
        
- list of characters in the scene
- list of objects in the scene
- list of scene locations
- positions of characters and objects
- list of events to which the character is to react
        
### Executor
The executor takes the plan and retrieves the corresponding A2A (action to animation) mapping.
        
      
