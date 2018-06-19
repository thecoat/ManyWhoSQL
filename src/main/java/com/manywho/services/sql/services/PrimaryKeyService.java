package com.manywho.services.sql.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;

public class PrimaryKeyService {

    private ObjectMapper objectMapper;

    public PrimaryKeyService() {
        objectMapper = new ObjectMapper();
    }

    public HashMap<String, String> deserializePrimaryKey(String serializedPrimaryKey) {
        try {
            byte[] decoded = Base64.decodeBase64(serializedPrimaryKey.getBytes());
            return objectMapper.readValue(new String(decoded, "UTF-8"), new TypeReference<HashMap<String,Object>>() {});

        } catch (Exception e) {
            throw new RuntimeException("error when deserialize primary key");
        }
    }

    public String serializePrimaryKey(HashMap<String, String> primaryKeys) {
        try {
            return new String(Base64.encodeBase64(objectMapper.writeValueAsString(primaryKeys).getBytes()), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("error when serialize primary key");
        }
    }
}
