# Searching for storiness: Story-generation from a reader’s perspective
## Bailey, Paul

## Summary
Flawed model only uses reader's worldview, does not account for anything outside for story generation. Also, is unimplemented, so no way of testing it. How convenient!

## Content
Looks at the response of the reader to a story as the measure of that story's success.

They say that previous work in automatic story generation can be divided into 3 groups:

1. Author models: story generation is approached from the perspective of a human author. Based on attempting to model the processes undergone by the human author during the creation of a story. (see papers listed if wanted)
2. Story models: story generation proceeds from an abstract representation of the story as a structural artefact. Colby 1973, Rumelhart 1975, Pemberton 1989, Lee 1994 are examples of generation systems based on _story grammar_. Wilensky 1983 is an analysis of story grammars.
3. World models: a world is constructed, with characters in it. They are given sufficient agency and complexity to result in their actions and interactions becoming representable as a story.

All these models rely on a _planning_ paradigm, using goals, etc.
Also, they do not encapsulate _general_ storiness.

TALE-SPIN (Meehan 1976) overgenerates, apparently. And MINSTREL (Turner 1994) undergenerates. Both because they have a limited sense of what makes a story.

This paper instead uses a paradigm of heuristic search rather than planning.

If a user responds to a joke in an appropriate way, then it is a joke.

Assumes that we can "read the user's mind" to tell what their brain state is.

## The model
Their model controls the cognitive responses of an imagined reader.

Model is made from a body of world-knowledge belonging to the imagined reader: their assumptions, opinions, beliefs, etc.

Reader response is:

- expectations
- questions

## Story generation
Loops through, going through these steps:

1. search space of possible next segments is generated from reader's knowledge base
2. effect of possible next segments on reader model is analysed
3. segment of patterns that best fit the reader's expectations are selected
4. segment is chosen, story is updated along with reader model

Doesn't this seem flawed? How can there be any new info or surprises if a story is generated from what the reader knows?

## Search space generation
Steps:

1. generalise (replaces part of a sentence with a more general concept)
2. specialise (replaces part of a sentence with a more specific concept)
3. detach (deletes part of a sentence)
4. join (produces a specialised sentence by joining two other sentences)

In order to generalise or specialise, the existence of a hierarchical taxonomy is assumed!


## Interactive story generation
Look at Sgouros, Papa... 1996, Mateas 1997 (Facade?)

Searching for "interactive storytelling" in Google scholar reveals _loads_ of great results.