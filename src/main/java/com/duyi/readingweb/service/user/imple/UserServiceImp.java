package com.duyi.readingweb.service.user.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.mapper.user.UserMapper;
import com.duyi.readingweb.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {
}
