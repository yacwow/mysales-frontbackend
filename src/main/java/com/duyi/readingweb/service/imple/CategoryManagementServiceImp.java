package com.duyi.readingweb.service.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.CategoryManagement;
import com.duyi.readingweb.mapper.CategoryManagementMapper;
import com.duyi.readingweb.service.CategoryManagementService;
import org.springframework.stereotype.Service;

@Service
public class CategoryManagementServiceImp extends ServiceImpl<CategoryManagementMapper, CategoryManagement>
implements CategoryManagementService {
}
