package com.lyj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cmfz", type = "article")
public class Article implements Serializable {
    @Id
    @org.springframework.data.annotation.Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Transient
    private String cover;

    @Field(type = FieldType.Keyword)
    private String author;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date)
    private Date create_date;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date)
    private Date publish_date;

    @Transient
    private String status;
    @Transient
    private String guru_id;
}
