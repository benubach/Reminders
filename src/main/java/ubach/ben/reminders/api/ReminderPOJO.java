package ubach.ben.reminders.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.Objects;
import ubach.ben.reminders.core.ReminderEntity;
import ubach.ben.reminders.core.Status;
import static ubach.ben.reminders.core.Status.PENDING;

/**
 *
 * @author benubach
 */
public class ReminderPOJO {

    private long id;
    private String content;
    private Status status;
    private LocalDateTime dueDate;

    private ReminderPOJO() {
        this(-1, null);
    }

    public ReminderPOJO(long id, String content, Status status, LocalDateTime dueDate) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.dueDate = dueDate;
    }

    public ReminderPOJO(long id, String content, Status status) {
        this(id, content, status, now().plusDays(7));
    }

    public ReminderPOJO(long id, String content) {
        this(id, content, PENDING);
    }

    public ReminderPOJO(ReminderEntity reminder) {
        this.id = reminder.getId();
        this.content = reminder.getContent();
        this.status = reminder.getStatus();
        this.dueDate = reminder.getDueDate();
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

    @JsonProperty
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty
    public Status getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
        final ReminderPOJO other = (ReminderPOJO) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.content, other.content)
                && Objects.equals(this.status, other.status)
                && Objects.equals(this.dueDate, other.dueDate);
    }

    @Override
    public String toString() {
        return "ReminderPOJO{" + "id=" + id + ", text=" + content + ", status=" + status + ", dueDate=" + dueDate + '}';
    }

}
