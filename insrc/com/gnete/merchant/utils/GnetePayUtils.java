package com.gnete.merchant.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * 支付网关需要的工具方法
 * @author wszf03
 * @date 2015-02-03  
 */
public class GnetePayUtils {
	
	
	/**
	 * 获取订单号,返回为当前日期
	 * @return
	 */
	public static String GetOrderNo()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String OrderNo = format.format(new Date());
		return OrderNo;
	}
	
	
	/**
	 * 获取当前日期
	 * @param format
	 * @return
	 */
	public static String GetCurrentDate(String formate)
	{
		SimpleDateFormat DateFormat = new SimpleDateFormat(formate);
		String DateStr = DateFormat.format(new Date());
		return DateStr;
	}
	
	
	/**
	 * 从待取值的字符串中取出符合域名的域值
	 * @param DecryptedText
	 * @param StrKey
	 * @return
	 */
	public static String GetValue(String DecryptedText, String StrKey)
	{
		String TextValue = "";
		if(StringUtils.isEmpty(DecryptedText) || StringUtils.isEmpty(StrKey))
		{
			return TextValue;
		}
		String[] StrArr = DecryptedText.split("&");
		for (String Str : StrArr)
		{
			String[] arr = Str.split("=");
			if (arr != null && arr.length > 1)
			{
				if (StrKey.equals(arr[0]) && arr.length > 1)
				{
					TextValue = (arr[1] == null ? "" : arr[1]);
				}
			}
		}
		return TextValue;
	}
	
	
	
	/**
	 * 把对账响应结果数据进行处理
	 * @param ResponseStr 对账响应结果
	 * @param Split 分割符号(&,/n,|)等。 注意:对于空格符，在被调用之前用&或*转换，不然直接传入\n会被替换为空格
	 * @param FiledCount 每行显示有多少个字段数
	 * 例如:订单的格式：订单日期&支付金额&商户订单号&商户终端号&系统参考号&响应码&清算日期&保留域1&保留域2& 则 Split = & , FiledCount = 9
	 * @return
	 */
	public static List<Map<String,String>> SplitStringToList(String ResponseStr,String Split,int FiledCount)
	{
		List<Map<String,String>> SplitValueList = new ArrayList<Map<String,String>>();
		if(StringUtils.isEmpty(ResponseStr) || StringUtils.isEmpty(Split))
		{
			return SplitValueList;
		}
		//去掉最后一个分割符
		ResponseStr = ResponseStr.substring(0, ResponseStr.lastIndexOf(Split));
		String[] ResponseArrays = ResponseStr.split(Split);
		if(ResponseArrays != null && ResponseArrays.length >0)
		{
			int Count = 0;
			Map<String,String> ValueMap = null;
			for(String Value : ResponseArrays)
			{
				if(Count % FiledCount == 0)
				{
					if(Count != 0)
					{
						SplitValueList.add(ValueMap);
					}
					Count = 0;
					ValueMap = new HashMap<String,String>();
				}
				ValueMap.put("Key"+Count, Value);
				Count++;
			}
			//新增最后一次ValueMap
			SplitValueList.add(ValueMap);
		}
		return SplitValueList;
	}
	
	
	
	public static void main(String[] args)
	{
		String ResponseStr = "20150206\n1.01\n20150206095405\n99800568\n074822583840\n00\n0206\n保留域1\n保留域2\n 20150206\n10.01\n20150206095941\n99800568\n048410062702\n00\n0206\n保留域1\n保留域2\n";
		String Split = "\n";
		int FiledCount = 9;
		
		List<Map<String,String>> SplitValueList =  GnetePayUtils.SplitStringToList(ResponseStr, Split, FiledCount);
		for(Map<String,String> MapValue  : SplitValueList)
		{
			System.out.println("map0:"+MapValue.get("Key0"));
			System.out.println("map1:"+MapValue.get("Key1")+"map2:"+MapValue.get("Key2"));
			System.out.println("map3:"+MapValue.get("Key3")+"map4:"+MapValue.get("Key4"));
			System.out.println("map5:"+MapValue.get("Key5")+"map6:"+MapValue.get("Key6"));
			System.out.println("map7:"+MapValue.get("Key7")+"map8:"+MapValue.get("Key8"));
			System.out.println("-------------------------");
		}
	}

}
