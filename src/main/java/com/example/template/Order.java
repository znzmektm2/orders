package com.example.template;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.Table;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue
    private Long code;
    private String userId;
    private String paymentType;
    private double total;
    private String productCode;
    private int quantity;
    private String type;
    //userId="js" uerAddr="pangyo" paymentType="credit" total="100000" productCode="1" quantity="1"
    
    /**
     * 주문이 들어옴
     */
    @PostPersist
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

            BeanUtils.copyProperties(this, orderRequested);
            json = objectMapper.writeValueAsString(orderRequested);
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