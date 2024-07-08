package com.demo.dao.borrowDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.demo.beans.Asset;
import com.demo.beans.Borrower;
import com.demo.beans.User;
import com.demo.dbutil.DBUtil;
import com.demo.exceptions.assetExceptions.AssetNotFoundException;
import com.demo.exceptions.borrowExceptions.BorrowerNotFoundException;

public class BorrowDaoImpl implements BorrowDao{
	
	static Connection conn;
	static PreparedStatement pselectAllBorrowers;
	static PreparedStatement pselectById;
	static PreparedStatement pissueAsset;
	static PreparedStatement pisAssetAvailable;
	static PreparedStatement pselectAssetByCategory;
	static PreparedStatement pupdateAssetById;
	static PreparedStatement pUpdateAvailability;
	static PreparedStatement pselectBorrowerByName;
	static PreparedStatement pselectBorrowerByDate;
	static PreparedStatement pselectBorrowerByCategory;
	static PreparedStatement psendMessage;
	static PreparedStatement pupdateAssetTransactionById;
	static PreparedStatement pdeleteBorrowerById;
	static PreparedStatement pgetcategory;
	static PreparedStatement pgetAvailability;
	// within asset : asset_status=1=>available 0=> not available
	// within asset transaction, if status=0 , then asset has not been returned yet else it has been returned
	static {
		try {
			conn=DBUtil.getConnConnection();
			pselectAllBorrowers=conn.prepareStatement("select * from assettransaction where status=0");
			pselectById=conn.prepareStatement("select * from assettransaction where userid=? and status=?");
			pissueAsset=conn.prepareStatement("insert into assettransaction(userid,assetid,dateofissue,dateofreturn,category,status)values(?,?,?,?,?,?)");
			pisAssetAvailable=conn.prepareStatement("select * from Asset_Availability where  category=? and Availability>0");
			pselectAssetByCategory=conn.prepareStatement("select * from asset where assetCategory=? and asset_status=1");
			pupdateAssetById=conn.prepareStatement("update asset set asset_status=? where assetid=?");
			pUpdateAvailability=conn.prepareStatement("update Asset_Availability set Availability=? where category=?");
			pselectBorrowerByName=conn.prepareStatement("select * from assettransaction where userId in (select userId from user where userName=?) and status=0");
			pselectBorrowerByDate=conn.prepareStatement("select * from assettransaction where dateofissue=? and status=0");
			pselectBorrowerByCategory=conn.prepareStatement("select * from assettransaction where category=? and status=0");
			psendMessage=conn.prepareStatement("insert into borrowerMessage(borrowerid,message)values(?,?)");
			pupdateAssetTransactionById=conn.prepareStatement("update assettransaction set status=? where assetid=?");
			pdeleteBorrowerById=conn.prepareStatement("delete from borrowerMessage where borrowerid=?");
			pgetcategory=conn.prepareStatement("select assetcategory from asset where assetid=?");
			pgetAvailability=conn.prepareStatement("select Availability from Asset_Availability where category=?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	@Override
	public List<Borrower> getAllborrowers() throws BorrowerNotFoundException {
		ResultSet rs=null;
		List<Borrower>listOfBorrowers=new ArrayList();
		try {
			rs=pselectAllBorrowers.executeQuery();
			while(rs.next()) {
			   	Borrower borrower=new Borrower(rs.getInt(1),rs.getInt(2),rs.getDate(3),rs.getDate(4),rs.getBoolean(5),rs.getString(6));
			   	listOfBorrowers.add(borrower);
			}
			if(listOfBorrowers.size()==0) {
				throw new BorrowerNotFoundException("No borrower exist");
			}
			return listOfBorrowers;
		} catch (SQLException e) {
			//e.printStackTrace();
		     throw new BorrowerNotFoundException("No borrower exist");
		}
	}

	@Override
	public Borrower getBorrowerById(int id) throws BorrowerNotFoundException {
		ResultSet rs=null;
		try {
			pselectById.setInt(1, id);
			pselectById.setInt(2, 0);
			rs=pselectById.executeQuery();
			if(rs.next()) {
				Borrower borrower=new Borrower(rs.getInt(1),rs.getInt(2),rs.getDate(3),rs.getDate(4),rs.getBoolean(5),rs.getString(4));
				return borrower;
			}
		} catch (SQLException e) {
			throw new BorrowerNotFoundException("No borrower exist");
			
		}
		throw new BorrowerNotFoundException("No borrower exist");
	}
	
	// method that will take user and Asset data as an input and issue an asset
  	public int issueAsset(User user,String category) throws AssetNotFoundException {
  		// check if category of the given asset is available or not
  		try {
			pisAssetAvailable.setString(1, category);
			System.out.println("category set ");
			ResultSet rs=pisAssetAvailable.executeQuery();
			System.out.println("rs aya");
			int total_available=0;
			while(rs.next()) {
				total_available=rs.getInt(2);
			}
			if(total_available==0) {
				throw new AssetNotFoundException("Asset not found");
			}
			System.out.println("total Available"+total_available);
			// now since asset is available, we will get the first asset of this category
			pselectAssetByCategory.setString(1, category);
			rs=pselectAssetByCategory.executeQuery();
			int id=-1;
			int lendingDays=0;
			while(rs.next()) {
				id=rs.getInt(1);
				lendingDays=rs.getInt(6);
			}
			if(id==-1) {
				throw new AssetNotFoundException("Asset not found");
			}
			System.out.println("user id : "+user.getUserId());
			// first make an entry in asset transaction table:
			pissueAsset.setInt(1, user.getUserId());
			pissueAsset.setInt(2, id);
			LocalDate issueDate = LocalDate.now(); // Create a date object
			pissueAsset.setString(3,issueDate.toString());
			// get the lending period of this asset
			System.out.println("Lendng days "+lendingDays);
			LocalDate returnDate=issueDate.plusDays(lendingDays);
			pissueAsset.setString(4, returnDate.toString());
			// since we are issuing our asset so status would  be false
			pissueAsset.setString(5, category);
			pissueAsset.setBoolean(6, false);
			pissueAsset.executeUpdate();
			// Now we have made an entry in our Asset transaction table, so we can remove this asset of this id from asset table
			pupdateAssetById.setInt(2, id); // set assetid
			pupdateAssetById.setInt(1, 0);  // set asset_status
			pupdateAssetById.executeUpdate();
			// and lastly reduce the category by 1 since we have issued an asset for this category
			pUpdateAvailability.setInt(1,total_available-1);
			pUpdateAvailability.setString(2, category);
			pUpdateAvailability.executeUpdate();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//	throw new AssetNotFoundException("Asset cant get issued");
		}
		return 0;
	}

	@Override
	public List<Borrower> getBorowerByName(String name) throws BorrowerNotFoundException {
		ResultSet rs=null;
		List<Borrower>listOfBorrowers=new ArrayList<>();
		try {
			pselectBorrowerByName.setString(1, name);
			System.out.println(pselectBorrowerByName);
			rs=pselectBorrowerByName.executeQuery();
			System.out.println("result set is : "+ rs);
			while(rs.next()) {
			    System.out.println("we came in while loop");
				Borrower borrower=new Borrower(rs.getInt(1),rs.getInt(2),rs.getDate(3),rs.getDate(4),rs.getBoolean(5),rs.getString(6));
			   	listOfBorrowers.add(borrower);
			}
			if(listOfBorrowers.size()==0) {
				throw new BorrowerNotFoundException("borrower with this name doesnot exist");
			}
			return listOfBorrowers;
		} catch (SQLException e) {
		//	e.printStackTrace();
			throw new BorrowerNotFoundException("borrower with this name doesnot exist");
		}
//		return null;
	}

	@Override
	public List<Borrower> getBorowerByDate(LocalDate date) throws BorrowerNotFoundException {
		ResultSet rs=null;
		List<Borrower>listOfBorrowers=new ArrayList<>();
		try {
			pselectBorrowerByDate.setString(1, date.toString());
			System.out.println(pselectBorrowerByDate);
			rs=pselectBorrowerByDate.executeQuery();
			while(rs.next()) {
				Borrower borrower=new Borrower(rs.getInt(1),rs.getInt(2),rs.getDate(3),rs.getDate(4),rs.getBoolean(5),rs.getString(6));
			   	listOfBorrowers.add(borrower);
			}
			if(listOfBorrowers.size()==0) {
				throw new BorrowerNotFoundException("borrower with this issuedate doesnot exist");
			}
			return listOfBorrowers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Borrower> getBorowerByCategory(String category) throws BorrowerNotFoundException {
		ResultSet rs=null;
		List<Borrower>listOfBorrowers=new ArrayList<>();
		try {
			pselectBorrowerByCategory.setString(1, category);
			rs=pselectBorrowerByCategory.executeQuery();
			
			while(rs.next()) {
				Borrower borrower=new Borrower(rs.getInt(1),rs.getInt(2),rs.getDate(3),rs.getDate(4),rs.getBoolean(5),rs.getString(6));
			   	listOfBorrowers.add(borrower);
			}
			if(listOfBorrowers.size()==0) {
				throw new BorrowerNotFoundException("borrower with this category doesnot exist");
			}
			return listOfBorrowers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int sendMessage(int borrowerId,String message) {
		try {
			psendMessage.setInt(1, borrowerId);
			psendMessage.setString(2, message);
			int status=psendMessage.executeUpdate();
			return status;
		}catch(SQLException e) {
			return 0;
		}
	}

	@Override
	public int returnAsset(int borrowerId, int assetId) {
		try {
			// updation in Asset table
			pupdateAssetById.setInt(1, 1);
			pupdateAssetById.setInt(2, assetId);
			int status =pupdateAssetById.executeUpdate();
			System.out.println("update asset ka status : "+ status);
			
			// updation in Asset transaction table
			pupdateAssetTransactionById.setInt(1, 1);
			pupdateAssetTransactionById.setInt(2, assetId);
			status=pupdateAssetTransactionById.executeUpdate();
			System.out.println("update asset transaction ka status : "+ status);
			
			// deletion from borrower table
			pdeleteBorrowerById.setInt(1, borrowerId);
		status=pdeleteBorrowerById.executeUpdate();
		System.out.println("deletion ka status : "+ status);
			// increase availability in Availability table:
			// 1) get category
			pgetcategory.setInt(1,assetId);
			System.out.println("set hua ");
			ResultSet rs=pgetcategory.executeQuery();
			
			String category="";
			while(rs.next()) {
				category=rs.getString(1);
			}
			System.out.println("Category is : "+category);
			// 2) get availability
			pgetAvailability.setString(1,category);
			rs=pgetAvailability.executeQuery();
			int availability=0;
			while(rs.next()) {
				availability=rs.getInt(1);
			}
			System.out.println("Availability is : "+availability);
			pUpdateAvailability.setInt(1, availability+1);
			pUpdateAvailability.setString(2, category);
            status=pUpdateAvailability.executeUpdate();	
            return status;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}


}
