package com.example.template;

import java.util.Map;

public class PaymentRequested {

		private String type;
	    private String stateMessage = "결제가 완료됨";
	
	    private Long code;
	    private String userId;
	    private String paymentType;
	    private Map<String, Integer> map;  //key=productCode, value=quantity;
	    
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
		public Map<String, Integer> getMap() {
			return map;
		}
		public void setMap(Map<String, Integer> map) {
			this.map = map;
		}

	    
}
