package com.ekc.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.xml.TName;

/**
 * 订单详细信息表
 * 
 * @author hui
 */
@Service("OrderInfoSer")
public class OrderInfoService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eOrderInfo;
	}

	@Override
	public String getPrimaryKey() {
		return "order_id";
	}
	
	@Override
	public List<Map<String, Object>> findList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list= select(getTable(), "*", map);
		//TODO 为了让erp能够一定获取到，读取list记录第一条的用户，用用户获取erp中的订单状态。
		if(list!=null&&list.size()>0){//只有存在订单才处理状态
		    String user_id=list.get(0).get("user_id").toString();
			//读取erp，back.do 参数会员id
//		    List<Map<String, Object>> dd=ItemHelper.getERPList(7, user_id);//读取erp中的订单状态
		
			//筛选对应订单的状态信息修改
		}
		return list;
	}
}
