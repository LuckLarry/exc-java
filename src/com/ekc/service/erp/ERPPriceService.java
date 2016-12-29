package com.ekc.service.erp;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * Gaohui date:2016-02-29
 * 
 * @author hui
 * 
 */
@Service("erppriceService")
public class ERPPriceService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eERPPrice;
	}

	@Override
	public String getPrimaryKey() {
		return "erp_price_id";
	}
	
	@Override
	public Map<String, Object> findPage(Map<String, Object> map) throws Exception{
		return selectPage(getTable(), "*", map);
	}
}
