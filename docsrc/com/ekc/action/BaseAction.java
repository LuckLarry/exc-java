package com.ekc.action;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekc.enumall.Message;
import com.ekc.ifc.TableUseIfc;
import com.ekc.util.ItemHelper;
import com.ekc.xml.MessageXml;
import com.ekc.xml.MethodsXml;

/**
 * 操作action 针对单表
 * 
 * @author pengbei_qq1179009513
 * 
 */
@Controller
public abstract class BaseAction {
	/**
	 * 获得操作数据对象
	 * 
	 * @return
	 */
	public abstract TableUseIfc getTabelServer();

	/**
	 * 通过对应字段的值获得对象记录
	 * 
	 * @param filed
	 * @param fildeValue
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=get&amp;date=one&amp;obj=field
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>filed:</strong>filed=?
	 *  <strong>fildeValue:</strong>fildeValue=?
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateOne,
			MethodsXml.objField })
	public @ResponseBody
	Map<String, Object> findRow(@RequestParam String filed,
			@RequestParam String fildeValue) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> map = null;
		try {
			map = getTabelServer().findRow(filed, fildeValue);
			if (map == null || map.size() == 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.data_key, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过主键查询记录
	 * 
	 * @param pkId
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=get&amp;date=one&amp;pkId=
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>pkId</strong>pkId=?
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateOne,
			MethodsXml.objPK })
	public @ResponseBody
	Map<String, Object> findRow(@RequestParam String pkId) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> map = null;
		try {
			map = getTabelServer().findRow(pkId);
			if (map == null || map.size() == 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.data_key, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过集合参数获得对象记录
	 * 
	 * @param map
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=get&amp;date=one
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateOne })
	public @ResponseBody
	Map<String, Object> findRow(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> mapData = null;
		try {
			mapData = getTabelServer().findRow(map);
			if (mapData == null || mapData.size() == 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.data_key, mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过集合参数查询分页集合信息
	 * 
	 * @param map
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=get&amp;date=page
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.datePage })
	public @ResponseBody
	Map<String, Object> findPage(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		Map<String, Object> mapData = null;
		try {
			mapData = getTabelServer().findPage(map);
			if (mapData == null
					|| Integer.parseInt(mapData.get(MessageXml.rowCount_key)
							.toString()) == 0) {
				mapRe = Message.NO_DATA.getObjMess();
			} else {
				mapRe.put(MessageXml.data_key, mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过集合参数查询所有信息
	 * 
	 * @param map
	 *            <pre>
	 * 	<strong>请求URL：</strong>...?m=get&amp;date=list
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 * @return
	 */
	@RequestMapping(params = { MethodsXml.find, MethodsXml.dateList })
	public @ResponseBody
	Map<String, Object> findList(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			List<Map<String, Object>> list = getTabelServer().findList(map);
			if (list != null && list.size() > 0) {
				mapRe.put(MessageXml.data_key, list);
			} else {
				mapRe = Message.NO_DATA.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

//	/**
//	 * 增加一条记录 主键id值为进行重写
//	 * 
//	 * @param pkfiled
//	 *            主键
//	 * @param map
//	 *            添加信息
//	 * @return <pre>
//	 * 	<strong>请求URL：</strong>...?m=add&amp;date=one&amp;obj=pk
//	 * 	<strong>ticket:</strong>ticket=?
//	 *  <strong>map:</strong>{[json格式]}
//	 * </pre>
//	 */
//	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne,
//			MethodsXml.objPK })
//	public @ResponseBody
//	Map<String, Object> addRowOnlyId(@RequestBody Map<String, Object> map) {
//		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
//		try {
//			int num = getTabelServer().addRowOnlyId(map);
//			if (num == 0) {
//				mapRe = Message.UNTREATED.getObjMess();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			mapRe = Message.UN_KNOW.getObjMess(e);
//		}
//		return mapRe;
//	}

	/**
	 * 增加一条记录
	 * 
	 * @param map
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=add&amp;date=one
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateOne })
	public @ResponseBody
	Map<String, Object> addRow(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num=0;
			if(map.containsKey("backId")&&!"".equals(map.get("backId"))){
				String key=ItemHelper.createPrimaryKey();
				map.put(getTabelServer().getPrimaryKey(), key);
				mapRe.put(getTabelServer().getPrimaryKey(), key);
				map.remove("backId");
				num = getTabelServer().addRow(map);
			}else{
				num = getTabelServer().addRowOnlyId(map);
			}			
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 增加多条记录
	 * 
	 * @param list
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=add&amp;date=rows&amp;obj=map
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>list:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.add, MethodsXml.dateRows })
	public @ResponseBody
	Map<String, Object> addRows(@RequestBody List<Map<String, Object>> list) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num[] = getTabelServer().addRows(list);
			int t = 0;
			for (int i = 0, len = num.length; i < len; i++) {
				t += num[i];
			}
			if (t == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	// 删除
	/**
	 * 根据主键，唯一值删除数据
	 * 
	 * @param pkValue
	 *            唯一值
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=del&amp;date=one&amp;obj=pk
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>pkValue:</strong>pkValue=?
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.delete, MethodsXml.dateOne,
			MethodsXml.objPK })
	public @ResponseBody
	Map<String, Object> delete(@RequestParam String pkValue) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().delete(pkValue);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 根据字段对应的值删除数据，请确保字段为唯一值【否则将删除多条记录】
	 * 
	 * @param field
	 *            字段
	 * @param value
	 *            字段对应的值
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=del&amp;date=one&amp;obj=field
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>filed:</strong>filed=?
	 *  <strong>value:</strong>value=?
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.delete, MethodsXml.dateOne,
			MethodsXml.objField })
	public @ResponseBody
	Map<String, Object> delete(@RequestParam String field,
			@RequestParam String value) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().delete(field, value);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 根据集合信息删除数据
	 * 
	 * @param map
	 *            集合信息能确定的数据，将全部删除
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=del&amp;date=rows
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.delete, MethodsXml.dateRows })
	public @ResponseBody
	Map<String, Object> delete(@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().delete(map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	// 修改
	/**
	 * 通过主键修改对应信息
	 * 
	 * @param pkValue
	 *            主键的值
	 * @param map
	 *            需要修改的集合信息
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=update&amp;date=rows&amp;obj=pk
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>pkValue:</strong>pkValue=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.update, MethodsXml.dateRows,
			MethodsXml.objPK })
	public @ResponseBody
	Map<String, Object> update(@RequestParam String pkValue,
			@RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().update(pkValue, map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过主键急值确定记录修改
	 * 
	 * @param field
	 *            主键字段
	 * @param value
	 *            主键值
	 * @param map
	 *            修改的值
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=update&amp;date=one&amp;obj=map
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>filed:</strong>filed=?
	 *  <strong>value:</strong>value=?
	 *  <strong>map:</strong>{[json格式]}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.update, MethodsXml.dateOne,
			MethodsXml.objMap })
	public @ResponseBody
	Map<String, Object> update(@RequestParam String field,
			@RequestParam String value, @RequestBody Map<String, Object> map) {
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().update(field, value, map);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 通过集合条件确定记录修改成对应的集合信息
	 * 
	 * @param wMap
	 *            集合条件
	 * @param map
	 *            需要修改成的集合信息
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=update&amp;date=rows
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>Map:</strong>
	 *  {
	 *  	"map":[json格式]//将值改成需要的数据
	 *  	"wMap":[json格式],//根据条件查询需要修改的数据
	 *  }
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = { MethodsXml.update, MethodsXml.dateRows })
	public @ResponseBody
	Map<String, Object> update(@RequestBody Map<String, Object> whereMap) {
		Map<String, Object> map = (Map<String, Object>) whereMap.get("map");
		Map<String, Object> wMap = (Map<String, Object>) whereMap.get("wMap");
		Map<String, Object> mapRe = Message.SUCCESS.getObjMess();
		try {
			int num = getTabelServer().update(map, wMap);
			if (num == 0) {
				mapRe = Message.UNTREATED.getObjMess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			mapRe = Message.UN_KNOW.getObjMess(e);
		}
		return mapRe;
	}

	/**
	 * 修改数据 
	 * [注1]如果没有主键或给的主键值为空字符串，那么则新增数据 
	 * [注2]如果有主键值，那么则修改
	 * 
	 * @return <pre>
	 * 	<strong>请求URL：</strong>...?m=update&amp;date=one
	 * 	<strong>ticket:</strong>ticket=?
	 *  <strong>Map:</strong> { Json 格式}
	 * </pre>
	 */
	@RequestMapping(params = { MethodsXml.update, MethodsXml.dateOne })
	public @ResponseBody
	Map<String, Object> updateOrAdd(@RequestBody Map<String, Object> map) {
		String pk = getTabelServer().getPrimaryKey();
		if (map.containsKey(pk)) {
			Object pk_value = map.get(pk);
			if (!ItemHelper.isEmpty(pk_value)) {
				map.remove(pk);
				return this.update(pk_value.toString(), map);// [注2]
			}
		}
		return this.addRow(map); // [注1]
	}

}
