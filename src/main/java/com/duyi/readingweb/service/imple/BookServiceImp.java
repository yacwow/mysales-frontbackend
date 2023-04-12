package com.duyi.readingweb.service.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.Book;
import com.duyi.readingweb.mapper.BookMapper;
import com.duyi.readingweb.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImp extends ServiceImpl<BookMapper, Book> implements BookService {
}
