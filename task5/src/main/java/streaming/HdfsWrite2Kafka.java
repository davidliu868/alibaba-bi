package streaming;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import entry.KafkaSerilize;
import kafkaUtils.KafkaProducer4Spark;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

public class HdfsWrite2Kafka {
    //config
    private Config config;
    private static String topic_behavior;
    private static String topic_ads;
    //上下文
    private JavaSparkContext jsc;
    public HdfsWrite2Kafka(){
        config = ConfigFactory.parseResources("spark.conf");
    }

    /**
     * spark读取hdfs，写入kafka
     * @param filePath 读取hdfs的文件路径
     */
    public void write2kafka_behavior( String filePath){
        jsc = creatSparkContext(config);
        ///home/huser/data4huser/record.list
        JavaRDD<String> csvFile = jsc.textFile(filePath);

//        jsc.setLogLevel("WARN");
        jsc.setLogLevel("INFO");
        csvFile.foreach(l -> {
            String[] row = l.split(",");
            String key = row[0];
            String value = "";
            for(int i=1;i<row.length;i++){
                if (row.length<5) break;
                value += i==row.length-1?row[i]:row[i]+"|";
            }
//            writekafka("ads01",key,value);

            KafkaProducer4Spark producer = new KafkaProducer4Spark();
            producer.produceMessage("behavior",key,value);
        });

        /* 文件合并 解决办法参考
        * input_data_path_1 = "hdfs://localhost:9002/input/2017-11-01.txt"
    result1 = sc.textFile(input_data_path_1)
    input_data_path_2= "hdfs://localhost:9002/input/2017-11-10.txt"
    result2 = sc.textFile(input_data_path_2)
    result = result1.union(result2)
        * */
    }
    public void write2kafka_ads( String filePath){
        jsc = creatSparkContext(config);
        ///home/huser/data4huser/record.list
        JavaRDD<String> csvFile = jsc.textFile(filePath);

//        jsc.setLogLevel("WARN");
        jsc.setLogLevel("INFO");
        csvFile.foreach(l -> {
            String[] row = l.split(",");
            String key = row[0];
            String value = "";
            for(int i=1;i<row.length;i++){
                if (row.length<6) break;
                value += i==row.length-1?row[i]:row[i]+"|";
            }
//            writekafka("ads01",key,value);

            KafkaProducer4Spark producer = new KafkaProducer4Spark();
            producer.produceMessage("ads",key,value);
        });

        /* 文件合并 解决办法参考
        * input_data_path_1 = "hdfs://localhost:9002/input/2017-11-01.txt"
    result1 = sc.textFile(input_data_path_1)
    input_data_path_2= "hdfs://localhost:9002/input/2017-11-10.txt"
    result2 = sc.textFile(input_data_path_2)
    result = result1.union(result2)
        * */
    }

    private JavaSparkContext creatSparkContext(Config config) {
        SparkConf conf = new SparkConf();
        conf.setAppName("Java Spark Analysis").setMaster("local[2]");
        jsc = new JavaSparkContext(conf);
        return jsc;
    }

    //main 函数
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: HdfsWrite2Kafka files  path <hdfs:behavior> <hdfs:raw_sample>");
            System.exit(1);
        }
        //behavior_log写入kafka “behavior”主题
        HdfsWrite2Kafka h2k = new HdfsWrite2Kafka();
        topic_behavior = "behavior";
//        h2k.write2kafka_behavior(args[0]);
        //raw_sample依次写入kafka的“ads”主题中
        topic_ads = "ads";
        h2k.write2kafka_ads(args[1]);
    }




//    /**
//     * 写入kafka
//     * @param topic
//     * @param key
//     * @param value
//     */
//    private void writekafka(String topic, String key, String value) {
//        KafkaProducer4Spark producer = new KafkaProducer4Spark();
//        producer.produceMessage(topic,key,value);
//    }
}
