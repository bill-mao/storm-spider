package spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSON;
import mybatis.Template;
import mybatis.TemplateMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

import java.util.List;
import java.util.Map;

/**
 * Created by coral on 2017/7/11.
 */
public class FetchTemplateSpout extends BaseRichSpout{

    SpoutOutputCollector _collector;
    TemplateMapper tm;
    int index;
    List<Template> templates = null;

    //initialization?
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        _collector = spoutOutputCollector;
        tm = new TemplateMapper();
        index = 0;
        try {
            templates = tm.findAllTemplate();
        } catch (Exception e) {
            System.out.println("fetch template spout can't get template from DB");
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        Template template;
        try {
            if(index == templates.size()){
                index = 0;
                //6 hours refresh template
                Thread.sleep(3600 * 6 *1000);
                templates = tm.findAllTemplate();
            }
            template = templates.get(index++);
            // TODO: 2017/7/9   get url html; wangchengjie
            Document urlHtml = Jsoup.connect(template.getChannel_url()).get();
            String html = urlHtml.html();
            //nextpage
            Template tmp = nextPageTemplate(html, template);
            if(tmp != null)
                templates.add(tmp);

            // TODO: 2017/7/13  must write in the end of function? like return?
            String templateJson = JSON.toJSONString(template);
            _collector.emit(new Values(templateJson));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("templateSpout"));
//        outputFieldsDeclarer.declareStream();
    }

    private Template nextPageTemplate(String html, Template template){
        Template tpl = cloneTemplate(template);
        List<String> l = getXpathContent(html, tpl.getChannel_url_nextpage());
        //0 or null(exception wrong xpath)
        if(l == null  || l.size() == 0 ){
            System.out.println("has no next page");
            return null;
        }
        String url = l.get(0);
        tpl.setChannel_url(url);
        return tpl;
    }

    //deal with wrong xpath exception
    private List<String> getXpathContent(String html, String xpath) {
        List<String> content = null;
        try {
            content = Xsoup.compile(xpath).evaluate(Jsoup.parse(html)).list();
        } catch (Exception e) {
            System.out.println("xpath is wrong");
            e.printStackTrace();
        }
        return content;
    }

    private Template cloneTemplate(Template template){
        //with id;
        Template tpl = new Template();
        tpl.setId(template.getId());
        tpl.setWebsite_name(template.getWebsite_name());
        tpl.setChannel_name(template.getChannel_name());
        tpl.setChannel_url(template.getChannel_url());
        tpl.setChannel_url_nextpage(template.getChannel_url_nextpage());
        tpl.setTitle_xpath(template.getTitle_xpath());
        tpl.setUrl_xpath(template.getUrl_xpath());
        tpl.setAuthor_xpath(template.getAuthor_xpath());
        tpl.setPubtime_xpath(template.getPubtime_xpath());
        tpl.setContent_xpath(template.getContent_xpath());
        tpl.setCreate_time(template.getCreate_time());
        return tpl;
    }
}
