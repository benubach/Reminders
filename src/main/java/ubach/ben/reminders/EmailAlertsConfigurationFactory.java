/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubach.ben.reminders;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.setup.Environment;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ubach.ben.reminders.core.MailAlertScheduler;
import ubach.ben.reminders.db.ReminderDAO;

/**
 *
 * @author benubach
 */
public class EmailAlertsConfigurationFactory {

    private static final Logger log = LoggerFactory.getLogger(EmailAlertsConfigurationFactory.class);

    @NotEmpty
    private String host;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String recipient;

    private int port;

    private boolean starttls;

    private boolean auth;

    public void buildEmailAlertScheduler(Environment environment, ReminderDAO dao, SessionFactory sessionFactory) {
        log.info("Building scheduler");
        Properties mailProperties = buildMailProperties();
        environment.lifecycle().manage(new MailAlertScheduler(mailProperties, host, username, password, recipient, dao, sessionFactory));
    }

    private Properties buildMailProperties() {
        Properties mailProperties = new Properties(System.getProperties());
        mailProperties.put("mail.smtp.port", String.valueOf(port));
        mailProperties.put("mail.smtp.auth", String.valueOf(auth));
        mailProperties.put("mail.smtp.starttls.enable", String.valueOf(starttls));
        log.info("Mail properties: {}", mailProperties);
        return mailProperties;
    }

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public int getPort() {
        return port;
    }

    @JsonProperty
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty
    public boolean isStarttls() {
        return starttls;
    }

    @JsonProperty
    public void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }

    @JsonProperty
    public boolean isAuth() {
        return auth;
    }

    @JsonProperty
    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    public String getRecipient() {
        return recipient;
    }

    @JsonProperty
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}
