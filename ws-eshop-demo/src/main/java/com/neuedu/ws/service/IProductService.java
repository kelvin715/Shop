package com.neuedu.ws.service;

import com.github.pagehelper.PageInfo;
import com.neuedu.ws.common.ServerResponse;
import com.neuedu.ws.pojo.Product;
import com.neuedu.ws.vo.ProductDetailVo;


public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);



	ServerResponse<ProductDetailVo> getProductDetail(Integer productId);



}
