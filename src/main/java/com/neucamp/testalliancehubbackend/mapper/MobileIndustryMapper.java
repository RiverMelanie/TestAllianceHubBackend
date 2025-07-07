package com.neucamp.testalliancehubbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MobileIndustryMapper {
    @Select(
            "SELECT i.dynamicId AS dynamicId, i.title, COUNT(v.visit_id) AS clickCount " +
                    "FROM mobile_dynamic_visit v " +
                    "INNER JOIN industry_dynamic i ON v.dynamic_id = i.dynamicId " +
                    "WHERE v.visit_time BETWEEN #{startTime} AND #{endTime} " +
                    "GROUP BY i.dynamicId, i.title " +
                    "ORDER BY clickCount DESC " +
                    "LIMIT 10"
    )
    List<Map<String, Object>> selectTopDynamicsByTimeRange(String startTime, String endTime);


    // 调试用：统计总访问量
    @Select("SELECT COUNT(*) AS totalVisits FROM mobile_dynamic_visit")
    long getTotalVisitCount();
}
