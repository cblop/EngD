# Combining ship trajectories and semantics with the simple event model (sem)
## Van Hage, Willem Robert and Malais{\'e}, V{\'e}ronique and de Vries, Gerben and Schreiber, Guus and van Someren, Maarten

This paper features an implementation of the Simple Event Model.

Actually, this is the paper that the SEM came from. They use it for a Maritime Safety and security use case about Situational Awareness.

They also use SWI-Prolog to derive abstract events from the recognized simple events.

## Simple event model

Consists of:

- event: what is happening
- actor: who is doing something (and on whom it is happening). Related to Events via the generic property participatesIn.
- object: describes "with what" something is being done. Can be physical objects or animate entities. Related to Events via the generic property involvedIn.
- place: "where" something is happening. Denoted with an instance of a PlaceType, which can be borrowed from external vocabs.
- role: function that is played by an instance of one of SEM's core classes, in the context of a given event.

## Linking to ontologies
Use GeoNames Features to classify the palces at which events happen. Not ideal for what I want to do: This finds places on the earth's surface.

## Follow-ups

Look at other ontologies: DOLCE [7], SUMO [9], CYC [8].
