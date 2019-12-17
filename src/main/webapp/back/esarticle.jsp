<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

<script>
    function find() {
        if ($('#dic').val() != "") {
            $.post('${pageContext.request.contextPath}/article/findByName', 'dic=' + $('#dic').val(), function (data) {
                $('#contentValue').empty();
                let str = '';
                for (let i = 0; i < data.length; i++) {
                    str += '<div><ul><li><span style="color:blue;">题目：' + data[i].name + '</span></li><li>作者：' + data[i].author + '</li><li>文章：' + data[i].content + '</li><li>创建时间：' + data[i].create_date + '</li><li>发行时间：' + data[i].publish_date + '</li></ul></div>';
                }
                $('#contentValue').append(str);
            }, 'json');
        } else {
            alert('请您输入有效的值!');
        }
    }
</script>

<!--右侧显示数据部分-->
<div class="col-md-10">
    <div class="container">
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-md-2 control-label">检索文章</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" id="dic" placeholder="请输入...">
                        </div>
                        <div class="col-md-2">
                            <button type="button" onclick="find()" class="btn btn-default">检索</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="row" id="contentValue">
        </div>
    </div>
</div>