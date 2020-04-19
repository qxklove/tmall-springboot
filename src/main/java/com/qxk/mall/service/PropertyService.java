package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.PropertyMapper;
import com.qxk.mall.model.Category;
import com.qxk.mall.model.Property;
import com.qxk.mall.util.Page4Navigator;
import com.qxk.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="properties")
public class PropertyService extends ServiceImpl<PropertyMapper, Property> {

	@Autowired CategoryService categoryService;

	@CacheEvict(allEntries=true)
	public void add(Property bean) {
		this.save(bean);
	}

	@CacheEvict(allEntries=true)
	public void delete(int id) {
		this.removeById(id);
	}

	@Cacheable(key="'properties-one-'+ #p0")
	public Property get(int id) {
		Property property = this.getById(id);
		property.setCategory(categoryService.getById(property.getCid()));
		return property;
	}

	@CacheEvict(allEntries=true)
	public void update(Property bean) {
		this.updateById(bean);
	}
	@Cacheable(key="'properties-cid-'+ #p0.id")
	public List<Property> listByCategory(Category category){
		return this.list(new QueryWrapper<Property>().eq("cid", category.getId()));
	}


	@Cacheable(key="'properties-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
	public Page4Navigator<Property> list(int cid, int start, int size,int navigatePages) {
		PageUtil<Property> p = new PageUtil<>(start, size);
		Page<Property> pageList = this.page(p, new QueryWrapper<Property>()
				.eq("cid", cid)
				.orderByDesc("id"));
		return new Page4Navigator<>(pageList, navigatePages);
	}

}


