# Automatic Discovery of Useful Facet Terms
## Dakka, Wisam and Dayal, Rishabh and Ipeirotis, Panagiotis G

(This is the sequel to Dakka 2005)

For faceted search, most people cite Hearst, then Pollitt and Yee.

Their technique uses WordNet hypernyms and a Support Vector Machine (SVM) classifier to assign new keywords to facets.

Their goals are to:

- automatically discover, in an _unsupervised_ manner, a set of candidate facet terms from free text
- automatically group together facet terms that belong to the same facet
- build the appropriate browsing structure for each facet

Faceted hierarchy generation research is reasonably new, but subject hierarchy generation is well established.

BUT! These all generate a single hierarchy for browsing the database. Dakka's previous paper separated terms into different facets, then built a hierarchy for each facet.

Their tool uses external sources to find the appropriate context for terms. They use WordNet, Google and Wikipedia. Which terms tend to co-occur often?

Their algorithm steps:

1. Identify important terms in a document.
2. Derivec context from external resources
3. Term frequency analysis
