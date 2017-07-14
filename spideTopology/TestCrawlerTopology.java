package spideTopology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
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

/**
 * Created by coral on 2017/7/5.
 */
public class TestCrawlerTopology {
    public static void main(String args[]) throws Exception{

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("urlspout", new UrlSpout(), 2);
//        builder.setBolt("fetchHTML", new FetchHTML(), 8).
//                localOrShuffleGrouping("urlspout");
//        builder.setBolt("Parse", new Parse(), 4).
//                localOrShuffleGrouping("html","baseurl");
//        builder.setBolt()

        builder.setBolt("fetchHtml", new FetchHTML(), 5).localOrShuffleGrouping("urlspout");
        builder.setBolt("parse", new Parse(), 5).localOrShuffleGrouping("fetchHtml");


        Config conf = new Config();
        conf.setDebug(true);

        if(args != null && args.length > 0){
            conf.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        }else{
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
//            System.out.notify();
            cluster.submitTopology("spide-test", conf, builder.createTopology());
            Thread.sleep(10000);
            cluster.shutdown();
        }
    }

    public static class FetchHTML extends BaseBasicBolt{

        @Override
        public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
            String url = tuple.getString(0);
            try {
                String result = AbstractSpider.getResult(url);
                basicOutputCollector.emit(new Values(result, url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("html","baseurl"));
        }
    }

    public static class Parse extends BaseBasicBolt{

        @Override
        public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
            //获取工具类返回的html,并用Jsoup解析
            String result = tuple.getString(0);
            String url = tuple.getString(1);
            Document document = Jsoup.parse(result);
            document.setBaseUri(url);
            //获取所有的img元素
            Elements elements = document.select("img");
            for (Element e : elements) {
                //获取每个src的绝对路径
                String src = e.absUrl("src");
                URL urlSource = null;
                try {
                    urlSource = new URL(src);
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
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
//            ???
            basicOutputCollector.emit(new Values(url));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("end"));
        }
    }

}

