package spideTopology;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class spide {
    public static void main(String args[]) {
        try {
//            String[] urls = new String[]{"http://jandan.net/pic/page-785#comments",
//                    "http://jandan.net/pic/page-786#comments"};

            String urls [] = new String[]{"http://edu.qq.com/articleList/rolls/" };


            for (String url : urls) {
                //获取工具类返回的html,并用Jsoup解析
                String result = AbstractSpider.getResult(url);
                System.out.println(result);
//                SpiderImgs spider = new SpiderImgs(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


/**
 * 2017-5-17 基础类，通过URL返回一个响应的html
 *
 * @author tom
 */
class AbstractSpider {

    public static String getResult(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse response = httpClient.execute(new HttpGetConfig(url))) {
            String result = EntityUtils.toString(response.getEntity());
            return result;
        } catch (Exception e) {
            System.out.println("获取失败");
            return "";
        }
    }
}

/**
 * 内部类，继承HttpGet，为了设置请求超时的参数
 *
 * @author tom
 */
class HttpGetConfig extends HttpGet {
    public HttpGetConfig(String url) {
        super(url);
        setDefaulConfig();
    }

    private void setDefaulConfig() {
        this.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000).build());
        this.setHeader("User-Agent", "spider");
    }
}


/**
 * 2017-5-17 爬取指定URL的图片
 *
 * @author tom
 */
class SpiderImgs {
    public SpiderImgs(String url) throws Exception {
        //获取工具类返回的html,并用Jsoup解析
        String result = AbstractSpider.getResult(url);
        Document document = Jsoup.parse(result);
        document.setBaseUri(url);
        //获取所有的img元素
        Elements elements = document.select("img");
        for (Element e : elements) {
            //获取每个src的绝对路径
            String src = e.absUrl("src");
            URL urlSource = new URL(src);
            URLConnection urlConnection = urlSource.openConnection();
            String imageName = src.substring(src.lastIndexOf("/") + 1, src.length());
            System.out.println(e.absUrl("src"));
            //通过URLConnection得到一个流，将图片写到流中，并且新建文件保存
            InputStream in = urlConnection.getInputStream();
            OutputStream out = new FileOutputStream(new File("d:/desktop/pic", imageName));
            byte[] buf = new byte[1024];
            int l = 0;
            while ((l = in.read(buf)) != -1) {
                out.write(buf, 0, l);
            }
        }
    }
}