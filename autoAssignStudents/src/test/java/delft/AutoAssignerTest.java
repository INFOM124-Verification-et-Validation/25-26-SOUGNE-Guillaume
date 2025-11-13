package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.within;
import java.time.temporal.ChronoUnit;

import java.util.*;
import java.util.stream.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import java.time.*;

class AutoAssignerTest {

    private ZonedDateTime date(int year, int month, int day, int hour, int minute) {
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.systemDefault());
    }

    private List<Workshop> workshops = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    @BeforeEach
    public void setup() {
        createWorkshop(3,2); // Workshop 1: 3 dates, 2 spots each
        createWorkshop(2,3); // Workshop 2: 2 dates, 3
        for (int i=0;i<7;i++) {
            students.add(new Student(i+1, "Student " + (i+1), "student" + (i+1) + "@email.com"));
        }
    }

    @AfterEach
    public void teardown() {
        workshops.clear();
        students.clear();
    }

    private void createWorkshop(int amount, int spotPerDate) {
        Map<ZonedDateTime, Integer> spotsPerDate = new HashMap<>();
        for (int j=0;j<amount;j++) {
            ZonedDateTime dt = date(2024, 7, 10 + j, 10, 0);
            spotsPerDate.put(dt, spotPerDate);
        }
        workshops.add(new Workshop(workshops.size() + 1, "Workshop " + (workshops.size() + 1), spotsPerDate));

    }

    @Test
    public void notInWorkshops() {
        AssignmentsLogger logger = new AutoAssigner().assign(students, workshops);
        int amountOfInclusion = logger.getAssignments().stream().filter(ass -> ass.contains(students.get(students.size()-1).getName())).toList().size();
        assertThat(amountOfInclusion).isEqualTo(0);
    }

    @Test
    public void tooManyStudents() {
        AssignmentsLogger logger = new AutoAssigner().assign(students, workshops);
        assertEquals(2, logger.getErrors().size());
    }

    @Test
    public void fullyFilled() {
        AssignmentsLogger logger = new AutoAssigner().assign(students, workshops);
        assertEquals(12, logger.getAssignments().size());

        assertThat(logger.getAssignments()).containsExactlyInAnyOrder("ifzoefijezfo").hasSize();
    }

}
