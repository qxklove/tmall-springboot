

package com.qxk.tmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.qxk.tmall.dao.ProductImageDAO;
import com.qxk.tmall.pojo.OrderItem;
import com.qxk.tmall.pojo.Product;
import com.qxk.tmall.pojo.ProductImage;
import com.qxk.tmall.util.SpringContextUtil;

@Service
@CacheConfig(cacheNames="productImages")
public class ProductImageService   {

	public static final String type_single = "single";
	public static final String type_detail = "detail";

	@Autowired ProductImageDAO productImageDAO;
	@Autowired ProductService productService;
	@Autowired CategoryService categoryService;

	@Cacheable(key="'productImages-one-'+ #p0")
	public ProductImage get(int id) {
		return productImageDAO.findOne(id);
	}

	public void setFirstProdutImage(Product product) {
		ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
		List<ProductImage> singleImages = productImageService.listSingleProductImages(product);
		if(!singleImages.isEmpty())
			product.setFirstProductImage(singleImages.get(0));
		else
			product.setFirstProductImage(new ProductImage()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。

	}
	@CacheEvict(allEntries=true)
	public void add(ProductImage bean) {
		productImageDAO.save(bean);

	}
	@CacheEvict(allEntries=true)
	public void delete(int id) {
		productImageDAO.delete(id);
	}

	@Cacheable(key="'productImages-single-pid-'+ #p0.id")
	public List<ProductImage> listSingleProductImages(Product product) {
		return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_single);
	}
	@Cacheable(key="'productImages-detail-pid-'+ #p0.id")
	public List<ProductImage> listDetailProductImages(Product product) {
		return productImageDAO.findByProductAndTypeOrderByIdDesc(product, type_detail);
	}

	public void setFirstProdutImages(List<Product> products) {
		for (Product product : products)
			setFirstProdutImage(product);
	}

	public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
		for (OrderItem orderItem : ois) {
			setFirstProdutImage(orderItem.getProduct());
		}
	}
}


