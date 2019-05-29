package com.example.demo.repository;

import com.example.demo.entity.db.Passages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassagesRepository extends JpaRepository<Passages,String> {
}
