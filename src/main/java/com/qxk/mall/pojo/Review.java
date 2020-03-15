package com.qxk.mall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@TableName("review")
@Getter
@Setter
@ToString
public class Review {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private Integer pid;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Product product;

    private String content;
    private Date createDate;
}


