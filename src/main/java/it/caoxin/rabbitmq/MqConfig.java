package it.caoxin.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MqConfig {

    /**
     * 抢购系统的逻辑
     */
    public static final String PBS_QUEUE = "pbs_queue";
    @Bean
    public Queue pbsQueue(){
        return new Queue(PBS_QUEUE,true);
    }

    public static final String QUEUE = "queue";

    /**
     * Direct模式 / Exchange交换机模式
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
    /**
     * Topic模式 交换机Exchange
     */
    public static final String TOPIC_QUEUE1 = "topicqueue1";
    public static final String TOPIC_QUEUE2 = "topicqueue2";
    public static final String TOPIC_ENXCHANGE = "topic_exchange";

    /**
     * 创建两个队列
     * 创建一个话题路由:相当于一个交换机
     * （接受对应的匹配内容去匹配不同的topic_key，根据key去到不同的消息队列）
     * 创建路由绑定
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_ENXCHANGE);
    }

    @Bean
    public Binding topic1Binding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topic1Binding2(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.#");
    }


    /**
     * Fanout模式 广播模式
     */
    public static final String HEADERS_QUEUE = "headerqueue";
    public static final String FANOUT_EXCHANGE = "fanoutexchange";

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding FanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding FanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
     * headersExchange 模式
     * 特殊的规则
     */
    public static final String HEADERS_EXCHANGE = "headersexchange";

    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headerQueue(){
        return new Queue(HEADERS_QUEUE,true);
    }
    @Bean
    public Binding headerBinding(){
        Map<String,Object> map = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }





}
