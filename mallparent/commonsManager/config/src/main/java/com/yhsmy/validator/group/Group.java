package com.yhsmy.validator.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @auth 李正义
 * @date 2019/11/8 9:40
 **/
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {
}
