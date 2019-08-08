package gjw.wx.untils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
/**
 * 解析xml
 * Created by Administrator on 2019/5/21 0021.
 */
public class WeixinMessageKit {

    public static Map handlerReceiveMsg(HttpServletRequest req) throws IOException {
        Map<String,String> msgMap = reqMsg2Map(req);
        System.out.println(msgMap);
        return msgMap;
    }

    private static String hanlderCommonMsg(Map<String, String> msgMap) {
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Map<String,String> reqMsg2Map(HttpServletRequest req) throws IOException {
        String xml = req2xml(req);
        try {
            Map<String,String> maps = new HashMap<String, String>();
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> eles = root.elements();
            for(Element e:eles) {
                maps.put(e.getName(), e.getTextTrim());
            }
            return maps;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String req2xml(HttpServletRequest req) throws IOException {
        BufferedReader br = null;
        br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String str = null;
        StringBuffer sb = new StringBuffer();
        while((str=br.readLine())!=null) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String map2xml(Map<String, Object> map) throws IOException {
        Document d = DocumentHelper.createDocument();
        Element root = d.addElement("xml");
        Set<String> keys = map.keySet();
        for(String key:keys) {
            Object o = map.get(key);
            if(o instanceof String) {
                root.addElement(key).addText((String)o);
            } else {

            }

        }
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriter(sw);
        xw.setEscapeText(false);
        xw.write(d);
        return sw.toString();
    }
}
