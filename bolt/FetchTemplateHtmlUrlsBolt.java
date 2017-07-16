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

import java.io.IOException;
import java.util.List;

/**
 * Created by coral on 2017/7/11.
 */

// have automatically implements ack and fail ; BaseRichBolt need to implements yourself
public class FetchTemplateHtmlUrlsBolt extends BaseBasicBolt{

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        //  serialization, to json
        String templateJson = tuple.getString(0);
        Template template = JSON.parseObject(templateJson, Template.class);

        try {
            // TODO: 2017/7/11  wangchengjie
            Document doc = Jsoup.connect(template.getChannel_url()).get();
            List<String> urls = Xsoup.compile(template.getUrl_xpath()).evaluate(doc).list();
            System.out.println("====================================the number of this template's urls is :" + urls.size());
            String urlsJson = JSON.toJSONString(urls.toArray( new String[urls.size()]));
            basicOutputCollector.emit(new Values(templateJson, urlsJson));//debug --> automatically print the the emit
        } catch (IOException e) {
            System.out.println("============================fetch template website urls failure");
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("template", "urls"));
    }

}
