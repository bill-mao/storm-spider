package bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import com.alibaba.fastjson.JSON;
import mybatis.Template;
import mybatis.Website;
import mybatis.WebsiteMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tmp.BloomFilter;
import us.codecraft.xsoup.Xsoup;

import java.sql.Timestamp;

/**
 * Created by coral on 2017/7/11.
 */
public class FetchWebsiteBolt extends BaseBasicBolt {
    static int ccount = 0;
    // TODO: 2017/7/13  bloomFilter ?? 怎么解决分布式问题？？  static ??
    // TODO: 2017/7/11   static works??  when to initialize
    private static BloomFilter bloomFilter = new BloomFilter();
    private WebsiteMapper webMap = new WebsiteMapper();

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String templateJson = tuple.getString(0);
        String urlsJson = tuple.getString(1);

        Template template = JSON.parseObject(templateJson, Template.class);
        String[] urls = JSON.parseObject(urlsJson, String[].class);

        //get every url's website, and insert into DB
        for (int j = 0; j < urls.length; j++) {
            String currentUrl = urls[j];

            //ignore repeated url
            if (bloomFilter.contains(currentUrl)) {
                System.out.println("=============================duplicated url ：" + currentUrl);
                continue;
            } else bloomFilter.addValue(currentUrl);

            // TODO: 2017/7/9  get web html
            try {
                Document doc = Jsoup.connect(currentUrl).get();
                if (doc == null) {
                    System.out.println("============================wrong url or bad network");
                    continue;
                }
                Website web = getWebsite(doc, template);
                webMap.insertWebsite(web);
                System.out.println("successfully insert DB a website");
                ccount++;
                System.out.println("一共插入DB website 数量： " + ccount);
            } catch (Exception e) {
                System.out.println("============================sql exception or bad network ? ");
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
            System.out.println("=============================xsoupHandleNull -- can't find xpath content，website不全？");
            return null;
        }
        return out;
    }
}
