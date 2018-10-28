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
package com.devsoap.corsproxy

import com.fnproject.fn.testing.FnTestingRule
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
