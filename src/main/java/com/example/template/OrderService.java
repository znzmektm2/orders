package com.example.template;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

	@Autowired
	private KafkaTemplate kafkaTemplate;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired(required = true)
	private RestTemplate restTemplate;
	
	private Product product;

	/**
	 * 주문 수량과 상품 재고 비교
	 */
	@KafkaListener(topics = "eventTopic")
    public boolean onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
		boolean zeroStock = false;
        System.out.println("##### listener : " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OrderRequested orderRequested = null;
        try {
        	orderRequested = objectMapper.readValue(message, OrderRequested.class);
        	
        	/**
        	 * 상품에 대한 데이터 가져오기
        	 */
        	final String baseUrl = "http://192.168.0.116:8085/products/1";
    	    HttpHeaders headers = new HttpHeaders();
    	    product = restTemplate.getForObject(baseUrl, Product.class);

            /**
             * 주문 요청 시 재고 수량 확인하기
             */
            if( orderRequested.getType().equals(OrderRequested.class.getSimpleName())){

                Order order= orderRepository.findByCode(orderRequested.getCode());
                if(order.getQuantity() > product.getStock())  zeroStock = true;
                    
              // 재고값이 0이면 주문을 취소시킨다.
                     
                
            }
            
        }catch (Exception e){
        
        }
        return zeroStock;
    }

	
	
}
	
	
	


