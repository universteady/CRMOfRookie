<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <!--引入JQUERY-->
    <script src="jquery/jquery-1.11.1-min.js"></script>
    <!-- 引入 ECharts 文件 -->
    <script src="jquery/echarts/echarts.min.js"></script>
    <title>演示Echarts插件  </title>
    <script type="text/javascript">
        $(function () {
            //当页面加载完成之后，发送异步请求，查询交易表中各个阶段的数据量
            $.ajax({
                url:'workbench/chart/activity/queryCostOfActivity.do',
                type:'post',
                dataType:'json',
                success:function (data) {
                    //对容器调用工具函数
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));
                    option = {
                        title: {
                            text: '市场活动花费图',
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}"
                        },
                        xAxis: {
                            type: 'category',
                            data: ['清仓大甩卖', '家具销售大会1', '家具销售大会2', '家具销售大会3', '家具销售大会4']
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            name:'数据量',
                            data:data,
                            type: 'bar'
                        }]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            });
        });
    </script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 800px;height:400px;"></div>
</body>
</html>
