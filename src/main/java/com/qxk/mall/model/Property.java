package com.qxk.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("property")
@Getter
@Setter
@ToString
public class Property {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer cid;

    @TableField(exist = false)
	private Category category;
}


