package bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSON;
import mybatis.Template;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;
import util.SeleniumSpider;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by coral on 2017/7/11.
 */

// have automatically implements ack and fail ; BaseRichBolt need to implements yourself
public class FetchTemplateHtmlUrlsBolt extends BaseBasicBolt{

    SeleniumSpider seleniumSpider = new SeleniumSpider();

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //  serialization, to json
        String templateJson = tuple.getString(0);
        Template template = JSON.parseObject(templateJson, Template.class);

        String next = template.getChannel_url_nextpage();
        String url = template.getChannel_url();
        String urlXpath = template.getUrl_xpath();

        Set<String> urls = new HashSet<>();
        try {
            if(next.startsWith("selenium")){
                String nextP = next.trim().split("%")[1];
                System.out.println("============================next page xpath is : " + nextP);
                urls = seleniumSpider.getUrls(url, nextP, urlXpath);
            }else {
                // TODO: 2017/7/11  wangchengjie;  exception， 没有emit 怎么办？
                Document doc = Jsoup.connect(url).get();
                urls.addAll(Xsoup.compile(urlXpath).evaluate(doc).list());
            }

            System.out.println("====================================the number of this template's urls is :" + urls.size());
            String urlsJson = JSON.toJSONString(urls.toArray(new String[urls.size()]));
            basicOutputCollector.emit(new Values(templateJson, urlsJson));//debug --> automatically print the the emit
        } catch (IOException e) {
            System.out.println("============================fetch template website urls failure");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("template", "urls"));
    }

}
