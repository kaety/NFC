package android.nfc.security;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import android.util.Base64;

/*
 * This Class represents pair a RSA keys. 
 * It has methods for generating a pair of keys and also methods for converting to and from strings.
 * 	
 * 	//Example code
 *		// Create krypto1 generates a KeyPair
 *		Krypto krypto = new Krypto();
 *
 *		// Create krypto2 generates the public key from a string
 *		Krypto krypto2 = new Krypto(krypto.publicKeyToString());
 *
 *		// Encrypt the message
 *		String toDecrypt = krypto2.encryptMessage("nyckel");
 *		
 *		//Decrypt the message
 *		String decrypted = krypto.decryptMessage(toDecrypt);
 *		System.out.println(decrypted);
 */
public class Krypto implements Serializable {

	//Version 1. This is a required property for Serialization
	private static final long serialVersionUID = 1L;
	private static transient final int KEY_SIZE= 512;
	private static transient final int END_EXP = 5;
	private static transient final String CHOSEN_CIPHER = "RSA/ECB/PKCS1Padding";
	private static transient final String CHOSEN_ALGORITHM = "RSA";
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	private static BigInteger exp;
	
	/**
	 * Empty Constructor to generate a Keypair 
	 */
	public Krypto() {
		createKeyPair();
	}
	
	/**
	 * Constructor to recreate the public key from a message and leaves the private empty
	 * @param publickey
	 */
	public Krypto(String publickey) {
		exp = new BigInteger("010001",16);
		BigInteger mod = new BigInteger(publickey,16);

		RSAPublicKeySpec RSAspec = new RSAPublicKeySpec(mod, exp);

		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(CHOSEN_ALGORITHM,"BC");
			publicKey = (RSAPublicKey) keyFactory.generatePublic(RSAspec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to encrypt a message with public key
	 * @param msg message to encrypt
	 * @return encrypted message
	 */
	public String encryptMessage(String msg) {
		byte[] encryptedMessage = null;
		try {
			Cipher cipher = Cipher.getInstance(CHOSEN_CIPHER ,"BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedMessage = cipher.doFinal(msg.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return Base64.encodeToString(encryptedMessage, Base64.NO_WRAP);
		
		//Return a hex string instead
		StringBuilder sb = new StringBuilder();
		for(byte b:encryptedMessage){
			sb.append(String.format("%02x", b&0xff));		
		}
		return sb.toString();
	}

	/**
	 * Method to decrypt a message with private key
	 * @param msg encrypted message to decrypt
	 * @return decrypted message
	 */
	public String decryptMessage(String msg) {
		if(publicKey != null){
		
			byte[] decrypt =Base64.decode(msg, Base64.DEFAULT);
			byte[] cipherData = null;

				Cipher cipher;
				try {
					cipher = Cipher.getInstance(CHOSEN_CIPHER,"BC");
					cipher.init(Cipher.DECRYPT_MODE, privateKey);
					cipherData = cipher.doFinal(decrypt);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (NoSuchProviderException e) {
					e.printStackTrace();
				}

			return new String(cipherData);
			
		}else{
			
			return null;
			
		}

	}
	
	/**
	 * method to create a key pair. Called from constructor
	 */
	public void createKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(CHOSEN_ALGORITHM, "BC");
			kpg.initialize(KEY_SIZE);
			KeyPair kp = kpg.genKeyPair();
			publicKey = (RSAPublicKey) kp.getPublic();
			privateKey = (RSAPrivateKey) kp.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method to generate a string from the public key
	 * @return publicKey as a string
	 */
	public String publicKeyToString() {
		KeyFactory fact;
		RSAPublicKeySpec pub = null;

			try {
				fact = KeyFactory.getInstance(CHOSEN_ALGORITHM,"BC");
				pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return pub.getPublicExponent().toString() + pub.getModulus().toString();
	}

}
