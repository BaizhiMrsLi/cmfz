<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
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
        var goEasy = new GoEasy({
            host: 'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey: "BC-4cb2a7179c8e4b83a8200c6cc6094371", //替换为您的应用appkey
        });

        function subMessage() {
            let area = $('#area').val();
            goEasy.publish({
                channel: "cmfz", //替换为您自己的channel
                message: area //替换为您想要发送的消息内容
            });
        }

        goEasy.subscribe({
            channel: "cmfz", //替换为您自己的channel
            onMessage: function (message) {
                console.log("Channel:" + message.channel + " content:" + message.content);
                let a = '<p>' + message.content + '</p>';
                $('#tta').append(a);
            }
        });
    </script>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 800px;height:600px;margin: 60px auto;">
    <div name="tta" id="tta" style="width: 400px;height:300px;overflow-scrolling: auto;border: 1px solid black">

    </div>
    <br>
    <input type="text" id="area" size="40">&nbsp;&nbsp;&nbsp;<input type="button" value="提交" id="sub"
                                                                    onclick="subMessage()">
</div>


</body>
</html>
