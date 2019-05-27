package com.example.demo.repository;

import com.example.demo.entity.db.Exercise;
import com.example.demo.entity.db.ExerciseExerciseRelation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-27 17:09
 */
public interface ExerciseExerciseRelationRepository extends JpaRepository<ExerciseExerciseRelation,String> {
}
