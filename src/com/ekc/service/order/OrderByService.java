package com.ekc.service.order;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;
/**
 * 仓库排序表
 * 
 * @author ZhengXiajun
 *  对仓库进行排序
 */
@Service("OrderBySer")
public class OrderByService extends TableUseAbs{

	@Override
	public String getTable() {
		return TName.eOrderBy;
	}

	@Override
	public String getPrimaryKey() {
		return "id";
	}

}
