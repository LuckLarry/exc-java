package com.ekc.service.tb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekc.ifc.TableUseIfc;
import com.ekc.service.BaseService;

@Service("GoodsObjService")
public class GoodsObjService extends BaseService {
	@Autowired
	TableUseIfc eGoodsAttributeValues_Ser;// 商品属性值
	@Autowired
	AttributesBase AttributesBase;// 属性
	@Autowired
	TableUseIfc eAttributeValues_Ser;// 属性值
	@Autowired
	TableUseIfc ePicturePlaceObject_Ser;// 图片位置


}
