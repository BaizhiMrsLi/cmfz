<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

<script>
    $(function () {
        // 1.表格初始化
        $('#adminlist').jqGrid({
            //必须先设置风格之后，才能显示出分页的小标签
            styleUI: "Bootstrap",//设置为bootstrap风格的表格
            url: "${pageContext.request.contextPath}/admin/findAll",
            mtype: "post",
            datatype: "json",
            colNames: ["ID", "账户", "角色"],
            colModel: [
                {name: "id", search: false, hidden: true},
                {
                    name: "username", editable: true, align: "center"
                },
                {
                    name: "roleIds", editable: true, edittype: "select",
                    editoptions: {
                        multiple: true,
                        dataUrl: "${pageContext.request.contextPath}/role/findAll"
                    },
                    formatter: function (cellvalue, options, rowObject) {
                        console.log(rowObject);
                        var values = '';
                        for (let i = 0; i < rowObject.roles.length; i++) {
                            values += rowObject.roles[i].role_name + ',';
                        }
                        return values;
                    }
                }
            ],
            pager: "#pager",//设置分页工具栏html
            // 注意: 1.一旦设置分页工具栏之后在根据指定url查询时自动向后台传递page(当前页) 和 rows(每页显示记录数)两个参数
            rowNum: 2,//这个代表每页显示记录数
            rowList: [2, 4, 6],//生成可以指定显示每页展示多少条下拉列表
            viewrecords: true,//显示总记录数
            editurl: "${pageContext.request.contextPath}/admin/opt",//开启编辑时执行编辑操作的url路径  添加  修改  删除
            autowidth: true,//自适应外部容器
            multiselect: true,//复选框
            height: 400,
            caption: "管理员列表"
        }).navGrid(
            "#pager",//参数1: 当前分页工具栏
            {edit: true, add: false, del: false},//开启编辑操作，就是增删改查的小工具，是否开启哪一个(默认值为true)
            {
                //控制修改操作
                closeAfterEdit: true, editCaption: "编辑信息", reloadAfterSubmit: true,
            },
            {
                //控制添加操作
                closeAfterAdd: true, addCaption: "添加", reloadAfterSubmit: true,
            },
            {closeAfterDel: true, delCaption: "删除一条信息", reloadAfterSubmit: true},//对删除时的配置对象
            {
                sopt: ['eq', 'ne', 'cn']
            },//对搜索时的配置对象
            {}//pView
        );//开启增删改工具按钮  注意:1.这里存在一个bug surl未实现
    });


</script>


<!--右侧显示数据部分-->
<div class="col-md-10" style="margin-top: -30px;">
    <div class="page-header">
        <h1>管理员列表</h1>
    </div>
    <!--    创建表格-->
    <table id="adminlist"></table>
    <!--创建工具-->
    <div id="pager"></div>
</div>
