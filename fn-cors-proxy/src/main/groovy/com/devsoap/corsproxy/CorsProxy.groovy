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

import com.fnproject.fn.api.OutputEvent
import com.fnproject.fn.api.httpgateway.HTTPGatewayContext
import groovyx.net.http.HttpBuilder

/**
 * A simple CORS proxy
 *
 * Usage:
 *
 *    Invoke with "url=http://<target API>" parameter to proxy the call
 */
class CorsProxy {

    /**
     * Proxies the a http request to the target URL and returns the response along with a proper
     * Access-Control-Allow-Origin header.
     *
     * @param context
     *      The Gateway context
     *
     * @return
     *      the response from the proxied call
     */
    OutputEvent proxy(HTTPGatewayContext context) {
        String response
        OutputEvent.Status status

        // Only support JSON calls
        String contentType = 'application/json'

        // Set access control headers
        context.setResponseHeader('Access-Control-Allow-Origin', '*')

        // Get proxy url
        def url = context.queryParameters.get('url')
        if (!url.isPresent()) {
            throw new IllegalArgumentException("'url' parameter was not set")
        }

        // Setup our client to proxy the input
        HttpBuilder http = HttpBuilder.configure {
            request.uri = url.get()
            //request.body = input.consumeBody({ stream -> stream.text })
            request.contentType = contentType
            request.accept = [contentType]
        }

        // Determine HTTP method and execute remote call
        println "Performing query $context.method ${url.get()} with content type $contentType"
        switch (context.method.toUpperCase()) {
            case 'GET': response = http.get(); break
            case 'POST': response = http.post(); break
            case 'PUT': response = http.put(); break
            case 'DELETE': response = http.delete(); break
            default: throw new IllegalArgumentException("HTTP method '$context.method' not implemented")
        }

        // Assume the call succeeded
        status = OutputEvent.Status.Success

        // Return remote call response
        OutputEvent.fromBytes(response.bytes, status,  contentType)
    }
}