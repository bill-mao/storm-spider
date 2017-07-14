package spideTopology;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * Created by coral on 2017/7/5.
 */
public class UrlSpout extends BaseRichSpout{
    SpoutOutputCollector _collector;
    Random _rand;

//    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        _collector = spoutOutputCollector;
        _rand = new Random();
    }

//    @Override
    public void nextTuple() {
//        Util.sleep
        String[] urls = new String[]{"http://jandan.net/pic/page-785#comments",
        "http://jandan.net/pic/page-786#comments"};
        String url = urls[_rand.nextInt(urls.length)];
        _collector.emit(new Values(url));
    }

//    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("urlspout"));

    }
}
