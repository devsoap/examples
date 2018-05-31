import groovy.sql.Sql
import org.flywaydb.core.Flyway
import org.javamoney.moneta.FastMoney
import ratpack.h2.H2Module
import ratpack.service.Service
import ratpack.service.StartEvent

import javax.sql.DataSource

import static javax.money.convert.MonetaryConversions.getConversion
import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {

    bindings {

        // Use H2 in-memory database
        module(new H2Module("sa", "", "jdbc:h2:mem:convertions;DB_CLOSE_DELAY=-1"))

        // Migrate flyway scripts on startup
        bindInstance new Service() {
            void onStart(StartEvent event) {
                new Flyway(dataSource: event.registry.get(DataSource)).migrate()
            }
        }


    }

    handlers {

        /*
            GET /convert/EUR/USD/100
         */
        get("convert/:from/:to/:amount") {

            // 1: Try to find the convertion from the db
            def amount = getAmountFromDb(get(DataSource), pathTokens.from, pathTokens.to, pathTokens.amount as BigDecimal)

            if(!amount) {
                // 2: Do the convertion via ECB
                amount = FastMoney.of(pathTokens.amount.toBigDecimal(), pathTokens.from)
                        .with(getConversion(pathTokens.to)) .number.toBigDecimal()

                // 3: Store the convertion for future reference
                setAmountToDb(get(DataSource), pathTokens.from, pathTokens.to, pathTokens.amount as BigDecimal, amount)
            }

            // 4: Render a response for a our clients
            render json([
                    "fromCurrency" : pathTokens.from,
                    "toCurrency" : pathTokens.to,
                    "fromAmount": pathTokens.amount.toBigDecimal().stripTrailingZeros(),
                    "toAmount": amount.toBigDecimal().stripTrailingZeros()
            ])
        }
    }
}

static Number getAmountFromDb(DataSource ds, String from, String to, Number amount) {
    new Sql(ds).firstRow("""
        SELECT toAmount FROM convertions 
        WHERE fromCurrency=:from
        AND toCurrency=:to
        AND fromAmount=:amount
    """, ['from': from, 'to': to, 'amount': amount])?.values()?.first()
}

static void setAmountToDb(DataSource ds, String from, String to, Number amount, Number convertedAmount) {
    new Sql(ds).executeInsert("""
        INSERT INTO convertions (fromCurrency, toCurrency, fromAmount, toAmount) 
        VALUES (:from, :to, :amount, :toAmount) 
    """, ['from': from, 'to': to, 'amount': amount, 'toAmount': convertedAmount])
}