package com.ekc.service.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 仓库库存信息表
 * 
 * @author hui
 */
@Service("WarehousesSer")
public class WarehousesService extends TableUseAbs {
	@Autowired
	TableUseIfc euserrank_Ser;
	@Override
	public String getTable() {
		return TName.eWarehouses;
	}
	@Override
	public String getPrimaryKey() {
		return "warehouse_id";
	}
//	/**
//	 * @see  加入不同会有价格
//	 */
//	public List<Map<String, Object>> findList(Map<String, Object> map) throws Exception {
//		List<Map<String,Object>> userList=euserrank_Ser.findList(new HashMap<String, Object>());
//		JoinTable join=new JoinTable(getTable(), "a");
//		StringBuffer colStr=new StringBuffer("a.*");
//		Map<String,String> uTmap=new HashMap<String, String>();
//		String key=null;
//		String keyV=null;
//		for (Map<String,Object> userMap:userList) {
//			if(ItemHelper.isNotEmpty(userMap,"rank_name")){
//				key=userMap.get("rank_name").toString();
//				keyV="(select stock_price from "+getTable()+" where user_rank="+userMap.get("rank_id").toString()+" and storage_id=a.storage_id and sku_id=a.sku_id)";
//			    uTmap.put(key, keyV);
//			    colStr.append(",").append(keyV).append(" as ").append(key);
//			}
//		}
//		WhereTable where=new WhereTable();
//		where.put("add", "GROUP BY a.storage_id");
//		for (String pkey:map.keySet()) {
//			if(uTmap.containsKey(pkey)){
//				where.put(uTmap.get(pkey).toString(), map.get(pkey));
//			}else{
//				where.put(pkey, map.get(pkey));
//			}
//		}
//		return select(join.toString(), colStr.toString(), where.getMap());
//	}

	
//	@SuppressWarnings("unchecked")
//	@Override
//	public int update(Map<String, Object> map, Map<String, Object> wMap) throws Exception{
//	    List<Map<String, Object>> userRankList=euserrank_Ser.findList(new HashMap<String, Object>());
//	    Map<String ,Object> mapPr=new HashMap<String,Object>();
//	    Map<String,Object> userRankMap=ItemHelper.findFiledToKey(userRankList, "rank_name");
//	    int doIndex=0;
//	    if(map.containsKey("warehouse_id")){
//	    	map.remove("warehouse_id");
//	    }
//	    for (String rankName:userRankMap.keySet()) {
//			if(map.containsKey(rankName)){
//				mapPr.put(rankName,map.get(rankName));
//				map.remove(rankName);
//			}
//		}
//	    Map<String,Object> hasMap=null;
//	    for (String rankName:mapPr.keySet()) {
//	    	map.put("stock_price", mapPr.get(rankName));
//	    	wMap.put("user_rank", ((List<Map<String,Object>>)userRankMap.get(rankName)).get(0).get("rank_id"));
//		    //TODO 存在与否 做修改或者添加
//	    	hasMap=findRow(wMap);
//	    	if(hasMap!=null){
//	    		//修改
//	    		doIndex=1;
//	    	}else{
//	    		//新增
//	    		doIndex=1;
//	    	}
//	    }
//	    return doIndex;
//	}
}
