package com.example.template;

import java.util.Map;

public class OrderDeleted {

	   private String type;
	    private String stateMessage = "주문이 삭제됨";
	    
	    private Long code;
	    private String userId;
	    private Map<String, Integer> map;  //key=productCode, value=quantity;
	    
	    public OrderDeleted(){
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
		public Map<String, Integer> getMap() {
			return map;
		}
		public void setMap(Map<String, Integer> map) {
			this.map = map;
		}
}
