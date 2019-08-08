/**
 * Author:   gaojiawei
 * Date:     2019/5/19 21:17
 * Description: 测试
 */

package gjw.wx.controller;

import gjw.wx.config.RedisConfig;
import gjw.wx.untils.HttpClientTool;
import gjw.wx.untils.TokenUntils;
;
import gjw.wx.untils.WeixinMessageKit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @RequestMapping(value = "demo" ,method = RequestMethod.GET)
    public String demo(){
        return "demo";
    }

    @RequestMapping("redisdemo")
    public String redisDemo(){
        RedisConfig.jedis.set("name", "chx");
        return  "redis-demo";
    }

    @RequestMapping(value ="wx" , method = RequestMethod.GET)
    public String wxGetInit(HttpServletRequest req, String echostr , String FromUserName){

        return echostr;
    }

    @RequestMapping(value ="wx" , method = RequestMethod.POST)
    public String wxPostInit(HttpServletRequest req, String echostr ){
        String rel = "";
        Map map = null;
        try {
            req.setCharacterEncoding("utf-8");
            map = WeixinMessageKit.handlerReceiveMsg(req);
            rel = map.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //经度
        double longitude = new Double(map.get("Longitude").toString());
        //纬度
        double latitude = new Double(map.get("Latitude").toString());

        List<Map<String, Object>> list = RedisConfig.getGeoList(longitude, latitude);
        System.out.println("Geo 查询返回 : " + list);
        System.out.println("--------rel:"+rel);
        String openid = map.get("FromUserName").toString();
        System.out.println("openid : " + openid);
        return echostr;
    }


    @RequestMapping("coordinate")
    public String addoordinate(double longitude ,double latitude ,String data){
        System.out.println("经度" + longitude + "纬度" + latitude + "数据" + data);
        return RedisConfig.myGeoAdd(longitude ,latitude ,data);
    }
    /**
     * 获取微信token
     * */
    @RequestMapping("getWxToken")
    public String getWX(){
        return TokenUntils.getToken();
    }

    /**
     * 设置菜单
     * */
    @RequestMapping("setMenu")
    public String setMenu() {
        String data = "\n" +
                "{\n" +
                "    \"button\": [\n" +
                "        {\n" +
                "            \"name\": \"扫码\", \n" +
                "            \"sub_button\": [\n" +
                "                {\n" +
                "                    \"type\": \"scancode_waitmsg\", \n" +
                "                    \"name\": \"扫码带提示1\", \n" +
                "                    \"key\": \"rselfmenu_0_0\", \n" +
                "                    \"sub_button\": [ ]\n" +
                "                }, \n" +
                "                {\n" +
                "                    \"type\": \"scancode_push\", \n" +
                "                    \"name\": \"扫码推事件1\", \n" +
                "                    \"key\": \"rselfmenu_0_1\",\n" +
                "\t\t    \"url\":\"http://www.soso.com/\", \n" +
                "                    \"sub_button\": [ ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }]\n" +
                "}\n"
                ;
        String backData = null;
        String access_token = RedisConfig.jedis.get("access_token");
        try {
            backData = HttpClientTool.doPostJson("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token+"",data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "set menu is ok : " + backData;
    }
}
