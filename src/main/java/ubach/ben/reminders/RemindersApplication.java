package ubach.ben.reminders;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ubach.ben.reminders.core.ReminderEntity;
import ubach.ben.reminders.db.ReminderDAO;
import ubach.ben.reminders.resources.ReminderResource;

public class RemindersApplication extends Application<RemindersConfiguration> {

    private static final Logger log = LoggerFactory.getLogger(RemindersApplication.class);

    private final HibernateBundle<RemindersConfiguration> hibernateBundle
            = new HibernateBundle<RemindersConfiguration>(ReminderEntity.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(RemindersConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new RemindersApplication().run(args);
    }

    @Override
    public String getName() {
        return "Reminders";
    }

    @Override
    public void initialize(final Bootstrap<RemindersConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<RemindersConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(RemindersConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new AssetsBundle("/frontend", "/frontend"));
    }

    @Override
    public void run(final RemindersConfiguration configuration,
            final Environment environment) {
        log.warn("Run:");
        final ReminderDAO dao = new ReminderDAO(hibernateBundle.getSessionFactory());
        environment.jersey().register(new ReminderResource(dao));
        configuration.getEmailConfigFactory().buildEmailAlertScheduler(environment, dao, hibernateBundle.getSessionFactory());
    }

}
