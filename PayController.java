package com.longxis.weixin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.longxis.utils.JsonUtil;
import com.longxis.utils.wxpay.Constant;
import com.longxis.utils.wxpay.HttpClientUtil;
import com.longxis.utils.wxpay.PayUtil;

/**
 * 支付控制层
 * @author Wangtao
 *
 */
@Controller
@RequestMapping("/wx/pay")
public class PayController {
    
    private Logger logger = Logger.getLogger(PayController.class);
    
    /**
     * 支付
     * @param mav
     * @return
     */
    @RequestMapping(value = "/wxpay", method = RequestMethod.POST)
    public ModelAndView pay(ModelAndView mav, HttpServletRequest request) {
        //wx.config参数
        String url = request.getRequestURL().toString();
        int index = -1;
        if((index = url.lastIndexOf("#")) != -1) {
            url = url.substring(0, index);
        }
        String configNoncestr = PayUtil.getRandomString(30);
        String configTimestamp = PayUtil.createTimestamp();
        String signature = Token.createSign(url, configNoncestr, configTimestamp);
        mav.addObject("config_appid", Constant.APPID);
        mav.addObject("config_timestamp", configTimestamp);
        mav.addObject("config_noncestr", configNoncestr);
        mav.addObject("config_signature", signature);
        
        //支付参数
        String prepayId = getPrepayId(request);
        //System.out.println(prepayId);
        Map<String, String> map = new TreeMap<String, String>();
        //准备参数
        String appId = Constant.APPID;
        String timeStamp = PayUtil.createTimestamp();
        String nonceStr = PayUtil.getRandomString(30);
        String packages = "prepay_id=" + prepayId;
        String signType = "MD5";
     
        map.put("appId", appId);
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", packages);
        map.put("signType", signType);
        String paySign = PayUtil.createSign(map);
        map.put("paySign", paySign);
        mav.addObject("map", map);
        mav.addObject("body", request.getParameter("body"));
        mav.addObject("price", request.getParameter("price"));
        mav.setViewName("wx-service/pay");
        return mav;
    }
    
    /**
     * 获取参数prepay_id
     * @return
     */
    private String getPrepayId(HttpServletRequest request) {
        Map<String, String> map = new TreeMap<String, String>();
        map.put("appid", Constant.APPID);//公众号id
        map.put("mch_id", Constant.MCH_ID);//商户号
        map.put("device_info", Constant.DEVICE_INFO);//设备号
        map.put("nonce_str", PayUtil.getRandomString(30));//随机字符串,不大于32位
        map.put("body", request.getParameter("body"));//商品简单描述
        map.put("out_trade_no", PayUtil.getOutTradeNo());//商品订单号,唯一并且不大于32位
        map.put("total_fee", request.getParameter("price"));//商品价格,以分为单位并且不能包含小数
        String ip = request.getRemoteAddr();
        map.put("spbill_create_ip", ip);
        map.put("notify_url", Constant.NOTIFY_URL);//通知地址, 必需外网可访问,不能携带参数
        map.put("trade_type", Constant.TRADE_TYPE);//交易类型
        //map.put("openid",(String)request.getSession().getAttribute("openid")); //动态获取
        map.put("openid", Constant.OPENID);//写死测试
        String sign = PayUtil.createSign(map);
        map.put("sign", sign);
        String xmlParams =  PayUtil.mapToXml(map);
        //System.out.println(xmlParams);
        String responseData = HttpClientUtil.doPostXml(Constant.GET_PREPAYID, xmlParams);
        String prepayId = null;
        try {
            prepayId = PayUtil.doXMLParse(responseData).get("prepay_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prepayId;
    }
    
    /**
     * 生成授权链接
     * @return
     */
    @RequestMapping("/authority")
    public ModelAndView authority(HttpServletRequest request, ModelAndView mav) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Constant.APPID);
        //回调地址
        String redirectUrl = Constant.REDIRECT_URI;
        sb.append("&redirect_uri=");
        try {
            sb.append(URLEncoder.encode(redirectUrl, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("&response_type=code");
        sb.append("&scope=snsapi_userinfo");
        sb.append("&state=" + PayUtil.getRandomString(5));
        sb.append("#wechat_redirect");
        //System.out.println(sb.toString());
        mav.addObject("url", sb.toString());
        mav.setViewName("wx-service/test");//待修改//////////////////////////////////////
        return mav;
    }
    
    /**
     * 授权回调同意后, 获取openid以及用户信息, 按需要修改
     */
    @RequestMapping("/redirectUrl")
    public ModelAndView redirectUrl(ModelAndView mav, HttpServletRequest request) {
        String code = request.getParameter("code");
        if(code != null){
            try {
                code = new String(code.getBytes("iso8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        System.out.println(code);
        System.out.println("回调成功");
        logger.info("code = " + code);
        logger.info("回调成功");
        
        //获取oppenid
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.APPID);
        sb.append("&secret=" + Constant.SECRET);
        sb.append("&code=" + code);
        sb.append("&grant_type=authorization_code");
        String response = HttpClientUtil.doGet(sb.toString());
        Map<String, String> jsonObject = JsonUtil.jsonToMap(response, String.class);
        //获取openid
        String openid = jsonObject.get("openid");
        String accessToken = jsonObject.get("access_token");
        request.getSession().setAttribute("openid", openid);
        
        //获取用户信息
        String url =  "https://api.weixin.qq.com/sns/userinfo?access_token="
                + accessToken + "&openid=" + openid + "&lang=zh_CN"; 
        String userInfo = HttpClientUtil.doGet(url);
        logger.info(userInfo);
        mav.setViewName("");//待写----------------------------------
        return mav;
    }
    
    /**
     * 回调
     */
    @RequestMapping("/callback")
    @ResponseBody
    public void callBack() {
        System.out.println("微信回调成功");
    }

}
