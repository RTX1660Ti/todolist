package org.example.todo.domain.dao;

import org.example.todo.bean.dto.BusinessQueryDto;
import org.example.todo.domain.entity.Business;

import java.util.List;

public interface BusinessMapper {


    Business selectById(Long id);

    int insert(Business business);

    int updateById(Business business);

    int deleteById(Long id);

    List<Business> selectList(BusinessQueryDto businessDTO);

}
