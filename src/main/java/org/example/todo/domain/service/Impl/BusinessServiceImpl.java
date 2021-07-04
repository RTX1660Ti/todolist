package org.example.todo.domain.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.example.common.enums.ResponseCode;
import org.example.todo.bean.dto.*;
import org.example.todo.domain.dao.BusinessMapper;
import org.example.todo.domain.entity.Business;
import org.example.todo.domain.service.BusinessService;
import org.example.todo.util.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private BusinessMapper businessMapper;


    @Resource
    private UserUtil userUtil;


    @Override
    public ResponseDto queryBusinessDetail(Long businessId) {
        ResponseDto responseDto = new ResponseDto(ResponseCode.SUCCESS);
        Business business = businessMapper.selectById(businessId);
        //鉴权,admin可查看所有,普通只能看当前用户
        if (null != business && userUtil.authentication(business.getUserId())){
            Map<String, Object> data = new HashMap<>();
            data.put("data",business);
            responseDto.setData(data);
        }
        return responseDto;
    }

    @Override
    public ResponseDto queryBusinessList(BusinessQueryDto businessQueryDto) {
        //鉴权
        if (!userUtil.authentication(businessQueryDto.getUserId()))
            return new ResponseDto(ResponseCode.PERMISSION_NO_ACCESS);
        //查库
        PageHelper.startPage(businessQueryDto.getPage(), businessQueryDto.getPageSize());
        List<Business> businessList = businessMapper.selectList(businessQueryDto);
        PageInfo PageInfo = new PageInfo<>(businessList);
        Map<String, Object> data = new HashMap<>();
        data.put("total",PageInfo.getTotal());
        data.put("list",PageInfo);
        return new ResponseDto(ResponseCode.SUCCESS).setData(data);
    }

    @Override
    public ResponseDto addBusiness(BusinessAddDto businessAddDto) {
        Business business = new Business();
        boolean flag = false;
        //鉴权,并确定userID
        if (null != businessAddDto.getUserId()){
            if (userUtil.authentication(businessAddDto.getUserId())){
                business.setUserId(businessAddDto.getUserId());
                flag = true;
            }
        }else{
            business.setUserId(userUtil.getUserId());
            flag = true;
        }
        //插入数据库
        if (flag){
            Date date = new Date();
            business.setIsDelete(false);
            business.setIsEnd(false);
            business.setBusinessTime(businessAddDto.getBusinessTime());
            business.setDescription(businessAddDto.getDescription());
            business.setCreateTime(date.getTime());
            business.setUpdateTime(date.getTime());
            businessMapper.insert(business);
            return new ResponseDto(ResponseCode.SUCCESS);
        }
        return new ResponseDto(ResponseCode.PERMISSION_NO_ACCESS);
    }

    @Override
    public ResponseDto delBusiness(Long businessId) {
        Business business = businessMapper.selectById(businessId);
        if (null == business )
            return new ResponseDto(ResponseCode.RESULT_DATA_NONE);
        if (userUtil.authentication(business.getUserId())){
            businessMapper.deleteById(businessId);
            return new ResponseDto(ResponseCode.SUCCESS);
        }
        return new ResponseDto(ResponseCode.PERMISSION_NO_ACCESS);
    }

    @Override
    public ResponseDto updateBusiness(BusinessUpDto businessUpDto) {
        if (!userUtil.authentication(businessUpDto.getUserId()))
            return new ResponseDto(ResponseCode.PERMISSION_NO_ACCESS);
        Business business = new Business();
        {
            business.setId(businessUpDto.getId());
            business.setUpdateTime(new Date().getTime());
            business.setUserId(business.getUserId());
            if (null != businessUpDto.getIsDelete())
                business.setIsDelete(businessUpDto.getIsDelete());
            if (null != businessUpDto.getIsEnd())
                business.setIsEnd(businessUpDto.getIsEnd());
            if (null != businessUpDto.getBusinessTime())
                business.setBusinessTime(businessUpDto.getBusinessTime());
            if (StringUtils.isNotBlank(businessUpDto.getDescription()))
                business.setDescription(businessUpDto.getDescription());
        }
        businessMapper.updateById(business);
        return new ResponseDto(ResponseCode.SUCCESS);
    }

}
