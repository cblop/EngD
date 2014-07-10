# Automatic construction of multifaceted browsing interfaces
## Dakka, Wisam and Ipeirotis, Panagiotis G. and Wood, Kenneth R.

"Thus far, multifaceted hierarchies have been created manually or semi-automatically"

"We present automatic and scalable methods for creation of multifaced interfaces"

Appy their techniques to a range of datasets, including:
- annotated images
- tv programming schedules
- web pages

Contributions of this paper:
- a technique for discovering facets that can be used to browse a collection and identifying the appropriate keywords for each discevered facet
- an efficient hierarchy costruction algorithm that leverages the capabilities of relational database systems and stores the hierarchy in a DB
- a set of methods for selecting the best portions of the generated hierarchies when the screen space is not sufficient for displaying all the hierarchy at once
- an extensive experimental evaluation, including three real-life datasets, demonstrating the scalability 

## Background
Some info on clustering in 6, 34, 23. 34 and 23 are just machine learning clustering descriptions, Scatter/Gather(6) applies it to large document collections.

The problem with these auto clustering methods is that they generate clusters that are labelled with only a set of keywords, i.e "battery california technology" (see 12 for clustering critique)

Sanderson & Croft [30] presented an alternative technique for generating concept hierarchies from text, based on subsumption. For terms x and y from a document collection, term x subsumes y $(x -> y)$ if:

$P(x|y) > 0.8 and P(y|x) < 1$

Where $P(x|y)$ is the probability that term x occurs in a document, given that term y does.
This creates a hierarchy of terms, but has shortcomings:
- requires n^2 computations of conditional probabilities (n is number of terms in the collection)
- requires the terms to have a unique meaning
(perhaps not a problem if tags are from an ontology?)

## Automatic facet extraction
Their technique uses a machine learning classifier. This doesn't generalise though, so they expand each keyword using its hypernyms from a lexical corpus such as WordNet.
So then each keyword is a set of words (e.g. cat -> cat, feline, carnivore, mammal, animal, living being, object, entity). This allows the classifier to assign unseen words to correct facets.

But then there is the problem of sense disambiguation. So then it becomes a text classification problem [20, 7] to find associated keywords. So then each keyword is _three sets_ of words (the keyword, the hypernyms, other associated keywords (also from wordnet?))

Their algorithm follows these steps:

- get a collection D of text-annotated objcets. Each object d_i has a set of associated keyworsd k_i1,...,k_in and each keyword k_ij is assigned to a facet F_ij
- for each keyword/facet pair k_ij/Fij:
  - define the facet F_ij as the target class
  - add the keyword i_ij in the first set of words
  - add the hypernyms of k_ij as the second set of words
  - add the other keywords associated with d_i (and their hypernyms) in the third set of words
- train a document classifier over the prepared training data

Used a classifier (supervised learning) with the Corbis collection.

## Fast hierarchy construction
(see the paper for the algorithm)

Kind of works as expected. Like the Heymann algorithm.

## Ranking categories
In what order to display the categories for browsing?

Methods:
- frequency
- set-cover (number of _distinct_ objects accessible from the top-k ranked categories). Fixes problem of overlapping categories
- merit-based: based on the estimated time it takes the user to find a category in the hierarchy

Merit-based rankings offer faster access to the contents of a collection.




## Related
### Clustering
6
12

