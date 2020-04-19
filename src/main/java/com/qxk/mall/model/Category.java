package com.qxk.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@TableName("category")
@Getter
@Setter
@ToString
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;

    @TableField(exist = false)
    List<Product> products;

    @TableField(exist = false)
    List<List<Product>> productsByRow;
}


