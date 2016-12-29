package com.ekc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public class ProductService extends BaseService {
	public List<Map<String, Object>> getlist() {
		String sql = "SELECT * FROM ekc_products";
		return super.jdbcTemplate.queryForList(sql);
	}
}
