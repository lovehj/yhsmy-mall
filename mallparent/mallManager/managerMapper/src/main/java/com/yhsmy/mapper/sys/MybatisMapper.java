package com.yhsmy.mapper.sys;

import com.yhsmy.entity.vo.sys.UpdateMap;

import java.util.List;

/**
 * 通用更新、删除mapper
 *
 * @auth 李正义
 * @date 2019/11/24 14:09
 **/
public interface MybatisMapper {
    /**
     * 更新
     *
     * @param updateMap
     * @return 更新的记录条数
     */
    public int update (UpdateMap updateMap);

    /**
     * 删除
     *
     * @param updateMap
     * @return 删除记录条数
     */
    public int delete (UpdateMap updateMap);

    /**
     * 批量更新
     *
     * @param updateMapList
     * @return 批量更新的记录条数
     */
    public int batchUpdate (final List<UpdateMap> updateMapList);

    /**
     * 批量删除
     *
     * @param updateMapList
     * @return 批量删除的记录条数
     */
    public int batchDelete (final List<UpdateMap> updateMapList);
}
