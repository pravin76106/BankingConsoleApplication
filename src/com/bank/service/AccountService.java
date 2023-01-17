package com.bank.service;

import com.bank.bean.Account;

public interface AccountService {

	public Account openAccount(String firstName,String lastName,String mobileNumber,String password,String accountType);
	public Double withDraw(Double amount,String mobileNumber);
	public Boolean deposit(Double amount,String mobileNumber);
	public Boolean logIn(String mobileNumber, String password);
	public Account getAccountDetails(String mobileNumber);
	public Boolean transfer(Double amount, String receiverAccountNo, String mobileNumber);
	public Boolean isAccountExist(String receiverAccountNo);
}
