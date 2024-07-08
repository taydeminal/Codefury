package com.demo.service.assetService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.demo.beans.Asset;
import com.demo.beans.User;
import com.demo.exceptions.assetExceptions.AssetNotFoundException;
import com.demo.exceptions.assetExceptions.AssetNotIssuedException;
import com.demo.exceptions.assetExceptions.AssetNotFoundException;
public interface AssetInterface {
	
	// method that will enable admin to add asset
	public int addAsset(Asset a) throws SQLException;
	
	// method that will search an IT asset by name
	public List<Asset> searchAssetByName(String name) throws AssetNotFoundException, SQLException;
	
	// method that will search an IT asset by date
	public List<Asset> searchAssetByDate(String date) throws AssetNotFoundException, SQLException;
	
	// method that will search an It asset by category
	public List<Asset> searchAssetByCategory(String category) throws AssetNotFoundException, SQLException;
	// method to getAllAssets 
	public List<Asset> getAllAssets() throws AssetNotFoundException, SQLException;
	
	
}
