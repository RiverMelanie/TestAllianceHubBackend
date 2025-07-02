package com.neucamp.testalliancehubbackend.controller;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import com.neucamp.testalliancehubbackend.mapper.IndustryDynamicMapper;
import com.neucamp.testalliancehubbackend.mapper.ManagerIndustryDynamicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@CrossOrigin
public class ManagerIndustryDynamicController {

    @Autowired
    ManagerIndustryDynamicMapper managerIndustryDynamicMapper;

    @Autowired
    IndustryDynamicMapper industryDynamicMapper;

    @RequestMapping("/reviewsearch")
    public Map<String, Object> searchDynamics(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "8") int pageSize
    ) {

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        // 查询当前页数据（直接返回所有数据，不移除 auditStatus=0 的记录）
        List<dynamicreviewrecordtable> dynamics = managerIndustryDynamicMapper.searchDynamics(params);

        // 查询总记录数（与查询数据的条件保持一致）
        int totalCount = managerIndustryDynamicMapper.searchTotalCount(params);

        // 封装结果（totalCount 与返回的数据完全匹配）
        Map<String, Object> result = new HashMap<>();
        result.put("data", dynamics);
        result.put("totalCount", totalCount);
        return result;
    }
    @RequestMapping("/upDynamic")
    public int upDynamic(@RequestBody IndustryDynamic industryDynamic){
        return managerIndustryDynamicMapper.upDynamic(industryDynamic);
    }
    @RequestMapping("/upReviewDynamic")
    public int upReviewDynamic(@RequestBody dynamicreviewrecordtable dynamicreviewrecordtable){
        int count=managerIndustryDynamicMapper.upReviewDynamic(dynamicreviewrecordtable);
        if(count>0){
            IndustryDynamic industryDynamic=industryDynamicMapper.searchreviewdDynamics(dynamicreviewrecordtable);
            industryDynamic.setAuditStatus(1);
            managerIndustryDynamicMapper.upDynamicPlus(industryDynamic);
        }
        return count;
    }
    @RequestMapping("/clean")
    public void clean(){
        managerIndustryDynamicMapper.cleanDynamics();
    }
    @RequestMapping("/delDynamic")
    public int delDynamic(int id){
        return managerIndustryDynamicMapper.delDynamic(id);
    }
}
