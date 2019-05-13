package streaming;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import kafkaUtils.KafkaProducer4Spark;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class HdfsWrite2Kafka {
    //config
    private Config config;
    //上下文
    private JavaSparkContext jsc;
    public HdfsWrite2Kafka(){
        config = ConfigFactory.parseResources("spark.conf");
    }

    /**
     * spark读取hdfs，写入kafka
     * @param topic kafka 主题
     * @param filePath 读取hdfs的文件路径
     */
    public void write2kafka(String topic, String filePath){
        jsc = creatSparkContext(config);
        JavaRDD<String> logFile = jsc.textFile(filePath);
        jsc.setLogLevel("WARN");
        logFile.foreach(l -> {
            String[] row = l.split(",");
            String key = row[0];
            String value = "";
            for(int i=1;i<row.length;i++){
                if (row.length<5) break;
                value += i==row.length-1?row[i]:row[i]+"|";
            }
            writekafka(topic,key,value);
        });

        /* 文件合并 解决办法参考
        * input_data_path_1 = "hdfs://localhost:9002/input/2017-11-01.txt"
    result1 = sc.textFile(input_data_path_1)
    input_data_path_2= "hdfs://localhost:9002/input/2017-11-10.txt"
    result2 = sc.textFile(input_data_path_2)
    result = result1.union(result2)
        * */
    }

    /**
     * 写入kafka
     * @param topic
     * @param key
     * @param value
     */
    private void writekafka(String topic, String key, String value) {
        KafkaProducer4Spark producer = new KafkaProducer4Spark();
        producer.produceMessage(topic,key,value);
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
        h2k.write2kafka("behavior",args[0]);
        //raw_sample依次写入kafka的“ads”主题中
        h2k.write2kafka("ads",args[1]);
    }
}
