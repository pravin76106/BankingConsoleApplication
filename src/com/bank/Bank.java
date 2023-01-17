package com.bank;

import java.util.Scanner;

import com.bank.bean.Account;
import com.bank.bean.CurrentAccount;
import com.bank.service.AccountService;
import com.bank.service.Impl.AccountServiceImpl;

public class Bank {

	Scanner sc = new Scanner(System.in);

	public static AccountService bankService = new AccountServiceImpl();

	public static void main(String[] args) {

		Bank b = new Bank();
		b.showBankDashBoard();
	}

	private void showBankDashBoard() {

		while (true) {
			System.out.println("1.Create new account");
			System.out.println("2.Login into account");
			System.out.println("3.Exit");

			switch (sc.nextInt()) {
			case 1:
				  selectAccountType();
				break;
			case 2:
				loginIntoAccount();
				break;
			case 3:
				System.out.println("Thank you");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private void loginIntoAccount() {
		System.out.println("--------------Login-Page--------------\n");

		Boolean flag = true;
		while (flag) {

			System.out.println("Enter your mobile number");
			String mobileNumber = sc.next();
			System.out.println("Enter your password");
			String password = sc.next();

			if (bankService.logIn(mobileNumber, password)) {
				showDashBoard(mobileNumber);
				flag = false;
			} else {
				System.out.println("Invalid mobileNumber or password !!\n");
				System.out.println("Press 1 for Go Back And any other number for Re-Enter ");
				Integer input = sc.nextInt();
				if (input == 1) {
					flag = false;
				}
			}
		}
	}

	private void showDashBoard(String mobileNumber) {

		Account account = bankService.getAccountDetails(mobileNumber);

		System.out.println("-------------User DashBoard------------");
		System.out.println("User Name : " + account.getUser().getFirstName() + " " + account.getUser().getLastName()
				+ " , Account No. : " + account.getAccountNo() + "\n");
		Boolean flag = true;

		while (flag) {
			System.out.println("1.Show Balance");
			System.out.println("2.Withdraw");
			System.out.println("3.Deposit");
			System.out.println("4.transfer");
			System.out.println("5.Show Transactions");
			System.out.println("6.Logout");
			switch (sc.nextInt()) {

			case 1:
				showBalance(account);
				break;

			case 2:
				withDraw(mobileNumber);
				break;
			case 3:
				deposit(mobileNumber);
				break;
			case 4:transfer(mobileNumber);
				break;
			case 5:
				showTransactions(mobileNumber);
				break;
			case 6:
				flag = false;
				break;
			default:
				System.out.println("Invalid input");
			}
		}
	}

	private void transfer(String mobileNumber) {
		System.out.println("---------------------------Transfer Money----------------------------\n");
		while(true) {
			System.out.println("Enter Receiver Account No.");
			String receiverAccountNo = sc.next();
			if(bankService.isAccountExist(receiverAccountNo)) {
				System.out.println("Enter ammount");
				Double ammount =sc.nextDouble();
				
				if(bankService.transfer(ammount, receiverAccountNo, mobileNumber)) {
					System.out.println("Trasnfer "+ammount+" rupees to account "+receiverAccountNo);
				}
				else {
					System.out.println("You don't have sufficient ammount for transfer");
				}
				break;
			}
			else {
				System.out.println("Account No not exist.\n");
				System.out.println("Press 1 for Re-Enter And Other Go Back.\n");
				if(sc.nextInt()!=1) {
					break;
				}
			}
		}
	}

	private void showTransactions(String mobileNumber) {

		Account account = bankService.getAccountDetails(mobileNumber);
		System.out.println("---------------------------------------Transactions-------------------------------------\n");
		account.getTransactions().forEach(transaction -> System.out.println(transaction));
		System.out.println("\n");
	}

	private void deposit(String mobileNumber) {
		System.out.println("-----------------------Deposit---------------------\n");

		System.out.println("Enter amount");
		Double ammount = sc.nextDouble();
		System.out.println("----------------------------------------------------\n");
		if (bankService.deposit(ammount, mobileNumber)) {
			System.out.println("\n Deposit " + ammount + " rupees succesfully\n");
		} else {
			System.out.println("faild to deposit !!\n");
		}
		System.out.println("----------------------------------------------------\n");
	}

	private void withDraw(String mobileNumber) {
		System.out.println("----------------------Withdraw---------------------\n");

		System.out.println("Enter amount");
		Double ammount = sc.nextDouble();
		System.out.println("----------------------------------------------------\n");
		if (bankService.withDraw(ammount, mobileNumber) != null) {
			System.out.println("\n Withdraw " + ammount + " rupees succesfully\n");
		} else {
			System.out.println("you don't have sufficient ammount.\n");

		}
		System.out.println("----------------------------------------------------\n");
	}

	private void showBalance(Account account) {
		System.out.println("\n-----------------------------------------------------\n");
		System.out.println("Total Balance = " + account.getBalance());
		System.out.println("\n-----------------------------------------------------\n");
	}

	private void selectAccountType() {
		Boolean flag = true;
		while (flag) {

			System.out.println("1.Current Account");
			System.out.println("2.Saving Account");
			System.out.println("3.Back");

			switch (sc.nextInt()) {

			case 1: Account currentAccount=openAccount("CURRENT_ACCOUNT");
			        if(currentAccount !=null){
			        	showCurrentAccountDetails(currentAccount);
			        	flag=false;
			        }

				break;
			case 2: Account savingAccount = openAccount("SAVING_ACCOUNT");
			        if(savingAccount !=null){
		        	showSavingAccountDetails(savingAccount);
		        	flag=false;
		        }
				break;
			case 3:
				flag = false;
				break;
			default:
				System.out.println("Invalid Choice");
			}
		}
	}

	private Account openAccount(String accountType) {

		System.out.println("---------------Open Saving Account--------------\n\n");

		System.out.println("Enter first Name");
		String firstName = sc.next();
		System.out.println("Enter last Name");
		String lastName = sc.next();
		System.out.println("Enter password");
		String password = sc.next();
		String mobileNumber = null;
		while (true) {

			System.out.println("Enter mobile number ");
			mobileNumber = sc.next();
			Account newAccount = bankService.openAccount(firstName, lastName, mobileNumber, password,accountType);
			if (newAccount != null) {
				return newAccount;
			} else {

				System.out.println("mobile number is already exits!\n");
				System.out.println("1. Re-Enter mobile number");
				System.out.println("2.Back");
				if (sc.nextInt() == 2) {
					return null;
				}
			}
		}

	}

	private void showSavingAccountDetails(Account savingAccount) {
		System.out.println("---------------Account-Created-Successfully----------- \n");
		System.out.println("Name            :- " + savingAccount.getUser().getFirstName() + " "
				+ savingAccount.getUser().getLastName() + "\n");
		System.out.println("Mobile Number   :- " + savingAccount.getUser().getMobileNumber() + "\n");
		System.out.println("Account No      :- " + savingAccount.getAccountNo() + "\n");
		System.out.println("Balance         :- " + savingAccount.getBalance() + "\n");
		System.out.println("minimum Balance :- " + savingAccount.getMinimumBalance() + "\n");
		System.out.println("Created Date    :- " + savingAccount.getCreatedDate() + "\n");
	}
	
	private void showCurrentAccountDetails(Account account) {
	   CurrentAccount currentAccount =(CurrentAccount)account;
	   System.out.println("Name            :- " + currentAccount.getUser().getFirstName() + " "
				+ currentAccount.getUser().getLastName() + "\n");
		System.out.println("Mobile Number   :- " + currentAccount.getUser().getMobileNumber() + "\n");
		System.out.println("Account No      :- " + currentAccount.getAccountNo() + "\n");
		System.out.println("Balance         :- " + currentAccount.getBalance() + "\n");
		System.out.println("OverDraftLimit :- " + currentAccount.getOverDraftLimit() + "\n");
		System.out.println("Created Date    :- " + currentAccount.getCreatedDate() + "\n");
	}
}
