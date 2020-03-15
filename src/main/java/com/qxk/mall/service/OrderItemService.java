package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.OrderItemMapper;
import com.qxk.mall.mapper.ProductMapper;
import com.qxk.mall.pojo.Order;
import com.qxk.mall.pojo.OrderItem;
import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.User;
import com.qxk.mall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="orderItems")
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItem> {
	@Autowired
	ProductImageService productImageService;
	@Autowired
	ProductMapper productMapper;

	public void fill(List<Order> orders) {
		for (Order order : orders) {
			fill(order);
		}
	}

	public void fill(Order order) {
		OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);
		List<OrderItem> orderItems = orderItemService.listByOrder(order);
		float total = 0;
		int totalNumber = 0;
		for (OrderItem oi :orderItems) {
			total+=oi.getNumber()*oi.getProduct().getPromotePrice();
			totalNumber+=oi.getNumber();
			productImageService.setFirstProdutImage(oi.getProduct());
		}
		order.setTotal(total);
		order.setOrderItems(orderItems);
		order.setTotalNumber(totalNumber);
	}

	@CacheEvict(allEntries=true)
    public void add(OrderItem orderItem) {
		this.save(orderItem);
    }

	@CacheEvict(allEntries = true)
	public void update(OrderItem orderItem) {
		this.updateById(orderItem);
	}

	@Cacheable(key="'orderItems-one-'+ #p0")
    public OrderItem get(int id) {
		OrderItem orderItem = this.getById(id);
		orderItem.setProduct(productMapper.selectById(orderItem.getPid()));
		return orderItem;
	}

	@CacheEvict(allEntries=true)
    public void delete(int id) {
		this.removeById(id);
    }

    public int getSaleCount(Product product) {
		OrderItemService orderItemService = SpringContextUtil.getBean(OrderItemService.class);
        List<OrderItem> ois =orderItemService.listByProduct(product);
        int result =0;
        for (OrderItem oi : ois) {
			if (null != oi.getOrder()) {
				if (null != oi.getOrder() && null != oi.getOrder().getPayDate()) {
					result += oi.getNumber();
				}
			}
        }
        return result;
    }

	@Cacheable(key = "'orderItems-uid-'+ #p0.id")
	public List<OrderItem> listByUser(User user) {
		List<OrderItem> list = this.list(new QueryWrapper<OrderItem>()
				.eq("uid", user.getId())
				.isNull("oid"));
		list.stream().forEach(item -> item.setProduct(productMapper.selectById(item.getPid())));
		list.stream().forEach(item -> item.setUser(user));
		return list;
	}

	@Cacheable(key = "'orderItems-pid-'+ #p0.id")
	public List<OrderItem> listByProduct(Product product) {
		List<OrderItem> list = this.list(new QueryWrapper<OrderItem>()
				.eq("pid", product.getId()));
		list.stream().forEach(item -> item.setProduct(product));
		return list;
	}

	@Cacheable(key = "'orderItems-oid-'+ #p0.id")
	public List<OrderItem> listByOrder(Order order) {
		List<OrderItem> list = this.list(new QueryWrapper<OrderItem>()
				.eq("oid", order.getId())
				.orderByDesc("id"));
		list.stream().forEach(item -> item.setProduct(productMapper.selectById(item.getPid())));
		return list;
	}

}


