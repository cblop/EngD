# Is the Event Ontology suitable for describing animation events?

The [Event Ontology](http://motools.sourceforge.net/event/event.html) was developed by the [Centre for Digital Music](http://www.elec.qmul.ac.uk/digitalmusic/) in [Queen Mary, University of London](http://www.qmul.ac.uk/) as part of the [Music Ontology](http://www.musicontology.com/). This document summarises its syntax and suitability as a way of describing the events that occur during an animation.

## The Event Model

<!-- change to local image -->
![The Event Model](http://motools.sourceforge.net/event/event.png)

The original event model consists of:

- factors
- places
- sub events
- time
- agents
- products

A factor is something that an agent interacts with. In the Music Ontology examples, factors are musical instruments or equipment. Places are actual real-life geographical locations. Sub-events are events that occur within events. Time is a point in time and/or a duration. An agent is something that performs an action as part of the event. A product is something that results as an outcome of the event.

## OWL-Time
The time concept is represented by the [OWL-Time](http://www.w3.org/2006/time#) ontology. OWL-Time's classes and subclasses are as follows:

- TemporalEntity
	- Instant
	- Interval
		- ProperInterval
			- DateTimeInterval
- DurationDescription
- DateTimeDescription
- TemporalUnit
- DayOfWeek

The TemporalEntity class, consisting of instants and intervals, is the most useful of these.

### The TemporalEntity class

#### Instants

An _instant_ is simply a single point in time. It can be expressed in two different formats: as a _dateTime_, or as an _XSDDateTime_.

In a _dateTime_, the date components are all specified separately. In this case, an _instant_ would be represented in N3 format like this:

    @prefix time: <http://www.w3.org/2006/time#> .

    :meetingStart
        a time:Instant ;
        time:inDateTime
            time:minute 30 ;
            time:hour 10 ;
            time:day 1 ;
            time:dayOfWeek :Sunday ;
            time:dayOfYear 1 ;
            time:week 1 ;
            time:month 1 ;
            time:year 2006 .

Or, alternatively, as an _XSDDateTime_, which encodes a date and time in the format **CCYY-MM-DDThh:mm:ss.sss**:


    @prefix time: <http://www.w3.org/2006/time#> .

    :meetingStart
        a time:Instant ;
        time:inXSDDateTime
            2006-01-01T10:30:00-5:00 .

#### Intervals

An _interval_ represents a span of time that happens over a given duration. It consists of a beginning (which is an _instant_) and a durationDescription. The durationDescription can be represented in the same formats as an _instant_: by a _dateTime_, or by an _XSDDateTime_.

An complete example of a meeting, using both an _instant_ to represent its start and an _interval_ to represent its duration, would be:

    @prefix time: <http://www.w3.org/2006/time#> .

    :meeting
        a time:Interval ;
        time:hasBeginning :meetingStart ;
        time:hasDurationDescription
            :meetingDuration .

    :meetingStart
        a time:Instant ;
        time:inXSDDateTime
            2006-01-01T10:30:00-5:00 .

    :meetingDuration
        a :DurationDescription ;
        time:minutes 45 .


## The Timeline Ontology
For their own purposes, the creators of the Event Ontology decided to redefine a TemporalEntity's instant and interval to have different meaning as part of the [Timeline Ontology](http://motools.sourceforge.net/timeline/timeline.html).

![TimeLine Ontology](http://motools.sourceforge.net/timeline/timeline.png)

The timeline ontology consists of:

- instants, which have durations and are on timelines
- intervals, which begin at a certain time, have durations and are on timelines

Interestingly, these are different from the _instant_ and _interval_ classes defined in the OWL-Time ontology. In the timeline ontology, an _instant_ is not a point in time, but rather a duration on a timeline. An _interval_ takes the role of both the _instant_ and _interval_ of the OWL-Time ontology, by being both a point in time and a duration.

In this case, a duration is expressed as a date in ISO 8601, as explained [here](http://www.w3.org/TR/xmlschema-2/#isoformats). For example, a duration of three days, two hours, thirty minutes and 1.4 seconds would be expressed as P3DT2H30M1.4S. In this case, the 'P' (period) signifies the start of the date string, then 3D means 3 days, the T signifies the end of the date part of the string, 2H means 2 hours, 30M is 30 minutes and 1.4S is 1.4 seconds.


The above diagram expressed in N3 format would be:

    @prefix tl: <http://purl.org/NET/c4dm/timeline.owl#>.

    :instant
      a tl:Instant;
      tl:timeline <http://zitgist.com/music/signal/6da76448-982a-4a01-b65b-9a710301c9c9>
      tl:at "PT3S"^^xsd:duration;
      .

    :interval
      a tl:Interval;
      tl:timeline tl:universaltimeline;
      tl:start "2001-10-26T12:00:00Z"^^xsd:dateTime;
      tl:duration "P7DT";
      .


## Can this be used for animations?
To move a character on a screen at some point in a scene, four things must be known:

- the character to animate
- the location to move the character to
- when to move the character
- the duration of the movement (speed)

It is assumed that the character is already located somewhere on the screen (or otherwise just out of view).

By combining the Event Ontology with the Timeline Ontology, we can attempt to describe an event where a character called "Isembard Kingdom Brunel" moves for 12.5 seconds 30 after the animation has started:

    @prefix event: <http://purl.org/NET/c4dm/event.owl#>
    @prefix tl: <http://purl.org/NET/c4dm/timeline.owl#>.

    :movement
        a event:Event ;
        event:agent [
            a foaf:Person ;
            foaf:name "Isembard Kingdom Brunel" ;
            ] ;
        event:time [
            a tl:Interval;
            tl:at "T00:00:30"^^xsd:dateTime;
            tl:duration "PT12.5S"^^xsd:duration;
            ];

This highlights the drawbacks of using the Event Ontology for describing animations. Being designed to describe musicians giving musical performances in real-word places at real-world times, it is less suitable for the fictional characters and events of an animation.


## Conclusion
While the Event Ontology appears useful, it is not entirely suitable for describing the events of an animation system. However, it has some useful features:

- using 'agents', which are a class of the FOAF ontology
- the use of OWL-Time (though they changed it with their Timeline ontology)

The next step is to create an 'animation' ontology that draws inspiration from the event ontology, describing animation events with fictional characters, times and positions on a screen.

