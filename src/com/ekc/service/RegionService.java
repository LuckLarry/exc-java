package com.ekc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.MessageXml;
import com.ekc.xml.TName;

/**
 * 操作地区表信息
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Service("rService")
public class RegionService extends BaseService {
	/**
	 * 通过regionId获得地区信息
	 * 
	 * @param region_idList
	 *            region_id集合
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getRegions(List<String> region_idList) {
		return select(TName.eRegion, "*", new WhereTable("region_id",
				region_idList).getMap());
	}

	/**
	 * 更加区号获得详细地址
	 * 
	 * @param district
	 * @return
	 */
	public String getAddress(int district) {
		return getAddress(0, 0, 0, district);
	}

	/**
	 * 
	 * @param country
	 *            国家
	 * @param province
	 *            省会
	 * @param city
	 *            城市
	 * @param district
	 *            区
	 * @return
	 */
	public String getAddress(int country, int province, int city, int district) {
		JoinTable join = new JoinTable(TName.eRegion, "g");
		join.leftJoin(TName.eRegion, "s", "s.parent_id=g.region_id");
		join.leftJoin(TName.eRegion, "c", "c.parent_id=s.region_id");
		join.leftJoin(TName.eRegion, "d", "d.parent_id=c.region_id");
		WhereTable wheremap = new WhereTable("d.region_id", district);
		return selectOne(
				join.toString(),
				"(CONCAT( g.region_name , s.region_name , c.region_name,d.region_name)) as address",
				wheremap.getMap()).toString();
	}

	/**
	 * 通过parentid获得地区列表的树状结构形式
	 * 
	 * @param parentId
	 *            父节点
	 * @return map
	 */
	public Map<String, Object> treeByParentId(int parentId) {
		WhereTable wt = new WhereTable("parent_id", parentId);
		return treeByParentId(wt.getMap()).get(0);
	}

	/**
	 * 根据map参数获得地区列表的树结构形式
	 * 
	 * @param pMap
	 *            条件参数
	 * 
	 *            <pre>
	 *  如： {'parent_id':1}
	 * </pre>
	 * @return map 树节点形式Map
	 */
	public List<Map<String, Object>> treeByParentId(Map<String, Object> pMap) {
		List<Map<String, Object>> list = select(TName.eRegion, "*", pMap);
		Map<String, Object> mapObj = null;
		if (list.size() > 0) {
			mapObj = ItemHelper.findFiledToKey(select(TName.eRegion, "*"),
					"parent_id");
		}
		return childData(mapObj,list);
	}
	public List<Map<String, Object>> cangSet(Map<String, Object> pMap) {
		List<Map<String,Object>> obList=simpleJdbcTemplate.queryForList("select * FROM e_OrderBy");
        StringBuffer obBuffer=new StringBuffer("select moren");
        StringBuffer ocBuffer=new StringBuffer("select moren");
        if(obList.size()>0){
        	int order=0;
        	for(Map<String, Object> map:obList){
        		order=Integer.parseInt(map.get("id").toString());
        		obBuffer.append(",MAX(b.").append(order).append(") '").append(order).append("'");
        		ocBuffer.append(",(case when `order`=").append(order).append(" then cang_name else '' end) '").append(order).append("'");
        	}
        	ocBuffer.append(" from e_OrderCang GROUP BY moren,cang_name");
        	obBuffer.append(" from (").append(ocBuffer).append(") b GROUP BY b.moren");
        }else{
        	return null;
        }
        return simpleJdbcTemplate.queryForList(obBuffer.toString());
	}
	/**
	 * 获取树节点子数据
	 * 
	 * @param mapObj
	 *            全部数据 键为节点
	 * @param list
	 *            顶层节点数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> childData(Map<String, Object> mapObj,
			List<Map<String, Object>> list) {
		if (list!=null&&list.size() > 0) {
			Map<String, Object> map = null;
			for (int i = 0, len = list.size() - 1; i < len; i++) {// 获取子数据
				map = list.get(i);
				if (mapObj.containsKey(map.get("region_id").toString())) {
					list.get(i).put(MessageXml.nodeChild_key,
							childData(mapObj,(List<Map<String, Object>>)mapObj.get(map.get("region_id").toString())));
				}
			}
		}
		return list;
	}
}
