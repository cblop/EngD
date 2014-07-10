# Towards Integrating Plot and Character for Interactive Drama
## Mateas, Michael and Stern, Andrew

This is like the initial brief for the facade project.

Aims for replayability of 6 or 7 times.

No obvious branches in the plot where the user has to make obvious decisions. More 'reactive', I guess.

While agent autonomy is important, agents that perform actions that move the story forward are moreso.

Drama manager: maintains story state
Agents: maintain character state, maintain moment-by-moment behaviour decisions.

Hooks are built into the agents so that the drama manager can activate certain goals and behaviours.

Scenes are composed of beats: the smallest unit of value change.

Examples:
- "Grace fails to discuss her marriage"
- "Trip successfully changes the topic away from marriage"

Their character agents are not strongly autonomous. They do not take action in the absence of the drama manager.

The characters instead accomplish low-level tasks. All higher-level motivation is provided by the drama manager.

## Related work
Believable agents:
- Mateas 1997
- Bates, Loyall and Reilly 1992
- Blumberg 1996
- Hayes-Roth, van Gent and Huber 1997
- Lester and Stone 1997
- Stern, Frank and Resner 1998

Integrating agents with plot:
- Weyrauch 1997: drama manager
- Blumberg and Galyean 1995 

Narratology:
- McKee 1997
