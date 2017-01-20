package ubach.ben.reminders;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RemindersConfiguration extends Configuration {

    public RemindersConfiguration() {
    }

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory datasourceFactory = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("email")
    private EmailAlertsConfigurationFactory emailConfigFactory = new EmailAlertsConfigurationFactory();

    public DataSourceFactory getDataSourceFactory() {
        return datasourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory factory) {
        datasourceFactory = factory;
    }

    public EmailAlertsConfigurationFactory getEmailConfigFactory() {
        return emailConfigFactory;
    }

    public void setEmailConfigFactory(EmailAlertsConfigurationFactory emailConfigFactory) {
        this.emailConfigFactory = emailConfigFactory;
    }

}
