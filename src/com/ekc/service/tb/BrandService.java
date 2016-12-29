package com.ekc.service.tb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 商品品牌表
 * 
 * @author hui
 */
@Service("eBrand_Ser")
public class BrandService extends TableUseAbs {
    @Autowired
    TableUseIfc eGoods_Ser;
	@Override
	public String getTable() {
		return TName.eBrand;
	}

	@Override
	public String getPrimaryKey() {
		return "brand_id";
	}
	
	/**
	 * @throws Exception 
	 * @see
	 * 删除品牌，如果包含商品，则先删除商品
	 * 如果不包含商品，则直接删除商品
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> goods=null;
		if(map.containsKey("brand_id")){
		goods=eGoods_Ser.findList("brand_id",map.get("brand_id").toString());
		}
		int ci=0;
		if ((goods!=null&&goods.size()>0)){
			int i=eGoods_Ser.delete("brand_id",map.get("brand_id").toString());
			if(i>0){
				ci=delete(getTable(), map);
			}
		}else{
			ci=delete(getTable(), map);
		}
		return ci;
	}
	
}
