package com.lyj.controller;

import com.lyj.util.SecurityCode;
import com.lyj.util.SecurityImage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("captcha")
public class CaptchaController {

    @RequestMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码随机数
        String securityCode = SecurityCode.getSecurityCode();
        /**
         * 将随机数存入session,未来将来做验证
         */
        HttpSession session = request.getSession();
        session.setAttribute("securityCode", securityCode);
        // 绘制生成验证码图片
        BufferedImage image = SecurityImage.createImage(securityCode);
        //相应到客户端
        OutputStream out = response.getOutputStream();
        /**
         * 第一个参数： 指定验证码图片对象
         * 第二个参数： 图片的格式
         * 第三个参数： 指定输出流
         */
        ImageIO.write(image, "png", out);
    }
}
