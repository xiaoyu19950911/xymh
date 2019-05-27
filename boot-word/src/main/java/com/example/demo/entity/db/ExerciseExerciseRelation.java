package com.example.demo.entity.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-27 16:25
 */
@Entity
public class ExerciseExerciseRelation {

    @Id
    @GeneratedValue(generator = "generateIdStrategy")
    @GenericGenerator(name = "generateIdStrategy",strategy = "com.example.demo.config.IdGenerator")
    private String id;

    private String exerciseId;

    private String roleId;

    private String exerciseIdTo;

    private String roleIdTo;

    private Integer weight;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getExerciseIdTo() {
        return exerciseIdTo;
    }

    public void setExerciseIdTo(String exerciseIdTo) {
        this.exerciseIdTo = exerciseIdTo;
    }

    public String getRoleIdTo() {
        return roleIdTo;
    }

    public void setRoleIdTo(String roleIdTo) {
        this.roleIdTo = roleIdTo;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ExerciseExerciseRelationRepository{" +
                "id='" + id + '\'' +
                ", exerciseId='" + exerciseId + '\'' +
                ", roleId='" + roleId + '\'' +
                ", exerciseIdTo='" + exerciseIdTo + '\'' +
                ", roleIdTo='" + roleIdTo + '\'' +
                ", weight=" + weight +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
