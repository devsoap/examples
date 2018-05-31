import groovy.sql.Sql
import org.javamoney.moneta.FastMoney
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

import javax.sql.DataSource

import static javax.money.convert.MonetaryConversions.getConversion
import static ratpack.jackson.Jackson.json

class MonetaryConvertionHandler extends GroovyHandler {

    @Override
    protected void handle(GroovyContext context) {
        context.with {

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

    private static Number getAmountFromDb(DataSource ds, String from, String to, Number amount) {
        new Sql(ds).firstRow("""
        SELECT toAmount FROM convertions 
        WHERE fromCurrency=:from
        AND toCurrency=:to
        AND fromAmount=:amount
    """, ['from': from, 'to': to, 'amount': amount])?.values()?.first()
    }

    private static void setAmountToDb(DataSource ds, String from, String to, Number amount, Number convertedAmount) {
        new Sql(ds).executeInsert("""
        INSERT INTO convertions (fromCurrency, toCurrency, fromAmount, toAmount) 
        VALUES (:from, :to, :amount, :toAmount) 
    """, ['from': from, 'to': to, 'amount': amount, 'toAmount': convertedAmount])
    }
}
