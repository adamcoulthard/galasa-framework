package dev.voras.framework.internal.creds;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import dev.voras.ICredentials;
import dev.voras.framework.spi.FrameworkPropertyFile;
import dev.voras.framework.spi.IConfigurationPropertyStoreService;
import dev.voras.framework.spi.IFramework;
import dev.voras.framework.spi.creds.CredentialsException;
import dev.voras.framework.spi.creds.CredentialsToken;
import dev.voras.framework.spi.creds.CredentialsUsername;
import dev.voras.framework.spi.creds.CredentialsUsernamePassword;
import dev.voras.framework.spi.creds.ICredentialsStore;

/**
 *  <p>This class is used to retrieve credentials stored locally, whether they are encrypted or not</p>
 * 
 * @author Bruce Abbott
 */

public class FileCredentialsStore implements ICredentialsStore {
	private FrameworkPropertyFile fpf;
	private Boolean encrypted = false;
	private SecretKeySpec key;
	private IFramework framework;
	private IConfigurationPropertyStoreService cpsService;

	public FileCredentialsStore(URI file, IFramework framework) throws CredentialsException {
		try {
			this.framework = framework;
			cpsService = this.framework.getConfigurationPropertyService("secure");         
			fpf = new FrameworkPropertyFile(file);
			String encryptionKey = cpsService.getProperty("credentials.file", "encryption.key");
			if (encryptionKey != null) {
				encrypted = true;
				key = createKey(encryptionKey);
			}
		} catch (Exception e) {
			throw new CredentialsException("Unable to initialise the credentials store", e);
		}
	}

	/**
	 * <p>This method is used to retrieve credentials as an appropriate object</p>
	 * 
	 * @param String credentialsId
	 * @throws CredentialsStoreException
	 */
	@Override
	public ICredentials getCredentials(String credentialsId) throws CredentialsException {
		String token = fpf.get("secure.credentials." + credentialsId + ".token");
		if (token != null) {
			if (encrypted) {
				try {
					token = decrypt(key, token);
				} catch(Exception e) {
					throw new CredentialsException("Unable to decrypt token for Credentials ID " + credentialsId, e);
				}
			}
			return new CredentialsToken(token);       
		}

		String username = fpf.get("secure.credentials." + credentialsId + ".username");
		String password = fpf.get("secure.credentials." + credentialsId + ".password");
		
		if (username == null) {
			return null;
		}

		if (password == null) {
			return new CredentialsUsername(username);
		}

		if (encrypted) {
			try {
				password = decrypt(key, password);
			} catch(Exception e) {
				throw new CredentialsException("Unable to decrypt password for Credentials ID " + credentialsId, e);
			}
		}
		return new CredentialsUsernamePassword(username, password);
	}

	private static SecretKeySpec createKey(String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException {	
		byte[] key = secret.getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		key = sha.digest(key);
		return new SecretKeySpec(key, "AES");
	}

	private static String decrypt(SecretKeySpec key, String encrypted) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
		return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
	}

}