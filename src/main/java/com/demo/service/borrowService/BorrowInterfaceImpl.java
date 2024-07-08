package com.demo.service.borrowService;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.demo.beans.Borrower;
import com.demo.beans.User;
import com.demo.dao.borrowDao.BorrowDao;
import com.demo.dao.borrowDao.BorrowDaoImpl;
import com.demo.exceptions.assetExceptions.AssetNotFoundException;
import com.demo.exceptions.borrowExceptions.BorrowerNotFoundException;

public class BorrowInterfaceImpl implements BorrowInterface {
	
	BorrowDao dao;
	    public BorrowInterfaceImpl(){
	    	dao=new BorrowDaoImpl();
	    }

	@Override
	public List<Borrower> getAllborrowers() throws BorrowerNotFoundException {
		List<Borrower> users=dao.getAllborrowers();
		return users;
	}

	@Override
	public Borrower getBorrowerById(int id) throws BorrowerNotFoundException {
		Borrower user=dao.getBorrowerById(id);
		return user;
	}

	@Override
	public List<Borrower> getBorowerByName(String name) throws BorrowerNotFoundException {
		List<Borrower>users=dao.getBorowerByName(name);
		return users;
	}

	@Override
	public List<Borrower> getBorowerByDate(LocalDate date) throws BorrowerNotFoundException {
		List<Borrower> borrowers=dao.getBorowerByDate(date);
		return borrowers;
	}

	@Override
	public List<Borrower> getBorowerByCategory(String category) throws BorrowerNotFoundException {
		List<Borrower>borrowers=dao.getBorowerByCategory(category);
		return borrowers;
	}

	@Override
	public int sendMessage(int borrowerId,String message) {
		int status =dao.sendMessage(borrowerId,message);
		return status;
	}

	@Override
	public int returnAsset(int borrowerId, int assetId) {
		int status=dao.returnAsset(borrowerId, assetId);
		return status;
	}

	@Override
	public int issueAsset(User user, String category) throws AssetNotFoundException {
		int status;
		try {
			status = dao.issueAsset(user, category);
		} catch (AssetNotFoundException | SQLException e) {
			throw new AssetNotFoundException("Asset not found");
			//e.printStackTrace();
		}
		return status;
	}

}
