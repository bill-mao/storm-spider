import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import bolt.FetchTemplateHtmlUrlsBolt;
import bolt.FetchWebsiteBolt;
import spout.FetchTemplateSpout;

/**
 * Created by coral on 2017/7/11.
 */
public class CrawlerTopology {


    public static void main(String args[]) throws Exception{
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("FetchTemplate", new FetchTemplateSpout(), 1);
        builder.setBolt("FetchTemplateHtmlUrls", new FetchTemplateHtmlUrlsBolt(), 2)
                .localOrShuffleGrouping("FetchTemplate");
        builder.setBolt("FetchWebsite", new FetchWebsiteBolt(), 5)
                .localOrShuffleGrouping("FetchTemplateHtmlUrls");

        Config conf = new Config();
        // TODO: 2017/7/14  
        conf.setDebug(true);

        if(args != null && args.length >0){
//        if(args == null && args.length >0){
            conf.setNumWorkers(20);
            conf.setMaxSpoutPending(5000);//?
            conf.setMessageTimeoutSecs(120);
//            conf.setNumAckers();
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        }else{
            conf.setMaxTaskParallelism(8);
//            conf.setMaxSpoutPending(2);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("CrawlerTopology", conf, builder.createTopology());
            //don't shutdown automatically
        }
    }



}
