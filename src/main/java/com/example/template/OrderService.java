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
    public void onListener(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {
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
             * 주문 요청 시
             * 주문 수량 >  재고량 이면 주문을 취소시키고
             * 주문 수량 <=재고량 이면 주문 요청 접수
             */
            if( orderRequested.getType().equals(OrderRequested.class.getSimpleName())){

            	int stock = product.getStock();
            	if(orderRequested.getQuantity() > stock) {
            		//주문 취소
            		throw new RuntimeException("해당 상품의 재고가 부족해 주문할 수 없습니다.");
            	}
            	
            	//주문 저장
            	Order order = new Order();
                order.setCode(orderRequested.getCode());
                order.setUserId(orderRequested.getUserId());
                order.setProductCode(orderRequested.getProductCode());
                order.setQuantity(orderRequested.getQuantity());
                order.setTotal(orderRequested.getQuantity()*product.getPrice());            
                orderRepository.save(order);

            }
  
        }catch (Exception e){
        
        }

    }

	
	
}
	
	
	


