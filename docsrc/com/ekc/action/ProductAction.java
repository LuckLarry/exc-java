package com.ekc.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.service.ProductService;
import com.ekc.xml.MethodsXml;

/**
 * 产品类
 * 
 * @author hui
 * 
 */
@Controller
@RequestMapping("/product.do")
public class ProductAction {
	@Autowired
	ProductService productService;

	/**
	 * 
	 * 查询分页 *
	 * 
	 * @return <pre>
	 * 暂无
	 * </pre>
	 * 
	 *         <h6>例子</h6>
	 * <strong>请求 URL：</strong>
	 * <strong>JSON 参数：</strong>
	 * <strong>Headers 参数：</strong>ticket = 45feb928-3ff2-4f98-a4a2-b9ebb3899992
	 * <strong>返回结果：</strong>
	 * <pre>
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage })
	public @ResponseBody
	List<Map<String, Object>> getlist() {
		return null;
	}
}
