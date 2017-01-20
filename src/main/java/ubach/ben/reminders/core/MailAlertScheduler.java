/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubach.ben.reminders.core;

import io.dropwizard.lifecycle.Managed;
import it.sauronsoftware.cron4j.Scheduler;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;
import static java.time.format.FormatStyle.FULL;
import static java.time.format.FormatStyle.SHORT;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.Message;
import javax.mail.MessagingException;
import static javax.mail.Session.getDefaultInstance;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ubach.ben.reminders.db.ReminderDAO;

/**
 *
 * @author benubach
 */
public class MailAlertScheduler implements Managed {

    private static final Logger log = LoggerFactory.getLogger(MailAlertScheduler.class);

    private static final DateTimeFormatter formatter = ofLocalizedDateTime(FULL, SHORT);

    private final Properties mailProperties;
    private final ReminderDAO dao;
    private final Scheduler scheduler;
    private final String username;
    private final String password;
    private final String host;
    private final String recipient;
    private final SessionFactory sessionFactory;

    public MailAlertScheduler(Properties mailProperties, String host, String username, String password, String recipient, ReminderDAO dao, SessionFactory sessionFactory) {
        log.info("Constructor");
        this.mailProperties = mailProperties;
        this.dao = dao;
        this.scheduler = new Scheduler();
        this.host = host;
        this.username = username;
        this.password = password;
        this.recipient = recipient;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void start() throws Exception {

        log.info("Starting Scheduler");
        scheduler.schedule("* * * * *", () -> {
            Session session = sessionFactory.openSession();
            List<ReminderEntity> remindersForAlert = null;
            try {
                ManagedSessionContext.bind(session);
                Transaction t = session.beginTransaction();
                try {
                    remindersForAlert = dao.findAllForAlert();
                    log.info("Executing alerts! Reminders: " + remindersForAlert);
                    t.commit();
                } catch (Exception e) {
                    log.error("Hibernate problems", e);
                    t.rollback();
                }

            } catch (Exception e) {
                log.error("Hibernate problems", e);
            } finally {
                session.close();
                ManagedSessionContext.unbind(sessionFactory);
            }
            if (remindersForAlert != null && !remindersForAlert.isEmpty()) {
                sendMail(remindersForAlert);
            }
        });
        scheduler.start();
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping Scheduler");
        scheduler.stop();
    }

    private void sendMail(List<ReminderEntity> remindersForAlert) {
        try {
            javax.mail.Session mailSession = getDefaultInstance(mailProperties);
            MimeMessage message = new MimeMessage(mailSession);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setFrom(username);
            message.setSubject("These reminders are due now!");
            StringBuilder messageBody = new StringBuilder("<ol>");
            for (ReminderEntity reminder : remindersForAlert) {
                messageBody.append("<li>'").append(reminder.getContent()).append("' due on ").append(reminder.getDueDate().format(formatter)).append("</li>");
            }
            messageBody.append("</ol>");
            message.setContent(messageBody.toString(), "text/html");
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (AddressException ex) {
            java.util.logging.Logger.getLogger(MailAlertScheduler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(MailAlertScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
