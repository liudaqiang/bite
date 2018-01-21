<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${ctxStatic}/layui/css/layui.css"  media="all">
<title>Insert title here</title>
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<%@ include file="/WEB-INF/jsp/common/top.jsp"%>
			<%@ include file="/WEB-INF/jsp/common/left.jsp"%>
		</div>
		<div class="layui-body">
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
			  <legend>表单集合演示</legend>
			</fieldset>
			<div class="demoTable">
			  搜索ID：
			  <div class="layui-inline">
			    <input class="layui-input" name="id" id="demoReload" autocomplete="off">
			  </div>
			  <button class="layui-btn" data-type="reload">搜索</button>
			</div>
			 
			<table class="layui-hide" id="LAY_table_user" lay-filter="user"></table> 
		</div>
		<%@ include file="/WEB-INF/jsp/common/foot.jsp"%>
	</div>
	<script src="${ctxStatic}/layui/layui.js" charset="utf-8"></script>
	<script src="${ctxStatic}/jquery/jquery1_11.js" charset="utf-8"></script>
	<script src="${ctxStatic}/jquery/jquery.cookie.js" charset="utf-8"></script>
	<script>
	layui.use('table', function(){
		  var table = layui.table;
		  
		  //方法级渲染
		  table.render({
		    elem: '#LAY_table_user'
		    	,data: [{
		    	      "id": "10001"
		    	      ,"username": "杜甫"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "116"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "108"
		    	      ,"joinTime": "2016-10-14"
		    	    }, {
		    	      "id": "10002"
		    	      ,"username": "李白"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "12"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	      ,"LAY_CHECKED": true
		    	    }, {
		    	      "id": "10003"
		    	      ,"username": "王勃"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "65"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	    }, {
		    	      "id": "10004"
		    	      ,"username": "贤心"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "666"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	    }, {
		    	      "id": "10005"
		    	      ,"username": "贤心"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "86"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	    }, {
		    	      "id": "10006"
		    	      ,"username": "贤心"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "12"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	    }, {
		    	      "id": "10007"
		    	      ,"username": "贤心"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "16"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	    }, {
		    	      "id": "10008"
		    	      ,"username": "贤心"
		    	      ,"email": "xianxin@layui.com"
		    	      ,"sex": "男"
		    	      ,"city": "浙江杭州"
		    	      ,"sign": "人生恰似一场修行"
		    	      ,"experience": "106"
		    	      ,"ip": "192.168.0.8"
		    	      ,"logins": "106"
		    	      ,"joinTime": "2016-10-14"
		    	    }]
		    ,cols: [[
		      {checkbox: true, fixed: true}
		      ,{field:'id', title: 'ID', width:80, sort: true, fixed: true}
		      ,{field:'username', title: '用户名', width:80}
		      ,{field:'sex', title: '性别', width:80, sort: true}
		      ,{field:'city', title: '城市', width:80}
		      ,{field:'sign', title: '签名'}
		      ,{field:'experience', title: '积分', sort: true, width:80}
		      ,{field:'score', title: '评分', sort: true, width:80}
		      ,{field:'classify', title: '职业', width:80}
		      ,{field:'wealth', title: '财富', sort: true, width:135}
		    ]]
		    ,id: 'testReload'
		    ,page: true
		    ,height: 315
		  });
		  
		  var $ = layui.$, active = {
		    reload: function(){
		      var demoReload = $('#demoReload');
		      
		      //执行重载
		      table.reload('testReload', {
		        page: {
		          curr: 1 //重新从第 1 页开始
		        }
		        ,where: {
		          key: {
		            id: demoReload.val()
		          }
		        }
		      });
		    }
		  };
		  
		  $('.demoTable .layui-btn').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
		});
	</script>
</body>
</html>