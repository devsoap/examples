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

import groovy.sql.Sql
import org.javamoney.moneta.Money
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler

import javax.sql.DataSource
import java.math.RoundingMode

import static javax.money.convert.MonetaryConversions.getConversion
import static ratpack.jackson.Jackson.json

/**
 * Endpoint for handling currency conversions
 *
 * @author John Ahlroos
 */
class MonetaryConvertionHandler extends GroovyHandler {

    @Override
    protected void handle(GroovyContext context) {
        context.with {

            // 1: Try to find the convertion from the db
            def amount = getAmountFromDb(get(DataSource), pathTokens.from, pathTokens.to, pathTokens.amount as BigDecimal)

            if(!amount) {
                // 2: Do the convertion via ECB
                def conversion = getConversion(pathTokens.to)

                amount = Money.of(pathTokens.amount.toBigDecimal(), pathTokens.from)
                        .with(conversion).number.toBigDecimal().setScale(4, RoundingMode.HALF_UP)

                // 3: Store the conversion for future reference
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
