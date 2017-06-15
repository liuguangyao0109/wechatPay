<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + path;
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>微信</title>
		<jsp:include page="/wxinclude.jsp"></jsp:include>
		<script>
			var iScale=1/window.devicePixelRatio;
			document.write('<meta name="viewport" content="width=device-width,initial-scale='+iScale+',minimum-scale='+iScale+',maximum-scale='+iScale+'" />')
		</script>
		<script>
			document.getElementsByTagName("html")[0].style.fontSize=document.documentElement.clientWidth/16+"px";
		</script>
	</head>
	<body>
		<nav>
			<a class="bg1" onclick="history.back(-1);"></a>
			<h3>我要充值</h3>
		</nav>
		<section style="background:#f0f0f0;">
	    <form action="${pageContext.request.contextPath}/wx/pay/wxpay" method="POST">   
			<div class="wycz">
				<div class="shuru">
					输入充值金额：<input type="text" value="100">元
				</div>
				<h2>选择充值金额</h2>
				<ul class="clearfix">
					<li>10元</li>
					<li>20元</li>
					<li>50元</li>
					<li>100元</li>
					<li>200元</li>
					<li>500元</li>
				</ul>
				<ol>
					<li><i><img src="${pageContext.request.contextPath}/WXAPP/images/wx.png"></i>微信支付</li>
					<li><i><img src="${pageContext.request.contextPath}/WXAPP/images/zfb.png"></i>支付宝</li>
				</ol>
				 <input type="hidden"  name="price" value="1">
				 <input type="hidden"  name="body" value="weixin">
				<input class="qrxg" type="submit" value="立即充值">
			</div>
			</form>
		<div class="bott"></div>
		</section>
	</body>
</html>
