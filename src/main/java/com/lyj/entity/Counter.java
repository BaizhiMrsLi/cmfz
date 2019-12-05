package com.lyj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counter implements Serializable {
    @Id
    private String id;
    private String name;
    private String count;
    @JsonFormat(pattern = "yyyy/MM/dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date create_date;
    private String course_id;
    private String user_id;
}
