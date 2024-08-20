/*
 * Copyright contributors to the Galasa project
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package dev.galasa.framework;

import java.util.Properties;

import dev.galasa.framework.internal.runner.BundleManager;
import dev.galasa.framework.internal.runner.RealAnnotationExtractor;
import dev.galasa.framework.spi.*;
import dev.galasa.framework.spi.language.GalasaTest;

public class TestRunnerDataProvider implements ITestRunnerDataProvider {

    private IConfigurationPropertyStoreService cps;
    private IDynamicStatusStoreService         dss;
    private IResultArchiveStore                ras;
    private IRun                               run;
    private IShutableFramework                 framework;
    private Properties                         overrideProperties;
    private IFileSystem                        fileSystem;

    public TestRunnerDataProvider(Properties bootstrapProperties, Properties overrideProperties) throws TestRunException {
        
        FrameworkInitialisation frameworkInitialisation = null;
        try {
            boolean isThisATestRun = true ;
            frameworkInitialisation = new FrameworkInitialisation(bootstrapProperties, overrideProperties, isThisATestRun);
            framework = frameworkInitialisation.getShutableFramework();
            cps = framework.getConfigurationPropertyService("framework");
            dss = framework.getDynamicStatusStoreService("framework");
        } catch (Exception e) {
            throw new TestRunException("Unable to initialise the Framework Services", e);
        }
        
        run = framework.getTestRun();
        ras = framework.getResultArchiveStore();
        fileSystem = new FileSystem();

        this.overrideProperties = overrideProperties;

    }
    
    @Override
    public IRun getRun() {
        return this.run;
    }
    
    @Override
    public IConfigurationPropertyStoreService getCPS() {
        return this.cps;
    }
    
    @Override
    public IDynamicStatusStoreService getDSS() {
        return this.dss;
    }
    
    @Override
    public IResultArchiveStore getRAS() {
        return this.ras;
    }
    
    @Override
    public IShutableFramework getFramework() {
        return this.framework;
    }
    
    @Override
    public IBundleManager getBundleManager() {
        return new BundleManager();
    }

    @Override
    public IAnnotationExtractor getAnnotationExtractor() {
        return new RealAnnotationExtractor();
    }

    @Override
    public Properties getOverrideProperties() {
        return this.overrideProperties;
    }

    @Override
    public ITestRunManagers createTestRunManagers(GalasaTest galasaTest) throws TestRunException {
        ITestRunManagers managers ;
        try {
            managers = new TestRunManagers(this.framework, galasaTest);
        } catch (FrameworkException e) {
            String msg = "FrameworkException Exception caught. "+e.getMessage();
            throw new TestRunException(msg,e);
        }
        return managers;
    }

    @Override
    public IFileSystem getFileSystem() {
        return this.fileSystem;
    }

}
