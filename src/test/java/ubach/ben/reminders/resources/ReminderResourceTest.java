package ubach.ben.reminders.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import ubach.ben.reminders.api.ReminderPOJO;
import ubach.ben.reminders.core.ReminderEntity;
import ubach.ben.reminders.core.Status;
import static ubach.ben.reminders.core.Status.PENDING;
import ubach.ben.reminders.db.ReminderDAO;

/**
 *
 * @author benubach
 */
public class ReminderResourceTest {

    ReminderDAO dao = mock(ReminderDAO.class);

    ReminderPOJO newReminder;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ReminderResource(dao))
            .build();
    private static final List<ReminderEntity> EMPTY_LIST = new ArrayList<>();
    private static final List<ReminderEntity> REMINDERS = Arrays.asList(new ReminderEntity(1, "Test 1", Status.DONE, now()),
            new ReminderEntity(2, "Test 2", Status.PENDING, now().plusDays(1)),
            new ReminderEntity(3, "Test 3", Status.DONE, now().plusHours(3)));

    public ReminderResourceTest() {
    }

    @Before
    public void setUp() {
        newReminder = new ReminderPOJO(-1, "Insert Test", PENDING, LocalDateTime.of(2018, 2, 12, 12, 24));
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    /**
     * Test of listReminders method, of class ReminderResource.
     */
    @Test
    public void testEmptyListReminders() {

        when(dao.findAll()).thenReturn(EMPTY_LIST);
        List<ReminderPOJO> pojos = resources.client().target("/reminder").request().get(new GenericType<List<ReminderPOJO>>() {
        });
        assertThat(pojos).hasSize(0);
    }

    @Test
    public void testListReminders() {
        when(dao.findAll()).thenReturn(REMINDERS);
        List<ReminderPOJO> pojos = resources.client().target("/reminder").request().get(new GenericType<List<ReminderPOJO>>() {
        });
        assertThat(pojos).hasSize(REMINDERS.size());

        //Truncated to minutes since Dropwizard sets the dueDate before returning the response
        assertThat(pojos).extracting("id", "content", "status", "dueDate")
                .contains(tuple(REMINDERS.get(0).getId(), REMINDERS.get(0).getContent(), REMINDERS.get(0).getStatus(), REMINDERS.get(0).getDueDate().truncatedTo(MINUTES)),
                        tuple(REMINDERS.get(1).getId(), REMINDERS.get(1).getContent(), REMINDERS.get(1).getStatus(), REMINDERS.get(1).getDueDate().truncatedTo(MINUTES)),
                        tuple(REMINDERS.get(2).getId(), REMINDERS.get(2).getContent(), REMINDERS.get(2).getStatus(), REMINDERS.get(2).getDueDate().truncatedTo(MINUTES))
                );

    }

    /**
     * Test of createReminder method, of class ReminderResource.
     */
    @Test
    public void testCreateFullReminder() {
        when(dao.create(any(ReminderEntity.class))).thenReturn(new ReminderEntity(1, "dummy"));
        ArgumentCaptor<ReminderEntity> entityArgumentCaptor = ArgumentCaptor.forClass(ReminderEntity.class);
        resources.client().target("/reminder").request(APPLICATION_JSON).post(Entity.json("{\"content\":\"Insert Test\",\"status\":\"DONE\", \"dueDate\": \"2018-02-12 12:24\"}"), ReminderPOJO.class);
        verify(dao).create(entityArgumentCaptor.capture());
        ReminderEntity persistedReminder = entityArgumentCaptor.getValue();
        assertThat(persistedReminder).extracting("content", "status", "dueDate").contains("Insert Test", Status.DONE, LocalDateTime.of(2018, 2, 12, 12, 24));
    }

    @Test
    public void testCreateReminderWithoutDueDateSetsDueDateInOneWeek() {
        when(dao.create(any(ReminderEntity.class))).thenReturn(new ReminderEntity(1, "dummy"));
        ArgumentCaptor<ReminderEntity> entityArgumentCaptor = ArgumentCaptor.forClass(ReminderEntity.class);
        resources.client().target("/reminder").request(APPLICATION_JSON).post(Entity.json("{\"content\":\"Insert Test\",\"status\":\"DONE\"}"), ReminderPOJO.class);
        verify(dao).create(entityArgumentCaptor.capture());
        ReminderEntity persistedReminder = entityArgumentCaptor.getValue();
        assertThat(persistedReminder).extracting("content", "status").contains("Insert Test", Status.DONE);
        assertThat(persistedReminder.getDueDate()).isEqualToIgnoringSeconds(now().plusDays(7));
    }

    @Test
    public void testCreateReminderWithoutStatusAndDueDateCreatesPendingDueInOneWeek() {
        when(dao.create(any(ReminderEntity.class))).thenReturn(new ReminderEntity(1, "dummy"));
        ArgumentCaptor<ReminderEntity> entityArgumentCaptor = ArgumentCaptor.forClass(ReminderEntity.class);
        resources.client().target("/reminder").request(APPLICATION_JSON).post(Entity.json("{\"content\":\"Insert Test\"}"), ReminderPOJO.class);
        verify(dao).create(entityArgumentCaptor.capture());
        ReminderEntity persistedReminder = entityArgumentCaptor.getValue();
        assertThat(persistedReminder).extracting("content", "status").contains("Insert Test", Status.PENDING);
        assertThat(persistedReminder.getDueDate()).isEqualToIgnoringSeconds(now().plusDays(7));
    }

}
