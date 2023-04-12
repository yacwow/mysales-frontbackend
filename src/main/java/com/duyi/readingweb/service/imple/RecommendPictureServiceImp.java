package com.duyi.readingweb.service.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.RecommendPicture;
import com.duyi.readingweb.mapper.RecommendPictureMapper;
import com.duyi.readingweb.service.RecommendPictureService;
import org.springframework.stereotype.Service;

@Service
public class RecommendPictureServiceImp extends ServiceImpl<RecommendPictureMapper, RecommendPicture>
        implements RecommendPictureService {
}
