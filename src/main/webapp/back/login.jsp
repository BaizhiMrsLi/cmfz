<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>love</title>
    <link href="favicon.ico" rel="shortcut icon" />
    <!--引入bootstrap css-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/boot/css/bootstrap.min.css">
    <!--引入jqgrid的bootstrap css-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jqgrid/css/trirand/ui.jqgrid-bootstrap.css">
    <!--引入jquery核心js-->
    <script src="${pageContext.request.contextPath}/boot/js/jquery-2.2.1.min.js"></script>
    <!--引入jqgrid核心js-->
    <script src="${pageContext.request.contextPath}/jqgrid/js/trirand/jquery.jqGrid.min.js"></script>
    <!--引入jqgrid国际化js-->
    <script src="${pageContext.request.contextPath}/jqgrid/js/trirand/i18n/grid.locale-cn.js"></script>
    <!--引入bootstrap组件js-->
    <script src="${pageContext.request.contextPath}/boot/js/bootstrap.min.js"></script>
    <script>
        function changeImg() {
            var captchaImg = document.getElementById('imgVcode');
            console.log(captchaImg);
            captchaImg.src='${pageContext.request.contextPath}/captcha/captcha?'+Math.random();
        }
        function login() {
            $.ajax({
                url:"${pageContext.request.contextPath}/admin/login",
                type:"POST",
                datatype:"JSON",
                data:$("#loginForm").serialize(),
                success:function (data) {
                    if (data!=null&data!=""){
                        $("#msg").html("<span class='error'>"+data+"</span>");
                    }else {
                        location.href = "${pageContext.request.contextPath}/back/main.jsp";
                    }
                }

            })
        }
    </script>
</head>
<body style=" background: url(${pageContext.request.contextPath}/img/1.jpg)no-repeat;background-size: 100%;">


<div class="modal-dialog" style="margin-top: 10%;background-color:#000;filter:alpha(opacity=50);opacity:0.8">
    <div class="modal-content">
        <div class="modal-header">
            <h4 class="modal-title text-center" id="myModalLabel">持明法洲</h4>
        </div>
        <form id="loginForm" method="post" action="javascript:;">
        <div class="modal-body" id = "model-body">
            <div class="form-group">
                <input type="text" class="form-control"placeholder="用户名" autocomplete="off" name="username">
            </div>
            <div class="form-group">
                <input type="password" class="form-control" placeholder="密码" autocomplete="off" name="password">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder="验证码" autocomplete="off" name="captcha">
            </div>
            <div class="form-group">
                验证码:
                <img id="imgVcode" src="${pageContext.request.contextPath}/captcha/captcha" onClick="changeImg()" />
                <a href="javascript:void(0);" onClick="changeImg()">换一张</a>
            </div>
            <span id="msg" style="color:red;"></span>
        </div>
        <div class="modal-footer">
            <div class="form-group">
                <button type="button" class="btn btn-primary form-control" id="log" onclick="login()">登录</button>
            </div>
        </div>
        </form>
    </div>
</div>
</body>
</html>
