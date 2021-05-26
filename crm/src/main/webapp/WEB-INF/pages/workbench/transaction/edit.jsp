<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    //http://127.0.0.1:8080/crm/
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //给"阶段"下拉框添加change事件
            $("#edit-transactionStage").change(function () {
                //收集参数
                //var stageValue=$("#edit-transactionStage option:selected").text();
                var stageValue = $("#edit-transactionStage").find("option:selected").text();
                //表单验证
                if (stageValue == "") {
                    //清空可能性
                    $("#edit-possibility").val("");
                    return;
                }
                //发送请求
                $.ajax({
                    url: 'workbench/transaction/getPossibilityByStageValue.do',
                    data: {
                        stageValue: stageValue
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        $("#edit-possibility").val(data);
                    }
                });
            });

            //当页面加载完成之后，对容器调用工具函数
            var name2id = {};
            $("#edit-accountName").typeahead({
                //source:['阿里巴巴','京东商城','腾讯','动力节点','字节跳动']
                source: function (query, process) {//每次键盘弹起，都会执行本函数；可以在函数中生成一个数据源(字符串数组)，交给source参数使用；source每次拿到数据源(字符串数组)，都会根据关键字比对,比对成功的字符串都会列容器下方，从而实现自动补全。
                    //query：是用户在容器中输入的关键字
                    //process：是一个bs_typeahead插件提供的函数，能够把一个json的字符串数组交给source使用。
                    //向后台发送异步请求，查询客户名称组成字符串数组，以json字符串的形式返回，交给source使用
                    $.ajax({
                        url: 'workbench/transaction/queryCustomerByName.do',
                        data: {
                            customerName: query
                        },
                        type: 'post',
                        dataType: 'json',
                        success: function (data) {//data就是json的字符串数组
                            var customerNameArr = [];
                            //遍历data复杂类型数组
                            $.each(data, function (index, obj) {
                                //生成简单类型数组
                                customerNameArr.push(obj.name);
                                //把obj的name和id赋值给name2id，把name作为name2id属性名，id作为name2id的属性值
                                name2id[obj.name] = obj.id;
                                $("#edit-customerId").val(null);
                            });
                            process(customerNameArr);
                        }
                    });
                },
                afterSelect: function (item) {//用户选中一项之后，自动触发本函数；
                    //item：选中项，补全之后的名字
                    //alert(name2id[item]);
                    $("#edit-customerId").val(name2id[item]);
                }
            });

            //当容器加载完成，对容器调用工具函数
            $(".mydate").datetimepicker({
                language: 'zh-CN',//语言
                format: 'yyyy-mm-dd',//日期格式
                minView: 'month',//日期选择器上最小能选择的日期的视图
                initialDate: new Date(),// 日历的初始化显示日期，此处默认初始化当前系统时间
                autoclose: true,//选择日期之后，是否自动关闭日历
                todayBtn: true,//是否显示当前日期的按钮
                clearBtn: true,//是否显示清空按钮
            });


            //给"保存"按钮添加单击事件
            $("#saveEditTranBtn").click(function () {
                //收集参数
                var id = "${tran.id}";
                var owner = $("#edit-transactionOwner").val();
                var money = $.trim($("#edit-amountOfMoney").val());
                var name = $.trim($("#edit-transactionName").val());
                var expectedDate = $("#edit-expectedClosingDate").val();
                var customerName = $.trim($("#edit-accountName").val());
                var customerId = $("#edit-customerId").val();
                var stage = $("#edit-transactionStage").val();
                var type = $("#edit-transactionType").val();
                var source = $("#edit-clueSource").val();
                var activityId = $("#edit-activityId").val();
                var contactsId = $("#edit-contactsId").val();
                var description = $.trim($("#edit-describe").val());
                var contactSummary = $.trim($("#edit-contactSummary").val());
                var nextContactTime = $("#edit-nextContactTime").val();
                //表单验证(作业)
                // alert(customerName);
                // alert(customerId );

                //发送请求
                $.ajax({
                    url: 'workbench/transaction/saveEditTran.do',
                    data: {
                        id:id,
                        owner: owner,
                        money: money,
                        name: name,
                        expectedDate: expectedDate,
                        customerName: customerName,
                        customerId: customerId,
                        stage: stage,
                        type: type,
                        source: source,
                        activityId: activityId,
                        contactsId: contactsId,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //跳转到交易的主页面
                            window.location.href = "workbench/transaction/index.do";
                        } else {
                            //提示信息
                            alert(data.message);
                        }
                    }
                });
            });

            //给"市场活动源"按钮添加单击事件
            $("#searchActivityBtn").click(function () {
                //弹出"搜索市场活动'的模态窗口
                $("#findMarketActivity").modal("show");
                queryActivityForDetailByName("");
            });

            //给"市场活动源搜索框"添加键盘弹起事件
            $("#searchActivityText").keyup(function () {
                //收集参数
                var activityName = this.value;
                //发送请求
                queryActivityForDetailByName(activityName);
            });

            //给市场活动源所有的"单选按钮"添加单击事件
            $("#tBody1").on("click", "input[type='radio']", function () {
                //收集参数
                var id = this.value;
                var activityName = $(this).attr("activityName");
                //把id显示在隐藏域中，把activityName显示在输入框中
                $("#edit-activityId").val(id);
                $("#edit-activitySrc").val(activityName);
                //关闭搜索市场活动的模态窗口
                $("#findMarketActivity").modal("hide");
            });

            //给"联系人名称"按钮添加单击事件
            $("#searchContactsBtn").click(function () {
                //弹出"搜索市场活动'的模态窗口
                $("#findContacts").modal("show");
                queryContactsForDetailByName("");
            });

            //给"联系人名称搜索框"添加键盘弹起事件
            $("#searchContactsText").keyup(function () {
                //收集参数
                var contactsName = this.value;
                //发送请求
                queryContactsForDetailByName(contactsName);
            });

            //给联系人名称所有的"单选按钮"添加单击事件
            $("#tBody2").on("click", "input[type='radio']", function () {
                //收集参数
                var id = this.value;
                var contactsName = $(this).attr("contactsName");
                //把id显示在隐藏域中，把activityName显示在输入框中
                $("#edit-contactsId").val(id);
                $("#edit-contactsName").val(contactsName);
                //关闭搜索市场活动的模态窗口
                $("#findContacts").modal("hide");
            });

            //展示当前交易信息
            showTranValue();
        });

        function queryActivityForDetailByName(activityName) {
            $.ajax({
                url: 'workbench/clue/queryActivityForDetailByName.do',
                data: {
                    activityName: activityName
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //遍历data，显示每一条市场活动
                    var htmlStr = "";
                    $.each(data, function (index, obj) {
                        htmlStr += "<tr>";
                        htmlStr += "<td><input type=\"radio\" value=\"" + obj.id + "\" activityName=\"" + obj.name + "\" name=\"activity\"/></td>";
                        htmlStr += "<td>" + obj.name + "</td>";
                        htmlStr += "<td>" + obj.startDate + "</td>";
                        htmlStr += "<td>" + obj.endDate + "</td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "</tr>";
                    });
                    //把htmlStr显示在列表
                    $("#tBody1").html(htmlStr);
                }
            });
        }

        function queryContactsForDetailByName(contactsName) {
            $.ajax({
                url: 'workbench/transaction/queryContactsForDetailByName.do',
                data: {
                    fullName: contactsName
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //遍历data，显示每一个联系人
                    var htmlStr = "";
                    $.each(data, function (index, obj) {
                        htmlStr += "<tr>";
                        htmlStr += "<td><input type=\"radio\" value=\"" + obj.id + "\" contactsName=\"" + obj.fullName + "\" name=\"contacts\"/></td>";
                        htmlStr += "<td>" + obj.fullName + "</td>";
                        htmlStr += "<td>" + obj.email + "</td>";
                        htmlStr += "<td>" + obj.mphone + "</td>";
                        htmlStr += "</tr>";
                    });
                    //把htmlStr显示在列表
                    $("#tBody2").html(htmlStr);
                }
            });
        }
        function showTranValue() {
            $("#edit-transactionOwner").val("${tran.owner}");
            $("#edit-amountOfMoney").val("${tran.money}");
            $("#edit-transactionName").val("${tran.name}");
            $("#edit-expectedClosingDate").val("${tran.expectedDate}");
            $("#edit-accountName").val("${tranDetail.customerId}");
            $("#edit-customerId").val("${tran.customerId}");
            $("#edit-transactionStage").val("${tran.stage}");
            $("#edit-possibility").val("${tranDetail.possibility}");
            $("#edit-transactionType").val("${tran.type}");
            $("#edit-clueSource").val("${tran.source}");
            $("#edit-activityId").val("${tran.activityId}");
            $("#edit-activitySrc").val("${tranDetail.activityId}");
            $("#edit-contactsId").val("${tran.contactsId}");
            $("#edit-contactsName").val("${tranDetail.contactsId}");
            $("#edit-describe").val("${tran.description}");
            $("#edit-contactSummary").val("${tran.contactSummary}");
            $("#edit-nextContactTime").val("${tran.nextContactTime}");
        }
    </script>
</head>
<body>

<!-- 查找市场活动 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input id="searchActivityText" type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                    </tr>
                    </thead>
                    <tbody id="tBody1">
                    <%--							<tr>--%>
                    <%--								<td><input type="radio" name="activity"/></td>--%>
                    <%--								<td>发传单</td>--%>
                    <%--								<td>2020-10-10</td>--%>
                    <%--								<td>2020-10-20</td>--%>
                    <%--								<td>zhangsan</td>--%>
                    <%--							</tr>--%>
                    <%--							<tr>--%>
                    <%--								<td><input type="radio" name="activity"/></td>--%>
                    <%--								<td>发传单</td>--%>
                    <%--								<td>2020-10-10</td>--%>
                    <%--								<td>2020-10-20</td>--%>
                    <%--								<td>zhangsan</td>--%>
                    <%--							</tr>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 查找联系人 -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input id="searchContactsText" type="text" class="form-control" style="width: 300px;"
                                   placeholder="请输入联系人名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="tBody2">
                    <%--							<tr>--%>
                    <%--								<td><input type="radio" name="activity"/></td>--%>
                    <%--								<td>李四</td>--%>
                    <%--								<td>lisi@bjpowernode.com</td>--%>
                    <%--								<td>12345678901</td>--%>
                    <%--							</tr>--%>
                    <%--							<tr>--%>
                    <%--								<td><input type="radio" name="activity"/></td>--%>
                    <%--								<td>李四</td>--%>
                    <%--								<td>lisi@bjpowernode.com</td>--%>
                    <%--								<td>12345678901</td>--%>
                    <%--							</tr>--%>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>修改交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button id="saveEditTranBtn" type="button" class="btn btn-primary">保存</button>
        <button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label for="edit-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-transactionOwner">
                <c:forEach items="${userList}" var="u">
                            <option value="${u.id}">${u.name}</option>
                </c:forEach>
            </select>
        </div>
        <label for="edit-amountOfMoney" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-amountOfMoney" >
        </div>
    </div>

    <div class="form-group">
        <label for="edit-transactionName" class="col-sm-2 control-label">名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-transactionName" >
        </div>
        <label for="edit-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control mydate" id="edit-expectedClosingDate" readonly="true" >
        </div>
    </div>

    <div class="form-group">
        <label for="edit-accountName" class="col-sm-2 control-label">客户名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="edit-customerId" >
            <input type="text" class="form-control" id="edit-accountName" placeholder="支持自动补全，输入客户不存在则新建" >
        </div>
        <label for="edit-transactionStage" class="col-sm-2 control-label">阶段<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-transactionStage">
                <option></option>
                <c:forEach items="${stageList}" var="s">
                            <option value="${s.id}">${s.value}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-transactionType">
                <option></option>
                <c:forEach items="${transactionTypeList}" var="tt">
                            <option value="${tt.id}">${tt.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="edit-possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-possibility" readonly >
        </div>
    </div>

    <div class="form-group">
        <label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-clueSource">
                <option></option>
                <c:forEach items="${sourceList}" var="so">
                            <option value="${so.id}">${so.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="edit-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a id="searchActivityBtn"
                                                                                         href="javascript:void(0);"
                                                                                         style="text-decoration: none;"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="edit-activityId" >
            <input type="text" class="form-control" id="edit-activitySrc" placeholder="点击左边搜索" readonly >
        </div>
    </div>

    <div class="form-group">
        <label for="edit-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a id="searchContactsBtn"
                                                                                          href="javascript:void(0);"
                                                                                          style="text-decoration: none;"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="edit-contactsId" >
            <input type="text" class="form-control" id="edit-contactsName" placeholder="点击左边搜索" readonly >
        </div>
    </div>

    <div class="form-group">
        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="edit-describe" ></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control mydate" id="edit-nextContactTime" readonly="true">
        </div>
    </div>

</form>
</body>
</html>