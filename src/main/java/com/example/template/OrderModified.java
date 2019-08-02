package com.example.template;

import java.util.Map;

public class OrderModified {

    private String type;
    private String stateMessage = "주문이 수정됨";
    
    private Long code;
    private String userAddr;
    //private Map<String, Integer> map;  //key=productCode, value=quantity;
    private String productCode;
    private int quantity;
	public Long getCode() {
		return code;
	}
	
	public OrderModified(){
        this.setType(this.getClass().getSimpleName());
    }
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getStateMessage() {
		return stateMessage;
	}

	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getUserAddr() {
		return userAddr;
	}
	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
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
