package org.example.todo.domain.service;

import org.example.todo.bean.dto.BusinessAddDto;
import org.example.todo.bean.dto.BusinessQueryDto;
import org.example.todo.bean.dto.BusinessUpDto;
import org.example.todo.bean.dto.ResponseDto;

public interface BusinessService {

    ResponseDto queryBusinessDetail(Long businessId);

    ResponseDto queryBusinessList(BusinessQueryDto businessQueryDto);

    ResponseDto addBusiness(BusinessAddDto businessAddDto);

    ResponseDto delBusiness(Long businessId);

    ResponseDto updateBusiness(BusinessUpDto businessUpDto);
}
