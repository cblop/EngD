# Guiding interactive drama
## Weyhrauch, Peter and Bates, Joseph

## Summary


## Notes

THIS IS A PHD THESIS

Also part of the Oz project.

How can the creator of an interactive drama get their themes and ideas across? The user's experience must be 'shaped'.

### Architecture

Architecture is called 'Moe'.

Technical achievements in this paper:
1. Ability to capture a dramatic aesthetic in an automated evaluation function
2. Three search strategies + search state that model and guide an interactive drama

Moe extends their Plot Graph Model.

Emotional Intensity can be a criterion used for evaluation of a Moe drama experience.

Moe uses adversary search to determine which move to make next.
Constructs something like a game-tree, where each path corresponds to a possible future.

The evaluation function assigns a value to each of the possible futures.

Each user move also has a recogniser. This allows the building of a history.

Moe moves also have refiners that allow the moves to make concrete changes to the world.

Freytag's pyramid:

				 climax
                   /\
    rising action /  \ falling action
	             /    \
	____________/      \_____________
	exposition            denouement


Drama vs narrative! I should look at the theory of drama, too.

### Artistic sources
Good to read that they have also had problems with integrating narrative theory!

Would it be worth recreating an existing piece of IF, like they did in this paper?

### The evaluation function

The evaluation function has seven features:

- _Thought flow_: whether one event in the User's experience relates logically to the next
- _Activity flow_: how bored the User feels by walking around uselessly
- _Options_: Measures how much freedom the User perceives she has
- _Motivation_: Measures whether the User's actions are motivated by her goals
- _Momentum_: Measures the proximity of certain events that the creator prefers to happen together
- _Intensity_: Measures whether the User's excitement builds while solving the mystery
- _Manipulation_: Measures how manipulated the User feels in repsonse to MOE MOVES

Evaluated by analizing the scenario move by move. E.g _thought flow_ is measured by the number of linked user moves.

Excitement is measured by how much of the truth the user has uncovered.

The measure of success is how well an artist's aesthetic has been encoded into a particular evaluation function.

The crux of how it works is adversary search. Read the thesis for details. Kind of like treating the search space of possible narratives as a game of chess.


### Related work

[21] Laurel defines interactive drama in a dissertation, designs the PLAYWRIGHT system
[22, 23] Lebowitz proposes plot fragments
[34] Ryan: do I have this book?
[1] Aristotle's Poetics
[30] Polti looks like Propp
[40, 27] appear to be more narrative theory
[21, 38] are PhD theses that could be helpful in linking narrative to interactive art
[29] is Talespin. Did I already look at this? It just generates stories, but not interactive ones.
[13] Dyer created a system which parses and understands stories, answers questions about those stories. Created Thematic Abstraction Units. Worth a look.

TAILOR [39] and UNIVERSE[22,33] are also examples of generative, but not interactive, narrative.
