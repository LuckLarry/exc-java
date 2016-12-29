package com.ekc.service.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import pb.pub.MapCom;
import pb.pwd.EcshopPwd;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

@Service("eUsers_ser")
public class EUsers extends TableUseAbs {

	@Override
	public String getTable() {
		return TName.eUsers;
	}

	@Override
	public String getPrimaryKey() {
		return "user_id";
	}
	
	@Override
	public int update(Map<String, Object> map, Map<String, Object> wMap) throws Exception{
		Map<String, Object> userMap=findRow(wMap);
		if(userMap!=null){//符合ecshop原有的加密方式
			EcshopPwd ePwd = new EcshopPwd();
			Map<String, String> map1 = new HashMap<String, String>();
			MapCom comMap = new MapCom();
			comMap.put(map1, userMap, ePwd.ec_salt_key);
			comMap.put(map1, userMap, ePwd.salt_key);
			String password=null;
			if(map.containsKey(ePwd.password_key)){
				password=map.get(ePwd.password_key).toString();
			}
			if(map.containsKey(ePwd.md5password_key)){
			   comMap.put(map1, map,ePwd.md5password_key);//已经加密
			   map.remove(ePwd.md5password_key);
			}			
			password = ePwd.compile_password(map1);// 模拟ecshop加密
			if(password!=null){
			   map.put(ePwd.password_key, password);
			}
		}
		return update(getTable(),map, wMap);
	}
}
