package kandidat.nfc.nfcapp;

import java.util.Scanner;

public class NFCPMessage {

	private String name ="";
	private int id =0;
	private int typeOfMessage=0;
	private int key=0;
	private boolean status=false;
	private int errorCode=0;
	
	
	//Constructor Type1 and Type3 Message
	public NFCPMessage(String name, int id, boolean status, int typeOfMessage,
			int errorCode) {
		super();
		this.name = name;
		this.id = id;
		this.status = status;
		this.typeOfMessage= typeOfMessage;
		this.errorCode = errorCode;
	}
	
	//Constructor Type2 Message
	public NFCPMessage(String name, int id, int typeOfMessage, int key) {
		super();
		this.name = name;
		this.id = id;
		this.typeOfMessage = typeOfMessage;
		this.key = key;
	}
	
	//Constructor for received Strings
	public NFCPMessage(String s){
		Scanner sc = new Scanner(s);
			name = sc.nextLine();
			sc.next();
			id = sc.nextInt();
			sc.next();
			typeOfMessage = sc.nextInt();
			sc.next();
			key = sc.nextInt();
			sc.next();
			status = sc.nextBoolean();
			sc.next();
			errorCode = sc.nextInt();
		
	}
	
	public void clearpenis(){
		name ="";
		id =0;
		typeOfMessage=0;
		key=0;
		status=false;
		errorCode=0;
		
	}
	
	@Override
	public String toString(){
		
		String protocolTemplate =name+ "\n" +
				"Låsid: "+id+"\n" +
				"Typ: "+typeOfMessage+ "\n" +
				"Nyckel: "+key + "\n" +
				"Status: "+status+ "\n" +
				"Felkod: "+errorCode;
		
		return protocolTemplate;
		
	}

	public int getType(){
		return typeOfMessage;
}

	public boolean getStatus() {
		return status;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
}
