package org.example.todo.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.common.CommonReturnData;
import org.example.common.exception.BusinessException;
import org.example.todo.bean.dto.BusinessAddDto;
import org.example.todo.bean.dto.BusinessQueryDto;
import org.example.todo.bean.dto.BusinessUpDto;
import org.example.todo.bean.dto.ResponseDto;
import org.example.todo.bean.form.BusinessAddForm;
import org.example.todo.bean.form.BusinessQueryForm;
import org.example.todo.bean.form.BusinessUpForm;
import org.example.todo.domain.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/business")
@Slf4j
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     * 查询事务详情
     * admin可查看所有,普通只能看当前用户
     * @param businessId 事务ID
     */
    @GetMapping(value = "/detail",produces =  "application/json;charset=UTF-8")
    public CommonReturnData queryBusinessDetail(@Valid @RequestParam @NotNull Long businessId) throws BusinessException {
        ResponseDto responseDto = businessService.queryBusinessDetail(businessId);
        return CommonReturnData.resultRet(responseDto);
    }

    /**
     *  查询事务列表
     *  (默认一页20条数据,最大100条)
     *  (默认查未被删除的)
     *  1 支持分页显示
     *  2 支持根据是否删除筛选
     *  3 支持是否完成筛选
     *  4 支持时间范围筛选
     *  admin可查看所有用户(可指定)的事务,普通只能看当前用户
     */
    @PostMapping(value = "/List",produces =  "application/json;charset=UTF-8")
    public CommonReturnData queryBusinessList(@RequestBody BusinessQueryForm queryForm) throws BusinessException {
        CommonReturnData commonReturnData;
        ResponseDto responseDto = businessService.queryBusinessList(getQueryDto(queryForm));
        {
            commonReturnData = CommonReturnData.resultRet(responseDto);
            commonReturnData.setData(responseDto.getData());
        }
        return  commonReturnData;
    }

    /**
     *  添加事务
     *  admin可给所所有用户添加事务,普通只能给自己
     */
    @PostMapping(value = "/add",produces =  "application/json;charset=UTF-8")
    public CommonReturnData addBusiness(@Valid @RequestBody BusinessAddForm addForm) throws BusinessException {
        ResponseDto responseDto = businessService.addBusiness(getAddDto(addForm));
        return CommonReturnData.resultRet(responseDto);
    }


    /**
     *  删除事务
     *  admin随便删事务,普通只能给自己删
     */
    @GetMapping(value = "/del",produces =  "application/json;charset=UTF-8")
    public CommonReturnData delBusiness(@Valid @RequestParam @NotNull Long businessId) throws BusinessException {
        ResponseDto responseDto = businessService.delBusiness(businessId);
        return CommonReturnData.resultRet(responseDto);
    }


    /**
     *  更新
     *  admin可给所有人改事务,也可更改事务的经办人,普通只能给自己改
     */
    @PostMapping(value = "/update",produces =  "application/json;charset=UTF-8")
    public CommonReturnData updateBusiness(@Valid @RequestBody BusinessUpForm upForm) throws BusinessException {
        ResponseDto responseDto = businessService.updateBusiness(getUpDto(upForm));
        return CommonReturnData.resultRet(responseDto);
    }



    //====================================私有方法===========================================

    /**
     * 查询事务列表时.转换bean的私有方法
     */
    private BusinessQueryDto getQueryDto(BusinessQueryForm queryForm){
        BusinessQueryDto queryDto = new BusinessQueryDto();
        {
            if (null != queryForm.getIsDelete()){
                queryDto.setIsDelete(queryForm.getIsDelete());
            }else {
                queryDto.setIsDelete(false);
            }
            if (null != queryForm.getIsEnd())
                queryDto.setIsEnd(queryForm.getIsEnd());
            if (null != queryForm.getBusinessStartTime())
                queryDto.setBusinessStartTime(queryForm.getBusinessStartTime());
            if (null != queryForm.getBusinessEndTime())
                queryDto.setBusinessEndTime(queryForm.getBusinessEndTime());
            if (null != queryForm.getUserId())
                queryDto.setUserId(queryForm.getUserId());
            queryDto.setPage(queryForm.getPage());
            queryDto.setPageSize(queryForm.getPageSize());
        }
        return queryDto;
    }


    /**
     * 查询事务列表时.转换bean的私有方法
     */
    private BusinessAddDto getAddDto(BusinessAddForm addForm){
        BusinessAddDto addDto = new BusinessAddDto();
        {
            addDto.setUserId(addForm.getUserId());
            addDto.setDescription(addForm.getDescription());
            addDto.setBusinessTime(addForm.getBusinessTime());
        }
        return addDto;
    }

    /**
     * 更新事务时.转换bean的私有方法
     */
    private BusinessUpDto getUpDto(BusinessUpForm upForm) {
        BusinessUpDto upDto = new BusinessUpDto();
        {
            if (null != upForm.getId())
                upDto.setId(upForm.getId());
            if (null != upForm.getUserId())
                upDto.setUserId(upForm.getUserId());
            if (null != upForm.getDescription())
                upDto.setDescription(upForm.getDescription());
            if (null != upForm.getBusinessTime())
                upDto.setBusinessTime(upForm.getBusinessTime());
            if (null != upForm.getIsDelete())
                upDto.setIsDelete(upForm.getIsDelete());
            if (null != upForm.getIsEnd())
                upDto.setIsEnd(upForm.getIsEnd());
        }
        return upDto;
    }

}
