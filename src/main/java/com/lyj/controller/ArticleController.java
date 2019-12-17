package com.lyj.controller;

import com.lyj.entity.Article;
import com.lyj.entity.Guru;
import com.lyj.repository.ArticleRepository;
import com.lyj.service.ArticleService;
import com.lyj.service.GuruService;
import com.lyj.util.PhotoUpload;
import org.apache.commons.io.FilenameUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping("findAll")
    public Map<String, Object> findAll(String searchField, String searchString, String searchOper, Integer page, Integer rows, Boolean _search) {
        //不做搜索处理
        Map<String, Object> map = new HashMap<>();

        List<Article> articles = new ArrayList<>();
        Integer total = null;
        Integer records = null;

        if (_search) {
//            banners = poemService.findAlls(searchField, searchString, searchOper,(page-1)*rows,rows);
//            records = poemService.findAllsTotalCounts(searchField, searchString, searchOper);
        } else {
            articles = articleService.findAll((page - 1) * rows, rows);
            records = articleService.findAllCount();
        }

        if (records % rows == 0) {
            total = records / rows;
        } else {
            total = records / rows + 1;
        }

        map.put("rows", articles);
        map.put("page", page);
        map.put("total", total);
        map.put("records", records);
        return map;
    }

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    //富文本框的图片上传
    @RequestMapping("uploadImg")
    public Map uploadImg(MultipartFile imgFile, HttpSession session, HttpServletRequest request) {
        HashMap hashMap = new HashMap();
        String dir = "/upload/articleImg/";
        try {
            String httpUrl = PhotoUpload.getHttpUrl(imgFile, request, session, dir);
            hashMap.put("error", 0);
            hashMap.put("url", httpUrl);
        } catch (Exception e) {
            hashMap.put("error", 1);
            hashMap.put("message", "上传错误");
            e.printStackTrace();
        }
        return hashMap;
    }

    @Autowired
    private GuruService guruService;

    //图片空间的回显
    @RequestMapping("showAllImgs")
    public Map showAllImgs(HttpSession session, HttpServletRequest request) {
        // 1. 获取文件夹绝对路径
        String realPath = session.getServletContext().getRealPath("/upload/articleImg/");
        // 2. 准备返回的Json数据
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        // 3. 获取目标文件夹
        File file = new File(realPath);
        File[] files = file.listFiles();
        // 4. 遍历文件夹中的文件
        for (File file1 : files) {
            // 5. 文件属性封装
            HashMap fileMap = new HashMap();
            fileMap.put("is_dir", false);
            fileMap.put("has_file", false);
            fileMap.put("filesize", file1.length());
            fileMap.put("is_photo", true);
            // 获取文件后缀 | 文件类型
            String extension = FilenameUtils.getExtension(file1.getName());
            fileMap.put("filetype", extension);
            fileMap.put("filename", file1.getName());
            // 获取文件上传时间 1. 截取时间戳 2. 创建格式转化对象 3. 格式类型转换
            String s = file1.getName().split("_")[0];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String format = simpleDateFormat.format(new Date(Long.valueOf(s)));
            fileMap.put("datetime", format);
            arrayList.add(fileMap);
        }
        hashMap.put("file_list", arrayList);
        hashMap.put("total_count", arrayList.size());
        // 返回路径为 项目名 + 文件夹路径
        hashMap.put("current_url", request.getContextPath() + "/upload/articleImg/");
        return hashMap;
    }

    //更新文章
    @RequestMapping("updateArticle")
    public void insertArticle(MultipartFile aa, Article article, HttpServletRequest request, HttpSession session) {
        if (aa.getSize() != 0) {
            String dir = "/upload/articleCover/";
            String httpUrl = PhotoUpload.getHttpUrl(aa, request, session, dir);
            article.setCover(httpUrl);
        }
        if (!"0".equals(article.getGuru_id())) {
            Guru guru = new Guru();
            guru.setId(article.getGuru_id());
            guru = guruService.selectById(guru);
            article.setAuthor(guru.getNickname());
        } else {
            article.setAuthor("通用文章");
        }
        //更新mysql数据库中文章信息
        articleService.update(article);
        //查找更新之后的文章
        Article a = articleService.selectById(article);
        //先删除es中该文章的信息
        articleRepository.deleteById(article.getId());
        //然后再加入修改后的文章
        articleRepository.save(a);
    }

    @RequestMapping("addArticle")
    public void addArticle(MultipartFile aa, Article article, HttpServletRequest request, HttpSession session) {
        article.setId(null);
        if (aa.getSize() != 0) {
            String dir = "/upload/articleCover/";
            String httpUrl = PhotoUpload.getHttpUrl(aa, request, session, dir);
            article.setCover(httpUrl);
        }
        if (!"0".equals(article.getGuru_id())) {
            Guru guru = new Guru();
            guru.setId(article.getGuru_id());
            guru = guruService.selectById(guru);
            article.setAuthor(guru.getNickname());
        } else {
            article.setAuthor("通用文章");
        }
        Date d = new Date();
        article.setPublish_date(d);
        article.setCreate_date(d);

        System.out.println(article);

        //将文章存到mysql数据库
        articleService.save(article);


        //将文章存到es中
        articleRepository.save(article);
    }

    //不用再判断是修改添加还是删除，在这里是只有删除
    @RequestMapping("opt")
    public void edit(Article article, String oper, HttpServletRequest request) {
        String[] split = article.getId().split(",");
        List<String> list = Arrays.asList(split);
        //mysql数据库批量删除
        articleService.deleteAll(list);
        //es批量删除
        for (String s : list) {
            articleRepository.deleteById(s);
        }
    }

    @RequestMapping("selArticleById")
    public Map selArticleById(String uid, Article article) {
        Map map = new HashMap();
        article = articleService.selectById(article);
        map.put("status", "200");
        map.put("article", article);
        return map;
    }

    @RequestMapping("findByName")
    public List<Article> findByName(String dic) {
        dic = dic.replace(" ", "");

        List<Article> articles = new ArrayList<>();

        //高亮字段
        HighlightBuilder.Field field = new HighlightBuilder.Field("*")
                .requireFieldMatch(false)
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        //查询的条件
        QueryStringQueryBuilder fields = QueryBuilders.queryStringQuery(dic).field("name").field("content");

        //是组合查询中的第一个参数
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cmfz")
                .withTypes("article")
                .withQuery(fields)
                .withHighlightFields(field)
                .build();

        elasticsearchTemplate.queryForPage(searchQuery, Article.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                //1.先获取所有数据
                SearchHits hits = searchResponse.getHits();

                //2.获取真实数据
                SearchHit[] searchHits = hits.getHits();

                for (SearchHit searchHit : searchHits) {
                    Article article = new Article();

                    //获取原始的数据
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    //获取高亮
                    Map<String, HighlightField> map = searchHit.getHighlightFields();

                    article.setId(sourceAsMap.get("id").toString());

                    article.setName(sourceAsMap.get("name").toString());
                    if (map.containsKey("name")) {
                        article.setName(map.get("name").fragments()[0].toString());
                    }

                    article.setAuthor(sourceAsMap.get("author").toString());

                    article.setContent(sourceAsMap.get("content").toString());
                    if (map.containsKey("content")) {
                        article.setContent(map.get("content").fragments()[0].toString());
                    }

                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

                    try {
                        article.setCreate_date(ft.parse(sourceAsMap.get("create_date").toString()));
                        article.setPublish_date(ft.parse(sourceAsMap.get("publish_date").toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    articles.add(article);
                }
                return null;
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });

        return articles;
    }
}
