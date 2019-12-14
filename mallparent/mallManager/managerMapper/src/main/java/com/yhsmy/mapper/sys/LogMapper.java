package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2019/12/10 9:44
 **/
public interface LogMapper {

    /**
     * 保存日志对象
     *
     * @param log
     */
    public void addLog (Log log);

    /**
     * 查询日志
     *
     * @param queryBy   1=操作的用户 2=操作的类型 3=设备来源
     * @param queryText
     * @param queryDate 1=创建时间
     * @param startDate
     * @param endDate
     * @return list
     */
    public List<Log> findLogList (@Param("queryBy") int queryBy, @Param("queryText") String queryText,
                                  @Param("queryDate") int queryDate, @Param("startDate") String startDate, @Param("endDate") String endDate);

}
