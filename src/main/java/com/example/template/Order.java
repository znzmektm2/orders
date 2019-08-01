package com.example.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

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
    private Map<String, Integer> map;  //key=productCode, value=quantity;

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

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

    
}