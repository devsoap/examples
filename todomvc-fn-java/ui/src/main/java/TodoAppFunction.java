/*
 * Copyright 2019 Devsoap Inc.
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

import com.fnproject.fn.api.OutputEvent;
import com.fnproject.fn.api.httpgateway.HTTPGatewayContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Serves our react UI via a function call
 */
public class TodoAppFunction {

    private static final String APP_NAME = "todomvc";

    /**
     * Handles the incoming function request
     *
     * @param context
     *      the request context
     *
     * @return
     *      the output event with the function output
     */
    public OutputEvent handleRequest(HTTPGatewayContext context) throws IOException {
        var url = context.getRequestURL();
        var filename = url.substring(url.lastIndexOf(APP_NAME) + APP_NAME.length());
        if("".equals(filename) || "/".equals(filename)) {
            filename = "/index.html";
        }

        var body = loadFileFromClasspath(filename);

        var contentType = Files.probeContentType(Paths.get(filename));
        if(filename.endsWith(".js")) {
            contentType = "application/javascript";
        } else if(filename.endsWith(".css")) {
            contentType = "text/css";
        }

        return OutputEvent.fromBytes(body, OutputEvent.Status.Success, contentType);
    }

    /**
     * Loads a file from inside the function jar archive
     *
     * @param filename
     *      the filename to load, must start with a /
     * @return
     *      the loaded file content
     */
    private static byte[] loadFileFromClasspath(String filename) throws IOException {
        var out = new ByteArrayOutputStream();
        try (var fileStream = TodoAppFunction.class.getResourceAsStream(filename)) {
            fileStream.transferTo(out);
        }
        return out.toByteArray();
    }
}