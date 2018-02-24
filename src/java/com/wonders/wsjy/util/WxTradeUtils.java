package com.wonders.wsjy.util;

import java.math.BigDecimal;

import com.wonders.esframework.util.NumberUtil;
import com.wonders.wsjy.bo.TdscTradeView;

public class WxTradeUtils {

	/**
	 * 亩均价
	 * 
	 * @param decimal 当前报价
	 * @param blockInfo	当前地块面积
	 * @return
	 */
	public static  String getBuildingPrice(BigDecimal decimal, TdscTradeView tdscTradeView) {
		String blockType = tdscTradeView.getBlockQuality();
		if ("101".equals(blockType)) {
			return NumberUtil.numberDisplay(decimal.multiply(new BigDecimal(666.67)).divide(tdscTradeView.getTotalLandArea(), BigDecimal.ROUND_HALF_UP,6),2);
		} else if ("102".equals(blockType)) {
			return NumberUtil.numberDisplay(decimal.multiply(new BigDecimal(10000)).divide(tdscTradeView.getTotalBuildingArea(), BigDecimal.ROUND_HALF_UP,6),2);
		}
		return null;
	}
	
	public static String getOverPrice(BigDecimal nowPrice, BigDecimal initPrice) {
		BigDecimal value = nowPrice.subtract(initPrice).multiply(new BigDecimal(100)).divide(initPrice, BigDecimal.ROUND_HALF_UP,6);
		return NumberUtil.numberDisplay(value,2) + "%";
	}
}
