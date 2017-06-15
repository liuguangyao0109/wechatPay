package com.longxis.utils.wxpay;

public class Constant {
   
   /**
    * openId, 测试时写死的， 需要动态获取
    */
   public  static final String   OPENID = "obbsMwmxmFPDmfp_PmUjGNLFictM";
    /**
     * 微信公众号的appid
     */
   public static String APPID ="*********";
   /**
    * 微信公众号的SECRET
    */
   public static String SECRET ="*******************";
      /**
    * 公众平台申请的商户的商户号
    */
   public static final String   MCH_ID ="*********";
    /**
    * 公众平台申请的商户的api秘钥
    */
   public static final String   API ="****************";
   
   
   
   /**
    * Oautch认证成功后重定向的路径  (自定义)
    */
   public static final String REDIRECT_URI="*********************";
   /**
    * 微信的统一下单接口：即获取prepayId的接口 （固定）
    */
   public  static  final  String  GET_PREPAYID  = "https://api.mch.weixin.qq.com/pay/unifiedorder";
   
   /**
    * 微信支付成功后回掉的商户接口地址(自定义)
    */
   public  static final String   NOTIFY_URL = "**************************" ;
   
   /**
    * 签名时的编码 必须是utf-8
    */
   public  static final String   SIGN_ENCODE = "UTF-8";
   
   /**
    * 交易类型
    */
   public static final String TRADE_TYPE = "JSAPI";
   
   /**
    * 设备号
    */
   public static final String DEVICE_INFO = "WEB";
   
   
   
   
   
}
