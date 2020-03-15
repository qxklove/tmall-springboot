package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.PropertyValueMapper;
import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.Property;
import com.qxk.mall.pojo.PropertyValue;
import com.qxk.mall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="propertyValues")
public class PropertyValueService extends ServiceImpl<PropertyValueMapper, PropertyValue> {

	@Autowired PropertyService propertyService;

	@CacheEvict(allEntries=true)
	public void update(PropertyValue bean) {
		this.updateById(bean);
	}

	public void init(Product product) {
		PropertyValueService propertyValueService = SpringContextUtil.getBean(PropertyValueService.class);
		List<Property> propertys= propertyService.listByCategory(product.getCategory());
		for (Property property: propertys) {
			PropertyValue propertyValue = propertyValueService.getByPropertyAndProduct(product, property);
			if(null==propertyValue){
				propertyValue = new PropertyValue();
				propertyValue.setProduct(product);
				propertyValue.setPid(product.getId());
				propertyValue.setProperty(property);
				propertyValue.setPtid(property.getId());
				this.save(propertyValue);
			}
		}
	}

	@Cacheable(key="'propertyValues-one-pid-'+#p0.id+ '-ptid-' + #p1.id")
	public PropertyValue getByPropertyAndProduct(Product product, Property property) {
		return this.getOne(new QueryWrapper<PropertyValue>()
				.eq("pid", product.getId())
				.eq("ptid", property.getId()));
	}

	@Cacheable(key="'propertyValues-pid-'+ #p0.id")
	public List<PropertyValue> list(Product product) {
		List<PropertyValue> list = this.list(new QueryWrapper<PropertyValue>()
				.eq("pid", product.getId())
				.orderByDesc("id"));
		list.stream().forEach(value -> value.setProduct(product));
		list.stream().forEach(value -> value.setProperty(propertyService.getById(value.getPtid())));
		return list;
	}

}


