package com.demo.service.assetService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.demo.beans.Asset;
import com.demo.beans.User;

import com.demo.dao.assetDao.AssetDaoIntf;
import com.demo.dao.assetDao.AsssetDaoImpl;
import com.demo.exceptions.assetExceptions.AssetNotFoundException;

public class AssetInterfaceImpl implements AssetInterface {
	
	AssetDaoIntf dao;
	       AssetInterfaceImpl(){
	    	   dao=new AsssetDaoImpl();
	       }

	@Override
	public int addAsset(Asset a) throws SQLException {
		int status=dao.addAsset(a);
		return status;
	}

	@Override
	public List<Asset> searchAssetByName(String name) throws AssetNotFoundException, SQLException {
		List<Asset> a=dao.getAssetByName(name);
		return a;
		
	}

	@Override
	public List<Asset> searchAssetByDate(String date) throws AssetNotFoundException, SQLException {
		List<Asset> a=dao.getAssetByDate(date);
		return a;
	}

	@Override
	public List<Asset> searchAssetByCategory(String category) throws AssetNotFoundException, SQLException {
		List<Asset> a=dao.getAssetByCategory(category);
		return a;
	}

	

	@Override
	public List<Asset> getAllAssets() throws AssetNotFoundException, SQLException {
		List<Asset> assets=dao.displayAllAssets();
		return assets;
	}
}
