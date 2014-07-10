# A description logic ontology for fairy tale generation
## Peinado, Federico and Gerv{\'a}s, Pablo and D{\'\i}az-Agudo, Bel{\'e}n

Page 61

Creates an OWL ontology oriented towards automatic story generation - exactly what we want to do!

Converts Propp's morphology into OWL-DL.

Propp says that folk tales are made from parts that change between tales and parts that don't change. He says that the names and certain attributes of the characters change, but their actions remain the same.

Top level:

- Character
- Description
- Place
- ProppFunction
	- Move
		- Preparation
		- Conflict
			- Villainy
			- Lack
			- ConnectiveIncident
			- ConsentToCounteraction
			- Departure
		- DonorMove
		- Helper
	- Resolution
	- AlienForms
	- Epilogue
- Role
- SymbolicObject

Case base is built up from texts from domain of fairy tales.

They use a structural CBR approach


## Follow-ups
Proppian fairy tale Markup Language (PftML) is worth looking at: Malec, 2004

Important computational models of story generation:
Meehan 1981, Rumelhart 1975, Lang 1997, Callaway and Lester 2002

Fairclough and Cunningham (2003) implement an interactive multiplayer story engine that operates over a way of describing stories based on Propp's work. Applies it to case-based planning and constraint satisfaction to make characters follow a coherent plot (Julian will be interested in this).

