package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.Visit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MobileIndustryMapper {
    @Select(
            "SELECT i.dynamic_id AS dynamicId, i.title, COUNT(v.visit_id) AS clickCount " +
                    "FROM mobile_dynamic_visit v " +
                    "INNER JOIN industry_dynamic i ON v.dynamic_id = i.dynamic_id " +
                    "WHERE v.visit_time BETWEEN #{startTime} AND #{endTime} " +
                    "GROUP BY i.dynamic_id, i.title " +
                    "ORDER BY clickCount DESC " +
                    "LIMIT 10"
    )
    List<Map<String, Object>> selectTopDynamicsByTimeRange(String startTime, String endTime);


    // 调试用：统计总访问量
    @Select("SELECT COUNT(*) AS totalVisits FROM mobile_dynamic_visit")
    long getTotalVisitCount();

    @Select("SELECT * FROM industry_dynamic " +
            "WHERE (title LIKE CONCAT('%',#{keyword},'%') OR content LIKE CONCAT('%',#{keyword},'%')) " +
            "AND auditStatus = 1 " +
            "ORDER BY createTime DESC " )
    List<IndustryDynamic> getAllDynamicsByKeyword(@Param("keyword") String keyword);

    @Insert("INSERT INTO mobile_dynamic_visit (user_id, dynamic_id, visit_time) " +
            "VALUES (#{user_id}, #{dynamic_id}, #{visit_time})")
    int insertVisit(Visit visit);

    @Select("SELECT COUNT(*) FROM mobile_dynamic_visit " +
            "WHERE user_id = #{user_id} AND dynamic_id = #{dynamic_id}")
    int checkVisitExists(@Param("user_id") Integer user_id,
                         @Param("dynamic_id") Integer dynamic_id);
}
