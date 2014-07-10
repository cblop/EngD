# Extending faceted navigation for RDF data
## Oren, Eyal and Delbru, Renaud and Decker, Stefan

USE THIS FOR THE FACET GENERATION

Their system is called BrowseRDF.

Lots of very useful stuff here!
        
Sample data: FBI's most wanted criminals, so not LOD.
        
Yes, Semantic Web data is semi-structured, so doesn't follow a fixed scheme. This makes taxonomy generation difficult!
        
They looked at four types of RDF data navigation:
- keyword search
- explicit queries
- graph visualisation
- faceted browsing
        
All of these have severe limitations.

## Automatic facet ranking
Ranganathan [17] says that good facets should be:
- temporal (year-of-publication, date-of-birth)
- spatial (conference-location, place-of-birth)
- personal (author, friend)
- material (topic, color)
- energetic (activity, location)

This metadata is not usually specified in datasets, though (though perhaps I can get it from different DBPedia fields)

They use these metrics:
- tree balance
- objects in a predicate
- cardinality of predicate
- frequency of predicate

They regard faceted browsing as constructing and traversing a decision tree

They see if the tree is well-balanced in order to determine the predicate's usefulness for use as a facet. They take the normalised variance of the number of subjects for each object value of p. (see paper)

They also work out the object cardinality (how many options get displayed under that object)

## Experimental evaluation
Good idea to use this as a guide.
- prototype: describe it
- methodology: they use a similar setup to Yee et al [23]. Only 15 test subjects. Look at RDF expertise (I could look at Japanese/art expertise). They offered keyword search, manual queyry construction and their faceted browser. (I should use keyword and/or single hierarchy). Ask them to perform a small set of tasks (find Japanese woodblock artists from the 16th century, etc). If you have same people using different interfaces, need to have similar but not same questions to prevent repeat answers.
        
[23] is original paper on faceted browsing, based on facet theory [17]
        
In their BrowseRDF prototype, users can browse the dataset by consraining one or several facets.
        
      
