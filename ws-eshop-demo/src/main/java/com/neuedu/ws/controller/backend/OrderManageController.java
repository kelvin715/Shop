package com.neuedu.ws.controller.backend;

import com.github.pagehelper.PageInfo;
import com.neuedu.ws.common.Const;
import com.neuedu.ws.common.ResponseCode;
import com.neuedu.ws.common.ServerResponse;
import com.neuedu.ws.pojo.User;
import com.neuedu.ws.service.IOrderService;
import com.neuedu.ws.service.IUserService;
import com.neuedu.ws.vo.OrderVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

  



   








}
