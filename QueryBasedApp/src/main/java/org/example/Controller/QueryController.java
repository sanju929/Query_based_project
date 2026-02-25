package org.example.Controller;

import org.example.DTO.QueryRequest;
import org.example.service.DynamicQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/query")
public class QueryController {

    @Autowired
    private DynamicQueryService dynamicQueryService;

    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody QueryRequest request) {

        List<?> result = dynamicQueryService.executeQuery(
                request.getQueryId(),
                request.getQueryDescription(),
                request.getParams()
        );

        return ResponseEntity.ok(result);
    }
}