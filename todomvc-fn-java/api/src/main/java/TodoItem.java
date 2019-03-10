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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@DynamoDBTable(tableName = "todomvc")
public class TodoItem implements Serializable {

    private String id = UUID.randomUUID().toString();
    private boolean active = true;
    private String description;

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Helper method to create a TodoItem from an InputStream
     */
    public static Optional<TodoItem> fromStream(InputStream stream) {
        try {
            return Optional.of(new ObjectMapper().readValue(stream, TodoItem.class));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Helper method to convert the items into a byte array
     */
    public Optional<byte[]> toBytes() {
        try {
            return Optional.of(new ObjectMapper().writeValueAsBytes(this));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}