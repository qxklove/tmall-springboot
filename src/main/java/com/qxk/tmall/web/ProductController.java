

package com.qxk.tmall.web;

import com.qxk.tmall.pojo.Product;
import com.qxk.tmall.service.CategoryService;
import com.qxk.tmall.service.ProductImageService;
import com.qxk.tmall.service.ProductService;
import com.qxk.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class ProductController {
	@Autowired
    ProductService productService;
	@Autowired
    CategoryService categoryService;
	@Autowired
    ProductImageService productImageService;

    @GetMapping("/categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
    	start = start<0?0:start;
    	Page4Navigator<Product> page =productService.list(cid, start, size,5 );

        productImageService.setFirstProdutImages(page.getContent());

    	return page;
    }

    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id) throws Exception {
    	Product bean=productService.get(id);
    	return bean;
    }


    @PostMapping("/products")
    public Object add(@RequestBody Product bean) throws Exception {
    	bean.setCreateDate(new Date());
        productService.add(bean);
        return bean;
    }



    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        productService.delete(id);
        return null;
    }

    @PutMapping("/products")
    public Object update(@RequestBody Product bean) throws Exception {
        productService.update(bean);
        return bean;
    }
}





