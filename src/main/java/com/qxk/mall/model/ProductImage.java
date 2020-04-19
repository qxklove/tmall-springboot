package com.qxk.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("product_image")
@Getter
@Setter
@ToString
public class ProductImage {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pid;

    @JsonBackReference
    @TableField(exist = false)
	private Product product;

    private String type;
}


