package com.ekc.sm;
import com.ekc.enumall.SM;

import cn.emay.sdk.client.api.Client;

public class SingletonClient {
	private static Client client=null;
	private static String softwareSerialNo = "6SDK-EMY-6688-KDQOS";	//默认通知类
	private static String key = "449721";
	
	private SingletonClient(){
	}
	public synchronized static void setClientType(String type) throws Exception {
		if (SM.YINGXIAO.getMess_en().equals(type)) {	//营销类
			softwareSerialNo = "6SDK-EMY-6688-KEWTS";
			key = "302047";
		} else {//默认通知类
			softwareSerialNo = "6SDK-EMY-6688-KDQOS";
			key = "449721";
		}
	}
	public synchronized static Client getClient(String softwareSerialNo,String key){
		if(client==null){
			try {
				client=new Client(softwareSerialNo,key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
	/**
	 * 通知类短信
	 * @return
	 * @throws Exception
	 */
	public synchronized static Client getClient() throws Exception {
		//ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		//if(client==null){
		client = null;
			//client=new Client(bundle.getString("softwareSerialNo"),bundle.getString("key"));
			//client=new Client("6SDK-EMY-6688-KDQOS","449721");	//用于发通知验证类短信，不再用。
			client=new Client(SingletonClient.softwareSerialNo,SingletonClient.key);	//新账户，用于发通知类短信。
		//}
		return client;
	}
}
