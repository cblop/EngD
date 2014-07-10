# Faceted metadata for image search and browsing
## Yee, Ka-Ping and Swearingen, Kirsten and Li, Kevin and Hearst, Marti

This is the paper that, after Pollitt, introduced faceted browsing.
        
Dataset: 'thinker' collection of the Fine Arts Museum of San Francisco (not LOD)
        
Browsing images based on hierarchical faceted metadata and dynamically generated query previews.
        
Dynamically generated query previews [14]
        
Garber and Grunes [7] is a similar method to this paper:
- showed meteadate associated with a target image, presented images in an order reflecting the number of categories they had in common with the target image
- allowed the user to select a set of category labels, showing sample images for similar categories
(hierarchy information was not shown)
        
Faceted metadata: composed of orthogonal sets of categories (themes, artist names, time periods, media, countries, etc)
        
Metadata/facet may be flat or hierarchical
May be single-valued or multi-valued (think checkboxes)
        
Three 'levels':
- opening: major facets, with top-level categories
Once a category(s) is selected:
- Middle game: result set is shown, current queries at the top (categories can be removed from the queries). They can add subcategories from a menu on the left. Clicking on an artifact from the list takes them to:
- Endgame: shows a single selected item in the context of the current query. Query terms are displayed next to the item.
        
