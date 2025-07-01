package com.neucamp.testalliancehubbackend.mapper;

import com.neucamp.testalliancehubbackend.entity.IndustryDynamic;
import com.neucamp.testalliancehubbackend.entity.dynamicreviewrecordtable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;
@Mapper
public interface ManagerIndustryDynamicMapper {
    @Select("<script>" +
            "SELECT * FROM dynamicreviewrecordtable " +
            "ORDER BY ReviewerID " +
            "LIMIT #{pageSize} OFFSET #{offset}" +
            "</script>")
    List<dynamicreviewrecordtable> searchDynamics(Map<String, Object> params);

    @Select("<script>" +
            "SELECT COUNT(*) FROM dynamicreviewrecordtable " +
            "</script>")
    int searchTotalCount(Map<String, Object> params);

    @Update("update industry_dynamic set publisher_id=#{publisherId},title=#{title},content=#{content},summary=#{summary},author=#{author},image_url=#{imageUrl},create_time=now(),audit_status=#{auditStatus} where dynamic_id=#{dynamicId}")
    int upDynamic(IndustryDynamic industryDynamic);

    @Update("update dynamicreviewrecordtable set ReviewerID=#{ReviewerID},Content=#{Content},NewsSummary=#{NewsSummary},Author=#{Author},ReviewResult=#{ReviewResult} where Title=#{Title} and NewsImage=#{NewsImage}")
    int upReviewDynamic(dynamicreviewrecordtable dynamicreviewrecordtable);
    @Update("update industry_dynamic set create_time=now(),audit_status=#{auditStatus} where publisher_id=#{publisherId} and title=#{title} and content=#{content} and summary=#{summary} and author=#{author} and image_url=#{imageUrl}")
    void upDynamicPlus(IndustryDynamic industryDynamic);
    @Delete("delete from dynamicreviewrecordtable where ReviewResult=1 ")
    void cleanDynamics();
    @Delete("delete from industry_dynamic where dynamic_id=#{id}")
    int delDynamic(int id);
}
