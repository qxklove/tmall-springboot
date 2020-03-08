

package com.qxk.tmall.web;

import com.qxk.tmall.pojo.Category;
import com.qxk.tmall.service.CategoryService;
import com.qxk.tmall.util.ImageUtil;
import com.qxk.tmall.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	@GetMapping("/categories")
	public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
		start = start<0?0:start;
		Page4Navigator<Category> page =categoryService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		return page;
	}

	@PostMapping("/categories")
	public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
		categoryService.add(bean);
		saveOrUpdateImageFile(bean, image, request);
		return bean;
	}
	public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
			throws IOException {
		File imageFolder= new File(request.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder,bean.getId()+".jpg");
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);
		ImageIO.write(img, "jpg", file);
	}

	@DeleteMapping("/categories/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
		categoryService.delete(id);
		File  imageFolder= new File(request.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder,id+".jpg");
		file.delete();
		return null;
	}

	@GetMapping("/categories/{id}")
	public Category get(@PathVariable("id") int id) throws Exception {
		Category bean=categoryService.get(id);
		return bean;
	}

	@PutMapping("/categories/{id}")
	public Object update(Category bean, MultipartFile image,HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		bean.setName(name);
		categoryService.update(bean);

		if(image!=null) {
			saveOrUpdateImageFile(bean, image, request);
		}
		return bean;
	}

}



