package com.lyj.controller;

import com.lyj.entity.Banner;
import com.lyj.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@RestController
@RequestMapping("banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Banner> banners = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
//            banners = poemService.findAlls(searchField, searchString, searchOper,(page-1)*rows,rows);
//            records = poemService.findAllsTotalCounts(searchField, searchString, searchOper);
        } else {
            banners = bannerService.findAll((page - 1) * rows, rows);
            records = bannerService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", banners);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @RequestMapping("opt")
    public Map<String, String> edit(Banner banner, String oper,HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        System.out.println("此时：" + banner);
        if ("add".equals(oper)) {
            try {
                banner.setId(null);
                banner.setCreate_date(new Date());
                bannerService.save(banner);
                System.out.println(banner.getId());
                map.put("status", "addOk");
                map.put("data", banner.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("del".equals(oper)) {
            String[] split = banner.getId().split(",");
            List<String> list = Arrays.asList(split);
            //图片存放路径
            String realPath = request.getServletContext().getRealPath("/upload/img");
            //循环删除文件
            for (String s : list) {
                Banner b = new Banner();
                b.setId(s);
                banner = bannerService.selectById(b);
                if(banner.getCover()!=null){
                    //删除原有文件
                    //先找到文件名
                    String[] reals = banner.getCover().split("/");
                    String cover = reals[reals.length-1];
                    File file = new File(realPath, cover);
                    file.delete();
                }
            }

            try {
                //批量删除
                bannerService.deleteAll(list);
                map.put("status", "delOk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("edit".equals(oper)) {
            try {
                bannerService.update(banner);
                map.put("data", banner.getId());
                map.put("status", "updateOk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @RequestMapping("motifyUpload")
    public void motifyUpload(MultipartFile aa, String id, HttpServletRequest request) throws IOException, IOException {
        System.out.println("id :" + id);
        System.out.println("cover:" + aa.getOriginalFilename());
        //存放路径
        String realPath = request.getServletContext().getRealPath("/upload/img");
        //上传图片的原始名和时间戳名
        String realcover = aa.getOriginalFilename();
        String cover = new Date().getTime() + realcover;

        //通过id找到该图片的详细信息
        Banner b = new Banner();
        b.setId(id);
        Banner banner = bannerService.selectById(b);
        System.out.println("修改图片" + banner);
        //判断是否上传空图片
        if (aa.getSize() == 0) {
            banner.setCover(banner.getCover());
        } else {
            if(banner.getCover()!=null){
                //删除原有文件
                //先找到文件名
                String[] reals = banner.getCover().split("/");
                String c = reals[reals.length-1];
                File file = new File(realPath, c);
                file.delete();
            }

            //文件上传
            aa.transferTo(new File(realPath, cover));

            //获取响应头
            String scheme = request.getScheme();
            //处理过的ip
            String ip = InetAddress.getLocalHost().toString().split("/")[1];
            //端口号
            int port = request.getServerPort();
            //动态获取项目名
            String contextPath = request.getContextPath();

            String uri = scheme+"://"+ip+":"+port+contextPath+"/upload/img/";
            cover = uri+cover;

            banner.setCover(cover);
        }

        //修改数据库中的图片路径
        banner.setRealcover(realcover);
        bannerService.update(banner);
    }

    @RequestMapping("upload")
    public void upload(MultipartFile aa, String id, HttpServletRequest request) throws IOException {
        System.out.println("id :" + id);
        System.out.println("cover:" + aa.getOriginalFilename());
        //图片文件的原始名和时间戳名
        String realcover = aa.getOriginalFilename();
        String cover = new Date().getTime() + realcover;

        //文件上传,先判断是否有文件夹，没有则创建
        File f = new File(request.getServletContext().getRealPath("/upload/img"));
        if(!f.exists()){
            f.mkdirs();
        }
        //如果不存在，则上传
        File file = new File(request.getServletContext().getRealPath("/upload/img"), cover);
        if (!file.exists()) {
            aa.transferTo(file);
        }

        //获取响应头
        String scheme = request.getScheme();
        //处理过的ip
        String ip = InetAddress.getLocalHost().toString().split("/")[1];
        //端口号
        int port = request.getServerPort();
        //动态获取项目名
        String contextPath = request.getContextPath();

        String uri = scheme+"://"+ip+":"+port+contextPath+"/upload/img/";
        cover = uri+cover;

        //修改数据库中的图片路径
        Banner banner = new Banner();
        banner.setId(id);
        banner = bannerService.selectById(banner);
        banner.setCover(cover);
        banner.setRealcover(realcover);
        bannerService.update(banner);
    }

}
