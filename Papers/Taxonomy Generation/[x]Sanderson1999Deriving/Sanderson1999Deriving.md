# Deriving concept hierarchies from text
## Sanderson, Mark and Croft, Bruce

Distinguishes between monothetic clusters (based on one property) and polythetic clusters (based on multiple properties).

The most important thing from this paper is their concept of _subsumption_.

## Subsumption
> For two terms, x and y, x is said to subsume y if the following two conditions hold:

1. P(x|y) = 1, P(y|x) < 1
(x subsumes y if the documents which y occurs in are a subset of the documents which x occurs in)


BUT they relaxed the condition to this to get better results:
2. P(x|y) >= 0.8, P(x|x) < 1


