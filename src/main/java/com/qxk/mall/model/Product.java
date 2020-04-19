package com.qxk.mall.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

@TableName("product")
@Getter
@Setter
@ToString
@Document(indexName = "mall_springboot",type = "product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cid;

    @TableField(exist = false)
	private Category category;

	//如果既没有指明 关联到哪个Column,又没有明确要用@Transient忽略，那么就会自动关联到表对应的同名字段
	private String name;
	private String subTitle;
	private float originalPrice;
	private float promotePrice;
	private int stock;
	private Date createDate;

    @TableField(exist = false)
	private ProductImage firstProductImage;

    @TableField(exist = false)
	private List<ProductImage> productSingleImages;

    @TableField(exist = false)
	private List<ProductImage> productDetailImages;

    @TableField(exist = false)
    private Integer reviewCount;

    @TableField(exist = false)
    private Integer saleCount;

	@Override
	public String toString() {
		return "Product [id=" + id + ", category=" + category + ", name=" + name + ", subTitle=" + subTitle
				+ ", originalPrice=" + originalPrice + ", promotePrice=" + promotePrice + ", stock=" + stock
				+ ", createDate=" + createDate + ", firstProductImage=" + firstProductImage + ", reviewCount="
				+ reviewCount + ", saleCount=" + saleCount + "]";
	}



}


