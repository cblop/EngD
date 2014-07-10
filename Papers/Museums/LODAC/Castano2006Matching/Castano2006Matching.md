# Matching Ontologies in Open Networked Systems : Techniques and Applications
## Castano, Silvana and Ferrara, Alfio and Montanelli, Stefano

How to find related ontologies on open networked systems?
        
Paper describes H-MATCH algorithm for ontology matching.
        
Ontology matching algorithms must consider spec languages.
        
"Peculiar requirements" can be changing ontology features used for matching.
        
HELIOS described in [19, 20]
        
H-match key feature: can be dynamically configured for adaptation to an ontology's semantic complexity.
        
ARTEMIS is an integration system [21]
        
H-MATCH is automated ontology matching, ARTEMIS is semi-automated schema matching, interaction is expected
        
## Ontology matching & representation
*Ontology matching* takes two ontologies as input and returns the mappings that identify corresponding concepts in the two ontologies.
        
A *mapping* is a correspondence between a concep in the first ontology and one or more concepts of the second ontology. Mappings can only happen after concepts are analysed for their similarities.
        
H-MATCH uses affinity metrics to get a semantic affinity measure (range [0,1]. Can then get one-to-one or one-to-many mappings.
        
Reference model: H-MODEL, a graph-based representation of ontologies.
        
They have 4 types of models for different levels of semantic complexity: surface, shallow, deep, intensive.
        
Does some matching based on WordNet[24], a kind of mega-thesaurus, which is used to find related terms in onts. It uses this to create its own graph-structure thesaurus. As well as synonyms, can find related terms and one with more general meanings.
        
A concept is the union of properties and adjacents of *c* (there are also strong and weak properties). Different properties have different weights.
        
Once a thesaurus is built, shallow matching, deep matching, etc can find related terms.
      