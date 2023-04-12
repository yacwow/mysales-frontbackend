package com.duyi.readingweb.service.user.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.user.Comment;
import com.duyi.readingweb.mapper.user.CommentMapper;
import com.duyi.readingweb.service.user.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImp extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {
}
