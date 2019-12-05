<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

<script>
    $(function () {
        // 1.表格初始化
        $('#albumlist').jqGrid({
            //必须先设置风格之后，才能显示出分页的小标签
            styleUI: "Bootstrap",//设置为bootstrap风格的表格
            url: "${pageContext.request.contextPath}/album/findAll",
            mtype: "post",
            datatype: "json",
            colNames: ["ID","专辑名", "图片", "作者", "播音", "内容", "出版时间","集数","评价","状态"],
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
                {name:"broadcast",editable:true,align:"center"},
                {name: "content", editable: true,align:"center"},
                {name: "create_date",align:"center"},
                {name: "count",align:"center"},
                {name: "score",editable: true,align:"center"},
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
            editurl: "${pageContext.request.contextPath}/album/opt",//开启编辑时执行编辑操作的url路径  添加  修改  删除
            autowidth: true,//自适应外部容器
            multiselect:true,//复选框
            height: 400,
            caption: "专辑列表",
            subGrid:true,
            subGridRowExpanded :function (subgrid_id,row_id) {
                addTable(subgrid_id,row_id);
            },
            subGridRowColapsed :function (subgrid_id,row_id) {
                
            }
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
                            url: "${pageContext.request.contextPath}/album/motifyUpload",//用于文件上传的服务器端请求地址
                            fileElementId: 'aa',    //文件上传空间的id属性  <input type="file" id="file" name="file" />
                            //dataType: 'json',       //返回值类型 一般设置为json
                            //type:'POST',
                            data: {id: id},
                            success: function () {
                                $("#albumlist").trigger("reloadGrid");
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
                            url: "${pageContext.request.contextPath}/album/upload",//用于文件上传的服务器端请求地址
                            fileElementId: 'aa',    //文件上传空间的id属性  <input type="file" id="file" name="file" />
                            //dataType: 'json',       //返回值类型 一般设置为json
                            //type:'POST',
                            data: {id: id},
                            success: function () {
                                $("#albumlist").trigger("reloadGrid");
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

    function addTable(subgrid_id,row_id) {
        var subgrid_table_id, pager_id;
        subgrid_table_id = subgrid_id + "_t";
        pager_id = "p_" + subgrid_table_id;
        $("#" + subgrid_id).html(
            "<table id='" + subgrid_table_id
            + "' class='scroll'></table><div id='"
            + pager_id + "' class='scroll'></div>");
        jQuery("#" + subgrid_table_id).jqGrid(
            {
                url : "${pageContext.request.contextPath}/chapter/findAll?album_id="+row_id,
                mtype: "post",
                datatype: "json",
                colNames: ["ID","标题", "大小", "时长", "上传时间", "音频名", "操作"],
                colModel: [
                    {name: "id", search: false,hidden:true},
                    {name: "name", editable: true,align:"center"},
                    {name: "size", align:"center"},
                    {name: "time",align:"center"},
                    {name: "create_date",align:"center"},
                    {
                        name: "videoName",align:"center",
                        formatter:function (value,options,row) {
                            if(row.cover!= null){
                                let videoname = row.cover;
                                let num = videoname.lastIndexOf('/')+1;
                                let name = videoname.substring(num);
                                let realnum = name.indexOf('_')+1;
                                let realname = name.substring(realnum);
                                return realname;
                            }
                            return '';
                        }
                    },
                    {
                        name: "bb", editable: true,align:"center",
                        edittype:"file",editoptions: {enctype: "multipart/form-data"},
                        formatter:function (value,options,row) {
                            var result = "";
                            result += "<a href='javascript:void(0)' onclick=\"playAudio('" + row.cover + "')\" class='btn btn-lg' title='播放'><span class='glyphicon glyphicon-play-circle'></span></a>";
                            result += "<a href='javascript:void(0)' onclick=\"downloadAudio('" + row.cover + "')\" class='btn btn-lg' title='下载'><span class='glyphicon glyphicon-download'></span></a>";
                            return result;
                        }
                    }
                ],
                pager : "#"+pager_id,
                // 注意: 1.一旦设置分页工具栏之后在根据指定url查询时自动向后台传递page(当前页) 和 rows(每页显示记录数)两个参数
                rowNum: 5,//这个代表每页显示记录数
                rowList: [5, 10, 15],//生成可以指定显示每页展示多少条下拉列表
                viewrecords: true,//显示总记录数
                editurl: "${pageContext.request.contextPath}/chapter/opt?album_id="+row_id,//开启编辑时执行编辑操作的url路径  添加  修改  删除
                autowidth: true,//自适应外部容器
                multiselect:true,//复选框
                styleUI:"Bootstrap",
                height: 200,
                caption: "音频章节列表",
            }).navGrid(
            "#"+pager_id,//参数1: 当前分页工具栏
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
                            url: "${pageContext.request.contextPath}/chapter/motifyUpload",//用于文件上传的服务器端请求地址
                            fileElementId: 'bb',    //文件上传空间的id属性  <input type="file" id="file" name="file" />
                            //dataType: 'json',       //返回值类型 一般设置为json
                            //type:'POST',
                            data: {id: id},
                            success: function () {
                                $("#" + subgrid_table_id).trigger("reloadGrid");
                                $("#albumlist").trigger("reloadGrid");
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
                            url: "${pageContext.request.contextPath}/chapter/upload",//用于文件上传的服务器端请求地址
                            fileElementId: 'bb',    //文件上传空间的id属性  <input type="file" id="file" name="file" />
                            //dataType: 'json',       //返回值类型 一般设置为json
                            //type:'POST',
                            data: {id: id},
                            success: function () {
                                $("#" + subgrid_table_id).trigger("reloadGrid");
                                $("#albumlist").trigger("reloadGrid");
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
        );//开启增删改工具按钮  注意:1.这里存在一个bug surl未实现;
        jQuery("#" + subgrid_table_id).jqGrid('navGrid',
            "#" + pager_id, {});
    }
    function playAudio(data) {
        $("#myModal").modal("show");
        $("#myaudio").attr("src",data);
    }
    function downloadAudio(data) {
        location.href = "${pageContext.request.contextPath}/chapter/down?url="+data;
    }

</script>


<!--右侧显示数据部分-->
<div class="col-md-10" style="margin-top: -30px;">
    <div class="page-header">
        <h1>专辑列表</h1>
    </div>
    <!--    创建表格-->
    <table id="albumlist"></table>
    <!--创建工具-->
    <div id="pager"></div>
</div>


<div class="modal fade" id="myModal" tabindex="-1" style="margin-top: 15%;margin-left: 40%;" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <audio src="" id="myaudio" controls></audio>
</div>

