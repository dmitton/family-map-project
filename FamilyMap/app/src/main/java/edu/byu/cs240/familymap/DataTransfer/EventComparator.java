package edu.byu.cs240.familymap.DataTransfer;

import java.util.Comparator;

import model.Event;

public class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event event, Event event2) {
        return Integer.compare(event.getYear(), event2.getYear());
    }
}
