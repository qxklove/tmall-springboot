package com.qxk.mall.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qxk.mall.service.OrderService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@TableName("order_")
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;

    @TableField(exist = false)
    private User user;

	private String orderCode;
	private String address;
	private String post;
	private String receiver;
	private String mobile;
	private String userMessage;
	private Date createDate;
	private Date payDate;
	private Date deliveryDate;
	private Date confirmDate;
	private String status;

    @TableField(exist = false)
	private List<OrderItem> orderItems;

    @TableField(exist = false)
	private float total;

    @TableField(exist = false)
    private Integer totalNumber;

    @TableField(exist = false)
	private String statusDesc;

	public String getStatusDesc(){
        if (null != statusDesc) {
            return statusDesc;
        }
		String desc ="未知";
		switch(status){
			case OrderService.waitPay:
				desc="待付";
				break;
			case OrderService.waitDelivery:
				desc="待发";
				break;
			case OrderService.waitConfirm:
				desc="待收";
				break;
			case OrderService.waitReview:
				desc="等评";
				break;
			case OrderService.finish:
				desc="完成";
				break;
			case OrderService.delete:
				desc="刪除";
				break;
			default:
				desc="未知";
		}
		statusDesc = desc;
		return statusDesc;
	}

}


