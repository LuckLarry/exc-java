package com.ekc.action.goods;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.action.BaseAction;
import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.util.ItemHelper;

/**
 * 操作 库存查询表
 * @author ZhengXiajun
 *
 */
@Controller
@RequestMapping("/goods/stockcheck.do")
public class StockCkeckAction extends BaseAction {
	@Autowired
	 TableUseIfc  StockCheckSer;
	@Override
	public TableUseIfc getTabelServer() {
		return StockCheckSer;
	}
	public @ResponseBody
	Map<String, Object> addRow(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			String keyId=ItemHelper.createPrimaryKey();
			map.put(StockCheckSer.getPrimaryKey(), keyId);
			int num = getTabelServer().addRow(map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}else{
				mapRe.put(StockCheckSer.getPrimaryKey(), keyId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}
}