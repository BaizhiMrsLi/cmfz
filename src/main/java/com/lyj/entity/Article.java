package com.lyj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {
    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    private String id;
    private String name;
    private String cover;
    private String author;
    private String content;
    @JsonFormat(pattern = "yyyy/MM/dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date create_date;
    @JsonFormat(pattern = "yyyy/MM/dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date publish_date;
    private String status;
    private String guru_id;
}
