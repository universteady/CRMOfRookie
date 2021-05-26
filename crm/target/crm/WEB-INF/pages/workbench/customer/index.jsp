<%--
  Created by IntelliJ IDEA.
  User: Magpie
  Date: 2021/5/13
  Time: 20:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">

        $(function(){

            //定制字段
            $("#definedColumns > li").click(function(e) {
                //防止下拉菜单消失
                e.stopPropagation();
            });

            //给"保存"按钮添加单击事件
            $("#saveCreateCustomerBtn").click(function () {
                //收集参数
                var owner = $("#create-customerOwner").val();
                var name = $.trim($("#create-customerName").val());
                var website = $.trim($("#create-website").val());
                var phone = $.trim($("#create-phone").val());
                var description = $.trim($("#create-describe").val());
                var contactSummary = $.trim($("#create-contactSummary").val());
                var nextContactTime = $("#create-nextContactTime").val();
                var address = $.trim($("#create-address1").val());
                //表单验证(作业)

                //发送请求
                $.ajax({
                    url: 'workbench/customer/saveCreateCustomer.do',
                    data: {
                        owner: owner,
                        name:name,
                        website: website,
                        phone: phone,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //关闭模态窗口
                            $("#createCustomerModal").modal("hide");
                            //刷新线索列表
                            //alert("刷新线索列表，显示第一页数据，保持每页显示条数不变");
                            queryCustomerForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                            $("#createCustomerModal").modal("show");
                        }
                    }
                });
            });

            //给"修改"按钮添加单击事件
            $("#editCustomerBtn").click(function(){
                //收集参数
                var chkedIds=$("#tBody input[type='checkbox']:checked");
                //表单验证
                if (chkedIds.size()==0){
                    alert("请选择要修改的记录");
                    return;
                }
                if (chkedIds.size()>1){
                    alert("一次只能修改一条记录");
                    return;
                }
                var id=chkedIds.get(0).value;
                $("#editCustomerModal").modal("show");
                //发送请求
                $.ajax({
                    url:'workbench/customer/editCustomer.do',
                    data:{
                        id:id
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        //把顾客的信息设置到form表单中
                        $("#edit-id").val(data.id);
                        $("#edit-customerOwner").val(data.owner);
                        $("#edit-customerName").val(data.name);
                        $("#edit-website").val(data.website);
                        $("#edit-phone").val(data.phone);
                        $("#edit-describe").val(data.description);
                        $("#create-contactSummary1").val(data.contactSummary);
                        $("#create-nextContactTime2").val(data.nextContactTime);
                        $("#create-address").val(data.address);
                    }
                });
            });

            //给"更新"按钮添加单击事件
            $("#saveEditCustomerBtn").click(function () {
                //收集参数
                var id = $("#edit-id").val();
                var owner = $("#edit-customerOwner").val();
                var name = $("#edit-customerName").val();
                var website = $("#edit-website").val();
                var phone = $("#edit-phone").val();
                var description = $("#edit-describe").val();
                var contactSummary = $("#create-contactSummary1").val();
                var nextContactTime = $("#create-nextContactTime2").val();
                var address = $("#create-address").val();
                //表单验证(作业)
                if(name==null || name == ""){
                    alert("姓名不能为空");
                    return;
                }

                //发送请求
                $.ajax({
                    url:'workbench/customer/saveEditCustomer.do',
                    data:{
                        id:id,
                        owner:owner,
                        name:name,
                        website:website,
                        phone:phone,
                        description:description,
                        contactSummary:contactSummary,
                        nextContactTime:nextContactTime,
                        address:address
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        if (data.code=="1"){
                            //关闭模态窗口
                            $("#editCustomerModal").modal("hide");
                            //刷新市场活动列表，保持页号和每页显示条数都不变
                            queryCustomerForPageByCondition($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        }else{
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                            $("#editCustomerModal").modal("show");
                        }
                    }
                });
            });

            //给"删除'按钮添加单击事件
            $("#deleteCustomerBtn").click(function () {
                //收集参数
                var chkedIds=$("#tBody input[type='checkbox']:checked");
                //表单验证
                if (chkedIds.size()==0){
                    alert("请选择要删除的记录");
                    return;
                }
                //遍历chkedIds，生成id=xx&id=xx&.....&id=xx
                var ids="";
                $.each(chkedIds,function () {
                    ids+="id="+this.value+"&";
                });
                //ids:id=xx&id=xx&.....&id=xx&
                ids=ids.substr(0,ids.length-1);

                if (window.confirm("确定删除吗？")){
                    //发送请求
                    $.ajax({
                        url:'workbench/customer/deleteCustomerByIds.do',
                        data:ids,
                        type:'post',
                        dataType:'json',
                        success:function (data) {
                            if (data.code=="1"){
                                //刷新市场活动列表，显示第一页数据，保持每页显示条数不变
                                queryCustomerForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                //提示信息
                                alert(data.message);
                            }
                        }
                    });
                }
            });

            //实现全选和取消全选(作业)
            $("#chked_all").click(function () {
                $("#tBody input[type='checkbox']").prop("checked",this.checked);
            });

            $("#tBody").on("click","input[type='checkbox']",function () {
                if ( $("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
                    $("#chked_all").prop("checked",true);
                }else{
                    $("#chked_all").prop("checked",false);
                }
            });

            //当容器加载完成，对容器调用工具函数
            $(".mydate").datetimepicker({
                language:'zh-CN',//语言
                format:'yyyy-mm-dd',//日期格式
                minView:'month',//日期选择器上最小能选择的日期的视图
                initialDate:new Date(),// 日历的初始化显示日期，此处默认初始化当前系统时间
                autoclose:true,//选择日期之后，是否自动关闭日历
                todayBtn:true,//是否显示当前日期的按钮
                clearBtn:true,//是否显示清空按钮
            });

            //当页面加载完成之后，显示所有数据的第一页，默认每页显示2条
            queryCustomerForPageByCondition(1, 2);

            //给"查询"按钮添加单击事件
            $("#queryCustomerBtn").click(function () {
                // alert(12345);
                //显示所有符合条件的数据的第一页，默认每页显示10条
                queryCustomerForPageByCondition(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
            });

        });

        function queryCustomerForPageByCondition(pageNo, pageSize) {
            //收集参数
            //var pageNo=1;
            //var pageSize=10;
            var name = $("#query-name").val();
            var owner = $("#query-owner").val();
            var phone = $("#query-phone").val();
            var website = $("#query-website").val();

            //发送请求
            $.ajax({
                url: 'workbench/customer/queryCustomerForPageByCondition.do',
                data: {
                    pageNo: pageNo,
                    pageSize: pageSize,
                    name: name,
                    owner: owner,
                    phone: phone,
                    website: website

                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //显示总条数。功能由pagination插件替代
                    //$("#totalRowsB").text(data.totalRows);
                    //遍历data.customerList,显示数据列表
                    var htmlStr = "";
                    $.each(data.customerList, function (index, obj) {
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\"  value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/customer/detailCustomer.do?id=" + obj.id + "'\">" + obj.name + "</a></td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.phone + "</td>";
                        htmlStr += "<td>" + obj.website + "</td>";
                        htmlStr += "</tr>";
                    });

                    //把htmlStr显示在tbody
                    $("#tBody").html(htmlStr);

                    //计算总页数
                    var totalPages = 1;
                    if (data.totalRows % pageSize == 0) {
                        totalPages = data.totalRows / pageSize;
                    } else {
                        totalPages = parseInt(data.totalRows / pageSize) + 1;
                    }

                    //显示翻页信息
                    $("#demo_pag1").bs_pagination({
                        currentPage: pageNo,//当前页

                        rowsPerPage: pageSize,//每页显示条数
                        totalRows: data.totalRows,//总条数
                        totalPages: totalPages,//总页数

                        visiblePageLinks: 5,//显示的翻页卡片数

                        showGoToPage: true,//是否显示"跳转到第几页"
                        showRowsPerPage: true,//是否显示"每页显示条数"
                        showRowsInfo: true,//是否显示"记录的信息"

                        //每次切换页号都会自动触发此函数，函数能够返回切换之后的页号和每页显示条数
                        onChangePage: function (e, pageObj) { // returns page_num and rows_per_page after a link has clicked
                            //alert(pageObj.currentPage);
                            //alert(pageObj.rowsPerPage);
                            queryCustomerForPageByCondition(pageObj.currentPage, pageObj.rowsPerPage);

                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

<!-- 创建客户的模态窗口 -->
<div class="modal fade" id="createCustomerModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建客户</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-customerOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-customerName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
                        </div>
                    </div>
                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control mydate" id="create-nextContactTime" readonly="true">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address1" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address1"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="saveCreateCustomerBtn" type="button" class="btn btn-primary" data-dismiss="modal">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改客户的模态窗口 -->
<div class="modal fade" id="editCustomerModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改客户</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">

                    <div class="form-group">
                        <label for="edit-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-customerOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-customerName" value="动力节点">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                        </div>
                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-phone" value="010-84846003">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-contactSummary1" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary1"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="create-nextContactTime2" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control mydate" id="create-nextContactTime2" readonly="true">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address">北京大兴大族企业湾</textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="saveEditCustomerBtn" type="button" class="btn btn-primary" >更新</button>
            </div>
        </div>
    </div>
</div>




<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>客户列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input id="query-name" class="form-control" type="text">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input id="query-owner" class="form-control" type="text">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input id="query-phone" class="form-control" type="text">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司网站</div>
                        <input id="query-website" class="form-control" type="text">
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryCustomerBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createCustomerModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
                <button id="editCustomerBtn" type="button" class="btn btn-default" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
                <button id="deleteCustomerBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="chked_all"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>公司座机</td>
                    <td>公司网站</td>
                </tr>
                </thead>
                <tbody id="tBody">
<%--                <tr>--%>
<%--                    <td><input type="checkbox" /></td>--%>
<%--                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点</a></td>--%>
<%--                    <td>zhangsan</td>--%>
<%--                    <td>010-84846003</td>--%>
<%--                    <td>http://www.bjpowernode.com</td>--%>
<%--                </tr>--%>
<%--                <tr class="active">--%>
<%--                    <td><input type="checkbox" /></td>--%>
<%--                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点</a></td>--%>
<%--                    <td>zhangsan</td>--%>
<%--                    <td>010-84846003</td>--%>
<%--                    <td>http://www.bjpowernode.com</td>--%>
<%--                </tr>--%>
                </tbody>
            </table>
            <div id="demo_pag1">12345</div>
        </div>

<%--        <div style="height: 50px; position: relative;top: 30px;">--%>
<%--            <div>--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>--%>
<%--            </div>--%>
<%--            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--                <div class="btn-group">--%>
<%--                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--                        10--%>
<%--                        <span class="caret"></span>--%>
<%--                    </button>--%>
<%--                    <ul class="dropdown-menu" role="menu">--%>
<%--                        <li><a href="#">20</a></li>--%>
<%--                        <li><a href="#">30</a></li>--%>
<%--                    </ul>--%>
<%--                </div>--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--            </div>--%>
<%--            <div style="position: relative;top: -88px; left: 285px;">--%>
<%--                <nav>--%>
<%--                    <ul class="pagination">--%>
<%--                        <li class="disabled"><a href="#">首页</a></li>--%>
<%--                        <li class="disabled"><a href="#">上一页</a></li>--%>
<%--                        <li class="active"><a href="#">1</a></li>--%>
<%--                        <li><a href="#">2</a></li>--%>
<%--                        <li><a href="#">3</a></li>--%>
<%--                        <li><a href="#">4</a></li>--%>
<%--                        <li><a href="#">5</a></li>--%>
<%--                        <li><a href="#">下一页</a></li>--%>
<%--                        <li class="disabled"><a href="#">末页</a></li>--%>
<%--                    </ul>--%>
<%--                </nav>--%>
<%--            </div>--%>
<%--        </div>--%>

    </div>

</div>
</body>
</html>
