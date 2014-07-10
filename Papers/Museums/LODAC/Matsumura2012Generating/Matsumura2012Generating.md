# Generating LOD from Web : A Case Study on The Workflow for Generation of Linked Data on
## Matsumura, Fuyuko and Kato, Fumihiro and Kamura, Tetsuro

Describes the LODAC project, a workflow to generate LOD from web data.
        
LOD consists of key-value pairs in this case. Uses museum collection data in Jp as an example.
        
Following steps are described:
1. Extract key-value pair data from artwork web pages using Apache Nutch and Solr.
2. Map keys in extracted data to a common schema by museum professionals
3. Merge redundantly described artifacts to have one URI.
4. Publish data as LOD with permalinks as a SPARQL endpoint.
5. Store snapshots of each crawled page in a git repository.
        
However, when crawling museum sites there are issues with inconsistently described data.
      
