package ubach.ben.reminders.core;

import java.io.Serializable;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.Objects;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.*;
import static ubach.ben.reminders.core.Status.PENDING;

/**
 *
 * @author benubach
 */
@Entity
@Table(name = "reminders")
@NamedQueries({
    @NamedQuery(
            name = "ubach.ben.reminders.core.ReminderEntity.findAll",
            query = "SELECT r FROM ReminderEntity r")
    ,
    @NamedQuery(
            name = "ubach.ben.reminders.core.ReminderEntity.findAlerts",
            query = "SELECT r FROM ReminderEntity r WHERE r.status = 'PENDING' and r.dueDate = :time"
    )
})
public class ReminderEntity implements Serializable {

    private static final long serialVersionUID = 1927454393875108506L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    @Enumerated(STRING)
    private Status status;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    public ReminderEntity() {
        this(-1l, "", PENDING, now().plusDays(7));
    }

    public ReminderEntity(long id, String text, Status status, LocalDateTime dueDate) {
        this.id = id;
        this.content = text;
        this.status = status;
        this.dueDate = dueDate;
    }

    public ReminderEntity(long id, String text, Status status) {
        this(id, text, status, now().plusDays(7));
    }

    public ReminderEntity(long id, String text) {
        this(id, text, PENDING);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, content, dueDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReminderEntity other = (ReminderEntity) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.content, other.content)
                && Objects.equals(this.status, other.status)
                && Objects.equals(this.dueDate, other.dueDate);
    }

    @Override
    public String toString() {
        return "Reminder{" + "id=" + id + ", text=" + content + ", status=" + status + ", dueDate=" + dueDate + '}';
    }

}
