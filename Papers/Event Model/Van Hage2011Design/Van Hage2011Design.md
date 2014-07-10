# Design and use of the Simple Event Model (SEM)
## Van Hage, Willem Robert and Malais{\'e}, V{\'e}ronique and Segers, Roxane and Hollink, Laura and Schreiber, Guus

Describes an event model, and a prolog API they made to go with it.

Four core classes:

1. sem:Event
2. sem:Actor
3. sem:Place
4. sem:Time

Each core class has an associated sem:Type class, which contains resources that indicate the type of a core individual. Individuals and their types are usually borrowed from other vocabularies. E.g, sem:Place "Indonesia" and sem:PlaceType "republic" are borrowed from the Getty Institute's Thesaures of Georgraphical Names (TGN).

## Constraints
Constraints can be applied to any property. There are three kinds:

1. sem:Role - role that an individual of a class is playing in the context of a specific event
2. sem:Temporary - sets temporal boundary within which the property holds
3. sem:View - points of view and opinions

Example: Indonesia would be a republic in 1945, according to the Indonesians. This would have sem:Temporary and sem:View constraints, because other sources of information would describe it as a controlled region rather than a republic.

## Properties
Three kinds: sem:eventProperty, sem:type properties and others like sem:accordingTo and sem:hasTimeStamp