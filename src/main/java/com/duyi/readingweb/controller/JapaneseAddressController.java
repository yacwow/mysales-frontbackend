package com.duyi.readingweb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.JapaneseAddress;
import com.duyi.readingweb.service.JapaneseAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class JapaneseAddressController {
    @Autowired
    private JapaneseAddressService japaneseAddressService;

    @RequestMapping("/api/getCityList/{province}")
    private ResultMsg getCityList( @PathVariable String province){
      List<JapaneseAddress> japaneseAddressList=  japaneseAddressService.list(new QueryWrapper<JapaneseAddress>()
                .eq("province",province).select("distinct city"));
        List<String> cityList = new ArrayList<>();
        for (JapaneseAddress japaneseAddress : japaneseAddressList) {
            String city = japaneseAddress.getCity();
            if (city != null && !city.isEmpty()&&!city.equals(" ")) {
                cityList.add(city);
            }
        }
        return ResultMsg.ok().result(true).data("cityList",cityList);
    }
    @RequestMapping("/api/getAddressList/{city}")
    private ResultMsg getAddressList( @PathVariable String city){
        List<JapaneseAddress> japaneseAddressList=  japaneseAddressService.list(new QueryWrapper<JapaneseAddress>()
                .eq("city",city).select("distinct area"));
        List<String> areaList = new ArrayList<>();
        for (JapaneseAddress japaneseAddress : japaneseAddressList) {
            String area = japaneseAddress.getArea();
            if (area != null && !area.isEmpty()&&!area.equals(" ")) {
                areaList.add(area);
            }
        }
        return ResultMsg.ok().result(true).data("areaList",areaList);
    }

    @RequestMapping("/api/getPostCode/{province}/{city}/{area}")
    private ResultMsg getPostCode(@PathVariable("province") String province, @PathVariable("city") String city,
                                  @PathVariable("area") String area){
        List<JapaneseAddress> japaneseAddressList=  japaneseAddressService.list(new QueryWrapper<JapaneseAddress>()
                .eq("city",city).eq("province",province).eq("area",area).select("postcode"));

        return ResultMsg.ok().result(true).data("postCode",japaneseAddressList.get(0).getPostcode());
    }


    @RequestMapping("/api/getAddress/{postcode}")
    private ResultMsg getAddress(@PathVariable("postcode") String postcode){
        List<JapaneseAddress> japaneseAddressList=  japaneseAddressService.list(new QueryWrapper<JapaneseAddress>()
                .eq("postcode",postcode).select("province","city","area"));
        if(japaneseAddressList.size()>0){
            Map<String,Object> address=new HashMap<>();
            address.put("province",japaneseAddressList.get(0).getProvince());
            address.put("area",japaneseAddressList.get(0).getArea());
            address.put("city",japaneseAddressList.get(0).getCity());
            List<JapaneseAddress> newJapaneseAddressList=  japaneseAddressService.list(new QueryWrapper<JapaneseAddress>()
                    .eq("province",japaneseAddressList.get(0).getProvince()).select("distinct city"));
            List<String> cityList = new ArrayList<>();
            for (JapaneseAddress japaneseAddress : newJapaneseAddressList) {
                String city = japaneseAddress.getCity();
                if (city != null && !city.isEmpty()&&!city.equals(" ")) {
                    cityList.add(city);
                }
            }
            List<JapaneseAddress> newJapaneseAddressList2=  japaneseAddressService.list(new QueryWrapper<JapaneseAddress>()
                    .eq("city",japaneseAddressList.get(0).getCity()).select("distinct area"));
            List<String> areaList = new ArrayList<>();
            for (JapaneseAddress japaneseAddress : newJapaneseAddressList2) {
                String area = japaneseAddress.getArea();
                if (area != null && !area.isEmpty()&&!area.equals(" ")) {
                    areaList.add(area);
                }
            }
            address.put("areaList",areaList);
            address.put("cityList",cityList);
            return ResultMsg.ok().result(true).data(address);
        }else{
            return ResultMsg.ok().result(false);
        }

    }
}
