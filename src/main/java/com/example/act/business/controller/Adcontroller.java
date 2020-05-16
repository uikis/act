package com.example.act.business.controller;

import com.example.act.business.dao.AdDao;
import com.example.act.business.domain.Advertisement;
import com.example.act.business.service.AdService;
import com.example.act.permission.domain.AjaxResult;
import com.example.act.permission.domain.Page;
import com.example.act.permission.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("business")
public class Adcontroller {
    @Autowired
    AdService adService;

    @RequestMapping("adQueryDo")
    @ResponseBody
    public Object adQueryDo(String queryText, Integer pageNo, Integer pageNum) {//一个是默认的当前页数，一个是默认的展现条数
        AjaxResult ajaxResult = new AjaxResult();
        try {
//            pageNo = 1;
//            pageNum = 3;
            Map<String, Object> map = new HashMap<>();//使用hashmap实现map
            map.put("start", (pageNo - 1) * pageNum);
            map.put("pageNum", pageNum);//通过计算获得开始的值和个数
            map.put("queryText", queryText);
            List<Advertisement> ads = adService.selectAds(map);
            //        model.addAttribute("users", users);//不跳转页面了，所以不需要传递
            //        model.addAttribute("pageNo",pageNo);//把User对象和当前页数传给前台
            //1.先获取最大数据库中的最大条数
            int maxCount = adService.selectCount(map);
            int maxpageNo = 1;
            //2.计算总的页数
            if (maxCount % pageNum == 0) {
                maxpageNo = maxCount / pageNum;
            } else {
                maxpageNo = maxCount / pageNum + 1;
            }

            Page<Advertisement> adPage = new Page<>();
            adPage.setMaxCount(maxCount);
            adPage.setMaxpageNo(maxpageNo);
            adPage.setPageNo(pageNo);
            adPage.setObject(ads);
            //【将已知的全部加入封装属性,在将封装好的属性添加到ajaxResult中】
            ajaxResult.setObject(adPage);//***************
            //       model.addAttribute("maxpageNo", maxpageNo);//需要将总页码和当前页码都传递给前台
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("addAdDo")
    @ResponseBody
    public Object addAdDo(HttpSession session, MultipartFile file, String name) {
        AjaxResult ajaxResult = new AjaxResult();
        User user = (User) session.getAttribute("user");
        Integer id = user.getId();//取得用户对象
        Advertisement ad = new Advertisement();

        try {
            ad.setUserid(id);//设置用户id
            ad.setName(name);//设置广告名称
            String filename = file.getOriginalFilename();
            //开始保存到对应盘符
            //动态生成文件名，采用UUID方法
            UUID uuid = UUID.randomUUID();
            String s = uuid.toString();
            int i = filename.lastIndexOf(".");//获取最后一个点的位置
            String substring = filename.substring(i);//从最后一个点的位置取值
//        String filepath = "D:\\upload\\"+s+substring;//获得文件的全名
//        file.transferTo(new File(filepath));//然后将文件传输为上述路径的文件

            //分文件夹
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//创建一个时间格式
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String sl = sdf.format(date);
            String createtime = sdf2.format(date);
            ad.setCreatetime(createtime);//设置创建时间
            String newPath = "F:\\IDEA\\act\\src\\main\\resources\\static\\upload\\" + sl;//取得文件夹路径
            File file1 = new File(newPath);//这名话并不真正会创建这个文件,而是创建了一个代表
            // 这个文件的一个File对象, 你需要判断,如果文件不存在,再创建,如:
            if (!file1.exists()) {//如果这个对象的具体文件不存在
                file1.mkdir();//如果没有就创建文件夹
            }
            String filepath1 = "F:\\IDEA\\act\\src\\main\\resources\\static\\upload\\" + sl + "\\" + s + substring;//文件绝对路径
            String filepath2 = "/static/upload/" + sl + "/" + s + substring;//这是网络地址用/
            ad.setImg(filepath2);//设置图片路径
            file.transferTo(new File(filepath1));//然后将文件传输为上述路径的文件//把内存中图片写入磁盘
            adService.addAd(ad);
            ajaxResult.setSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("deleteAdsDo")
    @ResponseBody
    public Object deleteAds(Integer[] roleid) {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> map = new HashMap<>();
        map.put("adid", roleid);
        try {
            adService.deleteAds(map);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("deleteAdDo")
    @ResponseBody
    public Object deleteAd(Integer id) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            adService.deleteRoleById(id);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("editAdDo")
    @ResponseBody
    public Object editAdDo(Advertisement ad) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            adService.updateAd(ad);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }
}
