package com.ekc.service.user;

import org.springframework.stereotype.Service;

import com.ekc.service.TableUseAbs;
import com.ekc.xml.TName;

/**
 *用户日志表
 * 
 * @author ZhengXiajun 
 */

@Service("userLogService")
public class UserLog extends TableUseAbs{
	@Override
	public String getTable() {
		return TName.eUserLog;
	}
	@Override
	public String getPrimaryKey() {
		return "log_id";
	}
	
	
	public int addRow( String log_id, String log_time, String user_id, String user_name, String log_info, String ip_address) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" insert "+TName.eUserLog);
		sql.append(" (log_id, log_time, user_id, user_name, log_info, ip_address) ");
		sql.append("values (?,?,?,?,?,?)");
		 int i  = jdbcTemplate.update(sql.toString(),log_id, log_time, user_id, user_name, log_info, ip_address);		
		return i;
	}
	
	
	public int update( String log_info, String log_id) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" update "+TName.eUserLog);
		sql.append(" set log_info = ? ");
		sql.append(" where log_id = ? ");
		int i = jdbcTemplate.update(sql.toString(),log_info, log_id);	
		return i;
	}
}
