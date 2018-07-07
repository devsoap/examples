/*
 * Copyright 2018 Devsoap Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        /*
         * This block serves our static files
         */
        files {
            /*
             * Our static files are located under /ratpack/static
             */
            dir('static')

            /*
             * Serve our index.html as the root
             */
            indexFiles('index.html')
        }
    }
}
