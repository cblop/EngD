# Streaming First Story Detection with application to Twitter
## Osborne, Miles and Lavrenko, Victor

FSD is to identify the first story to discuss a particular event.
        
They run their FSD algorithm, assigning a novelty score to each tweet. This score is based on the cosine distance to the nearest tweet, so we also know for each tweet which other tweet it is most similar to.
        
This means _threads_ of tweets can be analysed.
