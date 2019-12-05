package com.lyj.controller;

import com.lyj.entity.Album;
import com.lyj.entity.Chapter;
import com.lyj.service.AlbumService;
import com.lyj.service.ChapterService;
import com.lyj.util.FileSizes;
import org.apache.commons.io.IOUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("chapter")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private AlbumService albumService;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(Chapter chapter,String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        System.out.println(chapter);
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Chapter> chapters = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
            System.out.println("暂时不用");
        } else {
            chapters = chapterService.findAll(chapter,(page - 1) * rows, rows);
            records = chapterService.findAllCount(chapter);
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", chapters);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @RequestMapping("opt")
    public Map<String, String> edit(Chapter chapter, String oper, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        if ("add".equals(oper)) {
            try {
                chapter.setId(null);
                chapter.setCreate_date(new Date());
                chapterService.save(chapter);
                System.out.println(chapter.getId());
                map.put("status", "addOk");
                map.put("data", chapter.getId());
                //添加章节音频文件之后要修改专辑的集数
                Album album = new Album();
                album.setId(chapter.getAlbum_id());
                album = albumService.selectById(album);
                album.setCount(album.getCount()+1);
                albumService.update(album);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("del".equals(oper)) {
            String[] split = chapter.getId().split(",");
            List<String> list = Arrays.asList(split);
            //图片存放路径
            String realPath = request.getServletContext().getRealPath("/upload/video");
            //循环删除文件
            for (String s : list) {
                Chapter b = new Chapter();
                b.setId(s);
                chapter = chapterService.selectById(b);
                if(chapter.getCover()!=null){
                    //删除原有文件
                    //先找到文件名
                    String[] reals = chapter.getCover().split("/");
                    String cover = reals[reals.length-1];
                    File file = new File(realPath, cover);
                    file.delete();
                }
            }

            try {
                //批量删除
                chapterService.deleteAll(list);
                map.put("status", "delOk");
                //添加章节音频文件之后要修改专辑的集数
                Album album = new Album();
                album.setId(chapter.getAlbum_id());
                album = albumService.selectById(album);
                album.setCount(album.getCount()-list.size());
                albumService.update(album);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("edit".equals(oper)) {
            try {
                chapterService.update(chapter);
                map.put("data", chapter.getId());
                map.put("status", "updateOk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @RequestMapping("motifyUpload")
    public void motifyUpload(MultipartFile bb, String id, HttpServletRequest request) throws IOException, IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException {
        System.out.println("id :" + id);
        System.out.println("cover:" + bb.getOriginalFilename());
        //存放路径
        String realPath = request.getServletContext().getRealPath("/upload/video");
        //上传图片的原始名和时间戳名
        String realcover = bb.getOriginalFilename();
        String cover = new Date().getTime() +"_"+ realcover;

        //通过id找到该图片的详细信息
        Chapter b = new Chapter();
        b.setId(id);
        Chapter chapter = chapterService.selectById(b);
        //判断是否上传空图片
        if (bb.getSize() == 0) {
            chapter.setCover(chapter.getCover());
        } else {
            if(chapter.getCover()!=null){
                //删除原有文件
                //先找到文件名
                String[] reals = chapter.getCover().split("/");
                String c = reals[reals.length-1];
                File file = new File(realPath, c);
                file.delete();
            }

            //文件上传
            bb.transferTo(new File(realPath, cover));

            //还需修改文件所在数据库的文件大小，时长
            chapter.setSize(FileSizes.FormetFileSize(bb.getSize()));
            File c = new File(realPath+"/"+cover);
            AudioFile audio = AudioFileIO.read(c);
            int time = audio.getAudioHeader().getTrackLength();
            String sb = "";
            if(time % 60 == 0){
                sb = time/60 +"分";
            }else {
                if(time % 60 < 10){
                    sb = time/60+"分0"+time%60+"秒";
                }else {
                    sb = time/60+"分"+time%60+"秒";
                }
            }
            chapter.setTime(sb);

            //获取响应头
            String scheme = request.getScheme();
            //处理过的ip
            String ip = InetAddress.getLocalHost().toString().split("/")[1];
            //端口号
            int port = request.getServerPort();
            //动态获取项目名
            String contextPath = request.getContextPath();

            String uri = scheme+"://"+ip+":"+port+contextPath+"/upload/video/";
            cover = uri+cover;

            chapter.setCover(cover);
        }

        //修改数据库中的图片路径
        chapterService.update(chapter);


    }

    @RequestMapping("upload")
    public void upload(MultipartFile bb, String id, HttpServletRequest request) throws IOException, TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException {
        System.out.println("id :" + id);
        System.out.println("cover:" + bb.getOriginalFilename());
        //图片文件的原始名和时间戳名
        String realcover = bb.getOriginalFilename();
        String cover = new Date().getTime() +"_"+ realcover;

        //文件上传,先判断是否有文件夹，没有则创建
        File f = new File(request.getServletContext().getRealPath("/upload/video"));
        if(!f.exists()){
            f.mkdirs();
        }
        //如果不存在，则上传
        File file = new File(request.getServletContext().getRealPath("/upload/video"), cover);
        if (!file.exists()) {
            bb.transferTo(file);
        }

        Chapter chapter = new Chapter();

        //还需修改文件所在数据库的文件大小，时长
        chapter.setSize(FileSizes.FormetFileSize(bb.getSize()));
        File c = new File(request.getServletContext().getRealPath("/upload/video")+"/"+cover);
        AudioFile audio = AudioFileIO.read(c);
        int time = audio.getAudioHeader().getTrackLength();
        String sb = "";
        if(time % 60 == 0){
            sb = time/60 +"分";
        }else {
            if(time % 60 < 10){
                sb = time/60+"分0"+time%60+"秒";
            }else {
                sb = time/60+"分"+time%60+"秒";
            }
        }
        chapter.setTime(sb);

        //获取响应头
        String scheme = request.getScheme();
        //处理过的ip
        String ip = InetAddress.getLocalHost().toString().split("/")[1];
        //端口号
        int port = request.getServerPort();
        //动态获取项目名
        String contextPath = request.getContextPath();

        String uri = scheme+"://"+ip+":"+port+contextPath+"/upload/video/";
        cover = uri+cover;

        //修改数据库中的图片路径
        chapter.setId(id);
        Chapter cc = chapterService.selectById(chapter);
        cc.setCover(cover);
        cc.setSize(chapter.getSize());
        cc.setTime(chapter.getTime());
        cc.setCover(cover);
        chapterService.update(cc);
    }

    @RequestMapping("down")
    public void down(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

        String[] reals = url.split("/");
        String filename = reals[reals.length-1];

        //1.根据文件目录获取绝对路径
        String path = request.getServletContext().getRealPath("/upload/video");

        //读取需要下载的文件
        File file = new File(path,filename);
        //获取文件输入流
        FileInputStream is = new FileInputStream(file);

        String openStyle = "attachment";

        //默认的下载方式是在线打开  inline      附件形式下载是：attachment
        response.setHeader("content-disposition",openStyle+";fileName="+URLEncoder.encode(filename,"UTF-8"));

        //下载方式确定后，需要响应输出流
        ServletOutputStream os = response.getOutputStream();

        //IO拷贝
        IOUtils.copy(is,os);
        //拷贝完之后需要关流
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }
}
