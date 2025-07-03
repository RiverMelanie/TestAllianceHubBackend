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

    @Update("update industry_dynamic set publisherId=#{publisherId},title=#{title},content=#{content},summary=#{summary},author=#{author},imageUrl=#{imageUrl},createTime=now(),auditStatus=#{auditStatus} where dynamicId=#{dynamicId}")
    int upDynamic(IndustryDynamic industryDynamic);

    @Update("update dynamicreviewrecordtable set ReviewerID=#{ReviewerID},Content=#{Content},NewsSummary=#{NewsSummary},Author=#{Author},ReviewResult=#{ReviewResult} where Title=#{Title} and NewsImage=#{NewsImage}")
    int upReviewDynamic(dynamicreviewrecordtable dynamicreviewrecordtable);
    @Update("update industry_dynamic set createTime=now(),auditStatus=#{auditStatus} where publisherId=#{publisherId} and title=#{title} and content=#{content} and summary=#{summary} and author=#{author} and imageUrl=#{imageUrl}")
    void upDynamicPlus(IndustryDynamic industryDynamic);
    @Delete("delete from dynamicreviewrecordtable where ReviewResult=1 ")
    void cleanDynamics();
    @Delete("delete from industry_dynamic where dynamicId=#{id}")
    int delDynamic(int id);
}
