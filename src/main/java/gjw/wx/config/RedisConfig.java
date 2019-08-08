package gjw.wx.config;

import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GJW
 * Redis配置 及 封装 Geo 实现附近人功能
 * Created by Administrator on 2019/5/20 0020.
 */
public class RedisConfig {
    public final static Jedis jedis  = new Jedis("47.103.201.141", 6379);

    public final static String redisKey = "123";

    /**
     * 添加地理位置上的信息
     * @param longitude 经度
     * @param Latitude 纬度
     * @param data 数据
     * */
    public static String myGeoAdd(double longitude , double Latitude , String data ){

        try {
            jedis.geoadd(redisKey ,longitude ,Latitude ,data);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据经纬度获取附近地理位置上信息
     * @param longitude 经度
     * @param Latitude 纬度
     * */
    public static List<Map<String ,Object>> getGeoList(double longitude , double Latitude  ){


        List<Map<String ,Object>> list = new ArrayList();
        //获取附近所有信息
        List<GeoRadiusResponse> q = null;
        try {
            q = jedis.georadius(redisKey ,longitude ,Latitude,3000, GeoUnit.M , GeoRadiusParam.geoRadiusParam().withDist().withCoord().sortAscending());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (q == null){
            return null;
        }
        //封装
        for(GeoRadiusResponse geo:q){
            Map<String ,Object> map = new HashMap();
            map.put("data",geo.getMemberByString());
            map.put("distance",geo.getDistance());
            list.add(map);
            System.out.println(geo.getMemberByString()); //主键 有主键了个人信息就很简单了
            System.out.println(geo.getDistance());  //距离多少米
        }
        return list;
    }


}
