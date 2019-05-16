package streaming;

import com.google.common.collect.Sets;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dao.JavaDBDao;
import db.DBHelper;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BusinessOne {
    public Config config;
    public JavaStreamingContext ssc;
    public static SparkSession instance = null;
    public static final String PV_HASHKEY = "behavior_pv";
    //    private static final String CART_HASHKEY = "behavior_cart";
    public static final String BUY_HASHKEY = "behavior_buy";

    public BusinessOne() {
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
     * kafka 参数配置  bebavior主题
     *
     * @return
     */
    private Map<String, String> getKafkaParams_bebavior() {
        Map<String, String> params = new HashMap<String, String>();
        Config kafkaConfig = config.getConfig("kafka");
        params.put("metadata.broker.list", kafkaConfig.getString("metadata.broker.list"));
        params.put("auto.offset.reset", kafkaConfig.getString("auto.offset.reset"));
        params.put("group.id", kafkaConfig.getString("group.id_behavior"));
        return params;
    }

    /**
     * 业务一实现
     *
     * @throws InterruptedException
     */
    public void runAnalysis_1() throws InterruptedException {
        ssc = createStreamingContext(config);
        ssc.sparkContext().setLogLevel("WARN");
        String topic = config.getString("spark.topic_behavior");
        //从kafka读取behavior主题
        JavaPairInputDStream<String, String> input = KafkaUtils.createDirectStream(
                ssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                getKafkaParams_bebavior(),
                Sets.newHashSet(topic));

        input.cache();
//        分别统计每个品牌商品类目实时浏览次数、实时被放入购物车次数、实时购买次数和 实时购买总额 ，并存入redis
        //每个商品类目实时浏览次数
        statistics_pv(input);
        //和实时购买总额
//        statistics_totalPrice(input);
        ssc.start();
        ssc.awaitTermination();
    }

    /**
     * 实时购买总额 统计
     *
     * @param input
     */
    private void statistics_totalPrice(JavaPairInputDStream<String, String> input) {
        //从hdfs读取ad_feature广告基本信息数据， Broadcast TODO hdfs路径待确认，暂时写死
        JavaRDD<String> ad_file = ssc.sparkContext().textFile("/home/huser/tb_data_test/ad_feature.csv");
        JavaPairRDD<String, String> ad_featureJavaPairRDD = ad_file.mapToPair(x -> {
            String[] ads_list = x.split(",");
            // cate_id:brand,price
            return new Tuple2<String, String>(ads_list[1] + ":" + ads_list[4], ads_list[5].toString());
        });
        List<Tuple2<String, String>> take1 = ad_featureJavaPairRDD.take(9);
        //-----用户行为日志，实时购买记录筛选 buy --------
        JavaPairDStream<String, Integer> buy =
                input.filter(x -> x._2().split("\\|").length>3)
                        .filter(x -> x._2().split("\\|")[1].contains("buy"))
//                        .repartition(60)//没有coalesce TODO 测试？？？
                        .mapToPair(k -> {
                            String cate_brand = "";
                            String[] pv_list = k._2().split("\\|");
                            if (pv_list.length == 4) {
                                cate_brand = pv_list[2] + ":" + pv_list[3];
                            }
                            return new Tuple2<>(cate_brand, 1);
                        });
        //-----购买 价格 --------join---cate_brand,1; cate_brand,price-------------
        //-----reduceByKey price-----
        JavaDStream<String> finalPrice = buy.transform(new Function<JavaPairRDD<String, Integer>, JavaRDD<String>>() {
                          @Override
                          public JavaRDD<String> call(JavaPairRDD<String, Integer> rdd) throws Exception {
                              JavaPairRDD<String, String> stringDoubleJavaPairRDD = rdd.join(ad_featureJavaPairRDD).mapToPair(line -> {
                                  String cate_brand = line._1();
                                  String price = line._2()._2();
                                  return new Tuple2<String, String>(cate_brand, price);
                              });

                              return stringDoubleJavaPairRDD.map(x->x._1()+","+x._2());
                          }
                      }
        );

        JavaPairDStream<String, Double> stringDoubleJavaPairDStream = finalPrice.mapToPair(x -> {
            return new Tuple2<String, Double>(x.split(",")[0], Double.parseDouble(x.split(",")[1]));
        }).reduceByKey((d1,d2)->d1+d2);
        stringDoubleJavaPairDStream.foreachRDD(rdd-> {
                rdd.foreachPartition(stringIterator-> {
//                        Connection conn = DBHelper.getConnection();
                        while (stringIterator.hasNext()) {
                            try {
                                String[] split = stringIterator.next()._1().split(":");
                                // 发送到redis，可以修改为保存到mysql
//                                jedis.hincrBy(BUY_HASHKEY, Integer.parseInt(split[0]), Integer.parseInt(split[1]), stringIterator.next()._2());
//                                JavaDBDao.saveCateBuyTotalPrice(conn, Integer.parseInt(split[0]), Integer.parseInt(split[1]), stringIterator.next()._2());
                                System.out.println(Integer.parseInt(split[0])+"======"+Integer.parseInt(split[1])+"====="+stringIterator.next()._2());
                            } catch (Exception e) {
                                System.out.println("error:" + e);
                            }
                        }
//                        conn.close();
                });
        });
    }

    //分别统计每个品牌商品类目实时浏览次数--pv
    private void statistics_pv(JavaPairInputDStream<String, String> input) {
        JavaPairDStream<String, Integer> pv =
                input.filter(x -> x._2().split("\\|").length > 1)
                        .filter(x -> x._2().split("\\|")[1].contains("pv"))
//                .repartition(60)//没有coalesce TODO 测试？？？
                        .mapToPair(k -> {
                            String cate_brand = "";
                            String[] pv_list = k._2().split("\\|");
                            if (pv_list.length == 4)
                                cate_brand = pv_list[2] + ":" + pv_list[3];
                            return new Tuple2<>(cate_brand, 1);
                        }).reduceByKey((x, y) -> x + y);


        pv.foreachRDD(rdd -> {
            rdd.foreachPartition(partitions -> {
//                Connection conn = DBHelper.getConnection();
                while (partitions.hasNext()) {
                    try {
                        Tuple2<String, Integer> next = partitions.next();
                        // 发送到redis，可以修改为保存到mysql
//                                jedis.hincrBy(PV_HASHKEY, pv._1(), Integer.parseInt(pv._2().toString()));
                        //cate,brand,count
//                                JavaDBDao.savePvCountResult(conn, Integer.parseInt(next._1().split(":")[0]), Integer.parseInt(next._1.split(":")[1]), Integer.parseInt(next._2().toString()));
                        System.out.println(next._1().split(":")[0].toString() + " === "+ next._1().split(":")[1].toString() + "======================"+Integer.parseInt(next._2().toString())+"=============");
                    } catch (Exception e) {
                        System.out.println("error:" + e);
                    }
                }
//                conn.close();
            });
        });
    }

    /**
     * 获取ad_feature表信息
     *
     * @return
     */
   /* public JavaRDD<String> getAd_featureHdfs() {
//        JavaRDD<String> stringJavaRDD = ssc.sparkContext().textFile("hdfs://192.168.10.132:9000/tb_data/ad_feature/part-m-00000");
        JavaRDD<String> stringJavaRDD = ssc.sparkContext().textFile("/home/huser/tb_data_test/ad_feature");
        return stringJavaRDD;
    }*/

    public static void main(String[] args) {
        BusinessOne streaming = new BusinessOne();
        try {
            streaming.runAnalysis_1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
