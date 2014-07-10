# Jason notes

So an agent consists of:

- beliefs (state)
- plans (if beliefs exist, then execute certain functions)

Plan is:

- triggering event
- context
- sequence of actions/goals/belief updates

Achievement goals are prefixed with !, test goals are prefixed with ?.

Achievement goals state that the agent wants to achieve a state of the world where the associated predicate is true.
Test goals return a unification for the associated predicate with one of the agent's beliefs, otherwise they fail.

The AgentSpeak interpreter also manages _events_ and _intentions_. It requires three _selection functions_:

- S_e_: event selection function: selects an event from the set of events
- S_o_: option selection function: selects an applicable plan from the set of applicable plans
- S_i_: intention selection function: selects an intention from the set of intentions

Supposed to be based on the agent's characteristics. Choices are supposed to be non-deterministic (could be room for emotion stuff here?).

_Intentions_ are courses of actions to which an agent has committed: partially instatiated plans.
_Events_ can be external or internal.

Sequence:

- S_e_ selects an event, enterpreter finds applicable plans (relevant plans that can be applied now)
- S_o_ chooses a single applicable plan from that set, this becomes the _intended means_. Plan gets pushed to the top of an intention, or creates a new intention.
- S_i_ selects one of the agent's intentions. Since an intention is a stack of plans, the top plan of the selected intention is executed. So either an action is performed on the environment, an internal event is generated, or a test goal is performed (so the set of beliefs has to be checked)

From the point of view of the interpreter, an agent is:

- a set of beliefs
- a set of plans
- user-defined _selection functions_ and _trust function_ (norms)
- Belief Update Function: updates the agent's belief base from perception of the environment
- Belief Revision Function: used when the agent's beliefs are changed from the execution of one of its plans
- a "circumstance": includes pending events, intentions, etc




