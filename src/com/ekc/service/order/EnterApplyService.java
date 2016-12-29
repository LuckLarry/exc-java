package com.ekc.service.order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.util.ItemHelper;
import com.ekc.util.JoinTable;
import com.ekc.util.WhereTable;
import com.ekc.xml.TName;

/**
 * 入驻申请表
 * 
 * @author hui
 */
@Service("EnterApplySer")
public class EnterApplyService extends TableUseAbs {
	@Override
	public String getTable() {
		return TName.eEnterApply;
	}
    public String getFields(){
    	return "enter_apply_id,company_name,contact_name,province,city,district,address,telephone,home_phone,qq,mail,company_license,company_thumb,add_time,state,user_id,review_user,old_id,user_name,";
    }
	@Override
	public String getPrimaryKey() {
		return "enter_apply_id";
	}
	@Override
	public Map<String, Object> findPage(Map<String, Object> map) throws Exception{
		JoinTable join=new JoinTable(getTable(),"a");
		join.leftJoin(TName.eUsers,"b",  "a.user_id=b.user_id");
		join.leftJoin(TName.eRegion, "s", "s.region_id=a.province");
		join.leftJoin(TName.eRegion, "si", "si.region_id=a.city");
		join.leftJoin(TName.eRegion, "qu", "qu.region_id=a.district");
		WhereTable where=new WhereTable();
		String aFiled=getFields();//a表包含的字段
		String pageFile=getPageField();//分页字段
		String bfiled="user_name,";//b表字段
		Map<String,String> gDui=new HashMap<String, String>();
		gDui.put("sName", "s.region_name");
		gDui.put("siName", "si.region_name");
		gDui.put("quName", "qu.region_name");
		String gDuiCol="";
		for(String key:gDui.keySet()){
			gDuiCol+=","+gDui.get(key)+" as "+key;
		}
		String dd="";
		Object v="";
		for(String key:map.keySet()){
			v=map.get(key);
			if(key.indexOf(" ")!=-1){
				dd=key.substring(key.indexOf(" "));
				key=key.substring(0,key.indexOf(" "));
			}else{
				dd="";
			}
			if(aFiled.indexOf(key+",")!=-1){
				where.put("a."+key+dd, v);
			}else if(pageFile.indexOf(key+",")!=-1){
				where.put(key, v);
			}else if(gDui.containsKey(key)){
				where.put(gDui.get(key)+dd, v);
			}else if(bfiled.indexOf(key+",")!=-1){
				where.put("b."+key+dd, v);
			}
		}
		//解决排序问题 Gaohui 2016-01-28 mx
		if (map.containsKey("add")){
			where.put("add", map.get("add").toString());
		}
		return selectPage(join.toString(), "a.*,b.user_name"+gDuiCol, where.getMap());
	}
	
	@Override
	public int update(Map<String, Object> map, Map<String, Object> wMap) throws Exception{
		int update_code = update(getTable(),map, wMap);
		if (update_code<=0){
			return update_code;
		}
		try {
			if(map.containsKey("state") && map.containsKey("user_id") && wMap.containsKey("enter_apply_id")){
				Object userid = map.get("user_id");
				String user_id = "";
				if(!ItemHelper.isEmpty(userid)){
					user_id = userid.toString();
				}
				String enter_apply_id = wMap.get("enter_apply_id").toString();
				if(user_id != null && !"".equals(user_id) && !"".equals(enter_apply_id)){
					Map<String, Object> map_ea = findRow(enter_apply_id);
					if (map_ea != null){
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String date = dateFormat.format(new Date());
						
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						Map<String, Object> erpMap = new HashMap<String, Object>();
						Object ClienCode = map_ea.get("user_id");
						erpMap.put("ClienCode", ClienCode);
						erpMap.put("ClienType", 1);
						erpMap.put("ClienName", map_ea.get("company_name"));
						erpMap.put("Clienadd", map_ea.get("province"));
						erpMap.put("Clienadd2", map_ea.get("city"));
						erpMap.put("Address", map_ea.get("address"));
						erpMap.put("lianxfs", map_ea.get("home_phone"));
						erpMap.put("email",map_ea.get("mail"));
						erpMap.put("QQ", map_ea.get("qq"));
						erpMap.put("Lianxiren", map_ea.get("contact_name"));
						erpMap.put("LianxiTel", map_ea.get("telephone"));
						erpMap.put("HDmemo", "");
						erpMap.put("Regtime", date);
						erpMap.put("companyname", map_ea.get("company_name"));
						erpMap.put("clttype", "正式");
						erpMap.put("Tel", map_ea.get("telephone"));
						erpMap.put("sheng", getRegion(map_ea.get("province").toString()));
						erpMap.put("city", getRegion(map_ea.get("city").toString()));
						erpMap.put("classify", getRegion(map_ea.get("district").toString()));
						
						list.add(erpMap);
						
						StringBuffer stb = ItemHelper.insert_erp(list, "t701654", "ClienCode");
						log.info(stb.toString());
						
						//新“审核通过” 放开以下注释内容即可
//						//插入erp表sCltGeneral
//						String cltCode = getCode("sCltGeneral"); //单号
//						insertERPsCltGeneral(enter_apply_id, cltCode, ClienCode.toString());
//						//插入erp表t701715h
//						String DocCode = getCode("DocCode"); //单号
//						insertERPt701715h(enter_apply_id, DocCode, ClienCode.toString());
						
						return 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String getRegion(String region_id){
		String sql = "select region_name from ".concat(TName.eRegion).concat(" where region_id = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, region_id);
		if (list.size()>0){
			return list.get(0).get("region_name").toString();
		}
		return "不存在";
	}
	/**
	 * enterapply 审核    "通过"     执行
	 * @param enter_apply_id
	 */
	private void insertERPt701715h(String enter_apply_id, String DocCode, String ClienCode){
		try {
			Map<String, Object> enterApply = new HashMap<String, Object>();
			if(!ItemHelper.isEmpty(enter_apply_id) && !ItemHelper.isEmpty(DocCode) && !ItemHelper.isEmpty(ClienCode)){
				enterApply = findRow(enter_apply_id);
			} else {
				log.info("t701715h传参数值不能为空");
			}
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = dateFormat.format(new Date());
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("FormID", "701715");
			map.put("DocCode", DocCode);
			map.put("doctype", "起草");
			map.put("DocDate", date);
			map.put("periodid", date.indexOf(0, 7));
			map.put("DocStatus", 100);
			map.put("DocStatusName", "结束");
			map.put("companyid", "WX20");
			map.put("companyname", "company_name");
			map.put("EnterName", enterApply.get("EnterName"));
			map.put("EnterDate", date);
			map.put("CreateUsercode", "");
			map.put("CreateUsername", "");
			map.put("areaname", enterApply.get("areaname"));
			map.put("SDOrgID", enterApply.get("SDOrgID"));
			map.put("SDGroupName", enterApply.get("SDGroupName"));
			map.put("GDName", enterApply.get("GDName"));
			map.put("ClienName", enterApply.get("contact_name"));
			map.put("ClienCode", enterApply.get("user_id"));
			map.put("Telphone", enterApply.get("home_phone"));
			map.put("ShopName", enterApply.get("contact_name"));
			map.put("companyaddres", enterApply.get("address"));
			map.put("cltTpye", "企业会员");
			map.put("HDtext", "测试中");
			map.put("Regtime", date);
			map.put("Tel", enterApply.get("telephone"));
			map.put("cltcodetype", enterApply.get("cltcodetype"));
			map.put("sheng", getRegion(enterApply.get("province").toString()));
			map.put("city", getRegion(enterApply.get("city").toString()));
			map.put("classify", getRegion(enterApply.get("district").toString()));
			map.put("SHtype", enterApply.get("SHtype"));
			map.put("dispatchName", enterApply.get("dispatchName"));
			map.put("refcode", ClienCode);
			
			list.add(map);
			
			StringBuffer stb = ItemHelper.insert_erp(list, "t701715h", "DocCode");
			log.info(stb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * enterapply 审核    "通过"     执行
	 * @param enter_apply_id
	 */
	private void insertERPsCltGeneral(String enter_apply_id, String cltCode, String ClienCode){
		try {
			Map<String, Object> enterApply = new HashMap<String, Object>();
			if(!ItemHelper.isEmpty(enter_apply_id) && !ItemHelper.isEmpty(cltCode) && !ItemHelper.isEmpty(ClienCode)){
				enterApply = findRow(enter_apply_id);
			} else {
				log.info("sCltGeneral传参数值不能为空");
			}
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = dateFormat.format(new Date());
			Map<String, Object> sCltGeneral = new HashMap<String, Object>();
			sCltGeneral.put("cltCode", cltCode);
			sCltGeneral.put("cltname", enterApply.get("contact_name"));
			sCltGeneral.put("clttype", "e选材客户");
			sCltGeneral.put("areaid", "EWX");
			sCltGeneral.put("stoped", 0);
			sCltGeneral.put("passwd", "123456");
			sCltGeneral.put("inDomain", "fslola.cn");
			sCltGeneral.put("insertTime", date);
			sCltGeneral.put("companyidS", "WX20");
			sCltGeneral.put("ParentCode", enterApply.get(""));//DocCode
			sCltGeneral.put("companynames", "");//
			sCltGeneral.put("areaname", enterApply.get("areaname"));
			sCltGeneral.put("areaname2", "");
			sCltGeneral.put("areaname3", enterApply.get("SDOrgID"));
			sCltGeneral.put("SDGroupName", enterApply.get("SDGroupName"));
			sCltGeneral.put("clttype5", "企业会员");
			sCltGeneral.put("GDGroupID", enterApply.get("GDGroup"));
			sCltGeneral.put("GDGroup1", enterApply.get("GDName"));
			sCltGeneral.put("WebUser", enterApply.get("telephone"));
			sCltGeneral.put("GDAaccount", enterApply.get("ShopName"));
			sCltGeneral.put("WebCltcode", ClienCode); 
			sCltGeneral.put("clttype2", enterApply.get("cltcodetype"));
			sCltGeneral.put("NewWebCltcode", enterApply.get("user_id"));
			
			//ClienCode,ClienName,'e选材客户','EWX',0,'123456','fslola.cn',GETDATE(),'WX20',DocCode,WXPE,areaname,WXPE,SDOrgID,
			//SDGroupName,cltTpye,GDGroup,GDName,Telphone,ShopName,refcode,cltcodetype
			list.add(sCltGeneral);
			
			StringBuffer stb = ItemHelper.insert_erp(list, "sCltGeneral", "cltCode");
			log.info(stb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到单号
	 * @param tableName
	 */
	public String getCode(String tableName){
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("tableName", tableName);
		Map<String, Object> map = selectRow(TName.eAutoCode, "*", whereMap);
		if(map != null){
			int code = Integer.parseInt(map.get("code").toString());
			code++;
			String codeFlag = map.get("codeFlag").toString();
			int codeLength = Integer.parseInt(map.get("codeLength").toString());
			int zero = codeLength-codeFlag.length()-(code+"").length();
			String strzero = "";
			for (int i = 0; i < zero; i++) {
				strzero=strzero.concat("0");
			}
			String doccode = codeFlag.concat(strzero).concat(code+"");
			
			Map<String, Object> mapSet = new HashMap<String, Object>();
			mapSet.put("code", code);
			
			update(TName.eAutoCode, mapSet, whereMap);
			return doccode;
		}
		return "";
	}
	
	
}
