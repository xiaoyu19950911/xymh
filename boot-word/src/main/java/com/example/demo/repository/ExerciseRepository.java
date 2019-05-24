package com.example.demo.repository;

import com.example.demo.entity.db.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise,String> {
}
