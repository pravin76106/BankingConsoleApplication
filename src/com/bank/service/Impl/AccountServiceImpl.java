package com.bank.service.Impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.bank.bean.CurrentAccount;
import com.bank.bean.SavingAccount;
import com.bank.bean.Transaction;
import com.bank.bean.Account;

import com.bank.bean.User;
import com.bank.service.AccountService;

public class AccountServiceImpl implements AccountService {

	private static final String SAVING_ACCOUNT = "SAVING_ACCOUNT";
	private static final String CURRENT_ACCOUNT = "CURRENT_ACCOUNT";

	public static Map<String, Account> accounts = new HashMap<String, Account>();

	@Override
	public Account openAccount(String firstName, String lastName, String mobileNumber, String password,
			String accountType) {

		if (!accounts.containsKey(mobileNumber)) {
			User newuser = new User(firstName, lastName, mobileNumber, password);
			if (accountType.equals(SAVING_ACCOUNT)) {
				accounts.put(mobileNumber, new SavingAccount(newuser, generateAccountNo(), 200.0, LocalDate.now()));
			} else if (accountType.equals(CURRENT_ACCOUNT)) {
				accounts.put(mobileNumber,
						new CurrentAccount(newuser, generateAccountNo(), 0.0, 5000.0, LocalDate.now()));
			}

			return accounts.get(mobileNumber);
		} else {
			return null;
		}

	}

	private String generateAccountNo() {

		return "BOB0" + (accounts.size() + 1);
	}

	@Override
	public Double withDraw(Double amount, String mobileNumber) {

		Account account = accounts.get(mobileNumber);
		Transaction newTransaction = new Transaction("withdraw", " ", " ", LocalDateTime.now(), amount);
		
		if (account instanceof CurrentAccount) {
			CurrentAccount currentAccount = (CurrentAccount) account;     
			if (currentAccount.getAmountFromCurrentAccountBalance(amount) != null) {
			    currentAccount.getTransactions().add(newTransaction);
				return amount;
			} else {
				return null;
			}
		} else if (account instanceof SavingAccount) {
			if (account.getBalance() - account.getMinimumBalance() >= amount) {
				account.setBalance(account.getBalance() - amount);
				account.getTransactions().add(newTransaction);
				return amount;
			} else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public Boolean deposit(Double ammount, String mobileNumber) {

		Account account = accounts.get(mobileNumber);
	
		if (account != null) {
			Transaction newTransaction = new Transaction("Deposit", " ", " ", LocalDateTime.now(), ammount);
			if (account instanceof CurrentAccount) {
				CurrentAccount currentAccount = (CurrentAccount) account;
				currentAccount.setAmountToCurrentAccountBalance(ammount);
				currentAccount.getTransactions().add(newTransaction);
				return true;
			}
			else if(account instanceof SavingAccount) {
				account.setBalance(account.getBalance() + ammount);
				account.getTransactions().add(newTransaction);
				return true;
			}
			else {
				return false;	
			}
		} else {
			return false;
		}

	}

	@Override
	public Boolean logIn(String mobileNumber, String password) {

		if (accounts.containsKey(mobileNumber) && accounts.get(mobileNumber).getUser().getPassword().equals(password)) {
			return true;
		}
		return false;

	}

	@Override
	public Account getAccountDetails(String mobileNumber) {
		return accounts.get(mobileNumber);
	}

	@Override
	public Boolean transfer(Double amount, String receiverAccountNo, String mobileNumber) {

		Account senderAccount = accounts.get(mobileNumber);
		Account receiverAccount = accounts.values().stream()
				.filter(account -> account.getAccountNo().equals(receiverAccountNo)).findFirst().get();

		if (senderAccount instanceof SavingAccount && receiverAccount instanceof SavingAccount) {
			return transferAmountFromSavingAcToSavingAc(amount, senderAccount, receiverAccount);
		} else if (senderAccount instanceof CurrentAccount && receiverAccount instanceof SavingAccount) {
			return transferAmountFromCurrentAcToSavingAc(amount, senderAccount, receiverAccount);
		} else if (senderAccount instanceof SavingAccount && receiverAccount instanceof CurrentAccount) {
			return transferAmountFromSavingAcToCurrentAc(amount, senderAccount, receiverAccount);
		} else {
			return transferAmountFromCurrentAcToCurrentAc(amount, senderAccount, receiverAccount);
		}
	}

	private Boolean transferAmountFromCurrentAcToCurrentAc(Double amount, Account senderAccount,
			Account receiverAccount) {

		CurrentAccount senderCurrentAc = (CurrentAccount) senderAccount;
		CurrentAccount receiverCurrentAc = (CurrentAccount) receiverAccount;

		Transaction newTransactionForSender = new Transaction("transfer", senderCurrentAc.getAccountNo(),
				receiverCurrentAc.getAccountNo(), LocalDateTime.now(), amount);
		Transaction newTransactionForReceiver = new Transaction("received", senderCurrentAc.getAccountNo(),
				receiverCurrentAc.getAccountNo(), LocalDateTime.now(), amount);

		Boolean flag = false;
		if (senderCurrentAc.getBalance() >= amount) {
			senderCurrentAc.setBalance(senderCurrentAc.getBalance() - amount);
			flag = true;
		} else if (senderCurrentAc.getBalance() + senderCurrentAc.getOverDraftRemainingBalance() >= amount) {
			senderCurrentAc.getAmountFromCurrentAccountBalance(amount);
			flag = true;
		} else {
			flag = false;
		}

		if (flag) {
			receiverCurrentAc.setAmountToCurrentAccountBalance(amount);
			senderCurrentAc.getTransactions().add(newTransactionForSender);
			receiverCurrentAc.getTransactions().add(newTransactionForReceiver);
		}
		return flag;

	}

	private Boolean transferAmountFromSavingAcToCurrentAc(Double amount, Account senderAccount,
			Account receiverAccount) {
		CurrentAccount receiverCurrentAccount = (CurrentAccount) receiverAccount;

		if (senderAccount.getBalance() - senderAccount.getMinimumBalance() >= amount) {
			Transaction newTransactionForSender = new Transaction("transfer", senderAccount.getAccountNo(),
					receiverAccount.getAccountNo(), LocalDateTime.now(), amount);
			Transaction newTransactionForReceiver = new Transaction("received", senderAccount.getAccountNo(),
					receiverAccount.getAccountNo(), LocalDateTime.now(), amount);

			senderAccount.setBalance(senderAccount.getBalance() - amount);
			senderAccount.getTransactions().add(newTransactionForSender);
			receiverCurrentAccount.setAmountToCurrentAccountBalance(amount);
			receiverCurrentAccount.getTransactions().add(newTransactionForReceiver);
			return true;
		} else {
			return false;
		}
	}

	private Boolean transferAmountFromSavingAcToSavingAc(Double amount, Account senderAccount,
			Account receiverAccount) {

		if (senderAccount.getBalance() - senderAccount.getMinimumBalance() >= amount) {
			Transaction newTransactionForSender = new Transaction("transfer", senderAccount.getAccountNo(),
					receiverAccount.getAccountNo(), LocalDateTime.now(), amount);
			Transaction newTransactionForReceiver = new Transaction("received", senderAccount.getAccountNo(),
					receiverAccount.getAccountNo(), LocalDateTime.now(), amount);

			senderAccount.setBalance(senderAccount.getBalance() - amount);
			senderAccount.getTransactions().add(newTransactionForSender);
			receiverAccount.setBalance(receiverAccount.getBalance() + amount);
			receiverAccount.getTransactions().add(newTransactionForReceiver);
			return true;
		} else {
			return false;
		}
	}

	private Boolean transferAmountFromCurrentAcToSavingAc(Double amount, Account senderAccount,
			Account receiverAccount) {
		CurrentAccount senderCurrentAccount = (CurrentAccount) senderAccount;

		Transaction newTransactionForSender = new Transaction("transfer", senderCurrentAccount.getAccountNo(),
				receiverAccount.getAccountNo(), LocalDateTime.now(), amount);
		Transaction newTransactionForReceiver = new Transaction("received", senderCurrentAccount.getAccountNo(),
				receiverAccount.getAccountNo(), LocalDateTime.now(), amount);
		Boolean flag = false;
		if (senderCurrentAccount.getBalance() >= amount) {
			senderCurrentAccount.setBalance(senderCurrentAccount.getBalance() - amount);
			flag = true;
		} else if (senderCurrentAccount.getBalance() + senderCurrentAccount.getOverDraftRemainingBalance() >= amount) {
			senderCurrentAccount.getAmountFromCurrentAccountBalance(amount);
			flag = true;
		} else {
			flag = false;
		}

		if (flag) {
			senderCurrentAccount.getTransactions().add(newTransactionForSender);
			receiverAccount.setBalance(receiverAccount.getBalance() + amount);
			receiverAccount.getTransactions().add(newTransactionForReceiver);
		}
		return flag;
	}

	@Override
	public Boolean isAccountExist(String receiverAccountNo) {
		return accounts.values().stream().filter(account -> account.getAccountNo().equals(receiverAccountNo))
				.findFirst().isPresent() ? true : false;
	}

}
