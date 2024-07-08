package com.demo.exceptions.assetExceptions;

public class AssetNotFoundException extends Exception {
	
	public AssetNotFoundException(String message) {
		super(message);
	}

	public AssetNotFoundException() {
		super();
		
	}
}
