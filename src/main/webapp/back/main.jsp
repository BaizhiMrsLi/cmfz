<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
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
    <!--引入ajaxfileupload.js-->
    <script src="${pageContext.request.contextPath}/boot/js/ajaxfileupload.js"></script>
    <%--    引入富文本编辑器的核心js和中文支持--%>
    <script src="${pageContext.request.contextPath}/kindeditor/kindeditor-all-min.js"></script>
    <script src="${pageContext.request.contextPath}/kindeditor/lang/zh-CN.js"></script>

    <!-- 引入 ECharts 文件 -->
    <script src="${pageContext.request.contextPath}/echarts/echarts.min.js"></script>
    <script src="${pageContext.request.contextPath}/echarts/china.js"></script>

    <%--    引入goeasy--%>
    <script type="text/javascript" src="http://cdn.goeasy.io/goeasy-1.0.3.js"></script>

    <script>

        //安全退出
        function safeOut() {
            $.ajax({
                url: "${pageContext.request.contextPath}/admin/safeOut",
                type: "POST",
                datatype: "JSON",
                success: function (data) {
                    location.href = "${pageContext.request.contextPath}/back/login.jsp";
                }
            })
        }
        //模态框的富文本编辑器的初始化
        KindEditor.ready(function(K) {
            window.editor = K.create('#editor_id',{
                width:'800px',
                // 1. 指定图片上传路径
                uploadJson:"${pageContext.request.contextPath}/article/uploadImg",
                allowFileManager:true,
                fileManagerJson:"${pageContext.request.contextPath}/article/showAllImgs",
                //失去焦点，保存内容
                afterBlur:function () {
                    this.sync();
                }
            });
        });
        // 1. 获取上师信息 在表单回显
        $.get("${pageContext.request.contextPath}/guru/showGuruList",function (data) {
            var option = "<option value='0'>通用文章</option>";
            console.log(data);
            data.forEach(function (guru) {
                option += "<option value='"+guru.id+"'>"+guru.nickname+"</option>"
            });
            $("#guruList").html(option);
        },"json");

    </script>
</head>


<body>

<%--导航--%>
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="javascript:;">持明法洲后台管理系统</a>
        </div>
        <div>
            <!--向右对齐-->
            <a href="javascript:;" class="navbar-text navbar-right" onclick="safeOut()" style="text-decoration: none">退出登录&nbsp;&nbsp;&nbsp;&nbsp;</a>
            <p class="navbar-text navbar-right">欢迎-<span style="color: red;"><shiro:principal></shiro:principal></span>!
            </p>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <div class="row">
        <%--        左侧面板折叠部分--%>
        <div class="col-md-2">
            <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseOne">
                                用户管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/user.jsp');"
                                   class="list-group-item">
                                    用户列表
                                </a>
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/userRegist.jsp');"
                                   class="list-group-item">
                                    用户注册趋势图
                                </a>
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/userMap.jsp');"
                                   class="list-group-item">
                                    地理分布图
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseTwo">
                                上师管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/guru.jsp');" class="list-group-item">
                                    上师列表
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseThree">
                                文章管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/article.jsp');" class="list-group-item">
                                    文章列表
                                </a>
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/esarticle.jsp');"
                                   class="list-group-item">
                                    文章搜索
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseFour">
                                专辑管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFour" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/album.jsp');" class="list-group-item">
                                    专辑列表
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a data-toggle="collapse" data-parent="#accordion"
                               href="#collapseFive">
                                轮播图管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseFive" class="panel-collapse collapse">
                        <div class="panel-body">
                            <div class="list-group">
                                <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/banner.jsp');" class="list-group-item">
                                    轮播组图片列表
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <shiro:hasRole name="superadmin">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordion"
                                   href="#collapseSix">
                                    管理员管理
                                </a>
                            </h4>
                        </div>
                        <div id="collapseSix" class="panel-collapse collapse">
                            <div class="panel-body">
                                <div class="list-group">
                                    <a href="javascript:$('#content').load('${pageContext.request.contextPath}/back/admin.jsp');"
                                       class="list-group-item">
                                        管理员列表
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </shiro:hasRole>
            </div>
        </div>
        <%--        右侧部分--%>
        <div id="content">
            <div class="col-md-10">
                <%--            巨幕--%>
                <div class="jumbotron">
                    <h3>欢迎使用持明法洲后台管理系统!</h3>
                </div>
                <%--            轮播图--%>
                <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                        <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                    </ol>

                    <!-- Wrapper for slides -->
                    <div class="carousel-inner" role="listbox">
                        <div class="item active">
                            <img src="${pageContext.request.contextPath}/img/3e4d03381f30e924eebbff0d40086e061d95f7b0.jpg"
                                 alt="...">
                            <div class="carousel-caption">
                                ...
                            </div>
                        </div>
                        <div class="item">
                            <img src="${pageContext.request.contextPath}/img/009e9dfd5266d016d30938279a2bd40735fa3576.jpg"
                                 alt="...">
                            <div class="carousel-caption">
                                ...
                            </div>
                        </div>
                        <div class="item">
                            <img src="${pageContext.request.contextPath}/img/980186345982b2b7bcce9fcb3cadcbef76099b35.jpg"
                                 alt="...">
                            <div class="carousel-caption">
                                ...
                            </div>
                        </div>
                    </div>

                    <!-- Controls -->
                    <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
<%--页面底部--%>
<div class="panel-footer">
    <h4 style="text-align: center">百知教育</h4>
</div>

<%--模态框--%>
<div class="modal fade" id="kind" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">文章信息</h4>
            </div>
            <div class="modal-body">
                <form role="form" enctype="multipart/form-data" id="kindfrm">
                    <input name="id" type="hidden" id="formid">
                    <div class="form-group">
                        <label for="name">标题</label>
                        <input type="text" class="form-control" name="title" id="name" placeholder="请输入名称">
                    </div>
                    <div class="form-group">
                        <label for="aa">封面上传</label>
                        <input type="file" name="aa" id="aa">
                    </div>
                    <div class="form-group">
                        <label for="guruList">所属上师</label>
                        <select class="form-control" id="guruList" name="guru_id" >

                        </select>
                    </div>
                    <div class="form-group">
                        <label for="status">文章状态</label>
                        <select class="form-control" id="status" name="status" >
                            <option value="0">冻结</option>
                            <option value="1">展示</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editor_id">内容</label>
                        <textarea id="editor_id" name="content" style="width:700px;height:300px;">
                            &lt;strong&gt;HTML内容&lt;/strong&gt;
                        </textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer" id="modal_foot">
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>
