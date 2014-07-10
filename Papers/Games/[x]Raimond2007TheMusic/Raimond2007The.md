# The Music Ontology.
## Raimond, Yves and Abdallah, Samer and Sandler, MB and Giasson, Frederick

Music Ontology: a formal framework for dealing with music-related information on the Semantic Web (including _editorial_, _cultural_ and _acoustic_ information)
        
Can act as a grounding for more domain-specific knowledge representation.
        
[6] is Music Ontology paper
[7] is Timeline Ontology
[8] is OWL-Time
[12] is Functional Requirements for Bibliographic Records ontology
        
Interval and Instant are from OWL-Time. Timeline Ontology adds TimeLine: a backbone for temporal information.
        
Using the Event Ontology, music production can be modelled as events.
        
They based their event representation on the _token-reification [10] approach: each event occurrence is a first class object or 'token'.
        
Events can be sounds, performances, compositions, transduction/recording.
        
EVENT ONTOLOGY LOOKS USEFUL
        
## Events
Events have
- factors (instrument)
- products (sound produced)
- agents (performer)
        
Can be linked to a place through event:place
        
FRBR (Functional Requirements for Bibliographic Records ontology) has these concepts:
- work (abstract, distinct, artistic creation)
- manifestation (physical embodiment, like a record)
- item (single exemplar of such a manifestation, like a particular CD)
        
They also use FOAF.
        
They define MusicalWork on top of FRBR, MusicalManifestation (record/track, etc), MusicalItem (stream, particular CD/vinyl).
        
On top of FOAF, they have MusicArtist and MusicGroup
        
On top of the Event Ontology, they have Composition (creation of a MusicalWork), Arrangement (arrangement of a MusicalWork)
        
It can have a MusicalWork as a factor, an Arranger as an agent and a Score as a product, for example.
        
A Performance denotes a particular Performance, it can have factors like a MusicalWork and a Score. Agents could be a number of musical instruments, equipments and agents could be a number of musicians, sound engineers, conductors, listeners, etc.
        
Have a look at MusicBrainz for their instrument taxonomy.
      
