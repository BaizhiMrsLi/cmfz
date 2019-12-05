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
public class Course implements Serializable {
    @Id
    private String id;
    private String name;
    private String level;
    @JsonFormat(pattern = "yyyy/MM/dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date create_date;
    private String user_id;
}
