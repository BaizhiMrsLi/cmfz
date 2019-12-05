<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>

    <script type="text/javascript">
        var goEasy1 = new GoEasy({
            host: 'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
            appkey: "BS-ef804554e8094cedb8bf934bd72fed9c", //替换为您的应用appkey
        });
        $(function () {
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('userMap'));
            var option = {
                title: {
                    text: '用户分布图',
                    subtext: '纯属虚构',
                    left: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: ['省份']
                },
                visualMap: {
                    left: 'left',
                    top: 'bottom',
                    text: ['高', '低'],           // 文本，默认为数值文本
                    calculable: true
                },
                toolbox: {
                    show: true,
                    orient: 'vertical',
                    left: 'right',
                    top: 'center',
                    feature: {
                        mark: {show: true},
                        dataView: {show: true, readOnly: false},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                series: [
                    {
                        name: '省份',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        data: [
                            //Math.round(Math.random()*1000)
                        ]
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

            $.post('${pageContext.request.contextPath}/user/userMap',function (data) {
                myChart.setOption({
                    series: [
                        {
                            name: '省份',
                            type: 'map',
                            mapType: 'china',
                            roam: false,
                            label: {
                                normal: {
                                    show: false
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            data: data
                        }
                    ]
                });
            },'json');
            goEasy1.subscribe({
                channel: "userMap", //替换为您自己的channel
                onMessage: function (message) {
                    console.log(message.content);
                    var dd = JSON.parse(message.content);
                    myChart.setOption({
                        series: [
                            {
                                name: '省份',
                                type: 'map',
                                mapType: 'china',
                                roam: false,
                                label: {
                                    normal: {
                                        show: false
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                data: dd
                            }
                        ]
                    });
                }
            });
        });

    </script>



<!--右侧显示数据部分-->
<div class="col-md-10" style="margin-top: -30px;">
    <div class="page-header">
        <h1>用户地理分布图</h1>
    </div>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="userMap" style="width: 800px;height:500px;"></div>

</div>