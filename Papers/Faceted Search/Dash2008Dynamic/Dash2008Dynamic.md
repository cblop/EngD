# Dynamic faceted search for discovery-driven analysis
## Dash, Debabrata and Rao, Jun and Megiddo, Nimrod and Ailamaki, Anastasia and Lohman, Guy

This is really hardcore and hard to understand. My initial notes are below:
        
It kind of works like how Amazon does things. Choose a bunch of categories and constraints (like price)
        
In Amazon's case, it smoothly integrates free text search wit structured querying
        
They did some earier work in structured OLAP (online _ processing) [26] describes faceted search. [19, 20]. There they defined 'interestingness' as how surprising or unexpected a summary is, according to an expectation.
        
They organise their facets into a facet hierarchy.
        
The expected value of a cell is derived from other sibling cells at the same level or from cells that are one or more levels higher in the dimensional hierarchy.
        
