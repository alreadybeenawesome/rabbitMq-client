package com.centuryOf22th;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by louis on 17-1-13.
 */
public class App {


    private final static String QUEUE_NAME = "hello";
    static boolean isBreak =false;

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.01");
        factory.setVirtualHost("/");
        factory.setPort(5672);

        try {
            Connection connection = factory.newConnection();
            Channel channel =  connection.createChannel();

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    isBreak=true;
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);


            channel.queueDeclare(QUEUE_NAME, true, true, true, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            while(!isBreak){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            channel.close();
            connection.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
