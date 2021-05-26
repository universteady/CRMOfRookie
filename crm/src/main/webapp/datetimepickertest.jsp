<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <title>测试bs_datetimepicker插件</title>
    <script type="text/javascript">
        $(function () {
            //当页面加载完成，容器一定也已经加载完成，调用工具函数
            $("#birthday").datetimepicker({
                language:'zh-CN',//语言
                format:'yyyy-mm-dd',//日期格式
                minView:'month',//日期选择器上最小能选择的日期的视图
                initialDate:new Date(),// 日历的初始化显示日期，此处默认初始化当前系统时间
                autoclose:true,//选择日期之后，是否自动关闭日历
                todayBtn:true,//是否显示当前日期的按钮
                clearBtn:true//是否显示清空按钮
            });
        });
    </script>
</head>
<body>
<input type="text" id="birthday" readonly>
</body>
</html>
