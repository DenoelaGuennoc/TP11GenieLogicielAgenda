package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class AgendaTest {
    Agenda agenda;
    Agenda agendaA = new Agenda();
    Agenda agendaB = new Agenda();
    Agenda agendaC = new Agenda();
    Agenda agendaD = new Agenda();
    Agenda agendaE = new Agenda();
    
    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1__2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);
    
    // January 1st, 2020, 10:00
    LocalDateTime jan_1_2020_10_00 = LocalDateTime.of(2020, 1, 1, 10, 00);
    
    // January 1st, 2020, 13:00
    LocalDateTime jan_1_2020_13_00 = LocalDateTime.of(2020, 1, 1, 13, 00);
    
    // January 1st, 2020, 11:30
    LocalDateTime jan_1_2020_11_30 = LocalDateTime.of(2020, 1, 1, 11, 30);
    
    // January 1st, 2020, 11:30
    LocalDateTime jan_1_2020_13_30 = LocalDateTime.of(2020, 1, 1, 13, 30);
    
    // January 1st, 2020, 10:00
    LocalDateTime jan_1_2020_12_00 = LocalDateTime.of(2020, 1, 1, 12, 00);
    
    // 60 minutes
    Duration min_60 = Duration.ofMinutes(60);
    
    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);
    
    // 300 minutes
    Duration min_300 = Duration.ofMinutes(300);

    // simple events
    // November 1st, 2020, 22:30, 120 minutes
    Event simple = new Event("Simple event", nov_1__2020_22_30, min_120);
    
    // January 1st, 2020, 10:00, 120 minutes
    Event simpleA = new Event("Simple event A", jan_1_2020_10_00, min_120);
    
    // January 1st, 2020, 13:00, 120 minutes
    Event simpleB = new Event("Simple event B", jan_1_2020_13_00, min_120);
    
    // January 1st, 2020, 11:30, 120 minutes
    Event simpleC = new Event("Simple event", jan_1_2020_11_30, min_120);
    
    // January 1st, 2020, 10:00, 300 minutes
    Event simpleD = new Event("Simple event D", jan_1_2020_10_00, min_300);
    
    // January 1st, 2020, 10:00, 300 minutes
    Event simpleE = new Event("Simple event E", jan_1_2020_11_30, min_300);
    
    // January 1st, 2020, 10:00, 300 minutes
    Event simpleF = new Event("Simple event F", jan_1_2020_12_00, min_60);
    
    // January 1st, 2020, 10:00, 300 minutes
    Event simpleG = new Event("Simple event G", jan_1_2020_12_00, min_60);

    // A Weekly Repetitive event ending at a given date
    RepetitiveEvent fixedTermination = new FixedTerminationEvent("Fixed termination weekly", nov_1__2020_22_30, min_120, ChronoUnit.WEEKS, jan_5_2021);

    // A Weekly Repetitive event ending after a give number of occurrrences
    RepetitiveEvent fixedRepetitions = new FixedTerminationEvent("Fixed termination weekly", nov_1__2020_22_30, min_120, ChronoUnit.WEEKS, 10);
    
    // A daily repetitive event, never ending
    // November 1st, 2020, 22:30, 120 minutes
    RepetitiveEvent neverEnding = new RepetitiveEvent("Never Ending", nov_1__2020_22_30, min_120, ChronoUnit.DAYS);

    @BeforeEach
    public void setUp() {
        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }
    
    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(), "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
    }

    @Test
    public void testIsFreeFor(){
        // no conflict
        assertTrue(agenda.isFreeFor(simpleC), "Il n'y a pas d'événement dans l'agenda");
        agenda.addEvent(simple);
        assertTrue(agenda.isFreeFor(simpleC), "Aucun événement ne chevauche celui-ci");
        
        // an event starts before but finishes after the start of the other
        agendaA.addEvent(simpleA);
        assertFalse(agendaA.isFreeFor(simpleC), "Un événement fini après le début de celui-ci");
        
        // an event starts before the end of an other and finishes after it
        agendaB.addEvent(simpleB);
        assertFalse(agendaB.isFreeFor(simpleC), "Un événement commence avant la fin de celui-ci");
        
        // an event starts before an other and finishes after it
        agendaC.addEvent(simpleD);
        assertFalse(agendaC.isFreeFor(simpleC), "Un événement commence avant et fini après celui-ci");
        
        // the two events start or finish at the same moment
        agendaD.addEvent(simpleE);
        assertFalse(agendaD.isFreeFor(simpleC), "Un événement commence déjà à cet instant");
        
        agendaE.addEvent(simpleF);
        assertFalse(agendaE.isFreeFor(simpleC), "Un événement fini déjà au même instant");
        
        // an event starts after but finishes before the tested event
        agenda.addEvent(simpleG);
        assertFalse(agenda.isFreeFor(simpleC), "Un événement est déjà présent dans ce créneau");
    }
    
    @Test
    public void testFindByTitle(){
        assertTrue(agendaA.findByTitle("Simple event").isEmpty());
        
        agendaA.addEvent(simple);
        assertEquals(1, agendaA.findByTitle("Simple event").size(), "1 titre doit être trouvé");
        assertTrue(agendaA.findByTitle("Simple event").contains(simple), "l'événement trouvé n'est pas le bon");
        
        agendaA.addEvent(simpleC);
        agendaA.addEvent(simpleA);
        assertEquals(2, agendaA.findByTitle("Simple event").size(), "1 titre doit être trouvé");
        assertTrue(agendaA.findByTitle("Simple event").contains(simpleC), "l'événement simpleC n'a pas été trouvé");
        assertFalse(agendaA.findByTitle("Simple event").contains(simpleA), "l'événement simpleA ne devrait pas apparaître ici");
    }
    
    

}
