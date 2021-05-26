<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
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
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">

        $(function () {
            //给"保存"按钮添加单击事件
            $("#saveCreateClueBtn").click(function () {
                //收集参数
                var owner = $("#create-clueOwner").val();
                var company = $.trim($("#create-company").val());
                var appellation = $("#create-appellation").val();
                var fullName = $.trim($("#create-fullName").val());
                var job = $.trim($("#create-job").val());
                var email = $.trim($("#create-email").val());
                var phone = $.trim($("#create-phone").val());
                var website = $.trim($("#create-website").val());
                var mphone = $.trim($("#create-mphone").val());
                var state = $("#create-state").val();
                var source = $("#create-source").val();
                var description = $.trim($("#create-description").val());
                var contactSummary = $.trim($("#create-contactSummary").val());
                var nextContactTime = $("#create-nextContactTime").val();
                var address = $.trim($("#create-address").val());
                //表单验证(作业)

                //发送请求
                $.ajax({
                    url: 'workbench/clue/saveCreateClue.do',
                    data: {
                        fullName: fullName,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
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
                            $("#createClueModal").modal("hide");
                            //刷新线索列表
                            //alert("刷新线索列表，显示第一页数据，保持每页显示条数不变");
                            queryClueForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                            $("#createClueModal").modal("show");
                        }
                    }
                });
            });

            //给"修改"按钮添加单击事件
            $("#editClueBtn").click(function(){
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
                $("#editClueModal").modal("show");
                //发送请求
                $.ajax({
                    url:'workbench/clue/editClue.do',
                    data:{
                        id:id
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        //把市场活动的信息设置到form表单中
                        $("#edit-id").val(data.id);
                        $("#edit-clueOwner").val(data.owner);
                        $("#edit-company").val(data.company);
                        $("#edit-call").val(data.appellation);
                        $("#edit-surname").val(data.fullName);
                        $("#edit-job").val(data.job);
                        $("#edit-email").val(data.email);
                        $("#edit-phone").val(data.phone);
                        $("#edit-website").val(data.website);
                        $("#edit-mphone").val(data.mphone);
                        $("#edit-status").val(data.state);
                        $("#edit-source").val(data.source);
                        $("#edit-describe").val(data.description);
                        $("#edit-contactSummary").val(data.contactSummary);
                        $("#edit-nextContactTime").val(data.nextContactTime);
                        $("#edit-address").val(data.address);
                    }
                });
            });

            //给"更新"按钮添加单击事件
            $("#saveEditClueBtn").click(function () {
                //收集参数
                var id = $("#edit-id").val();
                var owner = $("#edit-clueOwner").val();
                var company = $("#edit-company").val();
                var appellation = $("#edit-call").val();
                var fullName = $("#edit-surname").val();
                var job = $("#edit-job").val();
                var email = $("#edit-email").val();
                var phone = $("#edit-phone").val();
                var website = $("#edit-website").val();
                var mphone = $("#edit-mphone").val();
                var state = $("#edit-status").val();
                var source = $("#edit-source").val();
                var description = $("#edit-describe").val();
                var contactSummary = $("#edit-contactSummary").val();
                var nextContactTime = $("#edit-nextContactTime").val();
                var address = $("#edit-address").val();
                //表单验证(作业)
                if(company==null || company == ""){
                    alert("公司不能为空");
                    return;
                }
                if(fullName==null || fullName == ""){
                    alert("姓名不能为空");
                    return;
                }

                //发送请求
                $.ajax({
                    url:'workbench/clue/saveEditClue.do',
                    data:{
                        id:id,
                        owner:owner,
                        company:company,
                        appellation:appellation,
                        fullName:fullName,
                        job:job,
                        email:email,
                        phone:phone,
                        website:website,
                        mphone:mphone,
                        state:state,
                        source:source,
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
                            $("#editClueModal").modal("hide");
                            //刷新市场活动列表，保持页号和每页显示条数都不变
                            queryClueForPageByCondition($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        }else{
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                            $("#editClueModal").modal("show");
                        }
                    }
                });
            });

            //给"删除'按钮添加单击事件
            $("#deleteClueBtn").click(function () {
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
                        url:'workbench/clue/deleteClueByIds.do',
                        data:ids,
                        type:'post',
                        dataType:'json',
                        success:function (data) {
                            if (data.code=="1"){
                                //刷新市场活动列表，显示第一页数据，保持每页显示条数不变
                                queryClueForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
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

            //当页面加载完成之后，显示所有数据的第一页，默认每页显示2条
            queryClueForPageByCondition(1, 2);

            //给"查询"按钮添加单击事件
            $("#queryClueBtn").click(function () {
                // alert(12345);
                //显示所有符合条件的数据的第一页，默认每页显示10条
                queryClueForPageByCondition(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
            });


        });

        function queryClueForPageByCondition(pageNo, pageSize) {
            //收集参数
            //var pageNo=1;
            //var pageSize=10;
            var fullName = $("#query-fullName").val();
            var company = $("#query-company").val();
            var mphone = $("#query-mphone").val();
            var phone = $("#query-phone").val();
            var source = $("#query-source").val();
            var owner = $("#query-owner").val();
            var state = $("#query-state").val();
            //发送请求
            $.ajax({
                url: 'workbench/clue/queryClueForPageByCondition.do',
                data: {
                    pageNo: pageNo,
                    pageSize: pageSize,
                    fullName: fullName,
                    company: company,
                    mphone: mphone,
                    phone: phone,
                    source: source,
                    owner: owner,
                    state: state
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //显示总条数。功能由pagination插件替代
                    //$("#totalRowsB").text(data.totalRows);
                    //遍历data.clueList,显示数据列表
                    var htmlStr = "";
                    $.each(data.clueList, function (index, obj) {
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\"  value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/detailClue.do?id=" + obj.id + "'\">" + obj.fullName + "</a></td>";
                        htmlStr += "<td>" + obj.company + "</td>";
                        htmlStr += "<td>" + obj.phone + "</td>";
                        htmlStr += "<td>" + obj.mphone + "</td>";
                        htmlStr += "<td>" + obj.source + "</td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.state + "</td>";
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
                            queryClueForPageByCondition(pageObj.currentPage, pageObj.rowsPerPage);

                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

<!-- 创建线索的模态窗口 -->
<div class="modal fade" id="createClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">创建线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-clueOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-appellation">
                                <option></option>
                                <c:forEach items="${appellationList}" var="a">
                                    <option value="${a.id}">${a.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-fullName" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-fullName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                        <label for="create-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-state">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="cs">
                                    <option value="${cs.id}">${cs.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="s">
                                    <option value="${s.id}">${s.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">线索描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
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
                                <input type="text" class="form-control" id="create-nextContactTime">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="saveCreateClueBtn" type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改线索的模态窗口 -->
<div class="modal fade" id="editClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-clueOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-company" value="动力节点">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-call" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-call">
                                <option></option>
                                <c:forEach items="${appellationList}" var="a">
                                    <option value="${a.id}">${a.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-surname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-surname" value="李四">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-job" value="CTO">
                        </div>
                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-phone" value="010-84846003">
                        </div>
                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-website"
                                   value="http://www.bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-mphone" value="12345678901">
                        </div>
                        <label for="edit-status" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-status">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="cs">
                                    <option value="${cs.id}">${cs.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="s">
                                    <option value="${s.id}">${s.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-nextContactTime" value="2017-05-01">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="saveEditClueBtn" type="button" class="btn btn-primary">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>线索列表</h3>
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
                        <input class="form-control" type="text" id="query-fullName">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司</div>
                        <input class="form-control" type="text" id="query-company">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input class="form-control" type="text" id="query-mphone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索来源</div>
                        <select class="form-control" id="query-source">
                            <option></option>
                            <c:forEach items="${sourceList}" var="s">
                                <option value="${s.id}">${s.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input class="form-control" type="text" id="phone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索状态</div>
                        <select class="form-control" id="query-state">
                            <option></option>
                            <c:forEach items="${clueStateList}" var="cs">
                                <option value="${cs.id}">${cs.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryClueBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createClueModal"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button id="editClueBtn" type="button" class="btn btn-default" ><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button id="deleteClueBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>


        </div>
        <div style="position: relative;top: 50px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="chked_all"/></td>
                    <td>名称</td>
                    <td>公司</td>
                    <td>公司座机</td>
                    <td>手机</td>
                    <td>线索来源</td>
                    <td>所有者</td>
                    <td>线索状态</td>
                </tr>
                </thead>
                <tbody id="tBody">
                <%--						<tr>--%>
                <%--							<td><input type="checkbox" /></td>--%>
                <%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detailClue.do?id=8cfb85063ab3462bb1f426a65a5f332d';">张三先生</a></td>--%>
                <%--							<td>动力节点</td>--%>
                <%--							<td>010-84846003</td>--%>
                <%--							<td>12345678901</td>--%>
                <%--							<td>广告</td>--%>
                <%--							<td>zhangsan</td>--%>
                <%--							<td>已联系</td>--%>
                <%--						</tr>--%>
                <%--                        <tr class="active">--%>
                <%--                            <td><input type="checkbox" /></td>--%>
                <%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>--%>
                <%--                            <td>动力节点</td>--%>
                <%--                            <td>010-84846003</td>--%>
                <%--                            <td>12345678901</td>--%>
                <%--                            <td>广告</td>--%>
                <%--                            <td>zhangsan</td>--%>
                <%--                            <td>已联系</td>--%>
                <%--                        </tr>--%>
                </tbody>
            </table>
            <!--创建容器-->
            <div id="demo_pag1">12345</div>
        </div>

        <%--			<div style="height: 50px; position: relative;top: 60px;">--%>
        <%--				<div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>--%>
        <%--				</div>--%>
        <%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
        <%--					<div class="btn-group">--%>
        <%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
        <%--							10--%>
        <%--							<span class="caret"></span>--%>
        <%--						</button>--%>
        <%--						<ul class="dropdown-menu" role="menu">--%>
        <%--							<li><a href="#">20</a></li>--%>
        <%--							<li><a href="#">30</a></li>--%>
        <%--						</ul>--%>
        <%--					</div>--%>
        <%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
        <%--				</div>--%>
        <%--				<div style="position: relative;top: -88px; left: 285px;">--%>
        <%--					<nav>--%>
        <%--						<ul class="pagination">--%>
        <%--							<li class="disabled"><a href="#">首页</a></li>--%>
        <%--							<li class="disabled"><a href="#">上一页</a></li>--%>
        <%--							<li class="active"><a href="#">1</a></li>--%>
        <%--							<li><a href="#">2</a></li>--%>
        <%--							<li><a href="#">3</a></li>--%>
        <%--							<li><a href="#">4</a></li>--%>
        <%--							<li><a href="#">5</a></li>--%>
        <%--							<li><a href="#">下一页</a></li>--%>
        <%--							<li class="disabled"><a href="#">末页</a></li>--%>
        <%--						</ul>--%>
        <%--					</nav>--%>
        <%--				</div>--%>
        <%--			</div>--%>

    </div>

</div>
</body>
</html>
