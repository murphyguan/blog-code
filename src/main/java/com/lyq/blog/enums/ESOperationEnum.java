package com.lyq.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ES操作类型
 *

 */
@Getter
@AllArgsConstructor
public enum ESOperationEnum {
    /**
     * 新增
     */
    INSERT("insert"),
    /**
     * 修改
     */
    UPDATE("update"),
    /**
     * 删除
     */
    DELETE("delete");

    /**
     * 描述
     */
    private final String value;

}
