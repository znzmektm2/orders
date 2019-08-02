package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue
    private Long code;
    private String userId;
    private String uerAddr;
    private String paymentType;
    private double total;
    private String productCode;
    private int quantity;
    private String type;

    
    /**
     * 주문이 들어옴
     */
    @PostLoad
    private void publishOrderRequested() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        OrderRequested orderRequested = new OrderRequested();
        try {
        	orderRequested.setCode(code);
        	orderRequested.setUserId(userId);
        	orderRequested.setProductCode(productCode);
        	orderRequested.setQuantity(quantity);
        	
        	RestTemplate restTemplate = Application.applicationContext.getBean(RestTemplate.class);
        	
      	//product 테이블에서 가격을 가져오기
        	final String baseUrl = "http://192.168.0.116:8085/products/1";
    	    HttpHeaders headers = new HttpHeaders();
    	    Product product = restTemplate.getForObject(baseUrl, Product.class);
    	    int productPrice = product.getPrice();
        	orderRequested.setTotal(quantity*productPrice);
        	
        	
//        	    orderRequested.setCode(1001L);
//        	    orderRequested.setUserId("js");
//        	    orderRequested.setProductCode("1");
//        	   orderRequested.setTotal(100000);
        	
            BeanUtils.copyProperties(this, orderRequested);
            json = objectMapper.writeValueAsString(orderRequested);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
        kafkaTemplate.send(producerRecord);
    }
    
    
    /**
     * 결제가 완료됨
     */
    @PostPersist
    private void publishPaymentRequested() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        PaymentRequested paymentRequested = new PaymentRequested();
        
        try {
        	paymentRequested.setType(PaymentRequested.class.getSimpleName());
        	paymentRequested.setCode(code);
        	paymentRequested.setUserId(userId);
        	paymentRequested.setType(type);
        	paymentRequested.setProductCode(productCode);
        	paymentRequested.setQuantity(quantity);
            BeanUtils.copyProperties(this, paymentRequested);
            json = objectMapper.writeValueAsString(paymentRequested);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
        kafkaTemplate.send(producerRecord);
    }
        
        
    /**
     * 주문이 수정됨
     */
    @PostUpdate
    private void publishOrderModified() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        OrderModified orderModified = new OrderModified();
        try {
        	orderModified.setCode(code);
        	orderModified.setType(type);
        	orderModified.setProductCode(productCode);
        	orderModified.setQuantity(quantity);
            BeanUtils.copyProperties(this, orderModified);
            json = objectMapper.writeValueAsString(orderModified);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
        kafkaTemplate.send(producerRecord);
    }
    
    /**
     * 주문이 취소됨
     */
    @PostRemove
    private void publishOrderDeleted() {
        KafkaTemplate kafkaTemplate = Application.applicationContext.getBean(KafkaTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        OrderDeleted orderDeleted = new OrderDeleted();
        try {
        	orderDeleted.setCode(code);
        	orderDeleted.setProductCode(productCode);
            BeanUtils.copyProperties(this, orderDeleted);
            json = objectMapper.writeValueAsString(orderDeleted);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        ProducerRecord producerRecord = new ProducerRecord<>("eventTopic", json);
        kafkaTemplate.send(producerRecord);
    }


	public Long getCode() {
		return code;
	}


	public void setCode(Long code) {
		this.code = code;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUerAddr() {
		return uerAddr;
	}


	public void setUerAddr(String uerAddr) {
		this.uerAddr = uerAddr;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	
    

}