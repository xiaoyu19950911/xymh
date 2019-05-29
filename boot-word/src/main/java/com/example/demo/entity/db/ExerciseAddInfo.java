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
 * @create: 2019-05-29 16:20
 */
@Entity
public class ExerciseAddInfo {

    @Id
    @GeneratedValue(generator = "generateIdStrategy")
    @GenericGenerator(name = "generateIdStrategy",strategy = "com.example.demo.config.IdGenerator")
    private String id;

    private String errorId;

    private String seoContent;

    private String searchableContent;

    private String xmlContent;

    private Integer completeness;

    private Integer hardValid;

    private Integer similarValid;

    private Integer knowledgePointValid;

    private Integer formTypeValid;

    private Integer semanticTypeValid;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getSeoContent() {
        return seoContent;
    }

    public void setSeoContent(String seoContent) {
        this.seoContent = seoContent;
    }

    public String getSearchableContent() {
        return searchableContent;
    }

    public void setSearchableContent(String searchableContent) {
        this.searchableContent = searchableContent;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    public Integer getCompleteness() {
        return completeness;
    }

    public void setCompleteness(Integer completeness) {
        this.completeness = completeness;
    }

    public Integer getHardValid() {
        return hardValid;
    }

    public void setHardValid(Integer hardValid) {
        this.hardValid = hardValid;
    }

    public Integer getSimilarValid() {
        return similarValid;
    }

    public void setSimilarValid(Integer similarValid) {
        this.similarValid = similarValid;
    }

    public Integer getKnowledgePointValid() {
        return knowledgePointValid;
    }

    public void setKnowledgePointValid(Integer knowledgePointValid) {
        this.knowledgePointValid = knowledgePointValid;
    }

    public Integer getFormTypeValid() {
        return formTypeValid;
    }

    public void setFormTypeValid(Integer formTypeValid) {
        this.formTypeValid = formTypeValid;
    }

    public Integer getSemanticTypeValid() {
        return semanticTypeValid;
    }

    public void setSemanticTypeValid(Integer semanticTypeValid) {
        this.semanticTypeValid = semanticTypeValid;
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
        return "ExerciseAddInfo{" +
                "id='" + id + '\'' +
                ", errorId='" + errorId + '\'' +
                ", seoContent='" + seoContent + '\'' +
                ", searchableContent='" + searchableContent + '\'' +
                ", xmlContent='" + xmlContent + '\'' +
                ", completeness=" + completeness +
                ", hardValid=" + hardValid +
                ", similarValid=" + similarValid +
                ", knowledgePointValid=" + knowledgePointValid +
                ", formTypeValid=" + formTypeValid +
                ", semanticTypeValid=" + semanticTypeValid +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
