package com.example.demo.entity.db;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-29 17:14
 */
@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Passages {

    @Id
    @GeneratedValue(generator = "generateIdStrategy")
    @GenericGenerator(name = "generateIdStrategy",strategy = "com.example.demo.config.IdGenerator")
    private String id;

    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<Object> content;

    private Date createTime;

    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getContent() {
        return content;
    }

    public void setContent(List<Object> content) {
        this.content = content;
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
        return "Passages{" +
                "id='" + id + '\'' +
                ", content=" + content +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
