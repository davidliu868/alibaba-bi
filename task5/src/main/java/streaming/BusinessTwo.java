package streaming;

import com.google.common.collect.Sets;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dao.JavaDBDao;
import db.DBHelper;
import entry.AdsEntry;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class BusinessTwo {
    public Config config;
    public JavaStreamingContext ssc;
    public static SparkSession instance = null;
    public static final String PV_HASHKEY = "behavior_pv";
    //    private static final String CART_HASHKEY = "behavior_cart";
    public static final String BUY_HASHKEY = "behavior_buy";

    public BusinessTwo() {
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
     * kafka 参数配置 ads 主题
     *
     * @return
     */
    private Map<String, String> getKafkaParams_ads() {
        Map<String, String> params = new HashMap<>();
        Config kafkaConfig = config.getConfig("kafka");
        params.put("metadata.broker.list", kafkaConfig.getString("metadata.broker.list"));
        params.put("auto.offset.reset", kafkaConfig.getString("auto.offset.reset"));
        params.put("group.id", kafkaConfig.getString("group.id_ads"));
        return params;
    }

    /**
     * 业务二实现
     *
     * @throws InterruptedException
     */
    public void runAnalysis_2() throws InterruptedException {
        ssc = createStreamingContext(config);
        ssc.sparkContext().setLogLevel("WARN");
        String topic = config.getString("spark.topic_ads");
        //从kafka读取 ads 主题
        JavaPairInputDStream<String, String> input = KafkaUtils.createDirectStream(
                ssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                getKafkaParams_ads(),
                Sets.newHashSet(topic));

        statisticsByAds(input);

        ssc.start();
        ssc.awaitTermination();
    }

    /**
     * 分别统计每个广告主下所有广告被点击的总次数，并存储到redis，选择合适的图表对结果实时可视化
     * 用sparkSql完成
     *
     * @param input ads: raw_sample
     */
    private void statisticsByAds(JavaPairInputDStream<String, String> input) {
        //读取广告基本信息，filter有用字段 adgroup_id：脱敏过的广告单元 ID；customer_id:脱敏过的广告主 ID；
        JavaPairRDD<String, String> ad_featureRDD = (JavaPairRDD<String, String>) getAd_featureHdfs().
                mapToPair(x -> {
//                     + "," + x.split(",")[3]
                    return new Tuple2<String, String>(x.split(",")[0], x.split(",")[3]);
                });
        /* 不推荐使用JavaPairRDD<String, String> 放入广播变量
        //广告基本信息放在广播变量中
        Broadcast<JavaPairRDD<String, String>> broadcastadFeature = ssc.sparkContext().broadcast(ad_featureRDD);*/
        //input 读入的流,处理为一个 两表join的DSteam结果流
        JavaDStream<String> stringJavaDStream = input.mapToPair(l -> {
//           1:adgroup_id：脱敏过的广告单元 ID； 4: noclk：为 1代表没有点击；为 0代表点击； 5:clk：为 0代表没有点击；为 1代表点击
            return new Tuple2<String, String>(l._2().split("|")[1], l._2().split("|")[4] + "," + l._2().split("|")[5]);
        }).transform(new Function<JavaPairRDD<String, String>, JavaRDD<String>>() {
            @Override
            public JavaRDD<String> call(JavaPairRDD<String, String> jpRDD) throws Exception {
                JavaPairRDD<String, Tuple2<String, String>> pairRDD = (JavaPairRDD<String, Tuple2<String, String>>) jpRDD.join(ad_featureRDD);
                JavaRDD<String> stringJavaRDD = pairRDD.map(line -> {
                    String returnStr = line._1() + "," + line._2()._1().split(",")[0] + line._2()._1().split(",")[1] + line._2()._2();
                    return returnStr;
                });
                return stringJavaRDD;
            }
        });
        //整理好的结果集，转换DataFream 用sql查询。
        stringJavaDStream.foreachRDD((rdd, time) -> {
            SparkSession spark = getInstance(rdd.context().getConf());
            JavaRDD<AdsEntry> adsEntry = rdd.map(new Function<String, AdsEntry>() {
                @Override
                //TODO 字段没有对应好，修改
                public AdsEntry call(String lins) throws Exception {
                    String[] ads = lins.split(",");
                    AdsEntry adsEntry = new AdsEntry();
//                            adsEntry.setUser(Integer.valueOf(ads[0]));
//                            adsEntry.setTime_stamp(Long.valueOf(ads[1]));
//                            adsEntry.setPid(Integer.valueOf(ads[3]));
                    adsEntry.setAdgroup_id(Integer.valueOf(ads[0]));
                    adsEntry.setCustomer_id(Integer.valueOf(ads[1]));
                    adsEntry.setNonclk(Integer.valueOf(ads[2]));
                    adsEntry.setClk(Integer.valueOf(ads[3]));

                    return adsEntry;
                }
            });
            Dataset<Row> wordsDataFrame = spark.createDataFrame(adsEntry, AdsEntry.class);
            wordsDataFrame.createOrReplaceTempView("totleClick");
            Dataset<Row> wordCountsDataFrame = spark.sql("select customer_id, count(adgroup_id) as total from totleClick group by customer_id where nonclk=0 and clk=1");
//            System.out.printf("========= %d =========\n", time.milliseconds());
            wordCountsDataFrame.foreachPartition(rows -> {
//                Jedis jedis = JavaRedisClient.get().getResource();
                Connection conn = DBHelper.getConnection();
                rows.forEachRemaining(row -> {
                    try {
//                        jedis.hincrBy(PV_HASHKEY, row.getString(0), row.getInt(1));
                        JavaDBDao.saveCustomerClickTotal(conn, Integer.parseInt(row.getString(0)),row.getInt(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                conn.close();
            });
        });
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

    public static void main(String[] args) {
        BusinessTwo streaming = new BusinessTwo();
        try {
            streaming.runAnalysis_2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
