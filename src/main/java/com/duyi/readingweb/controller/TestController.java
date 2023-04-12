package com.duyi.readingweb.controller;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.Book;
import com.duyi.readingweb.entity.JapaneseAddress;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.service.BookService;
import com.duyi.readingweb.service.JapaneseAddressService;
import com.duyi.readingweb.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TestController {




    @Autowired
    private JapaneseAddressService japaneseAddressService;
    @RequestMapping("/api/getAddress/")
    public ResultMsg test2() {
       return ResultMsg.error().message("need postcode");
    }

    @RequestMapping("/api/getAddress/{postcode}")
    public ResultMsg test1(@PathVariable("postcode") String postcode) {
        System.out.println(postcode);
        AbstractWrapper abstractWrapper = new QueryWrapper<JapaneseAddress>().eq("postcode", postcode);
        List<JapaneseAddress> japaneseAddresses = japaneseAddressService.list(abstractWrapper);
        if(japaneseAddresses.size()==0) return ResultMsg.error().message("no postcode match");
        JapaneseAddress japaneseAddress = japaneseAddresses.get(0);
        String province = japaneseAddress.getProvince();
        String city = japaneseAddress.getCity();
        String area = japaneseAddress.getArea();
        List<Object> cityList = japaneseAddressService.listObjs(
                new QueryWrapper<JapaneseAddress>().eq("province", province).select("DISTINCT city"));
        List<String> newCityList = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            newCityList.add((String) cityList.get(i));
        }
        List<Object> areaList = japaneseAddressService.listObjs(
                new QueryWrapper<JapaneseAddress>().eq("city", city).select("DISTINCT area"));
        List<String> newAreaList = new ArrayList<>();
        for (int i = 0; i < areaList.size(); i++) {
            newAreaList.add((String) areaList.get(i));
        }
        System.out.println(areaList.size());
        System.out.println(cityList.size());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("province", province);
        map.put("city", city);
        map.put("area", area);
        map.put("newCityList", newCityList);
        map.put("newAreaList", newAreaList);
        System.out.println(map.size());
        return ResultMsg.ok().data(map);
    }

    @RequestMapping("api/getCityList/{province}")
    public List<String> getCityList(@PathVariable("province") String province) {
        List<Object> list = japaneseAddressService.listObjs(new QueryWrapper<JapaneseAddress>()
                .eq("province", province).select("Distinct city"));
        List<String> cityList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).equals(" ")) {
//                continue;
//            }
            cityList.add((String) list.get(i));
        }
        return cityList;
    }

    @RequestMapping("api/getAddressList/{city}")
    public List<String> getAddressList(@PathVariable("city") String city) {
        List<Object> list = japaneseAddressService.listObjs(new QueryWrapper<JapaneseAddress>()
                .eq("city", city).select("Distinct area"));
        List<String> addressList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).equals(" ")) {
//                continue;
//            }
            addressList.add((String) list.get(i));
        }
        return addressList;
    }

    @RequestMapping("api/getPostCode/{province}/{city}/{address}")
    public List<Integer> getPostCode(@PathVariable("address") String address,
                                     @PathVariable("city") String city,@PathVariable("province")String province) {
        List<Object> list = japaneseAddressService.listObjs(new QueryWrapper<JapaneseAddress>()
                .eq("area", address).eq("province",province)
                .eq("city",city).select("postcode"));
        System.out.println(list);
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add((Integer) list.get(i));
        }
        return list1;
    }

    @Autowired
    private UserService userService;

    @RequestMapping("/mybatisplus")
    @ResponseBody
    public List<User> mybatisplus() {
        List<User> list = userService.list();
        return list;
    }

    @Autowired
    private BookService bookService;

    @RequestMapping("/addBook")
    @ResponseBody
    public Boolean addBook() {
        Book book = new Book();
        book.setName("夜晚");
        book.setAuthor("肘子");
        book.setDescp("文章的介绍");
        book.setStatus(1);
        book.setCount(22222222);

        return bookService.save(book);
    }

}
