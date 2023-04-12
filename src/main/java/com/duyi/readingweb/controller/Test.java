package com.duyi.readingweb.controller;

import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.PickUpPicture;
import com.duyi.readingweb.entity.RecommendPicture;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.service.RecommendPictureService;
import com.duyi.readingweb.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

//@RestController
@CrossOrigin()
@Api(tags = "test",description = "test")
public class Test {

    //测试正则mapping
    @ApiOperation("test")
    @GetMapping("/api/{test:[a-z0-9_-]*}")
    public String testReg(){
        return "true";
    }

    @RequestMapping("/api/img/{var}")
    public String getImg(HttpSession session){
        session.setAttribute("sessionId",1);
        return "hel";
    }
    @RequestMapping("/api/recommendPicture1")
    public void getImg2(HttpServletResponse response, String id, HttpSession session ) {
        System.out.println(session.getAttribute("sessionId"));
        System.out.println('1');
//        C:\Users\Administrator\IdeaProjects\readingWeb\src\main\resources\static\images
        File file = new File("C:\\Users\\Administrator\\IdeaProjects\\readingWeb\\src\\main\\resources\\static\\images\\test.webp");

        if(file.exists() && file.isFile()){
            FileInputStream fis = null;
            OutputStream os = null;
            System.out.println('2');
            try {
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
                System.out.println('3');
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println('4');
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        return "succ";

    }

    @RequestMapping("/api/test/heart1.png")
    public String getTest(){
        return "hel1";
    }

//    @RequestMapping("/api/login")
//    public ResultMsg testLogin(){
//        System.out.println('2');
//        ResultMsg resultMsg=new ResultMsg();
//        resultMsg.setCode(1);
//        resultMsg.setResult(true);
//        return resultMsg;
//
//    }

    @Autowired
    private RecommendPictureService recommendPictureService;
    @RequestMapping("/api/recommendPicture")
    public List<RecommendPicture> getRecommendPicture(){
        return recommendPictureService.list();
    }

    @RequestMapping("/api/pickUpPicture")
    public List<PickUpPicture> getPickUpPicture(){
        List<PickUpPicture> list=new ArrayList<>();
        PickUpPicture pickUpPicture=new PickUpPicture();
        pickUpPicture.setDate("2023.2.16");
        pickUpPicture.setHref("");
        pickUpPicture.setIntro("週間売れ筋ランキング");
        pickUpPicture.setImgSrc("/img/pickupPicture1.jpg");
        for (int i = 0; i < 7; i++) {
            list.add(pickUpPicture);
        }
        return list;
    }

    @RequestMapping("/api/image/{id}")
    public byte[] getImage(@PathVariable("id") String id) throws IOException  {
        File file = new File("C:\\Users\\Administrator\\Desktop\\123\\mainProduct"+id+".jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;

    }

    @Autowired
    private UserService userService;
    @RequestMapping("/api/pageTeacher/{current}/{limit}")
    public ResultMsg pageListTeacher(@PathVariable("current") long current,
                                     @PathVariable("limit") long limit,HttpServletResponse response) {
        System.out.println("in");
        List<User> list=userService.list();
        ResultMsg msg= ResultMsg.ok().data("total", list.size()).data("records", list);//另一种方式是将结果都封装到一个map里，一起返回
        System.out.println(msg);
        return msg;
        }

}
