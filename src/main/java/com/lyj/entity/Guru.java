package com.lyj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.ORDER;

import javax.persistence.Id;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guru implements Serializable {
    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    private String id;
    private String name;
    private String cover;
    private String nickname;
    private String status;
}
