
package com.devsoap.corsproxy


import com.fnproject.fn.testing.FnTestingRule
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import org.junit.Rule
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class CorsProxyTest {

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    void 'test api'() {
        testing.givenEvent()
                .withHeader("Fn-Http-Request-Url", "http://localhost?url=http://echo.jsontest.com/hello/world")
                .withHeader("Fn-Http-Method","GET")
                .withHeader('Content-Type', 'application/json')
                .enqueue()
                .thenRun(CorsProxy, 'proxy')

        def result = testing.onlyResult
        assertTrue(result.success)

        Optional<String> accessHeader = result.headers.get('Fn-Http-H-Access-Control-Allow-Origin')
        assertTrue(accessHeader.isPresent())
        assertEquals('*', accessHeader.get())
        assertEquals('{hello=world}', result.bodyAsString)
    }
}
