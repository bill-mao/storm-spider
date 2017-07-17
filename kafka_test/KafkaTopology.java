//package kafka_test;
//
///**
// * Created by coral on 2017/7/16.
// */
//public class KafkaTopology {
//
//
//    public static void main(String args[]){
//        Fields fields = new Fields("key", "message");
//        FixedBatchSpout spout = new FixedBatchSpout(fields, 4,
//                new Values("storm", "1"),
//                new Values("trident", "1"),
//                new Values("needs", "1"),
//                new Values("javadoc", "1")
//        );
//        spout.setCycle(true);
//        builder.setSpout("spout", spout, 5);
//        KafkaBolt bolt = new KafkaBolt()
//                .withTopicSelector(new DefaultTopicSelector("test"))
//                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper());
//        builder.setBolt("forwardToKafka", bolt, 8).shuffleGrouping("spout");
//
//        Config conf = new Config();
//        //set producer properties.
//        Properties props = new Properties();
//        props.put("metadata.broker.list", "localhost:9092");
//        props.put("request.required.acks", "1");
//        props.put("serializer.class", "kafka.serializer.StringEncoder");
//        conf.put(KafkaBolt.KAFKA_BROKER_PROPERTIES, props);
//
//        StormSubmitter.submitTopology("kafkaboltTest", conf, builder.createTopology());
//    }
//
//
//}
