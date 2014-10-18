package com.inspection.management.model;

import java.io.Serializable;

public class PurchaseOrder extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6816455815804980136L;

	public enum AcceptStatus {
		NONE, ACCEPTED, REJECTED;
	}

	private String orderNumber;
	private String itemName;
	private String itemDetails;
	private String contact;
	private AcceptStatus status;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(String itemDetails) {
		this.itemDetails = itemDetails;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public AcceptStatus getStatus() {
		return status;
	}

	public void setStatus(AcceptStatus status) {
		this.status = status;
	}
}
