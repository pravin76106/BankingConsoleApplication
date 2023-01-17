package com.bank.bean;

import java.time.LocalDateTime;

public class Transaction {
	private String senderAccountNo ;
	private String recipientAccountNo;
	private LocalDateTime dateTime;
	private String operation;
	private Double ammount;
	
	@Override
	public String toString() {
		return " Operation = " + operation +", Ammount = " + ammount +", SenderAccountNo = " + senderAccountNo + ", RecipientAccountNo = " + recipientAccountNo
				+ ", DateTime = " + dateTime ;
	}
	
	public Transaction(String operation,String senderAccountNo, String recipientAccountNo, LocalDateTime dateTime, Double ammount) {
		super();
		this.senderAccountNo = senderAccountNo;
		this.recipientAccountNo = recipientAccountNo;
		this.dateTime = dateTime;
		this.ammount = ammount;
		this.operation=operation;
	}
	
	public String getSenderAccountNo() {
		return senderAccountNo;
	}
	public void setSenderAccountNo(String senderAccountNo) {
		this.senderAccountNo = senderAccountNo;
	}
	public String getRecipientAccountNo() {
		return recipientAccountNo;
	}
	public void setRecipientAccountNo(String recipientAccountNo) {
		this.recipientAccountNo = recipientAccountNo;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public Double getAmmount() {
		return ammount;
	}
	public void setAmmount(Double ammount) {
		this.ammount = ammount;
	}
}
