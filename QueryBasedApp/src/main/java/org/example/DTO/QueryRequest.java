package org.example.DTO;

import lombok.Data;

import java.util.Map;
@Data
public class QueryRequest {

    private Long queryId;

    // this is query_store.description
    private String queryDescription;

    // these are SQL parameters (like :description inside SQL)
    private Map<String, Object> params;

    // getters & setters
}