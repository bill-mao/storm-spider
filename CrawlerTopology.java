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

    public static void main(String args[]) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        //parallelism = thread -- spout object的数量 ; executor default = task
        builder.setSpout("FetchTemplate", new FetchTemplateSpout(), 1);
        //.setNumTasks(2);//one thread 2 task 逻辑数量--{启动了两个spout}，影响输出结果:  (component : bolt spout)
        builder.setBolt("FetchTemplateHtmlUrls", new FetchTemplateHtmlUrlsBolt(), 1) // 1 防止重复？
                .localOrShuffleGrouping("FetchTemplate");
        builder.setBolt("FetchWebsite", new FetchWebsiteBolt(), 11)
                .localOrShuffleGrouping("FetchTemplateHtmlUrls");

        Config conf = new Config();
        // TODO: 2017/7/14
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            //storm rebalance mytopology -n 5 -e blue-spout=3 -e yellow-bolt=10
            conf.setMaxTaskParallelism(11);
            conf.setNumWorkers(3);// 本地模式为1  ，集群模式必须设置 //工作进程的数量表示集群中不同节点的拓扑可以创建多少个工作进程。 == slot
//            conf.setMaxTaskParallelism(1);
//            conf.setMaxSpoutPending(5000);//?
//            conf.setMessageTimeoutSecs(120);
//            conf.setNumAckers(22);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(11); //.setNumTasks(2); 限制了之前的spout
//            conf.setMaxSpoutPending(2);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("CrawlerTopology", conf, builder.createTopology());
        }
    }


}
