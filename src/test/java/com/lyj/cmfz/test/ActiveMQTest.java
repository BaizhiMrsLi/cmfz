package com.lyj.cmfz.test;

public class ActiveMQTest extends Basetest {
   /* @Test
    public void testProduct() throws JMSException {
        String url = "tcp://192.168.94.137:61616";
        //1.创建工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        //2.创建链接
        Connection connection = activeMQConnectionFactory.createConnection();
        //3.创建会话
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //4.创建生产者
        //创建目的地
        Destination destination = new ActiveMQQueue("producter");
        MessageProducer producer = session.createProducer(destination);
        //5.创建消息
        TextMessage textMessage = session.createTextMessage("hello world!");
        //6.使用生产者发送消息
        producer.send(textMessage);
        //7.提交
        session.commit();
        //8.关流
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testConsumer() throws JMSException {
       *//*
            1. 指定url路径
            2. 创建连接工厂
            3. 创建连接
            4. 创建session
            5. 创建目的地
            6. 创建消费者
            7. 接受消息
            8. commit
            9. close
         *//*
       String url = "tcp://192.168.94.137:61616";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Destination destination = new ActiveMQQueue("producter");
        MessageConsumer consumer = session.createConsumer(destination);
        TextMessage receive = (TextMessage) consumer.receive();
        String text = receive.getText();
        System.out.println(text);
        session.commit();
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testPublisher() throws JMSException {
        String url = "tcp://192.168.94.137:61616";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        TopicConnection topicConnection = activeMQConnectionFactory.createTopicConnection();
        TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher publisher = topicSession.createPublisher(new ActiveMQTopic("publisher"));
        TextMessage textMessage = topicSession.createTextMessage("hello world!");
        publisher.publish(textMessage);
        topicSession.commit();
        publisher.close();
        topicSession.close();
        topicConnection.close();
    }

    @Test
    public void testSuber() throws JMSException {
        String url = "tcp://192.168.94.137:61616";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
        TopicConnection topicConnection = activeMQConnectionFactory.createTopicConnection();
        topicConnection.start();
        TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        TopicSubscriber publisher = topicSession.createSubscriber(new ActiveMQTopic("publisher"));
        //由于是订阅者模式，所有在发布之前就需要先开启，才能接收开启之后发布者发布的消息
        while (true){
            TextMessage receive = (TextMessage) publisher.receive();
            String text = receive.getText();
            System.out.println(text);
            topicSession.commit();
        }
    }*/
}
