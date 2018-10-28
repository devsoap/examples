package com.devsoap.corsproxy


import com.fnproject.fn.api.InputEvent
import com.fnproject.fn.api.OutputEvent
import com.fnproject.fn.api.httpgateway.HTTPGatewayContext
import groovyx.net.http.HttpBuilder

class CorsProxy {

    OutputEvent proxy(HTTPGatewayContext context) {
        String response
        OutputEvent.Status status
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

        status = OutputEvent.Status.Success

        // Return remote call response
        OutputEvent.fromBytes(response.bytes, status,  contentType)
    }
}