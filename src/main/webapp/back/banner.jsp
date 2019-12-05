<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

    <script>
        $(function () {
            // 1.表格初始化
            $('#bannerlist').jqGrid({
                //必须先设置风格之后，才能显示出分页的小标签
                styleUI: "Bootstrap",//设置为bootstrap风格的表格
                url: "${pageContext.request.contextPath}/banner/findAll",
                mtype: "post",
                datatype: "json",
                colNames: ["ID","图片名", "图片", "链接", "创建时间", "内容", "状态"],
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
                    {name: "link", editable: true,align:"center"},
                    {name: "create_date",align:"center"},
                    {name: "content", editable: true,align:"center"},
                    {name: "status", editable: true,align:"center",formatter:function (data) {
                            if(data == "1"){
                                return "展示";
                            }else return "冻结";
                        },edittype: "select",editoptions: {value:"1:展示;2:冻结"}
                    }
                ],
                pager: "#pager",//设置分页工具栏html
                // 注意: 1.一旦设置分页工具栏之后在根据指定url查询时自动向后台传递page(当前页) 和 rows(每页显示记录数)两个参数
                rowNum: 5,//这个代表每页显示记录数
                rowList: [5, 10, 15],//生成可以指定显示每页展示多少条下拉列表
                viewrecords: true,//显示总记录数
                editurl: "${pageContext.request.contextPath}/banner/opt",//开启编辑时执行编辑操作的url路径  添加  修改  删除
                autowidth: true,//自适应外部容器
                multiselect:true,//复选框
                height: 400,
                caption: "轮播组图片列表"
            }).navGrid(
                "#pager",//参数1: 当前分页工具栏
                {},//开启编辑操作，就是增删改查的小工具，是否开启哪一个(默认值为true)
                {
                    //控制修改操作
                    closeAfterEdit: true, editCaption: "编辑信息", reloadAfterSubmit: true,
                    afterSubmit: function (response,postData) {
                        var id = response.responseJSON.data;
                        var code = response.responseJSON.status;
                        console.log('11'+response);
                        console.log('22'+code);
                        if (code == "updateOk") {
                            $.ajaxFileUpload({
                                url: "${pageContext.request.contextPath}/banner/motifyUpload",//用于文件上传的服务器端请求地址
                                fileElementId: 'aa',    //文件上传空间的id属性  <input type="file" id="file" name="file" />
                                //dataType: 'json',       //返回值类型 一般设置为json
                                //type:'POST',
                                data: {id: id},
                                success: function () {
                                    $("#bannerlist").trigger("reloadGrid");
                                }
                            })
                            return "true";
                        }
                        return postData;
                    }
                },
                {
                    //控制添加操作
                    closeAfterAdd: true, addCaption: "添加", reloadAfterSubmit: true,
                    afterSubmit: function (response,postData) {
                        var id = response.responseJSON.data;
                        var code = response.responseJSON.status;
                        console.log(code);
                        if (code == "addOk") {
                            $.ajaxFileUpload({
                                url: "${pageContext.request.contextPath}/banner/upload",//用于文件上传的服务器端请求地址
                                fileElementId: 'aa',    //文件上传空间的id属性  <input type="file" id="file" name="file" />
                                //dataType: 'json',       //返回值类型 一般设置为json
                                //type:'POST',
                                data: {id: id},
                                success: function () {
                                    $("#bannerlist").trigger("reloadGrid");
                                }
                            })
                            return "true";
                        }
                        return postData;
                    }
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
            <h1>轮播组列表</h1>
        </div>
        <!--    创建表格-->
        <table id="bannerlist"></table>
        <!--创建工具-->
        <div id="pager"></div>
    </div>
