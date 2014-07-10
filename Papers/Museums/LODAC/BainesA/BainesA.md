# A pragmatic approach to middleware for distributed intelligent systems
## Baines, Vincent and Lee, Jeehang and Padget, Julian
- Pub-sub based distributed intelligent system (DIS)
- JSON and RDF over XML -> protocol
- RDF and RDF over JSON -> semantic annotation
        
**Every component is a sensor**
        
System requirements and resolution:
1. Event-based notification over wide-area networks: uses XMPP
2. Standardised data representations: RDF and JSON
3. Capacity to support different programming languages and legacy applications: if there's an XMPP API, it's supported
        
Uses off the shelf components where possible.
        
Uses openfire server for pubsub.
        
Network latency may be an issue.
        
Four layers (low to high):
1. network
2. agent
3. normative (rules)
4. human
        
MonitorRDF package is used to monitor network performance.
        
Agents connect to the BSF and interact with remote entities. Uses Jason BDI (belief/desire/intention) architecture.
        
Normative layer may be more suited to handle large volumes of high frequency data than agent layer.
        
Human layer represents what is going on in some visual form (graphical representation).
        
BSF was used to develop these apps:
        
## Intelligent convoys
Vehicles are components that respond to commands received (setOrientation; setSpeed) and publish geospatial data to the framework.
        
Intelligence layor (Jason) is subscribed to this geo info, which updates Jason BDI agents.
        
Has 3D viewer based on jMonkey.
        
Vehicles publish their locations to each other, use that info to follow. Or one vehicle can just publish waypoint info.
        
## Agents in Second Life
BDI agents in VR game respond to sensor information by pubsub.
        
Consumer and producer need not be aware of each others' identities in these scenarios.