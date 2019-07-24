package com.bext;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLog {
    private static final String EXCHANGE_NAME = "logs";
    
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		String routingKey = "";
		channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
		
		System.out.println("[x] Esperando por Mensajes. CTRL+C para salir.");
		
		DeliverCallback deliverCallback = (coneumerTag, delivery) -> {
			String message = new String(delivery.getBody(),"UTF-8");
			System.out.println("[x] Recivido '" + message + "'");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag ->{});
	}

}
