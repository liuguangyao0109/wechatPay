package com.longxis.weixin;

import java.util.Map;

import com.longxis.utils.JsonUtil;
import com.longxis.utils.wxpay.Constant;
import com.longxis.utils.wxpay.HttpClientUtil;
import com.longxis.utils.wxpay.PayUtil;

/**
 * wx.config 中生成签名所必须的参数
 * @author Wangtao
 * @date   2017年6月14日
 */
public class Token {
   
    public static String accessToken;
    public static String jsapiTicket;
    
    /**
     * 有效时间
     */
    public static long validTime;
    /**
     * 7200秒 化成毫秒
     */
    public static final long TIME = 7200 * 100;
   
    /**
     * 获取accessToken,jsapiTicket
     */
    public static void getParams() {
        long time = System.currentTimeMillis();
        if(time -  validTime > TIME) {
            StringBuilder sb = new StringBuilder();
            sb.append("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");
            sb.append("&appid=" + Constant.APPID);
            sb.append("&secret=" + Constant.SECRET);
            String json1 = HttpClientUtil.doGet(sb.toString());
            Map<String, String> map = JsonUtil.jsonToMap(json1, String.class);
            if(map.containsKey("access_token")) {
                accessToken = map.get("access_token");
                //获取jsapi_ticket
                String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                        + accessToken + "&type=jsapi";
                String json2 = HttpClientUtil.doGet(url);
                map = JsonUtil.jsonToMap(json2, String.class);
                jsapiTicket = map.get("ticket");
                validTime = time;//更新时间, 用来做下次判断
                
            } else {
                accessToken = null;
            }
        }
        
    }
    
    /**
     * 生成签名sinature
     * @param url
     * @return
     */
    public static String createSign(String url, String noncestr, String timestamp) {
        getParams();
        StringBuilder sb = new StringBuilder();
        System.out.println("accessToken = " + accessToken);
        System.out.println("jsapiTicket = " + jsapiTicket);
        System.out.println("noncestr = " + noncestr);
        System.out.println("timestamp = " + timestamp);
        System.out.println("url = " + url);
        
        sb.append("jsapi_ticket=" + jsapiTicket);
        sb.append("&noncestr=" + noncestr);
        sb.append("&timestamp=" + timestamp);
        sb.append("&url=" + url);
        return PayUtil.getSha1(sb.toString());
    }
}
