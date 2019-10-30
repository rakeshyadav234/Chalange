package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }
  
  @override
  public Account transferFund(Account account) throws FundTransferException {
	  
	    Account debitAccount = accounts.get(account.getAccountId());
	    Account creditAccount = accounts.get(account.getCreditAccountId());

	  
	//Check debit AC has sufficient balance - Account
	if(debitAccount.getAmount() >=account.getAmount()) {
		
		if(creditAccount == null) {
			// Credit account not exist	
			
			throw new FundTransferException(
			        "Credit Account id " + account.getCreditAccountId() + " Not exists!");
			
		}
		
		// Update debit account 
		debitAccount.setAmount(debitAccount.getAmount() - account.getAmount());
		accounts.put(debitAccount.getAccountId(), debitAccount);
				
		// Update Credit Account
		creditAccount.setAmount(creditAccount.getAmount() + account.getAmount());
		accounts.put(creditAccount.getAccountId(), creditAccount)
		
		
	}
	else {
		// INsufficient balance, please enter amount
		throw new FundTransferException(
		        "INsufficient balance in  " + account.getAccountId() + "please enter less amount" );
		

	}

	  return account;
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

}
