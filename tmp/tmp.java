package tmp;

import com.alibaba.fastjson.JSON;
import mybatis.Template;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

/**
 * Created by coral on 2017/7/9.
 */
public class tmp {

    public static void main(String args[]) throws Exception {
        System.out.println(System.currentTimeMillis());
//        BloomFilter bf = new BloomFilter();




//    dongtai wangye       http://mil.sohu.com/
//        System.out.println(Jsoup.connect("   http://mil.sohu.com/").get().html());


//        Parse parse = new Parse();
//        Parse.testXpath("http://news.qq.com/a/20131204/013039.htm",
//                "//div[@bosszone='content']//allText()");

//        String data = (Jsoup.connect("http://news.qq.com/a/20131127/015008.htm").get().html());
//        WriteTxt.write(data, "d:/desktop/htmltest.txt");
    }


    public static void testXpath(String url, String xpath) throws Exception {
        //.get() .post() ````
        Document html = Jsoup.connect(url).get();
        System.out.println("=====" + html.title() + "======");
        String result = Xsoup.compile(xpath).evaluate(html).get();
        System.out.println(result);
    }
}
