# Inducing Ontology from Flickr Tags
## Schmitz, Patrick

Uses a subsumption-based model from Sanderson and Croft:
        
X subsumes Y if:
P(x | y >= 0.8) and P(y | x < 1)
        
Could perhaps reduce noise by matching concepts to wordnet?
        
This could be useful, perhaps it will be worth implementing their algorithm. However, the end result is only a tree taxonomy.
