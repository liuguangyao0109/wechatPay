package com.longxis.utils.wxpay;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 支付工具类, 用来产生各种支付所需的参数
 * @author thnk
 *
 */
public class PayUtil {
    
    /**
     * 随机获取字符串
     * 包含数字和小写字母
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        if ( length <= 0 || length > 32) {
            throw new IllegalArgumentException("参数必须大于0并且不大于32");
        }
        char[] randomChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a','b','c','d','e','f','g','h','i','j',
                               'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't','u','v','w','x','y','z'};
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        int N = randomChar.length;
        for (int i = 0; i < length; i++) {
            sb.append(randomChar[rand.nextInt(N)]);
        }
        return sb.toString();
    }
    
    /**
     * 生成时间戳
     * @return
     */
    public  static String createTimestamp() {
        return (System.currentTimeMillis() / 1000) + "";
    }
    
    /**
     * 签名算法
     * @param params
     * @return
     */
    public static String createSign(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        //拼接参数
        for(Map.Entry<String, String> entry : params.entrySet()) {
            String k =  entry.getKey();
            String v =  entry.getValue();
            if (v != null && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        //拼接秘钥
        sb.append("key=" + Constant.API);
        //System.out.println("拼接字符串---" + sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), Constant.SIGN_ENCODE).toUpperCase();
        return sign;
    }
    
    /**
     * 根据时间策略生成商品订单号
     * @return
     */
    public static String getOutTradeNo() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        Random rand = new Random();
        for(int i = 0; i < 3; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * 将map参数集合转化为xml格式
     * @return
     */
    public static String mapToXml(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for(Map.Entry<String, String> entry : map.entrySet()) {
            if(Objects.equals("sign", entry.getKey()))
                continue;
            sb.append("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
        }
        if(map.containsKey("sign"))
            sb.append("<" + "sign" + ">" + map.get("sign") + "</" + "sign" + ">");
        sb.append("</xml>");
        return sb.toString();
    }
    
    /**
     * 解析xml
     * @param strxml
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public static Map<String, String> doXMLParse(String xml) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Iterator<?> it = root.elementIterator();
        while(it.hasNext()) {
            Element ele = (Element) it.next();
            map.put(ele.getName(), ele.getTextTrim());
        }
        return map;
    }
    
    /**
     * sha1加密算法
     * @param str
     * @return
     */
    public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];      
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }
    
}
