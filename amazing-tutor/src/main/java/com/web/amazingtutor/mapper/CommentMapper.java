package com.web.amazingtutor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.amazingtutor.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
