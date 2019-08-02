package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

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
    
    
    //private Map<String, Integer> map;  //key=productCode, value=quantity;

    
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
        	
        	//product 테이블에서 가격을 가져와야 됨
        	int price=2500;
        	orderRequested.setTotal(quantity*price);
        	
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