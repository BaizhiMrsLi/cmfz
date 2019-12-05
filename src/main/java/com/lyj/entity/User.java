package com.lyj.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyj.override.ImageConverter;
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
@ColumnWidth(10)
@ContentRowHeight(50)
public class User implements Serializable {
    @Id
    @KeySql(sql = "select uuid()",order = ORDER.BEFORE)
    @ExcelProperty("用户ID")
    @ColumnWidth(60)
    private String id;
    @ExcelProperty("电话")
    private String tel;
    @ExcelIgnore
    private String password;
    @ExcelIgnore
    private String salt;
    @ExcelProperty("用户名")
    private String name;
    @ExcelProperty("昵称")
    private String nickname;
    @ExcelProperty("性别：1男，2女")
    private String sex;
    @ExcelProperty("个性签名")
    private String signature;
    @ExcelProperty(converter = ImageConverter.class,value = "头像")
    @ColumnWidth(30)
    private String cover;
    @ExcelProperty("地址")
    private String address;
    @JsonFormat(pattern = "yyyy/MM/dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @ExcelProperty("注册时间")
    private Date registration_date;
    @JsonFormat(pattern = "yyyy/MM/dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @ExcelProperty("最后登录时间")
    private Date last_login;
    @ExcelProperty("状态：1正常，2冻结")
    private String status;
}
