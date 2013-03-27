package kandidat.nfc.nfcapp;

public class NFCPMessage {

	//The three types of message
	public static final String MESSAGE_TYPE_BEACON = "1";
	public static final String MESSAGE_TYPE_UNLOCK = "2";
	public static final String MESSAGE_TYPE_RESULT = "3";
	
	//Different status
	public static final String STATUS_OK = "0";
	public static final String STATUS_ERROR = "1";
	
	//Errors codes
	public static final String ERROR_NONE = "0";
	
	
	
	//Fields
	private String name; //2 bytes alltså två ASCII-tecken
	private String id; //2 bytes alltså två ASCII-tecken
	private String typeOfMessage; //1 byte
	private String status;//1 byte
	private String errorCode;//1 byte //Only checked if status = "1"
	private String unlockID; //4 bytes
	private String publicKey; //This will be added if message is of type 1
	
	
	//Constructor Type1 and Type3 Message
	public NFCPMessage(String name, String id, String status, String typeOfMessage,
			String errorCode,String userID) {
		this.name = name;
		this.id = id;
		this.status = status;
		this.typeOfMessage= typeOfMessage;
		this.errorCode = errorCode;
		this.unlockID =userID;
	}
	
	/**
	 * Takes raw message in form of a string and constructs NFCPMessage
	 * @param s raw message
	 */
	public NFCPMessage(String s){
		if (s.length() == 11){
			name = s.substring(0, 2); //2 bytes alltså två ASCII-tecken
			id = s.substring(2, 4); //2 bytes alltså två ASCII-tecken
			typeOfMessage = s.substring(4, 5); //1 byte
			status = s.substring(5,6);//1 byte
			errorCode = s.substring(6, 7);
			unlockID = s.substring(7, 11); //4 bytes
		}
	}
	
	public void clear(){
		name ="";
		id ="";
		typeOfMessage="";
		status="";
		errorCode="";
		unlockID="";
	}
	
	@Override
	public String toString(){
		
		return name +
				id +
				typeOfMessage + 
				status +
				errorCode +
				unlockID;
		
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
	public String getUniqueId(){
		return name + id;
	}
	public String getName(){
		return name;
	}
	public String getId(){
		return id;
	}
	public String getPublicKey(){
		return publicKey;
	}
	public void setPublicKey(String publicKey){
		this.publicKey = publicKey;
	}
}
