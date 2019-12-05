<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

    <script>
        var goEasy = new GoEasy({
            host: 'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey: "BS-7a228529481340d7ad9f3e6a74753b1e", //替换为您的应用appkey
        });
        $(function () {
            $.post('${pageContext.request.contextPath}/user/regist',function (data) {
                myChart.setOption({
                    series:[
                        {
                            name: '男',
                            type: 'bar',
                            data: data.man
                        },{
                            name: '女',
                            type: 'bar',
                            data: data.woman
                        }
                    ]
                });
            },'json');
        });

        goEasy.subscribe({
            channel: "userRegist", //替换为您自己的channel
            onMessage: function (message) {
                console.log(message);
                var data = JSON.parse(message.content);
                myChart.setOption({
                    series:[
                        {
                            name: '男',
                            type: 'bar',
                            data: data.man
                        },{
                            name: '女',
                            type: 'bar',
                            data: data.woman
                        }
                    ]
                });
            }
        });
    </script>

<!--右侧显示数据部分-->
<div class="col-md-10" style="margin-top: -30px;">
    <div class="page-header">
        <h1>用户注册趋势图</h1>
    </div>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 800px;height:500px;"></div>

</div>

<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    // 指定图表的配置项和数据
    var option = {
        title: {
            text: '用户注册趋势图'
        },
        tooltip: {},
        legend: {
            data:['男','女']
        },
        xAxis: {
            data: ["1天内","1周内","1月内","1年内"]
        },
        yAxis: {},
        series: [{
            name: '男',
            type: 'bar',
            data: [5, 20, 36, 10]
        },{
        name: '女',
            type: 'bar',
            data: [5, 20, 36, 10]
    }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>

