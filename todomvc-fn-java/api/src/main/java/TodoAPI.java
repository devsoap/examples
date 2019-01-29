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

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnproject.fn.api.InputEvent;
import com.fnproject.fn.api.OutputEvent;
import com.fnproject.fn.api.httpgateway.HTTPGatewayContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.fnproject.fn.api.OutputEvent.Status.FunctionError;
import static com.fnproject.fn.api.OutputEvent.Status.Success;
import static com.fnproject.fn.api.OutputEvent.fromBytes;

/**
 * API Function for managing Todo Items
 */
public class TodoAPI {

    private static final String JSON_CONTENT_TYPE = "application/json";

    private final DynamoDBMapper dbMapper;

    public TodoAPI() {
        var awsProperties = getAWSProperties();

        var awsCredentials = new BasicAWSCredentials(
                awsProperties.getProperty("aws.accessKeyId"),
                awsProperties.getProperty("aws.secretKey"));

        var awsClient = AmazonDynamoDBClient.builder()
                .withRegion(awsProperties.getProperty("aws.region"))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        dbMapper = new DynamoDBMapper(awsClient);
    }

    /**
     * Handles requests to the function
     *
     * @param context
     *      the function context
     * @param input
     *      the input to the function
     */
    public OutputEvent handleRequest(HTTPGatewayContext context, InputEvent input) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        switch (context.getMethod()) {
            case "GET": {
                return fromBytes(objectMapper.writeValueAsBytes(getItems()), Success, JSON_CONTENT_TYPE);
            }
            case "POST": {
                TodoItem todoItem = input.consumeBody(TodoItem::fromStream);
                return fromBytes(objectMapper.writeValueAsBytes(addItem(todoItem)), Success, JSON_CONTENT_TYPE);
            }
            case "PUT": {
                TodoItem todoItem = input.consumeBody(TodoItem::fromStream);
                return fromBytes(objectMapper.writeValueAsBytes(updateItem(todoItem)), Success, JSON_CONTENT_TYPE);
            }
            case "DELETE": {
                TodoItem todoItem = input.consumeBody(TodoItem::fromStream);
                return fromBytes(objectMapper.writeValueAsBytes(deleteItem(todoItem)), Success, JSON_CONTENT_TYPE);
            }
            default: return OutputEvent.emptyResult(FunctionError);
        }
    }

    /**
     * Retrieve all todo items
     */
    private List<TodoItem> getItems() {
        return new ArrayList<>(dbMapper.scan(TodoItem.class, new DynamoDBScanExpression()));
    }

    /**
     * Update an todo item
     *
     * @param item
     *      the item to update
     */
    private TodoItem updateItem(TodoItem item) {
        dbMapper.save(item);
        return item;
    }

    /**
     * Add a new todo item
     *
     * @param item
     *      the item to add
     */
    private TodoItem addItem(TodoItem item) {
        dbMapper.save(item);
        return item;
    }

    /**
     * Remove a todo item
     *
     * @param item
     *      the item to remove
     */
    private TodoItem deleteItem(TodoItem item) {
        dbMapper.delete(item);
        return item;
    }

    /**
     * Returns the AWS Credentials
     */
    private static Properties getAWSProperties() {
        var awsProperties = new Properties();
        try {
            awsProperties.load(TodoAPI.class.getResourceAsStream("/aws-credentials.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load AWS credentials!", e);
        }
        return awsProperties;
    }
}