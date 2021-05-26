<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
//http://127.0.0.1:8080/crm/
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
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

	$(function(){

	    //给"创建"按钮添加单击事件
        $("#createTranBtn").click(function () {
            //发送同步请求
            window.location.href="workbench/transaction/createTran.do";
        });

		//给"修改"按钮添加单击事件
		$("#editTranBtn").click(function(){
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
			window.location.href="workbench/transaction/editTran.do?id="+id;
		});

		//给"删除'按钮添加单击事件
		$("#deleteTranBtn").click(function () {
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
					url:'workbench/tran/deleteTranByIds.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data) {
						if (data.code=="1"){
							//刷新市场活动列表，显示第一页数据，保持每页显示条数不变
							queryTranForPageByCondition(1,$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
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
		queryTranForPageByCondition(1, 2);

		//给"查询"按钮添加单击事件
		$("#queryTranBtn").click(function () {
			 //alert(12345);
			//显示所有符合条件的数据的第一页，默认每页显示10条
			queryTranForPageByCondition(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});
		
	});

	function queryTranForPageByCondition(pageNo, pageSize) {
		//收集参数
		//var pageNo=1;
		//var pageSize=10;
		var owner = $("#query-owner").val();
		var name = $("#query-name").val();
		var customer = $("#query-customer").val();
		var stage = $("#query-stage").val();
		var type = $("#query-type").val();
		var source = $("#query-source").val();
		var contacts = $("#query-contacts").val();
		//发送请求
		$.ajax({
			url: 'workbench/transaction/queryTranForPageByCondition.do',
			data: {
				pageNo: pageNo,
				pageSize: pageSize,
				owner:owner,
				name: name,
				customer: customer,
				stage: stage,
				type: type,
				source: source,
				contacts: contacts
			},
			type: 'post',
			dataType: 'json',
			success: function (data) {
				//显示总条数。功能由pagination插件替代
				//$("#totalRowsB").text(data.totalRows);
				//遍历data.tranList,显示数据列表
				var htmlStr = "";
				$.each(data.tranList, function (index, obj) {
					htmlStr += "<tr class=\"active\">";
					htmlStr += "<td><input type=\"checkbox\"  value=\"" + obj.id + "\"/></td>";
					htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/transaction/detailTran.do?id=" + obj.id + "'\">" + obj.name + "</a></td>";
					htmlStr += "<td>" + obj.customerId + "</td>";
					htmlStr += "<td>" + obj.stage + "</td>";
					htmlStr += "<td>" + obj.type + "</td>";
					htmlStr += "<td>" + obj.owner + "</td>";
					htmlStr += "<td>" + obj.source + "</td>";
					htmlStr += "<td>" + obj.contactsId + "</td>";
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
						queryTranForPageByCondition(pageObj.currentPage, pageObj.rowsPerPage);

					}
				});
			}
		});
	}
	
</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="query-customer">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="query-stage">
					  	<option></option>
					  	<c:forEach items="${stageList}" var="s">
                            <option value="${s.id}">${s.value}</option>
                        </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="query-type">
					  	<option></option>
                          <c:forEach items="${transactionTypeList}" var="tt">
                              <option value="${tt.id}">${tt.value}</option>
                          </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="query-source">
						  <option></option>
                          <c:forEach items="${sourceList}" var="so">
                              <option value="${so.id}">${so.value}</option>
                          </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="query-contacts">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryTranBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button id="createTranBtn" type="button" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button id="editTranBtn" type="button" class="btn btn-default" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button id="deleteTranBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="chked_all"/></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tBody">
<%--						<tr>--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detailTran.do?id=7667154af9a246dba4e92a53767758a4';">动力节点-交易测试04</a></td>--%>
<%--							<td>动力节点</td>--%>
<%--							<td>谈判/复审</td>--%>
<%--							<td>新业务</td>--%>
<%--							<td>zhangsan</td>--%>
<%--							<td>广告</td>--%>
<%--							<td>李四</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>--%>
<%--                            <td>动力节点</td>--%>
<%--                            <td>谈判/复审</td>--%>
<%--                            <td>新业务</td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>广告</td>--%>
<%--                            <td>李四</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
				<!--创建容器-->
				<div id="demo_pag1">12345</div>
			</div>
			
<%--			<div style="height: 50px; position: relative;top: 20px;">--%>
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