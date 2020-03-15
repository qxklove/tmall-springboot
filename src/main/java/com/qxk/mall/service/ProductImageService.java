package com.qxk.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qxk.mall.mapper.ProductImageMapper;
import com.qxk.mall.pojo.OrderItem;
import com.qxk.mall.pojo.Product;
import com.qxk.mall.pojo.ProductImage;
import com.qxk.mall.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="productImages")
public class ProductImageService extends ServiceImpl<ProductImageMapper, ProductImage> {

	public static final String type_single = "single";
	public static final String type_detail = "detail";

	@Autowired ProductService productService;
	@Autowired CategoryService categoryService;

	@Cacheable(key="'productImages-one-'+ #p0")
	public ProductImage get(int id) {
        return this.getById(id);
	}

    public void setFirstProdutImage(Product product) {
        ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
        List<ProductImage> singleImages = productImageService.listSingleProductImages(product);
        if (!singleImages.isEmpty()) {
            singleImages.get(0).setProduct(product);
            product.setFirstProductImage(singleImages.get(0));
        } else {
            product.setFirstProductImage(new ProductImage()); //这样做是考虑到产品还没有来得及设置图片，但是在订单后台管理里查看订单项的对应产品图片。
        }
    }

	@CacheEvict(allEntries=true)
	public void add(ProductImage bean) {
        this.save(bean);
    }

	@CacheEvict(allEntries=true)
	public void delete(int id) {
        this.removeById(id);
	}

	@Cacheable(key="'productImages-single-pid-'+ #p0.id")
	public List<ProductImage> listSingleProductImages(Product product) {
        List<ProductImage> list = this.list(new QueryWrapper<ProductImage>()
                .eq("pid", product.getId())
                .eq("type", type_single)
                .orderByDesc("id"));
        list.stream().forEach(image -> image.setProduct(product));
        return list;
    }

	@Cacheable(key="'productImages-detail-pid-'+ #p0.id")
	public List<ProductImage> listDetailProductImages(Product product) {
        List<ProductImage> list = this.list(new QueryWrapper<ProductImage>()
                .eq("pid", product.getId())
                .eq("type", type_detail)
                .orderByDesc("id"));
        list.stream().forEach(image -> image.setProduct(product));
        return list;
	}

	public void setFirstProdutImages(List<Product> products) {
        for (Product product : products) {
            setFirstProdutImage(product);
        }
	}

	public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
		for (OrderItem orderItem : ois) {
			setFirstProdutImage(orderItem.getProduct());
		}
	}
}


