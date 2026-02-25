package org.example.service;

import jakarta.persistence.*;
import org.example.repository.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

//@Service
//public class DynamicQueryService {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Autowired
//    private QueryRepository queryRepository;

//    public List<?> executeQuery(Long queryId, String queryDescription, Map<String, Object> params) {
//
//        // 1️⃣ Fetch SQL from query_store
//        String sql = queryRepository.getQueryById(queryId, queryDescription);
//
//        if (sql == null || sql.isBlank()) {
//            throw new RuntimeException("Query not found!");
//        }
//
//        // 2️⃣ Execute SQL
//        Query query = entityManager.createNativeQuery(sql);
//
//        // 3️⃣ Bind SQL parameters (like :description inside sk_test)
//        if (params != null) {
//            params.forEach(query::setParameter);
//        }
//
//        return query.getResultList();
//    }
//
//    @Service
//    public class DynamicQueryService {
//
//        @Autowired
//        private EntityManager entityManager;
//
//        @Autowired
//        private QueryRepository queryRepository;
//
//        public List<?> executeQuery(Long queryId,
//                                    String queryDescription,
//                                    Map<String, Object> params) {
//
//            // 1️⃣ Load SQL
//            String sql = queryRepository.getQueryById(queryId, queryDescription);
//
//            if (sql == null || sql.isBlank()) {
//                throw new RuntimeException("Query not found for id=" + queryId);
//            }
//
//            // 2️⃣ Create native query
//            Query query = entityManager.createNativeQuery(sql);
//
//            // 3️⃣ Bind ONLY parameters that actually exist in SQL
//            if (params != null && !params.isEmpty()) {
//
//                // Collect parameter names present in SQL
//                Set<String> sqlParams = query.getParameters()
//                        .stream()
//                        .map(p -> p.getName())
//                        .collect(Collectors.toSet());
//
//                // Bind only matching params
//                params.forEach((key, value) -> {
//                    if (sqlParams.contains(key)) {
//                        query.setParameter(key, value);
//                    }
//                });
//            }
//
//            return query.getResultList();
//        }
//    }
////}

@Service
public class DynamicQueryService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QueryRepository queryRepository;

    public List<Map<String, Object>> executeQuery(
            Long queryId,
            String queryDescription,
            Map<String, Object> params) {

        // 1️⃣ Load SQL from query_store
        String sql = queryRepository.getQueryById(queryId, queryDescription);
        if (sql == null || sql.isBlank()) {
            throw new RuntimeException("Query not found for id=" + queryId);
        }

        // 2️⃣ Create native query with Tuple
        Query query = entityManager.createNativeQuery(sql, Tuple.class);

        // 3️⃣ Bind parameters safely (only existing SQL params)
        if (params != null && !params.isEmpty()) {
            Set<String> sqlParams = query.getParameters()
                    .stream()
                    .map(Parameter::getName)
                    .collect(Collectors.toSet());

            params.forEach((k, v) -> {
                if (sqlParams.contains(k)) {
                    query.setParameter(k, v);
                }
            });
        }

        // 4️⃣ Convert Tuple → Map<String,Object>
        List<Tuple> tuples = query.getResultList();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Tuple tuple : tuples) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (TupleElement<?> element : tuple.getElements()) {
                row.put(element.getAlias(), tuple.get(element));
            }
            result.add(row);
        }

        return result;
    }
}