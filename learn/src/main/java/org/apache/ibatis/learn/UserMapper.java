package org.apache.ibatis.learn;

import org.apache.ibatis.annotations.Param;

/**
 * 极简 Mapper 接口
 */
public interface UserMapper {

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户主键 ID
     * @return User 实体
     */
    User selectById(@Param("id") Long id);
}
