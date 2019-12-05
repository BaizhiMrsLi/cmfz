package com.lyj.controller;

import com.lyj.entity.Album;
import com.lyj.entity.Chapter;
import com.lyj.service.AlbumService;
import com.lyj.service.ChapterService;
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
@RequestMapping("album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private ChapterService chapterService;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Album> albums = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
            System.out.println("暂时不用");
        } else {
            albums = albumService.findAll((page - 1) * rows, rows);
            records = albumService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", albums);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @RequestMapping("opt")
    public Map<String, String> edit(Album album, String oper, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        if ("add".equals(oper)) {
            try {
                album.setId(null);
                album.setCreate_date(new Date());
                album.setCount(0);
                albumService.save(album);
                System.out.println(album.getId());
                map.put("status", "addOk");
                map.put("data", album.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("del".equals(oper)) {
            String[] split = album.getId().split(",");
            List<String> list = Arrays.asList(split);
            //图片存放路径
            String realPath = request.getServletContext().getRealPath("/upload/img");
            //循环删除文件
            for (String s : list) {
                Album b = new Album();
                b.setId(s);
                album = albumService.selectById(b);
                if(album.getCover()!=null){
                    //删除原有文件
                    //先找到文件名
                    String[] reals = album.getCover().split("/");
                    String cover = reals[reals.length-1];
                    File file = new File(realPath, cover);
                    file.delete();
                }
            }

            try {
                //先删除专辑下的音频章节
                Chapter chapter = new Chapter();
                chapter.setAlbum_id(album.getId());
                chapterService.delete(chapter);
                //专辑批量删除
                albumService.deleteAll(list);
                map.put("status", "delOk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("edit".equals(oper)) {
            try {
                albumService.update(album);
                map.put("data", album.getId());
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
        Album b = new Album();
        b.setId(id);
        Album album = albumService.selectById(b);
        //判断是否上传空图片
        if (aa.getSize() == 0) {
            album.setCover(album.getCover());
        } else {
            if(album.getCover()!=null){
                //删除原有文件
                //先找到文件名
                String[] reals = album.getCover().split("/");
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

            album.setCover(cover);
        }

        //修改数据库中的图片路径
        albumService.update(album);
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
        Album album = new Album();
        album.setId(id);
        album = albumService.selectById(album);
        album.setCover(cover);
        albumService.update(album);
    }

    //根据专辑id查看详情
    @RequestMapping("selAlbumById")
    public Map selAlbumById(String uid,Album album){
        Map map = new HashMap();
        album = albumService.selectById(album);
        map.put("status","200");
        map.put("album",album);
        return map;
    }
}
