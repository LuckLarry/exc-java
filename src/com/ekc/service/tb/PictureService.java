package com.ekc.service.tb;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 图片表
 * 
 * @author ZhengXiajun 仅存储图片的基础信息
 */
@Service("ePicture_Ser")
public class PictureService extends TableUseAbs {
	@Autowired 
	TableUseIfc ePicturePlaceObject_Ser;
	@Override
	public String getTable() {
		return TName.ePicture;
	}

	@Override
	public String getPrimaryKey() {
		return "picture_id";
	}
	
	/**
	 * @throws Exception 
	 * @see
	 * 删除图片，如果图片位置，则先删除图片位置，再删除分类
	 * 如果分类中不包含图片位置，则删除图片
	 */
	public int delete(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> places=null;
		if(map.containsKey("picture_id")){
			places=ePicturePlaceObject_Ser.findList("picture_id",map.get("picture_id").toString());
		}
		int ci=0;
		if ((places!=null&&places.size()>0)){
			int i=ePicturePlaceObject_Ser.delete("picture_id",map.get("picture_id").toString());
			if(i>0){
				ci=delete(getTable(), map);
			}
		}else{
			ci=delete(getTable(), map);
		}
		return ci;
	}
	
	 
}
