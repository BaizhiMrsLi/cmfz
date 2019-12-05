package com.lyj.cmfz.test;

import com.lyj.CmfzApplication;
import com.lyj.dao.AdminDao;
import com.lyj.dao.GuruDao;
import com.lyj.dao.UserDao;
import com.lyj.entity.Admin;
import com.lyj.entity.Guru;
import com.lyj.entity.MapVO;
import com.lyj.util.FileSizes;
import org.apache.ibatis.session.RowBounds;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = CmfzApplication.class)
@RunWith(SpringRunner.class)
public class CmfzApplicationTests {

    @Autowired
    private AdminDao adminDao;

    @Test
    public void firstconn() {
        List<Admin> admins = adminDao.selectAll();
        admins.forEach(admin -> System.out.println(admin));
    }

    @Test
    public void adminLogin() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        System.out.println(adminDao.selectOne(admin));
    }

    @Test
    public void testServ() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {

        String s = FileSizes.FormetFileSize(4355680);
        System.out.println(s);
    }

    @Autowired
    private GuruDao guruDao;
    @Test
    public void guruAll() {
        Guru guru = new Guru();
        System.out.println(guruDao.selectByRowBounds(new Guru(),new RowBounds(0,3)));
    }

    @Autowired
    private UserDao userDao;
    @Test
    public void test01(){
        List<MapVO> mapVOS = userDao.AddressAndCount();
        System.out.println(mapVOS);
    }
    @Test
    public void test02(){
        Integer integer = userDao.rangeByTime("1", 365);
        System.out.println(integer);
    }

}
