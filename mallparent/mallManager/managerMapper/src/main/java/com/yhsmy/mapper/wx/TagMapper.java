package com.yhsmy.mapper.wx;

import com.yhsmy.entity.vo.wx.other.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth 李正义
 * @date 2020/2/15 14:38
 **/
public interface TagMapper {

    /**
     * 保存
     *
     * @param tag
     */
    public void addTag (Tag tag);

    /**
     * 按标签名查询
     *
     * @param name
     * @return
     */
    public List<Tag> findTagList (@Param("name") String name);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public Tag findTag (@Param("id") long id);

}
