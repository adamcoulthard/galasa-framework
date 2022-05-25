/*
 * Copyright contributors to the Galasa project
 */
package dev.galasa.framework.spi;


public interface ICertificateStoreService {
	
	/**
	 * <p>
	 * Retrieves the PEM certificate as a string.
	 * </p>
	 * 
	 * @param id
	 * @return String certificate
	 * @throws CertificateStoreException if no certificate found
	 */
	public String getPem(String id) throws CertificateStoreException;
	
	/**
	 * <p>
	 * Retrieves the DER certificate as a string.
	 * </p>
	 * 
	 * @param id
	 * @return String certificate
	 * @throws CertificateStoreException if no certificate found
	 */
	public String getDer(String id) throws CertificateStoreException;
	
	/**
	 * <p>
	 * Returns the default keystore. This will be populated by any certificates defined:
	 * certificates.default.ids=XXXX,YYYY,etc
	 * certificates.x509.XXXX.pem=...
	 * certificates.x509.YYYY.der=...
	 * </p>
	 * @return
	 * @throws CertificateStoreException
	 */
	public IKeyStore getKeystore() throws CertificateStoreException;
	
	/**
	 * <p>
	 * Returns a defined group of certificates inside a keystore. This will be populated by any certificates defined:
	 * certificates.<GroupId>.ids=XXXX,YYYY,etc
	 * certificates.x509.XXXX.pem=...
	 * certificates.x509.YYYY.der=...
	 * </p>
	 * @param id
	 * @return
	 * @throws CertificateStoreException
	 */
	public IKeyStore getKeystore(String id) throws CertificateStoreException;
	
	/**
	 * <p>
	 * Generate a Keystore populated with the list of certificates from the certificate 
	 * store using thier property ID
	 * </p>
	 * 
	 * <p>
	 * If no id's are passed then an empty Keystore is generated and passed back, with 
	 * the assumption the tester has the desired certificates in a test resource.
	 * </p>
	 * @param certifitcateId
	 * @return
	 * @throws CertificateStoreException
	 */
	public IKeyStore generateKeyStore(String... certifitcateId) throws CertificateStoreException;


}
