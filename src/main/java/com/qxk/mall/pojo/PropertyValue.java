package com.qxk.mall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("property_value")
@Getter
@Setter
@ToString
public class PropertyValue {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private Integer ptid;

    @TableField(exist = false)
	private Product product;

    @TableField(exist = false)
	private Property property;

    private String value;

	@Override
	public String toString() {
		return "PropertyValue [id=" + id + ", product=" + product + ", property=" + property + ", value=" + value + "]";
	}

}


