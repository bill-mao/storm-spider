//import org.htmlcleaner.HtmlCleaner;
//import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.List;

/**
 * Created by coral on 2017/7/7.
 */
public class Parse {

    public static void main(String args[]) throws Exception{
        String urls[] = {"http://news.e23.cn/tiyu/index.html"
        };
        String xpath[] = {"/html/body/div[2]/div[2]/div[2]/div[1]/div[2]/div[1]/h4/a"
        };
        for( int i =0; i< urls.length; i++){
            testXpath(urls[i], xpath[i]);
        }

    }

    public static void testXpath (String url, String xpath) throws Exception{
        //.get() .post() ````
        Document html = Jsoup.connect(url).get();
        System.out.println("====="+ html.title() + "======");

//        HtmlCleaner hc = new HtmlCleaner();
//        TagNode tn = hc.clean(html); //String html

        List<String> result = Xsoup.compile(xpath).evaluate(html).list();
        System.out.println(result);
    }

    public Website getWebsite(String html, List<String> xpaths){
        Website web = new Website();// ORM : mapped to entity website
//        web.setAuthor();





        return web;
    }



}
