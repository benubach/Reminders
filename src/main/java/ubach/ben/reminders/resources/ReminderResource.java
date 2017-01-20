package ubach.ben.reminders.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ubach.ben.reminders.api.ReminderPOJO;
import ubach.ben.reminders.core.ReminderEntity;
import ubach.ben.reminders.db.ReminderDAO;

/**
 *
 * @author benubach
 */
@Path("/reminder")
public class ReminderResource {

    private final AtomicInteger counter;

    private final ReminderDAO reminderDao;
    //SLF4J is provided with dropwizard. Logback is also provided
    Logger log = LoggerFactory.getLogger(ReminderResource.class);

    public ReminderResource(ReminderDAO dao) {
        this.counter = new AtomicInteger();
        this.reminderDao = dao;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public List<ReminderPOJO> listReminders() {
        List<ReminderEntity> reminders = reminderDao.findAll();
        List<ReminderPOJO> pojos = new ArrayList<>();
        for (ReminderEntity entity : reminders) {
            pojos.add(new ReminderPOJO(entity));
        }
        return pojos;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public ReminderPOJO createReminder(@NotNull @Valid ReminderPOJO reminder) {
        log.debug("Received new Reminder: " + reminder.toString());
        ReminderEntity created = reminderDao.create(new ReminderEntity(reminder.getId(), reminder.getContent(), reminder.getStatus(), reminder.getDueDate()));

        return new ReminderPOJO(created);
    }

}
