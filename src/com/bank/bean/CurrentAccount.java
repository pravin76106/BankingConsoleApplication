package com.bank.bean;


import java.time.LocalDate;


public class CurrentAccount extends Account   {

	private final  Double overDraftLimit;
	private Double overDraftRate=1.0;
    private Double overDraftRemainingBalance;
	public CurrentAccount(User user, String accountNo, Double minimumBalance,Double overDraftLimit,LocalDate createdDate) {
		super(user, accountNo,minimumBalance,createdDate);
	    this.overDraftLimit=overDraftLimit;
	    this.overDraftRemainingBalance=overDraftLimit;
	}
	public Double getOverDraftLimit() {
		return overDraftLimit;
	}
	public Double getOverDraftRate() {
		return overDraftRate;
	}
	public void setOverDraftRate(Double overDraftRate) {
		this.overDraftRate = overDraftRate;
	}
	public Double getOverDraftRemainingBalance() {
		return overDraftRemainingBalance;
	}
	public void setOverDraftRemainingBalance(Double overDraftRemainingBalance) {
		this.overDraftRemainingBalance = overDraftRemainingBalance;
	}
	
	public void setAmountToCurrentAccountBalance(Double amount) {

		if (this.getOverDraftRemainingBalance() < this.getOverDraftLimit()) {
			Double overDraftedAmount = this.getOverDraftLimit()
					- this.getOverDraftRemainingBalance();
			if (amount <= overDraftedAmount) {
				this.setOverDraftRemainingBalance(this.getOverDraftRemainingBalance() + amount);
			} else {
				this.setOverDraftRemainingBalance(
						this.getOverDraftRemainingBalance() + overDraftedAmount);
				this.setBalance(amount - overDraftedAmount);
			}
		} else {
			this.setBalance(this.getBalance() + amount);
		}
	}
	
	public Double getAmountFromCurrentAccountBalance(Double amount) {

		if (this.getBalance() >= amount) {
			this.setBalance(this.getBalance() - amount);
			return amount;
		} else if (this.getBalance() + this.getOverDraftRemainingBalance() >= amount) {
			Double senderAccountBalance = this.getBalance();
			this.setBalance(0.0);
			this.setOverDraftRemainingBalance(
					this.getOverDraftRemainingBalance() + senderAccountBalance - amount);
		    return amount;
		}
		else {
			return null;
		}
	}
}
