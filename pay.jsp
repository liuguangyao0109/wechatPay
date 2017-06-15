<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
	</head>
	<body>
		<nav>
			<a class="bg1" onclick="history.back(-1);"></a>
			<h3>支付订单</h3>
		</nav>
		<section style="background:#f0f0f0;">
			<div class="wycz">
				<div class="shuru">
					确认充值金额：${price }元
				</div>
				<div class="shuru">
					详情：${body}
				</div>
				<input class="qrxg" type="button"  onclick="pay()" value="确认支付">
			</div>
			</form>
		<div class="bott"></div>
	</body>
	
	<script type="text/javascript">
	
	wx.config({
	    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: '${config_appid}', // 必填，公众号的唯一标识
	    timestamp: '${config_timestamp}', // 必填，生成签名的时间戳
	    nonceStr: '${config_noncestr}', // 必填，生成签名的随机串
	    signature: '${config_signature}',// 必填，签名，见附录1
	    jsApiList: ['chooseWXPay','getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
	
	function onBridgeReady(){
	    WeixinJSBridge.invoke(
	       'getBrandWCPayRequest', {
	           "appId":"${map['appId']}",     //公众号名称，由商户传入     
	           "timeStamp":"${map['timeStamp']}",         //时间戳，自1970年以来的秒数     
	           "nonceStr":"${map['nonceStr']}", //随机串     
	           "package":"${map['package']}",     
	           "signType":"${map['signType']}",         //微信签名方式：     
	           "paySign":"${map['paySign']}" //微信签名 
	       },
	       function(res){
	    	// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
	           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
	        	   alert("支付成功!");
	           } else {
	        	   alert("支付失败");
	           }     
	       }
	   ); 
	}
	
	function pay() {
        //判断微信的版本号
        var wechatInfo = navigator.userAgent.match(/MicroMessenger\/([\d\.]+)/i) ;
        if( !wechatInfo ) {
            alert("请在微信客户端中打开") ;
        } else if ( wechatInfo[1] < 5.0 ) {
            alert("你的版本过低，微信支付必须在5.0以上,请升级你的微信") ;
        }else{
        	if (typeof WeixinJSBridge == "undefined"){
       	       if( document.addEventListener ){
       	           document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
       	       }else if (document.attachEvent){
       	           document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
       	           document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
       	       }
       	    }else{
       	       onBridgeReady();
       	    }
        }
    }
	</script>
</html>
