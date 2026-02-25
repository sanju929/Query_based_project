package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "query_store")
public class QueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_text")
    private String queryText;

    private String description;

    // getters and setters
}
