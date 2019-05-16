package com.fushun.springboot.demo.web.data;

import lombok.Data;

import java.util.Date;

@Data
public class Model1 {

    private Date date;

    private ModelChild modelChild;

}
