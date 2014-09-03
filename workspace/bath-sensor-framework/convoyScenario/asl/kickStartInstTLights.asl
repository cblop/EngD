//no beliefs

!start.

+!start : true  <- .print("kick starting instituion scenario - using m25 waypoints");
						.wait(1000);
						.send(centralMember1, achieve, startUseInstTrafficLightFullSUMO);
						//.send(centralMember2, achieve, startUseInstTrafficLightFullSUMO);
						.print("done starting things").
