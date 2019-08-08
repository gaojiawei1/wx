package gjw.wx.untils;

import gjw.wx.config.RedisConfig;

import java.util.Map;

/**
 * Created by Administrator on 2019/5/20 0020.
 */
public class TokenUntils {
    //应用ID与秘钥
    final static String Appid = "wx847259d2acd9ef4d";
    final static String AppSecret = "cdc372564b581b58f3698c22564be18f";
    //wx获取token地址
    static String wxUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&" +
            "appid="+Appid+"&" +
            "secret="+AppSecret;

    /**
     * 获取微信token
     * @return
     */
    public static String  getToken(){
        String res = HttpClientTool.doGet(wxUrl,null);
        Map<String,Object> map = GsonUntils.gson.fromJson(res,Map.class);
        String access_token = map.get("access_token").toString();
        RedisConfig.jedis.set("access_token",access_token);
        return "get token is ok" + RedisConfig.jedis.get("access_token");
    }
}
