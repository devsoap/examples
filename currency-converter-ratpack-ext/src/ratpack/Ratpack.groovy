import ratpack.h2.H2Module

import static ratpack.groovy.Groovy.ratpack

ratpack {

    bindings {

        // Use H2 in-memory database
        module(new H2Module("sa", "", "jdbc:h2:mem:convertions;DB_CLOSE_DELAY=-1"))

        // Register our handlers and service
        bind FlyWayMigrationService
        bind MonetaryConvertionHandler

    }

    handlers {

        // GET /convert/EUR/SEK/100
        get("convert/:from/:to/:amount", MonetaryConvertionHandler)
    }
}
