package com.example.demo.repository;

import com.example.demo.entity.db.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,String> {
}
