package edu.bath.sumoVehicles;

import edu.bath.sensorframework.DataReading;

import edu.bath.sensorframework.client.ReadingHandler;
import edu.bath.sensorframework.client.SensorClient;
import edu.bath.sensorframework.sensor.Sensor;
import edu.bath.sensorframework.JsonReading;

import javax.vecmath.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.Color;

import org.jivesoftware.smack.XMPPException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

import it.polito.appeal.traci.*;
import it.polito.appeal.traci.protocol.*;
import it.polito.appeal.traci.ReadObjectVarQuery.StringListQ;

public class sumoXMPPSim extends Sensor {

	private boolean alive = true;
	private String currentLocation;
	private String primaryHandle;
	SensorClient sensorClient;
	private String temp;
	private final double updateRate = 0.5f;
	private boolean doneCheck = false;

	private static Double offSetX = 0.0D;
	private static Double offSetY = 0.0D;
	
	private static String vehicleName="sumo";
	private static String XMPPServer = "127.0.0.1";
	private static double lastUpdateTime=0;
	private static String jasonSensorVehicles = "jasonSensorVehicles";
	private static String jasonSensorVehiclesCmds = "jasonSensorVehiclesCmds";
	private static String aoiNodeName = "aoiSensor";
	private SumoTraciConnection conn;
	private RoadmapPosition newRoadPos;
	private long lastUpdateTimeNano=0;
	private long nanoToMili=1000000;
	private static String knownRoute = "";
	private static String scenarioUsed = "";
	private Map<String, InductionLoop> inductionloops;
	//private static SensorClient aoiSensorClient;
	private static WorkerNonThreadSender aoiMsgSender;
	private static WorkerInstSender instSensor;
	private static boolean singleRouteAllVehicles = true;
	private ArrayList<LaneDistance> laneDistances = new ArrayList<LaneDistance>();
	private ArrayList<VehicleDistancesPerLane> distancesPerLane = new ArrayList<VehicleDistancesPerLane>();
	private LinkedBlockingQueue<String> pendingVehicleCmdMessages = new LinkedBlockingQueue<String>();
	
	private LinkedBlockingQueue<String> pendingAOIMessages = new LinkedBlockingQueue<String>();	
	
	private ArrayList<String> knownLanes = new ArrayList<String>();
	private ArrayList<VehicleAdd> vehiclesToAdd = new ArrayList<VehicleAdd>();
	private ArrayList<String> modifiedVehicles = new ArrayList<String>();
	private CopyOnWriteArrayList<StopPositionPair> vehicleStops = new CopyOnWriteArrayList<StopPositionPair>();
	private CopyOnWriteArrayList<JasonVehicleLocalState> jasonVehicleStates = new CopyOnWriteArrayList<JasonVehicleLocalState>();
	private ArrayList<VehicleLane> foundVehLanes = new ArrayList<VehicleLane>();
	private int numberVehMessagesSent = 0;
	
	public sumoXMPPSim(String serverAddress, String id, String password, String nodeName, String currentLocation, String primaryHandle) throws XMPPException {
		super(serverAddress, id, password, nodeName);
		this.currentLocation = currentLocation;
		this.primaryHandle = primaryHandle;
	}
	
	public static void main(String[] args) throws Exception {
		
		if (args.length > 0) 
		{
			String scenarioFlag = args[0];
			System.out.println(scenarioFlag);
			if (scenarioFlag.equals("m25"))
			{
				scenarioUsed = scenarioFlag;
			}
			else if (scenarioFlag.equals("m25-vsl"))
			{
				scenarioUsed = scenarioFlag;
			}
			else if (scenarioFlag.equals("m25-crash"))
			{
				scenarioUsed = scenarioFlag;
			}
			else if (scenarioFlag.equals("bath"))
			{
				scenarioUsed = scenarioFlag;
				offSetX = 2385.33;
				offSetY = 1691.39;
			}
			else
			{
				scenarioUsed = "m25";
			}
		}
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));
			String line;
			while((line = br.readLine()) != null) 
			{
				if (line.contains("OPENFIRE"))
				{
					String[] configArray = line.split("=");
					XMPPServer = configArray[1];
					//System.out.println("Using config declared IP address of openfire server as: " + XMPPServer);
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("Error loading config.txt file");
			e.printStackTrace();
		}


		
		System.out.println("Using defaults: " + XMPPServer + ", central, jasonpassword, jasonSensor, http://127.0.0.1/vehicleSensors, http://127.0.0.1/vehicleSensors/vehicle1");
		sumoXMPPSim ps = new sumoXMPPSim(XMPPServer, vehicleName, "jasonpassword", jasonSensorVehicles , "http://127.0.0.1/vehicleSensors", "http://127.0.0.1/vehicleSensors/test1-vehicle");

		aoiMsgSender = new WorkerNonThreadSender(XMPPServer, "sumo-sender", "jasonpassword", aoiNodeName, "http://127.0.0.1/AOISensors", "http://127.0.0.1/AOISensors/SUMO");

		instSensor = new WorkerInstSender(XMPPServer, "sumo-inst-sender", "jasonpassword", "NODE_PERCEPT");

		Thread.currentThread().sleep(1000);

		ps.run();
		
	}
	
	
	public String getCurrentLocation() {
		return currentLocation;
	}
	
	public String getPrimaryHandle() {
		return primaryHandle;
	}


	public void run() throws XMPPException {	
		if (scenarioUsed.equals("m25"))
		{
			conn = new SumoTraciConnection(
			"m25.sumo.cfg",  // config file
			12345,                                 // random seed
			true                                  // look for geolocalization info in the map
			);
		}
		else if (scenarioUsed.equals("m25-vsl"))
		{
			conn = new SumoTraciConnection(
			"m25vsl.sumo.cfg",  // config file
			12345,                                 // random seed
			true                                  // look for geolocalization info in the map
			);
		}
		else if (scenarioUsed.equals("m25-crash"))
		{
			conn = new SumoTraciConnection(
			"m25crash.sumo.cfg",  // config file
			12345,                                 // random seed
			true                                  // look for geolocalization info in the map
			);
		}
		else if (scenarioUsed.equals("bath"))
		{
			conn = new SumoTraciConnection(
			"bath.sumo.cfg",  // config file
			12345,                                 // random seed
			true                                  // look for geolocalization info in the map
			);
		}
		try{
			conn.runServer();
			conn.nextSimStep();		
			conn.nextSimStep();
			Map<String, Lane> lanes = conn.getLaneRepository().getAll();
			for (Lane laneV : lanes.values()) 
			{
				//System.out.println(laneV);
				knownLanes.add(laneV.getID());
			}
			System.out.println("added " + knownLanes.size() + " to known lanes list");

			inductionloops = conn.getInductionLoopRepository().getAll();
			System.out.println("added " + inductionloops.size() + " to known induction loops");
//conn.getScreenShot("3.bmp");
//conn.getScreenShot("2.bmp");
//conn.getScreenShot("1.bmp");


		}
		catch(Exception e) 
		{
			System.out.println("exception in initial setup performing two timesteps and extracting lane info..");
			System.out.println(e);
		}
		
   
				

		System.out.println("Running as vehicle: " + vehicleName);
		while(sensorClient == null) {
			try {
				sensorClient = new SensorClient(XMPPServer, vehicleName+"-receiver", "jasonpassword");
				System.out.println("Guess sensor connected OK then!");
			} catch (XMPPException e1) {
				System.out.println("Exception in establishing client.");
				e1.printStackTrace();
			}
		}
		sensorClient.addHandler(jasonSensorVehiclesCmds, new ReadingHandler() 
		{ 
			@Override
			public void handleIncomingReading(String node, String rdf) {
				//System.out.println("received a vehicle command rdf");
				try {
					pendingVehicleCmdMessages.put(rdf);
				}
				catch (Exception e)
				{
					System.out.println("Error adding new message to queue..");
					e.printStackTrace();
				}
				
			}
		});
		try {
			sensorClient.subscribe(jasonSensorVehiclesCmds);
		} catch (XMPPException e1) {
			System.out.println("Exception while subscribing to " + jasonSensorVehiclesCmds);
			e1.printStackTrace();
		}

		sensorClient.addHandler(aoiNodeName, new ReadingHandler() 
		{ 
			@Override
			public void handleIncomingReading(String node, String rdf) {
				try {

					pendingAOIMessages.put(rdf);
				}
				catch (Exception e)
				{
					System.out.println("Error adding new message to queue..");
					e.printStackTrace();
				}
			}
		});
		try {
			sensorClient.subscribe(aoiNodeName);
		} catch (XMPPException e1) {
			System.out.println("Exception while subscribing to " + aoiNodeName);
			e1.printStackTrace();
		}

		while(alive) 
		{
			long currentLoopStartedNanoTime = System.nanoTime();

			try {
				if(sensorClient.checkReconnect())
				{
					sensorClient.subscribe(aoiNodeName);
					sensorClient.subscribe(jasonSensorVehiclesCmds);
				}
			} catch (XMPPException e1) {
				System.out.println("Couldn't reconnect!! ");
				e1.printStackTrace();
				try {
					System.out.println("trying to reconnect");
					Thread.sleep(30*1000);
				} catch (InterruptedException e) 
				{
					System.out.println("error in reconnection");
					e.printStackTrace();
				}
				continue;
			}
			
			//first add in vehicles that crashed and need to be put back into sim
			try {
				processVehicleToAdd();
			}
			catch (Exception e) 
			{
				System.out.println("processVehicleToAdd error");
				e.printStackTrace();
			}

			publishSimTime();
			
			//in this update cycle, first process pending messages
			long p1Time = System.nanoTime();
			processAOIMessages();
			processVehicleCmdMessages();
			long endTime = System.nanoTime() - p1Time;
			///System.out.println("pending messages took: " + endTime/nanoToMili);

			//now thats done, publish and update our sim info
			try
			{
				long t01 = System.currentTimeMillis();
				Map<String, Vehicle> vehicleMap = conn.getVehicleRepository().getAll();
				publishDetectorsAndLights();
				updateCrashedVehicles(vehicleMap);
				long t02 = System.currentTimeMillis() - t01;
				///System.out.println("post updateCrash took: " + t02);
	
				long t03 = System.currentTimeMillis();
				vehicleMap = conn.getVehicleRepository().getAll();
				updateSingleRouteVehicles(vehicleMap);
				long t04 = System.currentTimeMillis() - t03;
				///System.out.println("post updateSingleRouteVehicles took: " + t04);

				long t1 = System.currentTimeMillis();
				publishVehicleInfo(vehicleMap);
				long t2 = System.currentTimeMillis() - t1;
				///System.out.println("post publishVehicleInfo took: " + t2	);

				long t3 = System.currentTimeMillis();
				updateAllVehicles(vehicleMap);
				long t4 = System.currentTimeMillis() -t3;
				///System.out.println("post updateAllVehicles took: " + t4);
			}
			catch (Exception e)
			{
				System.out.println("Error in main update..");
				e.printStackTrace();
			}

			try 
			{
				//double newfNum = 1000000+conn.querySimTime();
				//conn.getScreenShot(newfNum+".bmp");
				//Thread.currentThread().sleep(50);
				conn.nextSimStep();
				
				//temp hack, adjust all vehicles to not change lane
				//conn.getAddedVehicles();
			}
			catch (Exception e) 
			{
				System.out.println("Error in main conn.nextSimStep call for some reason..");
				e.printStackTrace();
			}

			try {
				long currentNanoTime = System.nanoTime();
				long loopTimeNano = currentNanoTime - currentLoopStartedNanoTime;
				double simStepD = updateRate*1000;
				long simStepNano = (long) simStepD*nanoToMili;
				long remainingTime = simStepNano - loopTimeNano;
				if (remainingTime >=0 )
				{						
					waitUntil(currentNanoTime + remainingTime);
				}
				else
				{
					System.out.println("XX WARNING: loop time longer than sim interval");

				}
				
			} catch (Exception e) {
				System.out.println("Error post sim step wait..");
				e.printStackTrace();
			}
			
		}

		cleanup();
	}

	public void sendVehicleRDF(String sender, String type, String content)
	{
		try 
		{
			DataReading testReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
			testReading.setTakenBy("http://127.0.0.1/vehicles/"+sender);
			testReading.addDataValue(null, type, content, false);
			//System.out.println("publishing" + type);
			publish(testReading);
		} 
		catch (Exception e) 
		{
			System.out.println("Error in sendVehicleRDF method");
			e.printStackTrace();
		}
	}	

	public void sendVehicleRDFAOI(String sender, String type, String content)
	{
		try 
		{
			DataReading testReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
			testReading.setTakenBy("http://127.0.0.1/vehicles/"+sender);
			testReading.addDataValue(null, type, content, false);
			//System.out.println("publishing" + type);
			aoiMsgSender.generateAndSendMsg(testReading);
		} 
		catch (Exception e) 
		{
			System.out.println("Error in sendVehicleRDAOI method..");
			e.printStackTrace();
		}
	}

	public void publishLightInfo(Map<String, TrafficLight> tlights)
	{
		String controlledLanes = "";
		DataReading testReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
		testReading.setTakenBy("http://127.0.0.1/trafficLights/sumoXMPPSim");
		int numPublishedReadings = 0;
		System.out.println("called publish Light Info");
		for (TrafficLight tlight : tlights.values())
		{
			try 
			{
			
				List<Lane> laneList = tlight.queryReadControlledLanes().get();
				//System.out.println("got this many lanes for for loop " + laneList.size());
				
				for (Lane currentLane : laneList)
				{
					Path2D shape = currentLane.queryReadShape().get();
					PathIterator it = shape.getPathIterator(null);
					double[] coords = new double[6];
					while (!it.isDone()) {
						it.currentSegment(coords);
						it.next();
					}
					Point2D lastPoint = new Point2D.Double(coords[0], coords[1]);
					String coordString = coords[0] + ","+  coords[1];
					testReading.addDataValue(null, "http://127.0.0.1/trafficLights/Lanes", currentLane.getID(), false);
					testReading.addDataValue(null, "http://127.0.0.1/lanes/"+currentLane.getID(), coordString,false);
					numPublishedReadings++;
					//System.out.println("adding a lane light data reading");
				}
			}
			catch (Exception e)
			{
				System.out.println("Error in publishLightInfo method...");
				e.printStackTrace();
			}
		}
		//System.out.println("these lanes controlled by a signal " + controlledLanes);
		System.out.println("generated this many testReadings for controlled lanes : " + numPublishedReadings );
		//System.out.println(testReading.toRDF());
		aoiMsgSender.generateAndSendMsg(testReading);
	}

	private void processAOIMessages()
	{
		String rdfAOI = pendingAOIMessages.poll();
		while (rdfAOI != null)
		{
			try 
			{
				DataReading dr = DataReading.fromRDF(rdfAOI);
				String takenBy = dr.getTakenBy();

				DataReading.Value reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/sendAllTraficLights", null);
				if(reqVal != null) 
				{
					System.out.println("asked to send all traffic light info..");
					Map<String, TrafficLight> tlights = conn.getTrafficLightRepository().getAll();
					System.out.println("found this many sets of lights: " + tlights.size());
					publishLightInfo(tlights);					
				}
			}
			catch(Exception e) 
			{
				System.out.println("Error in processAOIMessages method..");
				System.out.println(e);
			}
			rdfAOI = pendingAOIMessages.poll();
		}
	}

	private void processVehicleCmdMessages()
	{
			//System.out.println("this many msg in queue " + pendingVehicleCmdMessages.size());
			String rdf = pendingVehicleCmdMessages.poll();
			while (rdf != null)
			{
				//System.out.println("rdf is " + rdf);
				try 
				{
					//System.out.println("this many msg in queue " + pendingVehicleCmdMessages.size());
					DataReading dr = DataReading.fromRDF(rdf);
					String takenBy = dr.getTakenBy();

					if ((dr.getTimestamp() < lastUpdateTime) && lastUpdateTime != 0)
					{
						System.out.println("XXXXXXXX out of sync dr.timestamp XXXXXXXXXXX ");
						System.out.println("Time stamp of dr is " + dr.getTimestamp());
						System.out.println("Time stamp of last updated is " + lastUpdateTime);
					}
					else if (dr.getTimestamp() == lastUpdateTime)
					{
						//	System.out.println("maybe sync issue occuring");
					} 
					lastUpdateTime = dr.getTimestamp();
					boolean foundTargetVehicle = false;
					
					DataReading.Value reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setOrientation", null);
					if(reqVal != null) 
					{
						foundTargetVehicle = true;
						String reqMsg = (String)reqVal.object;
						//String[] orientationReq = reqMsg.split(",");
						Double newOrientation = Double.parseDouble(reqMsg);
						System.out.println("XXX: Received a setOrientation request, SUMO can't handle this.. ");
						//currentOrientation = newOrientation;
					}
					
					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setSpeed", null);
					if(reqVal != null) 
					{
						System.out.println("asked to set speed with old option, for " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							//System.out.println(vehicle.getID());
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								String reqMsg = (String)reqVal.object;
								Double newSpeed = Double.parseDouble(reqMsg);
								ChangeSpeedQuery csq = vehicle.queryChangeSpeed();
								csq.setValue(newSpeed);
								csq.run();
								//System.out.println("updated speed of " + vehicle.getID() + " to " + newSpeed);
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so message not communicated");
						}
					}

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setSpeedOverTime", null);
					if(reqVal != null) 
					{
						//System.out.println("asked to set speed for " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							//System.out.println(vehicle.getID());
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								String reqMsg = (String)reqVal.object;
								Double newSpeed = Double.parseDouble(reqMsg);
								ChangeSpeedOverTimeQuery csq = vehicle.queryChangeSpeedOverTime();
								csq.setValue(newSpeed);
								csq.run();
								System.out.println("updated speed of " + vehicle.getID() + " to " + newSpeed);
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so message not communicated");
						}
					}

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/modifySpeedOverTime", null);
					if(reqVal != null) 
					{
						System.out.println("asked to set modify for " + takenBy);
						Double currentVehSpeed = -1.0d;
						for (JasonVehicleLocalState checkVeh : jasonVehicleStates)
						{
							//System.out.println("checking " + checkVeh.getName() + " against " + takenBy);
							if (takenBy.contains(checkVeh.getName()))
							{
								currentVehSpeed = checkVeh.getSpeed();
							}
						}
						if (currentVehSpeed >= 0)
						{
		
							Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
							for (Vehicle vehicle : vehicles.values()) 
							{
								//System.out.println(vehicle.getID());
								if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
								{
									foundTargetVehicle = true;
									String reqMsg = (String)reqVal.object;
									Double speedDelta = Double.parseDouble(reqMsg);
									Double newSpeed = currentVehSpeed+speedDelta;
									ChangeSpeedOverTimeQuery csq = vehicle.queryChangeSpeedOverTime();
									csq.setValue(newSpeed);
									csq.run();
									System.out.println("modified speed of " + vehicle.getID() + " to " + newSpeed);
								}
							}
							if (!foundTargetVehicle)
							{
								System.out.println("didn't find " + takenBy + " so message not communicated");
							}
						}
						else
						{
							System.out.println("couldn't find speed of " + takenBy + " so unable to modify its speed by requested value");
						}
					}

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setAutonomy", null);
					if(reqVal != null) 
					{
						System.out.println("asked to set autonomy " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							//System.out.println(vehicle.getID());
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								String reqMsg = (String)reqVal.object;
								int autState = Integer.parseInt(reqMsg);
								ChangeSpeedModeQuery csmq = vehicle.queryChangeSpeedMode();
								csmq.setValue(autState);
								csmq.run();
								System.out.println("updated autonomy state of " + vehicle.getID() + " to " + autState);
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so setAutonomy message not communicated");
						}
					}

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setLaneChange", null);
					if(reqVal != null) 
					{
						System.out.println("asked to set lane change mode " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							//System.out.println(vehicle.getID());
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								String reqMsg = (String)reqVal.object;
								int laneState = Integer.parseInt(reqMsg);
								ChangeLaneChangeModeQuery clcmq = vehicle.queryChangeLaneChangeMode();
								clcmq.setValue(laneState);
								clcmq.run();
								System.out.println("updated lane change state of " + vehicle.getID() + " to " + laneState);
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so setLaneChange message not communicated");
						}
					}

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setRoute", null);
					if(reqVal != null) 
					{
						System.out.println("asked to set route for " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								String routeMsg = (String)reqVal.object;
								ChangeRouteByIDQuery crq = vehicle.queryChangeRouteByID();
								crq.setValue(routeMsg);
								crq.run();
								System.out.println("updated route of " + vehicle.getID() + " to " + routeMsg);
								ChangeResumeQuery cresumeq = vehicle.queryChangeResume();
								cresumeq.setValue(0);
								cresumeq.run();
								System.out.println("sending resume");

								int deletePos=-1;
								for (int stopNum=0; stopNum < vehicleStops.size(); stopNum++) 
								{
									System.out.println(vehicle.getID() + " already has a stop location, cancelling that before setting off on new route");
									StopPositionPair vehicleStop = vehicleStops.get(stopNum);
									if (vehicleStop.getStopVehicleName().equals(vehicle.getID()))
									{
										ChangeStopZeroQuery cstopzq = vehicle.queryChangeStopZero();
										cstopzq.setValue(vehicleStop.getStopVehicleRoadpos());
										cstopzq.run();
										deletePos = stopNum;
									}
								}
								if (deletePos >= 0)
								{
									vehicleStops.remove(deletePos);
								}
								else
								{
									System.out.println("no stop position found to delete");
								}

								ValueReadQuery<List<Edge>> routeQuery = vehicle.queryReadCurrentRoute();
								List<Edge> foundEdges = routeQuery.get();
								//System.out.println("found " + foundEdges.size() + " on this new route");
								String edgeListCSV="";
								for (int i=0; i<foundEdges.size(); i++)
								{
									if (edgeListCSV.equals(""))
									{
									edgeListCSV = foundEdges.get(i).getID();
									}
									else
									{
										edgeListCSV = edgeListCSV + "," + foundEdges.get(i).getID();
									}
								}
								sendVehicleRDFAOI(vehicle.getID(), "http://127.0.0.1/sensors/route#edges", edgeListCSV);
								System.out.println("sent edge list of " + edgeListCSV + " for " + vehicle.getID());
							}
						}


						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so route change message not communicated");
						}
					}

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/quickLaneChange", null);
					if(reqVal != null) 
					{
						System.out.println("asked to quickly change lane by " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							//System.out.println(vehicle.getID());
							String thisVehName = vehicle.getID();
							if (takenBy.equals("http://127.0.0.1/agent/"+thisVehName))
							{
								for (JasonVehicleLocalState checkVeh : jasonVehicleStates)
								{
									if (checkVeh.getName().equals(thisVehName))
									{
										int nowLane = checkVeh.getCurrentLane();
										String edgeName = checkVeh.getCurrentEdge();
										//routes are done assuming europe style, so we actually want to increment and see if that lane exists for us to move to
										String mainRoad = edgeName;//edgeName.substring(0,edgeName.length()-1);
										int newLaneReq= nowLane+1;
										System.out.println("XXX: in lane " + nowLane + " of " + edgeName + " target lane is " + newLaneReq);
										String desiredEdge = mainRoad + "_" + newLaneReq;
										System.out.println("trying to move to " + desiredEdge);
										if (knownLanes.contains(desiredEdge))
										{
											System.out.println("moving in, ok to move to " + desiredEdge);	
											ChangeLaneQuery clq = vehicle.queryChangeLane();
											clq.setValue(newLaneReq);
											clq.run();
										}
										else
										{
											newLaneReq= nowLane-1;
											desiredEdge = mainRoad + "_" + newLaneReq;
											if (knownLanes.contains(desiredEdge))
											{
												System.out.println("couldnt move in, but ok to move to " + desiredEdge);	
												ChangeLaneQuery clq = vehicle.queryChangeLane();
												clq.setValue(newLaneReq);
												clq.run();
											}
											else
											{
												System.out.println("WARNING! Currently in " + edgeName + " and can't move to any known alternative lane!");
											}
										}
										foundTargetVehicle = true;
									}
								/*
								String reqMsg = (String)reqVal.object;
								int laneState = Integer.parseInt(reqMsg);
								ChangeLaneChangeModeQuery clcmq = vehicle.queryChangeLaneChangeMode();
								clcmq.setValue(laneState);
								clcmq.run();
								System.out.println("updated lane of " + thisVehName + " to " + laneState);*/
								}
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " in Jason vehicle list");
						}
					}		

					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/setFrontLights", null);
					if(reqVal != null) 
					{
						//System.out.println("asked to turn set front lights " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						for (Vehicle vehicle : vehicles.values()) 
						{
							//System.out.println(vehicle.getID());
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								String reqMsg = (String)reqVal.object;
								int lightState=0;
								if (reqMsg.equals("on"))
								{
									lightState=4;
								}
								ChangeLightsQuery cLq = vehicle.queryChangeLights();
								cLq.setValue(lightState);
								cLq.run();
								System.out.println("updated light state of " + vehicle.getID() + " to " + reqMsg);
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so lightState message not communicated");
						}
					}	
					
					
					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/addVehicle", null);
					if(reqVal != null) 
					{
						String reqString = (String)reqVal.object;
						String id = reqString;
						String routeName = "dummyRoute";
						int lane = 0;
						int speed = 0;
						int departTime = -1;
						if (reqString.contains(","))
						{
							String[] splitString = reqString.split(",");
							if (splitString.length == 2)
							{
								id = splitString[0];
								routeName = splitString[1];
							}
							else if (splitString.length == 3)
							{
								id = splitString[0];
								routeName = splitString[1];
								lane = Integer.parseInt(splitString[2]);
							}
							else if (splitString.length == 4)
							{
								id = splitString[0];
								routeName = splitString[1];
								lane = Integer.parseInt(splitString[2]);
								speed = Integer.parseInt(splitString[3]);
							}
							else if (splitString.length == 5)
							{
								id = splitString[0];
								routeName = splitString[1];
								lane = Integer.parseInt(splitString[2]);
								speed = Integer.parseInt(splitString[3]);
								departTime = Integer.parseInt(splitString[4]);
								System.out.println("set depart time to " + departTime);
							}						
							else
							{
								System.out.println("length of args was " + splitString.length + " not sure what to do");
							}
						}
						System.out.println("asked to add a new vehicle: " +id );
						//TODO: fix this - should be passed as a second data point really, better than just assuming central 1 is the lead..
						//String id = (String)reqVal.object;							
						Route route = conn.getRouteRepository().getByID(routeName);
						/*if (id.equals("centralMember1"))
						{
							route = conn.getRouteRepository().getByID("dummyRoute");
						}*/
												

						VehicleType vType = conn.getVehicleTypeRepository().getByID("BSFCar");
						if (route != null && vType !=null)
						{
							AddVehicleQuery avq = conn.queryAddVehicle();
							System.out.println("sim time " + conn.querySimTime() + " and departTime set to " + departTime);
							avq.setVehicleData(id, vType, route, lane, 0, speed, departTime);
							avq.run();
							System.out.println("added " + id);
							ValueReadQuery<List<Edge>> routeQuery = route.queryReadRoute();
							List<Edge> foundEdges = routeQuery.get();
							//System.out.println("found " + foundEdges.size() + " on this new route");
							String edgeListCSV="";
							for (int i=0; i<foundEdges.size(); i++)
							{
								if (edgeListCSV.equals(""))
								{
									edgeListCSV = foundEdges.get(i).getID();
								}
								else
								{
									edgeListCSV = edgeListCSV + "," + foundEdges.get(i).getID();
								}
							}
							sendVehicleRDFAOI(id, "http://127.0.0.1/sensors/route#edges", edgeListCSV);
							//System.out.println("sent edge list of " + edgeListCSV + " for " + id);
							JasonVehicleLocalState newJasonVehicle = new JasonVehicleLocalState(id);
							boolean alreadyExists=false;
							for (JasonVehicleLocalState checkVeh : jasonVehicleStates)
							{
								String foundVehName = checkVeh.getName();
								if (checkVeh.equals(id))
								{
									alreadyExists=true;
								}
							}
							if (!alreadyExists)
							{
								jasonVehicleStates.add(newJasonVehicle);
							}
						}
						else
						{
							System.out.println("couldn't find the dummy f or vehicle type for adding vehicles");
						}					
					}	
					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/moveTo", null);
					if(reqVal != null) 
					{
						//System.out.println("asked to move to location by " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						
						for (Vehicle vehicle : vehicles.values()) 
						{
							String thisVehicleID = vehicle.getID();
							if (takenBy.equals("http://127.0.0.1/agent/"+thisVehicleID))
							{
								foundTargetVehicle = true;
								String reqMsg = (String)reqVal.object;
								String[] arrayMsg = reqMsg.split(",");
								if (arrayMsg.length == 2)
								{
									Double x = Double.parseDouble(arrayMsg[0]);
									Double y = Double.parseDouble(arrayMsg[1]);
									//System.out.println("move to: " + x + " , " + y);
									PositionConversionRoadmapQuery pcq = conn.queryPositionRoadmapConversion();
									Point2D convPos = new Point2D.Double(x, y);
									//true is for latlon, false for UTM..
									pcq.setPositionToConvert(convPos,true);
									///MultiQuery multi = conn.makeMultiQuery();
									try 
									{
										
										RoadmapPosition requestedRoadPos = pcq.get();
										//System.out.println("trying to send stop on edge: " + requestedRoadPos.edgeID);
										//System.out.println("change target");
										ChangeTargetQuery ctq = vehicle.queryChangeTarget();
										//System.out.println("ctq created");
										ctq.setValue(conn.getEdgeRepository().getByID(requestedRoadPos.edgeID));
										//System.out.println("ctq value set");
										///multi.add(ctq);
										ctq.run();
										//System.out.println("route updated");
									}
									catch(Exception e) 
									{
										System.out.println("change target failed");
										e.printStackTrace();
									} 
									
									try
									{	
										for (JasonVehicleLocalState checkVeh : jasonVehicleStates)
										{
											if (checkVeh.getName().equals(thisVehicleID))
											{	
												//System.out.println("checking park status for " + thisVehicleID);
												//in theory we only need to send a resume if the vehicle is fully parked, otherwise its not really valid..
												if (checkVeh.getIsParkedState())
												{
													ChangeResumeQuery cresumeq = vehicle.queryChangeResume();
													cresumeq.setValue(0);
													cresumeq.run();
													System.out.println("sending resume");
												}
											}	
										}
									}
									catch(Exception e)
									{
										//System.out.println("resume failed, vehicle probably still moving");
									}
									try
									{
										//cancel any previous stop for this vehicle if there's one pending
										int deletePos=-1;
										for (int stopNum=0; stopNum < vehicleStops.size(); stopNum++) 
										{
											//System.out.println(thisVehicleID + " already has a stop location, cancelling that before setting new one for this moveTo");
											StopPositionPair vehicleStop = vehicleStops.get(stopNum);
											if (vehicleStop.getStopVehicleName().equals(thisVehicleID))
											{
												ChangeStopZeroQuery cstopzq = vehicle.queryChangeStopZero();
												cstopzq.setValue(vehicleStop.getStopVehicleRoadpos());
												cstopzq.run();
												deletePos = stopNum;
											}
										}
										if (deletePos >= 0)
										{
											vehicleStops.remove(deletePos);
										}
										
										RoadmapPosition requestedRoadPos = pcq.get();
										//System.out.println("done moveTo, route should be recalculated so now send stop..");
										ChangeStopQuery cstopq = vehicle.queryChangeStop();
										cstopq.setValue(requestedRoadPos);
										//as we're in theory single threading now, the previous for loop should have deleted any stops.. but lets check anyway
										for (StopPositionPair vehicleStop : vehicleStops)
										{
											if (vehicleStop.getStopVehicleName().equals(thisVehicleID))
											{
												System.out.println("XXXXXXXX problem - there's a stop position hanging around which shouldnt exist");
											}
										}
										StopPositionPair newVehicleStop = new StopPositionPair(thisVehicleID, requestedRoadPos);
										vehicleStops.add(newVehicleStop);
										
										//multi.add(cstopq);
										cstopq.run();							
									}
									catch (Exception e)
									{
										//TODO: FIX THIS!! 
										//System.out.println("stop query failed - SUPPRESING STACK");
										///e.printStackTrace();
									}
									//System.out.println("kicking off multi run");
									//multi.run();
									//System.out.println("multirun returned");
								}
								else
								{
									System.out.println("didn't handle this: " + reqMsg);
								}
							}
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so moveTo message not communicated");
						}
					}
					reqVal = dr.findFirstValue(null, "http://127.0.0.1/request/resume", null);
					if(reqVal != null) 
					{
						System.out.println("asked to resume movement by " + takenBy);
						Map<String, Vehicle> vehicles = conn.getVehicleRepository().getAll();
						
						for (Vehicle vehicle : vehicles.values()) 
						{
							if (takenBy.equals("http://127.0.0.1/agent/"+vehicle.getID()))
							{
								foundTargetVehicle = true;
								try 
								{
									ChangeResumeQuery cresumeq = vehicle.queryChangeResume();
									cresumeq.setValue(0);
									cresumeq.run();
									System.out.println("set resume for " + vehicle.getID());
								}
								catch(Exception e) 
								{
									System.out.println("error in resume request");
									e.printStackTrace();
								} 
							}
							
						}
						if (!foundTargetVehicle)
						{
							System.out.println("didn't find " + takenBy + " so message not communicated");
						}
					}
										
					//Thread.currentThread().sleep(5000);
				}
				catch(Exception e) 
				{
					System.out.println("Error working through pendingVehicleCmdMessage..");
					System.out.println(e);
				}
			
			rdf = pendingVehicleCmdMessages.poll();
			
			}
	}


	private void updateCrashedVehicles(Map<String, Vehicle> vehicles)
	{
			try
			{	
				Route uniqueR = conn.getRouteRepository().getByID("m25");
		
				//get teleported vehicle names - i.e. ones that crashed
				List<String> teleportedVeh = conn.getTeleportedVehicles();
				
				//System.out.println("processing " + teleportedVeh.size() + " crashed vehicles ");
				for (String vehTel : teleportedVeh)
				{
					//need to send vehicle update from here, because it will no longer appear in the SUMO vehicle list used below
					sendVehicleRDF("http://127.0.0.1/vehicles/"+vehTel, "http://127.0.0.1/sensors/types#healthState", "crashed");
					//delete the vehicle to stop it reappearing
					Vehicle deletedVehicle = null;
					for (Vehicle vehicle : vehicles.values()) 
					{
						if (vehicle.getID().equals(vehTel))
						{
							deletedVehicle=vehicle;
						}
					}
					if (deletedVehicle != null)
					{
						System.out.println("removing " + deletedVehicle.getID() + " from sim");					
						double lanePos = deletedVehicle.queryReadLanePosition().get();
						String id = deletedVehicle.getID();
						int laneNum = deletedVehicle.queryReadCurrentLaneIndex().get();
						List<Edge> myRoute = deletedVehicle.queryReadCurrentRoute().get();

						RemoveVehicleQuery rvq = conn.queryRemoveVehicle();
						rvq.setVehicleData(deletedVehicle, 0);
						rvq.run();
						
						VehicleType crashedVType = conn.getVehicleTypeRepository().getByID("brokenVeh");
						if (crashedVType == null )
						{
							System.out.println("Couldn't find brokenVeh vehicle type!!!");
						}

						//now add it back in, with same position but max speed zero
						//ahh can't add it directly back in as name conflicts! have to add to list and process
						//System.out.println("i think crashed vehicle should be in lane " + laneNum + " at pos " + lanePos);conn.getRouteRepository().getAll().size() > 1

						for (JasonVehicleLocalState jV : jasonVehicleStates)
						{
							if (jV.getName().equals(id))
							{
								//System.out.println("but really its at " + jV.getPosition() + " and lane " + jV.getCurrentLane());

								//if theres one unique route we dont need to bother with this
								if (!singleRouteAllVehicles)
								{
									String nextEdge="1";
									String finalEdge="2";
									for (int i=0; i < myRoute.size(); i++)
									{
										if (myRoute.get(i).getID().equals(jV.getCurrentEdge()))
										{
											System.out.println("next part of route is " + myRoute.get(i+1).getID());
										
											nextEdge = myRoute.get(i+1).getID();
											//finalEdge = myRoute.get(i+2).getID();
										}
									}
									myRoute.clear();
								
									System.out.println("route should be " + jV.getCurrentEdge() + " " + nextEdge);
									myRoute.add(conn.getEdgeRepository().getByID(jV.getCurrentEdge()));
									myRoute.add(conn.getEdgeRepository().getByID(nextEdge));
								}
								lanePos = jV.getPosition();
								laneNum = jV.getCurrentLane();
							}
						}

						Route crashedRoute = uniqueR;
						if (!singleRouteAllVehicles)
						{
							System.out.println("seems to be more than one route in action so add new crashd route");
							final String routeID = "crashedRoute"+id;
							AddRouteQuery arq = conn.queryAddRoute();
							arq.setVehicleData(routeID, myRoute);
							arq.run();
							crashedRoute = conn.getRouteRepository().getByID(routeID);
							List<Edge> newEd = crashedRoute.queryReadRoute().get();
							System.out.println(routeID + " has " + newEd.size() + " edges");
						}
											
						if (crashedRoute == null)
						{
							System.out.println("WARN: Something went wrong in route selection as its null!!");
						}

						VehicleAdd newV = new VehicleAdd(id, crashedVType , crashedRoute, laneNum, lanePos, 0d);
						
						vehiclesToAdd.add(newV);
						
						System.out.println("added crashed vehicle, should be in lane " + laneNum + " at pos " + lanePos);
						//TODO: im not sure this is great, but its the only way I can think of to update
						conn.nextSimStep();
					}

				}
			}
			catch (Exception e) 
			{
				System.out.println("exception in updateCrashedVehicles..");
				e.printStackTrace();
			}
	}
	
	private void updateSingleRouteVehicles(Map<String, Vehicle> vehicles)
	{		
			try
			{
				//vehicleDistances.clear();
				for (VehicleDistancesPerLane tempVD : distancesPerLane)
				{
					tempVD.clearDistances();
				}
				
				if (singleRouteAllVehicles)
				{
					foundVehLanes.clear();
					Map<String, Edge> allEdges = conn.getEdgeRepository().getAll();
					//System.out.println(allEdges.size() + " many edges");
					for (Edge testEdge : allEdges.values())
					{
						String tEName = testEdge.getID();
						if (tEName.equals("14394795") || tEName.equals("34706943") || tEName.equals("188881551") || tEName.equals("23189135") || tEName.equals("140992312") || tEName.equals("3256124") || tEName.equals("131470133") || tEName.equals("23189048") || tEName.equals("4837085") || tEName.equals("19825229"))
						{
							List<String> foundIDs = testEdge.queryReadVehicleIDs().get();
							if (foundIDs.size() > 0)
							{
								String edgeName = testEdge.getID();
								for (String idString : foundIDs)
								{
									//System.out.println("found " + idString + " vehicle in " + edgeName);
									VehicleLane foundVL = new VehicleLane(idString,edgeName);
									foundVehLanes.add(foundVL);
								}
							}
						}
					}

					//TODO: far from optimal, but lets build up a list of all vehicles and their position along the route first, so that when publish vehicle position, we can publish distance to vehicle in front too
					//long timeTakenGet = 0;
					for (Vehicle vehicle : vehicles.values()) 
					{
						String sumoName = vehicle.getID();
						//work out vehicles position along entire route, only works for unique routes
						//long t1 = System.currentTimeMillis();
						///Edge currentEdge = vehicle.queryReadCurrentEdge().get();
						//System.out.println("took " + (System.currentTimeMillis() - t1));
						//timeTakenGet = timeTakenGet + (System.currentTimeMillis() - t1);
						int laneIntVal = vehicle.queryReadCurrentLaneIndex().get();

						String edgeName = "";
						for (VehicleLane vl : foundVehLanes)
						{
							if(vl.getID().equals(sumoName)) { edgeName=vl.getEdge(); } 
						}
						//System.out.println(sumoName + " at " + edgeName);
						
						if (!edgeName.equals("") )
						//if (currentEdge != null )
						{
							if (!laneDistances.isEmpty())
							{
								int lanePos = -1;
								for (int i=0; i<laneDistances.size(); i++)
								{
									LaneDistance ld = laneDistances.get(i);
									if (ld.laneID.equals(edgeName))
									//if (ld.laneID.equals(currentEdge.getID()))
									{
										lanePos=i;
									}
								}
								if (lanePos >= 0)
								{
									//System.out.println("int for " + edgeName + " is " + lanePos);
									Double totalRouteDist = laneDistances.get(lanePos).totalDist + vehicle.queryReadLanePosition().get();				
									VehicleDistance vd = new VehicleDistance(sumoName,totalRouteDist);
									//System.out.println(sumoName + " at " + totalRouteDist);
									boolean foundLaneForVehicle = false;
									for (VehicleDistancesPerLane checkVDLane : distancesPerLane)
									{
										if (checkVDLane.getLaneVal() == laneIntVal)
										{
											checkVDLane.addVehicleDistance(vd);
											foundLaneForVehicle=true;
										}
									}
									if (!foundLaneForVehicle)
									{
										System.out.println("no lane distances created yet for lane " + laneIntVal);
										VehicleDistancesPerLane newVDLane = new VehicleDistancesPerLane(laneIntVal);
										newVDLane.addVehicleDistance(vd);
										distancesPerLane.add(newVDLane);
									}
									
								}
								else
								{
									System.out.println("vehicle distance calculation failed for some reason");
								}
							}
						}
					}

					//now vehicleDistances should contain all vehicles and their distance along route.
					//iterate that list, and populate with distance to nearest vehicle ahead of it
					//this completes really quickly..
					for (VehicleDistancesPerLane processLane : distancesPerLane)
					{
						processLane.populateGaps();
						processLane.sortAlongRoute();
					}
					//System.out.println(timeTakenGet + " extracting data from SUMO..");

				}

				//now sort the list by distance along route
				//Collections.sort(vehicleDistances);
			}	
			catch (Exception e2)
			{
				System.out.println("exception in updateSingleRouteVehicles..");
				e2.printStackTrace();
			}
	}

	private void publishVehicleInfo(Map<String, Vehicle> vehicles)
	{	
		long tTime = 0;	
		long exT = 0;	
			try
			{
				
				for (Vehicle vehicle : vehicles.values()) 
				{
					long ex1 = System.currentTimeMillis();
					Point2D sumoPos = vehicle.queryReadPosition().get();			
					Double sumoTempAngle = vehicle.queryReadAngle().get();
	
					Double sumoAngle = 0D;
					if (sumoTempAngle <= 0)
					{
						sumoAngle = Math.abs(sumoTempAngle) + 180;
					}
					else
					{
						sumoAngle = 180 - sumoTempAngle;
					}

					String sumoName = vehicle.getID();
															
					boolean brakeLightOn = false;
					boolean frontLightsOn = false;
					boolean turnLeftLightOn = false;
					boolean turnRightLightOn = false;
					int signalState = vehicle.queryReadSignalState().get();
					String binarySigState = Integer.toBinaryString(signalState);
					Double fuelUsed =-1.0;
					Double co2Val =-1.0;
					Double coVal =-1.0;
					Double hcVal =-1.0;
					Double pmxVal =-1.0;
					Double noxVal =-1.0;
					Double currentSpeed = -1.0d;
					int currentLaneInt = -1;
					Edge currentEdge = null;
					String edgeName = "";
					
					//update locally held jason vehicle data if this vehicle was created by Jason
					try 
					{	
						currentLaneInt = vehicle.queryReadCurrentLaneIndex().get();
												
						for (VehicleLane vl : foundVehLanes)
						{
							if(vl.getID().equals(sumoName)) 
							{ 
								edgeName=vl.getEdge(); 
							} 
						}

						for (JasonVehicleLocalState checkVeh : jasonVehicleStates)
						{
							if (checkVeh.getName().equals(sumoName))
							{
								//Also get the vehicles current speed, can add it as a data reading later
								ValueReadQuery<Double> readSpeedQuery = vehicle.queryReadSpeed();
								
								currentEdge = vehicle.queryReadCurrentEdge().get();

								double foundPos = vehicle.queryReadLanePosition().get();
								checkVeh.setPosition(foundPos);
	
								//get vehicle performance metrics, fuel, emissions..
								fuelUsed = vehicle.queryReadFuelConsumption().get();
								co2Val = vehicle.queryReadCO2Emission().get();
								coVal = vehicle.queryReadCOEmission().get();
								hcVal = vehicle.queryReadHCEmission().get();
								pmxVal = vehicle.queryReadPMXEmission().get();
								noxVal = vehicle.queryReadNOXEmission().get();
							
								//GET PARKING STATE AND SET IT HERE, THEN CAN BE USED IN THE MOVE CMD
								Integer parkState = vehicle.queryReadStopState().get();
								if (parkState > 0)
								{
									checkVeh.setParked(true);
								}
								else
								{
									checkVeh.setParked(false);
								}	

								
								
								if (currentEdge != null )
								{
									checkVeh.setCurrentEdge(currentEdge.getID());
									if (singleRouteAllVehicles)
									{
										Double lanesDistance = 0d;
										boolean foundMyLane = false;
										for (LaneDistance ld : laneDistances)
										{
											if (ld.laneID.equals(currentEdge.getID()))
											{
												foundMyLane = true;
												lanesDistance=ld.totalDist;
											}
										}
										//int lanePos = laneDistances.indexOf(currentEdge.getID());
										//if (lanePos >= 0)
										if (foundMyLane)
										{
											Double totalRouteDist = lanesDistance + vehicle.queryReadLanePosition().get();	
										}
										else
										{
											System.out.println(currentEdge.getID() + " of " + checkVeh.getName() + " doesn't appear on valid route yet..");
										}
									
									}
								}
								if (currentLaneInt >= 0)
								{
									checkVeh.setCurrentLane(currentLaneInt);
								}
								//List<Edge> routeEdges = vehicle.queryReadCurrentRoute().get();
								//System.out.println(sumoName + " has " + routeEdges.size() + " edges in current route");
							
							}
						}
					}
					catch (Exception e3)
					{
						System.out.println("error working through jasonVehicleStates..");
						e3.printStackTrace();
					}

					//long ex1 = System.currentTimeMillis();
					//comment this out to just output fuel used of Jason vehicles...
					//Though this doubles the query if we've already run it for these ones
					//fuelUsed = vehicle.queryReadFuelConsumption().get();

					if (binarySigState.length() == 4 && binarySigState.charAt(0)=='1')
					{
						if (binarySigState.charAt(1)=='1')
						{
							frontLightsOn=true;
							//System.out.println(sumoName + " has front lights on and brake lights!!");
						}
						brakeLightOn = true;
						//System.out.println(sumoName + " brakes on!");
					}
					else if (binarySigState.length() == 3 && binarySigState.charAt(0)=='1')
					{
						frontLightsOn = true;
						//System.out.println(sumoName + " has front lights on!");
					}
					else if (binarySigState.length() == 2 && binarySigState.charAt(0)=='1')
				 	{
						turnLeftLightOn = true;
						//System.out.println(sumoName + " indicating left!");
					}
					else if (binarySigState.length() == 1 && binarySigState.charAt(0)=='1')
					{
						turnRightLightOn = true;
						//System.out.println(sumoName + " indicating right!");
					}
					else if (binarySigState.length() == 1 && binarySigState.charAt(0)=='0' )
					{
						//TODO: send a signals here? actually, we're set all to off unless turned on above, so should work fine..
					}
					else
					{
						System.out.println("ERROR: " + sumoName  + " signal state not handled: " + signalState + " binary " + binarySigState + " and length was " + binarySigState.length());								
					}
					
					String lightState = turnLeftLightOn + "," + turnRightLightOn + "," + brakeLightOn + "," + frontLightsOn;
					exT = exT+ (System.currentTimeMillis() - ex1);

					//now get vehicle emission / fuel consumption data... questionable exactly where this should be published, but
					//it does relate to the vehicle, so maybe here is the right place..?
				
					try 
					{
						DataReading testReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
						testReading.setTakenBy("http://127.0.0.1/vehicles/"+sumoName);
						String spatialInfo = ((sumoPos.getY()-offSetY) + ", 0, " + (sumoPos.getX()-offSetX) + ", " + sumoAngle);
						testReading.addDataValue(null, "http://127.0.0.1/sensors/types#spatial", spatialInfo, false);

						if (fuelUsed >= 0 )
						{
							testReading.addDataValue(null, "http://127.0.0.1/sensors/types#fuelUsedML", fuelUsed, false);
						}
							
						//assuming if one emission value has been found then all were attempted to be found				
						if (co2Val >=0 )
						{
							String emissionString= new String(co2Val + "," + coVal + "," + hcVal +","+pmxVal+","+noxVal);
							testReading.addDataValue(null, "http://127.0.0.1/sensors/types#emissions", emissionString, false);
						}

						ValueReadQuery<Double> readSpeedQuery = vehicle.queryReadSpeed();
						currentSpeed= readSpeedQuery.get();
						if (currentSpeed >= 0)
						{
							testReading.addDataValue(null, "http://127.0.0.1/sensors/types#vehicleSpeed", currentSpeed, false);
						}
						testReading.addDataValue(null, "http://127.0.0.1/sensors/types#LightState", lightState, false);
						/* replace this with lookup table approach to edges..
						if ((currentLaneInt >= 0) && (currentEdge != null))
						{
							testReading.addDataValue(null, "http://127.0.0.1/sensors/types#EdgeLane", currentEdge.getID()+"_"+currentLaneInt, false);
						}*/

						if ((currentLaneInt >= 0) && (!edgeName.equals("")))
						{
							testReading.addDataValue(null, "http://127.0.0.1/sensors/types#EdgeLane", edgeName+"_"+currentLaneInt, false);
						}	

						//add distance to vehicle ahead and current route distance if applicable
						for (VehicleDistancesPerLane tempVD : distancesPerLane)
						{
							if (!tempVD.getDistances().isEmpty() && singleRouteAllVehicles)
							{
								Double routeDist = -1d;
								Double vehAheadDist = -1d;
								for (VehicleDistance thisVeh : tempVD.getDistances())
								{
									if (thisVeh.getID().equals(sumoName))
									{
										routeDist=thisVeh.getDist();
										//System.out.println(routeDist);
										vehAheadDist=thisVeh.getAheadVehicleDist();
									}
								}
								if (routeDist > -1)
								{
									testReading.addDataValue(null, "http://127.0.0.1/sensors/types#RouteDistance", routeDist, false);
								}
								if (vehAheadDist > -1)
								{
									testReading.addDataValue(null, "http://127.0.0.1/sensors/types#VehicleAheadDistance", vehAheadDist, false);
									//System.out.println("adding vehicle ahead distance of " + vehAheadDist + " for " + sumoName);
								}
							}
						}						
						long p1 = System.currentTimeMillis();
						publish(testReading);
						numberVehMessagesSent++;
						tTime = tTime +  (System.currentTimeMillis()-p1);
					} 
					catch (Exception innerE) 
					{
						System.out.println("exception in vehicle extract in publishVehicleInfo method..");
						innerE.printStackTrace();
					}
				
				}
			}
			catch (Exception e) 
			{
				System.out.println("exception in overall publishVehicleInfo method..");
				e.printStackTrace();
			}
		///System.out.println("total publishing time " + tTime);
		///System.out.println("total veh extracting " +exT);
	}

	private void publishDetectorsAndLights()
	{
			try 
			{

				if (inductionloops.size() > 0)
				{
					DataReading inductionReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
					inductionReading.setTakenBy("http://127.0.0.1/vehicles/inductionloops");

					for (InductionLoop loop : inductionloops.values())
					{
						//System.out.println("sending RDF info for " + loop + " induction loop");
						//System.out.println("time since last vehicle passed: " + loop.queryReadLastTimeSinceDetection().get() + ", avg speed was " + loop.queryReadLastStepMeanSpeed().get() + " and this many vehicles: " + loop.queryReadLastStepVehicleNumber().get());
						inductionReading.addDataValue(null, "http://127.0.0.1/sensors/"+loop, loop.queryReadLastTimeSinceDetection().get()+","+loop.queryReadLastStepMeanSpeed().get()+","+loop.queryReadLastStepVehicleNumber().get() , false);
					}
					publish(inductionReading);
				}
				
				Map<String, TrafficLight> tlights = conn.getTrafficLightRepository().getAll();
				//System.out.println("found this many sets of lights: " + tlights.size());
				for (TrafficLight tlight : tlights.values())
				{
					//if (tlight.getID().equals("joinedS_4") || tlight.getID().equals("379921"))
					//{
						try 
						{
							//TLState lightState = tlight.queryReadState().get().toString;
							//List<Lane> laneList = tlight.queryReadControlledLanes().get();
							ControlledLinks links = tlight.queryReadControlledLinks().get();
							String cLightState = tlight.queryReadState().get().toString();

							//String lanesControlled = (String) tlight.queryReadControlledLanes().get();
							DataReading testReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
							testReading.setTakenBy("http://127.0.0.1/trafficLights/"+tlight.getID());
							for (int i=0;i<links.getLinks().length;i++)
							{
								ControlledLink[] linksForSignal = links.getLinks()[i];
								ControlledLink thisLink = linksForSignal[0];	
								String lightState = Character.toString(cLightState.charAt(i));								
								//System.out.println("from " + thisLink.getIncomingLane() + " to " + thisLink.getOutgoingLane() + " and its currently " + lightState);
								testReading.addDataValue(null, "http://127.0.0.1/trafficLightSensors/entryExitColour", thisLink.getIncomingLane()+","+thisLink.getOutgoingLane()+","+lightState, false);
								//System.out.println("adding test reading for " + thisLink.getOutgoingLane());
							}
							aoiMsgSender.generateAndSendMsg(testReading);
						} 
						catch (Exception e) 
						{
							System.out.println("exception in publishDetectorsAndLights method accessing lights");
							e.printStackTrace();
						}
					//}
				}
			}
			catch (Exception e) 
			{
				System.out.println("exception in overall publishDetectorsAndLights method..");
				e.printStackTrace();
			}
	}

	private void processVehicleToAdd()
	{
		if (vehiclesToAdd.size() > 0)
		{
			System.out.println("trying to crashed add vehicles, " + vehiclesToAdd.size() + " pending..");
			for (VehicleAdd addV : vehiclesToAdd)
			{	
				try {
					AddVehicleQuery avqCrash = conn.queryAddVehicle();
					System.out.println("adding " + addV.getID());
					int departTime = conn.querySimTime() + 1000;
					avqCrash.setVehicleData(addV.getID(), addV.getVType(), addV.getRoute(), addV.getLane(), addV.getLanePos(), addV.getSpeed(), departTime);
					avqCrash.run();
				}
				catch (Exception eP) 
				{
					System.out.println("error adding crashed vehicle back in..");
					eP.printStackTrace();
				}
			}
			//System.out.println("should have added " + vehiclesToAdd.size() + " vehicles now");
			vehiclesToAdd.clear();
		}
	}

	private void updateAllVehicles(Map<String, Vehicle> vehicles)
	{

		try
		{
			DataReading vehTotalReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
			vehTotalReading.addDataValue(null, "http://127.0.0.1/sensors/vehicleCount", vehicles.size() , false);
			publish(vehTotalReading);

			for (Vehicle testVeh : vehicles.values())
			{

				List<Edge> myRoute = testVeh.queryReadCurrentRoute().get();
				String routeCSV = "";
				for (Edge checkE : myRoute)
				{
					//System.out.println(checkE.getID());
					routeCSV=routeCSV+checkE.getID();
				}
				//if we dont already know about any routes, then add this one
				if (knownRoute.equals(""))
				{
					knownRoute=routeCSV;
					System.out.println("adding first case of known route: " + routeCSV);
					
					for (Edge checkE : myRoute)
					{
						//
						String laneName = checkE.getID()+"_0";
						if (knownLanes.contains(laneName))
						{
							Lane targetLane = conn.getLaneRepository().getByID(laneName);
							Double laneLength = targetLane.queryReadLength().get();
							//System.out.println("adding " + checkE.getID() + " with length " + laneLength);
							if (laneDistances.isEmpty())
							{
								LaneDistance newLD = new LaneDistance(checkE.getID(), laneLength);
								laneDistances.add(newLD);
							}
							else
							{
								laneLength=laneLength+laneDistances.get(laneDistances.size()-1).totalDist;
								LaneDistance newLD = new LaneDistance(checkE.getID(), laneLength);
								laneDistances.add(newLD);
							}
						}
					}
					System.out.println("reference lane length now contains " + laneDistances.size() + " and " + laneDistances.get(laneDistances.size() -1 ).totalDist + "m long");
					//now reverse iterate, and shift the lengths by -1 position to get things correct
					for (int i=laneDistances.size()-1; i >= 0; i--)
					{
						if (i>0)
						{
							laneDistances.get(i).totalDist = laneDistances.get(i-1).totalDist;
						}
						else if (i==0)
						{
							laneDistances.get(i).totalDist = 0;
						}
					}
					singleRouteAllVehicles=true;
					for (LaneDistance testLD : laneDistances)
					{
						System.out.println(testLD.laneID + " , " + testLD.totalDist);
					}
				}	
				//if its not empty, but some other value, then we have multiple routes :(		
				else if (!knownRoute.equals(routeCSV))
				{	
					System.out.println(knownRoute + " does not equal " + routeCSV + ", multiple routes detected");
					singleRouteAllVehicles=false;
				}

			
				//set lane change models if not already set
				if (!modifiedVehicles.contains(testVeh.getID()))
				{	

					//update its lane change
					ChangeLaneChangeModeQuery clcmq = testVeh.queryChangeLaneChangeMode();
					//1+4+16+64
					clcmq.setValue(1);
					clcmq.run();

					//will be moved to agent too
					/*if (testVeh.getID().equals("c1.16"))
					{
						Color newCol = new Color(1.0f,0.5f,0.75f);
						ChangeVehicleColourQuery ccq = testVeh.queryChangeVehicleColour();
						ccq.setValue(newCol);
						ccq.run();
					}*/
				}
				modifiedVehicles.add(testVeh.getID());

				
				//this should all move out to agents really rather than coded here..
				/*
				if (testVeh.getID().equals("c1.16") && ((conn.getCurrentSimStep() >= 300) && (conn.getCurrentSimStep() < 312)))
				{
					//System.out.println("c1.16 braking hard...");						
					//ChangeSpeedQuery csq = testVeh.queryChangeSpeed();
					//csq.setValue(0d);
					//csq.run();

					//send inst message, braking hard, and names. though these arent really Jason vehicles, which removes the MAS feel a bit, could be later I guess

					int foundVehPos=-1;
					for (int i=0; i < vehicleDistances.size(); i++)
					{
						if (vehicleDistances.get(i).getID().equals("c1.16"))
						{
							foundVehPos=i;
						}
					}
					String vehsAffected="";
					if ((foundVehPos >=0) && ((foundVehPos+5) < (vehicleDistances.size()-1)))
					{
						vehsAffected=vehicleDistances.get(foundVehPos+1).getID() +","+vehicleDistances.get(foundVehPos+2).getID() +","+vehicleDistances.get(foundVehPos+3).getID() +","+vehicleDistances.get(foundVehPos+4).getID() +","+vehicleDistances.get(foundVehPos+5).getID();
					}
					String testMsg = "braking_hard("+vehicleDistances.get(foundVehPos).getID()+","+vehsAffected;
					instSensor.releaseEvent(testMsg);
					System.out.println("sent " + testMsg +  " inst update now..");
					
				}
				if (testVeh.getID().equals("c1.16") && ((conn.getCurrentSimStep() >= 312) && (conn.getCurrentSimStep() < 314)))
				{
					System.out.println("c1.16 resuming speed");		
					ChangeSpeedQuery csq = testVeh.queryChangeSpeed();
					csq.setValue(-1d);
					csq.run();
				}*/
			}
		}
		catch (Exception e) 
		{
			System.out.println("error updating lane change behaviour.");
			e.printStackTrace();
		}


	}

	public void publishSimTime()
	{
		int simTimeVal = conn.querySimTime();
		//System.out.println("published sim time as " + simTimeVal + " and sent " + numberVehMessagesSent + " veh states in this simstep");
		numberVehMessagesSent=0;
		DataReading timeReading = new DataReading(getPrimaryHandle(), getCurrentLocation(), System.currentTimeMillis());
		timeReading.setTakenBy("http://127.0.0.1/sim/sumo");
		timeReading.addDataValue(null, "http://127.0.0.1/sensors/types#simTime", simTimeVal, false);
		try 
		{
			publish(timeReading);
		}
		catch (Exception e)
		{
			System.out.println("error in publishSimTime...");
			e.printStackTrace();
		}
	}

	public void waitUntil(long delayTo)
	{		
		long currentT=0;
		do{
			currentT = System.nanoTime();
		}while(delayTo >= currentT);
	}
}
