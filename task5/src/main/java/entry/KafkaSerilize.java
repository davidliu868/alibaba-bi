package entry;

import java.io.Serializable;

/**
 * Created by huser on 5/14/19.
 */
public class KafkaSerilize implements Serializable {
    private String topic;
    private String key;
    private String value;
    public KafkaSerilize(String topic, String key, String value) {
        this.topic = topic;
        this.key = key;
        this.value = value;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
