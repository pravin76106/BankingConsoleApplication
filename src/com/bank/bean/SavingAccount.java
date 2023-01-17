package com.bank.bean;

import java.time.LocalDate;

public class SavingAccount extends Account {

	public SavingAccount(User user, String accountNo, Double minimumBalance,LocalDate createdDate) {
		super(user, accountNo, minimumBalance,createdDate);
	}

}
