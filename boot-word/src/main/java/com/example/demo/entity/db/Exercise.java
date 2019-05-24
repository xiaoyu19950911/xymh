package com.example.demo.entity.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(generator = "generateIdStrategy")
    @GenericGenerator(name = "generateIdStrategy",strategy = "com.example.demo.config.IdGenerator")
    private String id;

    private Integer formType;

    private String semanticTypes;

    private String knowlegePointIds;

    private String sourceId;

    private String content;

    private Integer subjectId;

    private Integer gradeId;

    private Integer significance;

    private Boolean selected;

    @Column(name = "error-prone")
    private Boolean errorProne;

    private Date createTime;

    private Date updateTime;

    private String relationType;

    private String parentId;

    private Integer kpDifficulty;

    private Integer absoluteDifficulty63;

    private Integer absoluteDifficulty54;

    private BigDecimal scoreWeight;

    private Integer seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public String getSemanticTypes() {
        return semanticTypes;
    }

    public void setSemanticTypes(String semanticTypes) {
        this.semanticTypes = semanticTypes;
    }

    public String getKnowlegePointIds() {
        return knowlegePointIds;
    }

    public void setKnowlegePointIds(String knowlegePointIds) {
        this.knowlegePointIds = knowlegePointIds;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getSignificance() {
        return significance;
    }

    public void setSignificance(Integer significance) {
        this.significance = significance;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getErrorProne() {
        return errorProne;
    }

    public void setErrorProne(Boolean errorProne) {
        this.errorProne = errorProne;
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

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getKpDifficulty() {
        return kpDifficulty;
    }

    public void setKpDifficulty(Integer kpDifficulty) {
        this.kpDifficulty = kpDifficulty;
    }

    public Integer getAbsoluteDifficulty63() {
        return absoluteDifficulty63;
    }

    public void setAbsoluteDifficulty63(Integer absoluteDifficulty63) {
        this.absoluteDifficulty63 = absoluteDifficulty63;
    }

    public Integer getAbsoluteDifficulty54() {
        return absoluteDifficulty54;
    }

    public void setAbsoluteDifficulty54(Integer absoluteDifficulty54) {
        this.absoluteDifficulty54 = absoluteDifficulty54;
    }

    public BigDecimal getScoreWeight() {
        return scoreWeight;
    }

    public void setScoreWeight(BigDecimal scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id='" + id + '\'' +
                ", formType=" + formType +
                ", semanticTypes='" + semanticTypes + '\'' +
                ", knowlegePointIds='" + knowlegePointIds + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", content='" + content + '\'' +
                ", subjectId=" + subjectId +
                ", gradeId=" + gradeId +
                ", significance=" + significance +
                ", selected=" + selected +
                ", errorProne=" + errorProne +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", relationType='" + relationType + '\'' +
                ", parentId='" + parentId + '\'' +
                ", kpDifficulty=" + kpDifficulty +
                ", absoluteDifficulty63=" + absoluteDifficulty63 +
                ", absoluteDifficulty54=" + absoluteDifficulty54 +
                ", scoreWeight=" + scoreWeight +
                ", seq=" + seq +
                '}';
    }
}
