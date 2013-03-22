package kandidat.nfc.nfcapp;

import java.util.Scanner;

public class NFCPMessage {

	private String name; //2 bytes alltså två ASCII-tecken
	private String id; //2 bytes alltså två ASCII-tecken
	private String typeOfMessage; //1 byte
	private String userID; //4 bytes
	private String status;//1 byte
	private String errorCode;//1 byte
	
	
	//Constructor Type1 and Type3 Message
	public NFCPMessage(String name, String id, String status, String typeOfMessage,
			String errorCode,String userID) {
		//super();
		this.name = name;
		this.id = id;
		this.status = status;
		this.typeOfMessage= typeOfMessage;
		this.errorCode = errorCode;
		this.userID =userID;
	}
	
	//Constructor Type2 Message
	public NFCPMessage(String name, String id, String typeOfMessage, String key) {
		//super();
		this.name = name;
		this.id = id;
		this.typeOfMessage = typeOfMessage;
		this.userID = key;
	}
	
	//Constructor for received Strings
	public NFCPMessage(String s){
		//TODO HEX TO Message
		
	}
	
	public void clear(){
		name ="";
		id ="";
		typeOfMessage="";
		userID="";
		status="";
		errorCode="";
		
	}
	
	@Override
	public String toString(){
		
		String string = name +
						id +
						typeOfMessage+ 
						status +
						errorCode+
						userID;
		
		return string;
		
	}

	public String getType(){
		return typeOfMessage;
}

	public String getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
}
