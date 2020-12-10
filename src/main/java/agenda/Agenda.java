package agenda;

import java.time.LocalDate;
import java.util.*;

/**
 * Description : An agenda that stores events
 */
public class Agenda {
    
    private ArrayList<Event> myEvents = new ArrayList<>();
    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        myEvents.add(e);
    }

    /**
     * Computes the events that occur on a given day
     *
     * @param day the day toi test
     * @return and iteraror to the events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        ArrayList<Event> myEventsInDay = new ArrayList<>();
        for(Event e : myEvents){
            if(e.isInDay(day)){
                myEventsInDay.add(e);
            }
        }
        return myEventsInDay;
    }
    
    /**
     * Trouver les événements de l'agenda en fonction de leur titre
     * @param title le titre à rechercher
     * @return les événements qui ont le même titre
     */
    public List<Event> findByTitle(String title) {
        // TODO : implémenter cette méthode
        ArrayList<Event> eventsByTitle = new ArrayList<>();
        for (Event e : myEvents){
            if(e.getTitle().equals(title)){
                eventsByTitle.add(e);
            }
        }
        return eventsByTitle;
    }
        
    /**
     * Déterminer s’il y a de la place dans l'agenda pour un événement
     * @param e L'événement à tester (on se limitera aux événements simples)
     * @return vrai s’il y a de la place dans l'agenda pour cet événement
     */
    public boolean isFreeFor(Event e) {
        // TODO : implémenter cette méthode
        if(myEvents.isEmpty()){
            return true;
        }
        for (Event event : myEvents){
            // an event starts before but finishes after the start of the other
            if(event.getStart().plus(event.getDuration()).isAfter(e.getStart()) && event.getStart().isBefore(e.getStart().plus(e.getDuration()))){
                return false;
            } 
            // an event starts before the end of an other and finishes after it
            if(event.getStart().isBefore(e.getStart().plus(e.getDuration())) && event.getStart().isAfter(e.getStart())){
                return false;
            }
            // an event starts before an other and finishes after it
            if(event.getStart().isBefore(e.getStart()) && event.getStart().plus(event.getDuration()).isAfter(e.getStart().plus(e.getDuration()))){
                return false;
            }
            // the two events start or finish at the same moment
            if(event.getStart().equals(e.getStart()) || event.getStart().plus(event.getDuration()).equals(e.getStart().plus(e.getDuration()))){
                return false;
            }
            // an event starts after but finishes before the tested event
            if(event.getStart().isAfter(e.getStart()) && event.getStart().plus(event.getDuration()).isBefore(e.getStart().plus(e.getDuration()))){
                return false;
            }
        }
        return true;
    }
}
