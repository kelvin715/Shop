package com.neuedu.ws.service;

import com.github.pagehelper.PageInfo;
import com.neuedu.ws.common.ServerResponse;
import com.neuedu.ws.vo.OrderVo;

import java.util.Map;


public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);
    ServerResponse aliCallback(Map<String,String> params);
//    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
//    ServerResponse createOrder(Integer userId,Integer shippingId);
//    ServerResponse getOrderCartProduct(Integer userId);
//    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
//    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);
//
//
//
//    //backend
//    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);
	ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);


}
