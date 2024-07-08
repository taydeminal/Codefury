package com.demo.service.borrowService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.demo.beans.Borrower;
import com.demo.beans.User;
import com.demo.exceptions.assetExceptions.AssetNotFoundException;
import com.demo.exceptions.borrowExceptions.BorrowerNotFoundException;

public interface BorrowInterface {
	
	// method to display all borrowers
	public List<Borrower> getAllborrowers() throws BorrowerNotFoundException;
	
	// method to get borrower by id
	public Borrower getBorrowerById(int id) throws BorrowerNotFoundException;
  
	// method to get borrower by name
	public List<Borrower> getBorowerByName(String name) throws BorrowerNotFoundException;
	
	// method to get borrower by date
	public List<Borrower> getBorowerByDate(LocalDate date) throws BorrowerNotFoundException;
	
	// method to get borrower by date
	public List<Borrower> getBorowerByCategory(String category) throws BorrowerNotFoundException;
		
	// method that enable admin to send message to borrower
	public int sendMessage(int borrowerId,String message);
	
	// method that will perform return Asset operation by a borrower
	public int returnAsset(int borrowerId, int assetId);
	public int issueAsset(User user,String category) throws AssetNotFoundException;

}
