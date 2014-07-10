# Emad meeting, Tue Jan 21

Protege RDF & RDFS export: is putting in class numbers instead of names. Something wrong with export?

Check SPARQL query. It works, but is there a better way of writing it? Query is:

select ?x ?y where {
?CompositeSystemGoals :hasSubGoal ?Null
}