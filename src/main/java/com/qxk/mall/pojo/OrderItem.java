package com.qxk.mall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("order_item")
@Getter
@Setter
@ToString
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private Integer oid;
    private Integer uid;
    private Integer number;

    @TableField(exist = false)
	private Product product;

    @TableField(exist = false)
	private Order order;

    @TableField(exist = false)
	private User user;
}


