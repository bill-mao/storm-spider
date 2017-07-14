package bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSON;
import mybatis.Template;
import mybatis.Website;
import mybatis.WebsiteMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tmp.BloomFilter;
import us.codecraft.xsoup.Xsoup;
import util.TemplateUrls;

import javax.print.Doc;
import java.sql.Timestamp;

/**
 * Created by coral on 2017/7/11.
 */
public class FetchWebsiteBolt extends BaseBasicBolt {
    // TODO: 2017/7/13  bloomFilter ?? 怎么解决分布式问题？？  static ??
    // TODO: 2017/7/11   static works??  when to initialize
    private static BloomFilter bloomFilter = new BloomFilter();
    private WebsiteMapper webMap = new WebsiteMapper();

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //be clear what tuple [0 ,1 ] is !!
        String templateJson = tuple.getString(0);
        String urlsJson = tuple.getString(1);

        Template template =JSON.parseObject(templateJson, Template.class);
        String[] urls = JSON.parseObject(urlsJson, String[].class);

        //get every url's website, and insert into DB
        for (int j = 0; j < urls.length; j++) {
            String currentUrl =urls[j];

            //ignore repeated url
            if(bloomFilter.contains(currentUrl)){
                System.out.println("duplicated url ");
                continue;
            }
            else    bloomFilter.addValue(currentUrl);

            // TODO: 2017/7/9  get web html
            try{
                Document doc = Jsoup.connect(currentUrl).get();
                if (doc == null ) {
                    System.out.println("wrong url, fail to load html content");
                    continue;
                }
                Website web = getWebsite(doc, template);
                webMap.insertWebsite(web);
                System.out.println("successfully insert DB a website");
//                basicOutputCollector.emit(new Values()); //last no emit
            }catch(Exception e){
                System.out.println("sql exception?");
                System.out.println("something wrong with network?? ");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //no emit
    }

    private Website getWebsite(Document doc, Template template) {
        Website web = new Website();// ORM : mapped to entity website

        try {
            //// TODO: 2017/7/9
            web.setWebsite_name(template.getWebsite_name());
            web.setChannel_name(template.getChannel_name());
            //template id --> web tid
            web.setTid(template.getId());

            web.setTitle(xsoupHandleNull(doc, template.getTitle_xpath()));
            web.setContent(xsoupHandleNull(doc, template.getContent_xpath()));
            web.setPubtime(xsoupHandleNull(doc, template.getPubtime_xpath()));
            web.setAuthor(xsoupHandleNull(doc, template.getAuthor_xpath()));

            Timestamp time = new Timestamp(System.currentTimeMillis());
            web.setCrawler_time(time);
            web.setUrl(template.getChannel_url());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return web;
    }
    //catch exception to keep program continue to run
    private String xsoupHandleNull(Document doc, String xpath) {
        String out;
        try {
            //// TODO: 2017/7/9   test
//            WriteTxt.write(doc.html() + "\n" + "xpath==" + xpath + "\n\n", "default" + "\n\n");
            out = Xsoup.compile(xpath).evaluate(doc).get();
        } catch (Exception e) {
            System.out.println("xsoupHandleNull -- null 值，website不全？");
//            e.printStackTrace();
            return null;
        }
        return out;
    }
}
