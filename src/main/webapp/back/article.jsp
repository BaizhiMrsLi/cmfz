<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>


<script>
    $(function () {
        // 1.表格初始化
        $('#articlelist').jqGrid({
            //必须先设置风格之后，才能显示出分页的小标签
            styleUI: "Bootstrap",//设置为bootstrap风格的表格
            url: "${pageContext.request.contextPath}/article/findAll",
            mtype: "post",
            datatype: "json",
            colNames: ["ID","文章名", "图片", "作者", "上传时间" , "发布时间", "状态", "上师","文章","操作"],
            colModel: [
                {name: "id", search: false,hidden:true},
                {
                    name: "name", editable: true,align:"center"
                },
                {
                    name: "aa", editable: true,align:"center",
                    edittype:"file",editoptions: {enctype: "multipart/form-data"},
                    formatter:function (value,options,row) {
                        return '<img style="height: 50px;width:100px;" src="' + row.cover + '"/>';
                    }
                },
                {name: "author", editable: true,align:"center"},
                {name: "create_date",align:"center"},
                {name: "publish_date",align:"center"},
                {name: "status", editable: true,align:"center",formatter:function (data) {
                        if(data == "1"){
                            return "展示";
                        }else return "冻结";
                    },edittype: "select",editoptions: {value:"1:展示;2:冻结"}
                },
                {name: "guru_id", search: false,hidden:true},
                {name: "content", search: false,hidden:true},
                {
                    name :"caozuo",align:"center",formatter:function (value,options,row) {
                        var result = '';
                        result += "<a href='javascript:void(0)' onclick=\"showModel('" + row.id + "')\" class='btn btn-lg' title='查看详情'> <span class='glyphicon glyphicon-th-list'></span></a>";
                        return result;
                    }
                }
            ],
            pager: "#pager",//设置分页工具栏html
            // 注意: 1.一旦设置分页工具栏之后在根据指定url查询时自动向后台传递page(当前页) 和 rows(每页显示记录数)两个参数
            rowNum: 5,//这个代表每页显示记录数
            rowList: [5, 10, 15],//生成可以指定显示每页展示多少条下拉列表
            viewrecords: true,//显示总记录数
            editurl: "${pageContext.request.contextPath}/article/opt",//开启编辑时执行编辑操作的url路径  添加  修改  删除
            autowidth: true,//自适应外部容器
            multiselect:true,//复选框
            height: 400,
        }).navGrid(
            "#pager",//参数1: 当前分页工具栏
            {edit : false, add : false,},//开启编辑操作，就是增删改查的小工具，是否开启哪一个(默认值为true)
            {},//控制修改操作
            {},//添加操作
            {closeAfterDel: true, delCaption: "删除一条信息", reloadAfterSubmit: true},//对删除时的配置对象
            {},//对搜索时的配置对象
            {}//pView
        );//开启增删改工具按钮  注意:1.这里存在一个bug surl未实现
    });

    // 编辑文章
    function showModel(id) {
        // 返回指定行的数据，返回数据类型为name:value，name为colModel中的名称，value为所在行的列的值，如果根据rowid找不到则返回空。在编辑模式下不能用此方法来获取数据，它得到的并不是编辑后的值
        var data = $("#articlelist").jqGrid("getRowData",id);
        $("#name").val(data.name);
        if(data.status == "冻结"){
            $("#status").val(0);
        }else{
            $("#status").val(1);
        }
        $("#formid").val(data.id);
        $("#guruList").val(data.guru_id);
        // KindEditor 中的赋值方法 参数1: kindeditor文本框 的id
        KindEditor.html("#editor_id",data.content);
        //防止未触发富文本框的失去焦点事件，失去富文本框内容。
        editor.sync();
        $("#modal_foot").html("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">关闭</button>"+
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"updateArticle()\">修改</button>")
        $("#kind").modal("show");
    }
    function updateArticle() {
        $.ajaxFileUpload({
            url:"${pageContext.request.contextPath}/article/updateArticle",
            datatype:"json",
            type:"post",
            fileElementId:"aa",
            // ajaxFileUpload 不支持序列化数据上传id=111&&title="XXX"
            //                只支持 Json格式上传数据
            // 解决方案 : 1.更改 ajaxFileUpload 源码 2. 手动封装Json格式
            data:{id:$("#formid").val(),name:$("#name").val(),guru_id:$("#guruList").val(),status:$("#status").val(),content:$("#editor_id").val()},
            success:function (data) {
                $("#articlelist").trigger("reloadGrid");
                $("#kind").modal("hide");
            }
        })
    }

    // 打开模态框
    function showAddArticle() {
        // 清除表单内数据
        $("#kindfrm")[0].reset();
        // kindeditor 提供的数据回显方法  通过"" 将内容设置为空串
        KindEditor.html("#editor_id", "");
        // 未提供查询上师信息 发送ajax请求查询
        $("#modal_foot").html("<button type=\"button\" class=\"btn btn-danger\" data-dismiss=\"modal\">关闭</button>"+
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"addArticle()\">添加</button>")
        $("#kind").modal("show");
    }
    // 添加文章
    function addArticle() {
        $.ajaxFileUpload({
            url:"${pageContext.request.contextPath}/article/addArticle",
            datatype:"json",
            type:"post",
            fileElementId:"aa",
            // ajaxFileUpload 不支持序列化数据上传id=111&&title="XXX"
            //                只支持 Json格式上传数据
            // 解决方案 : 1.更改 ajaxFileUpload 源码 2. 手动封装Json格式
            data:{id:$("#formid").val(),name:$("#name").val(),guru_id:$("#guruList").val(),status:$("#status").val(),content:$("#editor_id").val()},
            success:function (data) {
                $("#articlelist").trigger("reloadGrid");
                $("#kind").modal("hide");
            }
        })
    }


</script>


<!--右侧显示数据部分-->
<div class="col-md-10" style="margin-top: -30px;">
    <div class="page-header">
        <h1>文章列表</h1>
    </div>
    <div>
        <!-- Nav tabs -->
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">文章列表</a></li>
            <li role="presentation"><a href="javascript:;" onclick="showAddArticle()">文章添加</a></li>
        </ul>
        <!-- Tab panes -->
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="home">
                <!--    创建表格-->
                <table id="articlelist"></table>
                <!--创建工具-->
                <div id="pager"></div>
            </div>
        </div>

    </div>
</div>

