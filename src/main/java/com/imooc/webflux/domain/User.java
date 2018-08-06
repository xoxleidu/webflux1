package com.imooc.webflux.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "user")
@Data//自动set-get-toString
public class User {

    @Id
    private String id;
    private String name;
    private String password;
    private int age;
    private Date date;

}
