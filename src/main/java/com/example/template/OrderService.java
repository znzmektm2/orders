package com.example.template;

import java.net.URI;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

	@Autowired
	private KafkaTemplate kafkaTemplate;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired(required = true)
	RestTemplate restTemplate;

	/**
	 * 상품 가격 가져오기
	 */

	
	public void getProducts()
	{
	     
	    final String baseUrl = "http://192.168.0.116:8085/products/1";
	     
	    HttpHeaders headers = new HttpHeaders();

	    Product product = restTemplate.getForObject(baseUrl, Product.class);
	    System.out.println(product.getPrice());
	    
	}
}
	
	
	


