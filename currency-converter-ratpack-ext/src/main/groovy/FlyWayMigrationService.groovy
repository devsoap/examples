import org.flywaydb.core.Flyway
import ratpack.service.Service
import ratpack.service.StartEvent

import javax.sql.DataSource

/**
 * Service for migrating Flyway scripts
 */
class FlyWayMigrationService implements Service {
    void onStart(StartEvent event) {
        new Flyway(dataSource: event.registry.get(DataSource)).migrate()
    }
}
