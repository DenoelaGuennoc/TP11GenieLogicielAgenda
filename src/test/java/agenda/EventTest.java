package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1__2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // 89 minutes
    Duration min_89 = Duration.ofMinutes(89);
    
    // 36 hours = 2160 minutes
    Duration min_2160 = Duration.ofMinutes(2160);

    // A simple event
    // November 1st, 2020, 22:30, 89 minutes
    Event simple = new Event("Simple event", nov_1__2020_22_30, min_89);
    
    // An event overlapping two days
    // November 1st, 2020, 22:30, 120 minutes
    Event overlapping = new Event("Overlapping event", nov_1__2020_22_30, min_120);
    
    // An event overlapping three days
    // November 1st, 2020, 22:30, 2160 minutes
    Event overlapping2 = new Event("Overlapping event 2", nov_1__2020_22_30, min_2160);

    @Test
    public void eventIsInItsStartDay() {
        assertTrue(simple.isInDay(nov_1_2020), "Un événement a lieu dans son jour de début");
        assertTrue(overlapping.isInDay(nov_1_2020), "Un événement a lieu dans son jour de début");
    }

    @Test
    public void eventIsNotInDayBefore() {
        assertFalse(simple.isInDay(nov_1_2020.minus(1, ChronoUnit.DAYS)),  "Un événement n'a pas lieu avant son jour de début");
        assertFalse(overlapping.isInDay(nov_1_2020.minus(1, ChronoUnit.DAYS)),  "Un événement n'a pas lieu avant son jour de début");
    }

    @Test
    public void overlappingEventIsInDayAfter() {
        assertFalse(simple.isInDay(nov_1_2020.plus(1, ChronoUnit.DAYS)),      "Cet événement ne déborde pas sur le jour suivant");
        assertTrue(overlapping.isInDay(nov_1_2020.plus(1, ChronoUnit.DAYS)),  "Cet événement déborde sur le jour suivant");
    }
    @Test
    public void toStringShowsEventTitle() {
        assertTrue(simple.toString().contains("Simple event"), "toString() doit montrer le titre de l'événements");
    }
    @Test
    public void testIsInDayOverlapping(){
        assertTrue(overlapping2.isInDay(nov_1_2020.plusDays(1)), "Cet événement déborde sur les deux jours suivants");
        assertTrue(overlapping2.isInDay(nov_1_2020.plusDays(2)));
    }
    
}
