package test.cts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Test;

import io.ejat.framework.internal.cts.FrameworkConfidentialTextService;
import io.ejat.framework.spi.ConfidentialTextException;
import io.ejat.framework.spi.ConfigurationPropertyStoreException;
import io.ejat.framework.spi.IConfidentialTextService;
import io.ejat.framework.spi.IConfigurationPropertyStore;
import io.ejat.framework.spi.IConfigurationPropertyStoreRegistration;
import io.ejat.framework.spi.IConfigurationPropertyStoreService;
import io.ejat.framework.spi.IDynamicStatusStoreService;
import io.ejat.framework.spi.IFramework;
import io.ejat.framework.spi.IFrameworkInitialisation;
import io.ejat.framework.spi.IResultArchiveStoreService;
import io.ejat.framework.spi.creds.ICredentialsStore;
import io.ejat.framework.spi.creds.CredentialsStoreException;
import io.ejat.framework.spi.IDynamicStatusStore;
import io.ejat.framework.spi.DynamicStatusStoreException;

/**
 * This test class ensures that confidential texts that have been registered are reomved from text.
 * 
 * @author James Davies
 */
public class ConfidentialTextServiceTest {

    /**
     * The test method adds a confidential text to the service.
     * 
     * @throws ConfidentialTextException
     * @throws IOException
     */
    @Test
    public void testRegisterText() throws ConfidentialTextException, IOException{
        FrameworkConfidentialTextService ctsService = new FrameworkConfidentialTextService();

        ctsService.registerText("test1", "This is a test comment");
        assertTrue("dummy", true);
    }

    /** 
     * This test method ensures that any regitered words or phrases are removed from a text.
     * 
     * @throws ConfidentialTextException
     * @throws IOException
     */
    @Test
    public void testRemoveConfidentialText() throws ConfidentialTextException, IOException{
        FrameworkConfidentialTextService ctsService = new FrameworkConfidentialTextService();

        ctsService.registerText("test1", "This is a test comment");
        ctsService.registerText("test2", "This is a test comment");
        ctsService.registerText("test3", "This is a test comment");

        String testSentence = "The current password is test1, the old password is test3, and the new password is test2";
        String expected = "The current password is **0**, the old password is **2**, and the new password is **1**";

        String result = ctsService.removeConfidentialText(testSentence);
        System.out.println(result + "\n" + expected);
        assertEquals("Did not remove confidential imfomation ",expected, result);
    }


    /**
     * This is a stubbed framework intialisation class to test the registering of the service.
     */
    private class FrameworkInitialisation implements IFrameworkInitialisation {
        private URI uri;

        public FrameworkInitialisation(URI uri) {
            this.uri=uri;
        }

        @Override
        public void registerConfidentialTextService(@NotNull IConfidentialTextService cts) throws ConfidentialTextException{
        }
        @Override
        public URI getBootstrapConfigurationPropertyStore() {return uri;}
        @Override
        public void registerDynamicStatusStore(@NotNull IDynamicStatusStore dynamicStatusStore) throws DynamicStatusStoreException{}
        @Override
        public IFramework getFramework(){return null;}
        
        @Override
        public void registerConfigurationPropertyStore(@NotNull IConfigurationPropertyStore configurationPropertyStore)
                throws ConfigurationPropertyStoreException {
        }

		@Override
		public URI getDynamicStatusStoreUri() {return null;}

		@Override
		public List<URI> getResultArchiveStoreUris() {return null;}

		@Override
		public void registerResultArchiveStoreService(@NotNull IResultArchiveStoreService resultArchiveStoreService) {
        }
        
        @Override
        public void registerCredentialsStore(@NotNull ICredentialsStore credentialsStore) throws CredentialsStoreException {           
        }
    }
}