package android.nfc.security;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import javax.crypto.Cipher;

import android.util.Base64;


public class Krypto {
	private final int KEY_SIZE= 1024;//If change keysize also change end exp
	private final int END_EXP = 5;
	private final String CHOSEN_ALGORITHM = "RSA";
	private RSAPublicKey publicKey = null;
	private RSAPrivateKey privateKey = null;
	private KeyPair kp = null;

/*	public static void main(String[] args) {
		// Create krypto1 generates a KeyPair
		Krypto krypto = new Krypto();

		// Create krypto2 generates the public key from a string
		Krypto krypto2 = new Krypto(krypto.publicKeyToString());

		// Encrypt the message
		String toDecrypt = krypto2.encryptMessage("nyckel");
		
		//Decrypt the message
		String decrypted = krypto.decryptMessage(toDecrypt);
		System.out.println(decrypted);
	}*/
	
	
	/**
	 * Empty Constructor to generate a Keypair 
	 */
	public Krypto() {
		createKeyPair();
	}
	
	/**
	 * Constructor to recreate the publickey from a message
	 * @param publickey
	 */
	public Krypto(String publickey) {
		BigInteger exp = new BigInteger(publickey.substring(0, END_EXP));
		BigInteger mod = new BigInteger(publickey.substring(END_EXP));

		RSAPublicKeySpec RSAspec = new RSAPublicKeySpec(mod, exp);

		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(CHOSEN_ALGORITHM);
			publicKey = (RSAPublicKey) keyFactory.generatePublic(RSAspec);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to encrypt a message with public key
	 * @param msg
	 * @return
	 */
	public String encryptMessage(String msg) {
		byte[] encryptedMessage = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedMessage = cipher.doFinal(msg.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.encodeToString(encryptedMessage, Base64.DEFAULT);
	}

	/**
	 * Method to decrypt a message with private key
	 * @param msg
	 * @return
	 */
	public String decryptMessage(String msg) {

		byte[] decrypt =Base64.decode(msg, Base64.DEFAULT);
		byte[] cipherData = null;

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			cipherData = cipher.doFinal(decrypt);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new String(cipherData);

	}
	
	/**
	 * method to create a keypair
	 */
	public void createKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(CHOSEN_ALGORITHM);
			kpg.initialize(KEY_SIZE);
			kp = kpg.genKeyPair();
			publicKey = (RSAPublicKey) kp.getPublic();
			privateKey = (RSAPrivateKey) kp.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			}
	}

	/**
	 * method to generate a string from the public key
	 * @return
	 */
	public String publicKeyToString() {
		KeyFactory fact;
		RSAPublicKeySpec pub = null;

		try {
			fact = KeyFactory.getInstance(CHOSEN_ALGORITHM);
			pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pub.getPublicExponent().toString() + pub.getModulus().toString();
	}

}
