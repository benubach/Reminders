package ubach.ben.reminders.db;

import io.dropwizard.hibernate.AbstractDAO;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import ubach.ben.reminders.core.ReminderEntity;

/**
 *
 * @author benubach
 */
public class ReminderDAO extends AbstractDAO<ReminderEntity> {

    public ReminderDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<ReminderEntity> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public ReminderEntity create(ReminderEntity person) {
        return persist(person);
    }

    public List<ReminderEntity> findAll() {
        return list(namedQuery("ubach.ben.reminders.core.ReminderEntity.findAll"));
    }

    public List<ReminderEntity> findAllForAlert() {
        return list(namedQuery("ubach.ben.reminders.core.ReminderEntity.findAlerts")
                .setParameter("time", LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
    }

}
