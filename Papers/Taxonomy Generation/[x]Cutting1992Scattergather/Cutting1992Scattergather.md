# Scatter/gather: A cluster-based approach to browsing large document collections
## Cutting, Douglass R and Karger, David R and Pedersen, Jan O and Tukey, John W
Very early paper, can be thought as a precursor to faceted search.

Scatter/gather is their system for browsing documents. They also introduce near linear-time clustering algorithms.

Scatter: organise into facets, present short summaries
Gather: selected groups are gathered together to form a subcollection

Then the subcollections are scattered, gathered, etc again.

They recommend the combo of their scatter/gather system with direct text search. Kind of like what the other faceted people were saying.

They have two algorithms: _buckshot_ for online reclustering, and _fractionation_ which is more careful and better suited to the partitioning of the corpus when it is first presented to the user.

Interesting historically. It was used to cluster news articles.
