package streaming;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import redis.clients.jedis.Jedis;
import scala.Tuple2;
import utils.JavaRedisClient;

import java.util.Iterator;

public class Business {
    public Config config;
    public JavaStreamingContext ssc;
    public static SparkSession instance = null;
    public static final String PV_HASHKEY = "behavior_pv";
    //    private static final String CART_HASHKEY = "behavior_cart";
    public static final String BUY_HASHKEY = "behavior_buy";

    public Business() {
        config = ConfigFactory.parseResources("spark.conf");
    }

    public static SparkSession getInstance(SparkConf conf) {
        if (instance == null) {
            instance = SparkSession.builder().config(conf).getOrCreate();
        }
        return instance;
    }
    /**
     * 配置 JavaStreamingContext
     *
     * @param config 配置信息
     * @return JavaStreamingContext
     */
    public JavaStreamingContext createStreamingContext(Config config) {
        SparkConf conf = new SparkConf();
        conf.setAppName("Java Behavior from kafka Streaming Analysis");
        conf.set("spark.streaming.stopGracefullyOnShutdown", "true");
        conf.setMaster("local[2]");
        Duration batchInterval = Durations.seconds(config.getLong("spark.interval"));
        JavaStreamingContext ssc = new JavaStreamingContext(conf, batchInterval);
        return ssc;
    }
    /**
     * 获取ad_feature表信息
     *
     * @return
     */
    public JavaRDD<String> getAd_featureHdfs() {
        JavaRDD<String> stringJavaRDD = ssc.sparkContext().textFile("hdfs://192.168.10.132:9000/tb_data/ad_feature");
        return stringJavaRDD;
    }

    /**
     * 提取公共的写入数据库的方法 待实现 TODO
     * @param key
     * @param field
     * @param value
     */
    private void foreachRDDRealization(String key, String field, Integer value) {
        new VoidFunction<JavaPairRDD<String, Integer>>() {
            @Override
            public void call(JavaPairRDD<String, Integer> rdd) throws Exception {
                rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String, Integer>>>() {
                    @Override
                    public void call(Iterator<Tuple2<String, Integer>> partitionOfRecords) throws Exception {
                        Jedis jedis = JavaRedisClient.get().getResource();
                        while (partitionOfRecords.hasNext()) {
                            try {
                                Tuple2<String, Integer> pv = partitionOfRecords.next();
                                //TODO 发送到redis，可以修改为保存到mysql
                                jedis.hincrBy(key, field, value);
                            } catch (Exception e) {
                                System.out.println("error:" + e);
                            }

                        }
                    }
                });
            }
        };
    }

}
