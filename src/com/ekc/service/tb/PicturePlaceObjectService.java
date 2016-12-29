package com.ekc.service.tb;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 * 图片位置表
 * 
 * @author ZhengXiajun 仅存储图片位置信息
 */
@Service("ePicturePlaceObject_Ser")
public class PicturePlaceObjectService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.ePicturePlaceObject;
	}

	@Override
	public String getPrimaryKey() {
		return "ppo_id";
	}
}
