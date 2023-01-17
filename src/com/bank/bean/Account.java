package com.bank.bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Account {

	private User user;
	private String accountNo;
	private Double balance=0.0;
	private Double intrestRate=0.4;
	private Double minimumBalance=0.0;
	private  List<Transaction>  transactions=new ArrayList<Transaction>();
	private LocalDate createdDate;

	public Account(User user, String accountNo, Double minimumBalance, LocalDate createdDate) {
		super();
		this.user = user;
		this.accountNo = accountNo;
		this.createdDate = createdDate;
		this.minimumBalance=minimumBalance;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getIntrestRate() {
		return intrestRate;
	}

	public void setIntrestRate(Double intrestRate) {
		this.intrestRate = intrestRate;
	}

	public Double getMinimumBalance() {
		return minimumBalance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void setMinimumBalance(Double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	@Override
	public String toString() {
		return "AccountDetails [accountNo=" + accountNo + ", balance=" + balance + ", createdDate=" + createdDate + "]";
	}

}
