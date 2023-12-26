package com.neuedu.ws.dao;

import org.apache.ibatis.annotations.Mapper;

import com.neuedu.ws.pojo.PayInfo;

@Mapper
public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}