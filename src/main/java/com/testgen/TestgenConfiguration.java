package com.testgen;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * <configuration>
 * 		<docTestPaths>
 * 			<docTestPath>com.my.restful.services.v1.*</docTestPath>
 * 		</docTestPaths>
 * 		<docPaths>
 * 			<docPath>com.my.restful.dto.MyDTO</docPath>
 * 			<docPath>com.my.restful.mydtos.*</docPath>
 * 		</docPaths>
 * 		<testPaths>
 * 			<testPath>com.my.restful.services.v2.*</testPath>
 * 		</testPaths>
 * 		<!-- Add extra links to the testgen pages on the right hand side links section-->
 * 		<links>
 * 			<link>link1.html</link>
 * 			<link>link2.html</link>
 * 		</links>
 * 		<requestContentType>json</requestContentType>
 * 		<useBootstrapUI>true</useBootstrapUI>
 *      <urlPrefix>urlprefix</urlPrefix>
 * 		<resourcepath>src/main/resources</resourcepath>
 * 		<uripath>testgen</uripath>
 * 		<copywright>Testgen 2012</copywright>
 * 		<loginpath>login</loginpath>
 * 		<loginmeth>POST</loginmeth>
 * 		<authextract>token,json</authextract>
 * 		<!-- XML xpath value
 * 			<authextract>//node//token,xml</authextract>
 * 		-->
 * 		<!-- 
 * 			<authextract>token,plain</authextract>
 * 		-->
 * 		<!-- 
 * 			<authextract>COOKIE-ID,cookie</authextract>
 * 		-->
 * 		<!-- 
 * 			<authextract>token,header</authextract>
 * 		-->
 * 		<apiauth>token,queryparam</apiauth>
 * 		<!-- 
 * 			<apiauth>token,postparam</apiauth>
 * 		-->
 * 		<!-- 
 * 			<apiauth>token,header</apiauth>
 * 		-->
 * 		<loginuser>username,header</loginuser>
 * 		<loginpass>password,header</loginpass>					
 * 		<!-- 
 * 			<loginuser>username,postparam</loginuser>
 * 			<loginpass>password,postparam</loginpass>
 * 		-->
 * 		<!-- 
 * 			<loginuser>username,queryparam</loginuser>
 * 			<loginpass>password,queryparam</loginpass>
 * 		-->
 * 		<!-- 
 * 			<loginuser>username,json</loginuser>
 * 			<loginpass>password,json</loginpass>
 * 		-->
 * 		<!-- 
 * 			<loginuser>username,authbasic</loginuser>
 * 			<loginpass>password,authbasic</loginpass>
 * 		-->
 * 	</configuration>
 * 
 * @author sumeetc
 *
 */
@XStreamAlias("configuration")
@JsonAutoDetect(getterVisibility=Visibility.NONE, fieldVisibility=Visibility.ANY, isGetterVisibility=Visibility.NONE)
@JsonSerialize(include=Inclusion.NON_NULL)
public class TestgenConfiguration {

	
    private String[] docTestPaths;

    /**
     * @return the docTestPaths
     */
    public String[] getDocTestPaths()
    {
        return docTestPaths;
    }

    /**
     * @param docTestPaths packageNames/classes to set
     */
    public void setDocTestPaths(String[] docTestPaths)
    {
        this.docTestPaths = docTestPaths;
    }

    private String[] docPaths;

    /**
     * @return the docPaths
     */
    public String[] getDocPaths()
    {
        return docPaths;
    }

    /**
     * @param docPaths packageNames/classes to set
     */
    public void setDocPaths(String[] docPaths)
    {
        this.docPaths = docPaths;
    }

    private String[] testPaths;

    /**
     * @return the testPaths
     */
    public String[] getTestPaths()
    {
        return testPaths;
    }

    /**
     * @param testPaths packageNames/classes to set
     */
    public void setTestPaths(String[] testPaths)
    {
        this.testPaths = testPaths;
    }

    private String[] links;

    /**
     * @return the testPaths
     */
    public String[] getLinks()
    {
        return links;
    }

    /**
     * @param testPaths packageNames/classes to set
     */
    public void setLinks(String[] links)
    {
        this.links = links;
    }

    private String targetDir;

    /**
     * @return the targetDir
     */
    public String getTargetDir()
    {
        return targetDir;
    }

    /**
     * @param targetDir the targetDir to set
     */
    public void setTargetDir(String targetDir)
    {
        this.targetDir = targetDir;
    }

    private String sourceDir;

    /**
     * @return the sourceDir
     */
    public String getSourceDir()
    {
        return sourceDir;
    }

    /**
     * @param sourceDir the sourceDir to set
     */
    public void setSourceDir(String sourceDir)
    {
        this.sourceDir = sourceDir;
    }

    private String resourcepath;

    /**
     * @return the resourcepath
     */
    public String getResourcepath()
    {
        return resourcepath;
    }

    /**
     * @param resourcepath the resourcepath to set
     */
    public void setResourcepath(String resourcepath)
    {
        this.resourcepath = resourcepath;
    }

    private String urlPrefix;

    public String getUrlPrefix()
    {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix)
    {
        this.urlPrefix = urlPrefix;
    }

    private String copywright;

    /**
     * @return the copywright
     */
    public String getCopywright()
    {
        return copywright;
    }

    /**
     * @param copywright the copywright to set
     */
    public void setCopywright(String copywright)
    {
        this.copywright = copywright;
    }

    private String uripath;

    /**
     * @return the uripath
     */
    public String getUripath()
    {
        return uripath;
    }

    /**
     * @param uripath the uripath to set
     */
    public void setUripath(String uripath)
    {
        this.uripath = uripath;
    }

    private String authextract;

    private String apiauth;

    /**
     * @return the authExtract
     */
    public String getAuthextract()
    {
        return authextract;
    }

    /**
     * @param authExtract the authExtract to set
     */
    public void setAuthExtract(String authextract)
    {
        this.authextract = authextract;
    }

    /**
     * @return the apiAuth
     */
    public String getApiAuth()
    {
        return apiauth;
    }

    /**
     * @param apiAuth the apiAuth to set
     */
    public void setApiAuth(String apiAuth)
    {
        this.apiauth = apiAuth;
    }

    private String loginpath;

    private String loginuser;

    private String loginpass;

    private String loginmeth;

    /**
     * @return the loginpath
     */
    public String getLoginpath()
    {
        return loginpath;
    }

    /**
     * @param loginpath the loginpath to set
     */
    public void setLoginpath(String loginpath)
    {
        this.loginpath = loginpath;
    }

    /**
     * @return the loginuser
     */
    public String getLoginuser()
    {
        return loginuser;
    }

    /**
     * @param loginuser the loginuser to set
     */
    public void setLoginuser(String loginuser)
    {
        this.loginuser = loginuser;
    }

    /**
     * @return the loginpass
     */
    public String getLoginpass()
    {
        return loginpass;
    }

    /**
     * @param loginpass the loginpass to set
     */
    public void setLoginpass(String loginpass)
    {
        this.loginpass = loginpass;
    }

    /**
     * @return the loginmeth
     */
    public String getLoginmeth()
    {
        return loginmeth;
    }

    /**
     * @param loginmeth the loginmeth to set
     */
    public void setLoginmeth(String loginmeth)
    {
        this.loginmeth = loginmeth;
    }

    private boolean debugEnabled;

    /**
     * @return the debugEnabled
     */
    public boolean isDebugEnabled()
    {
        return debugEnabled;
    }

    /**
     * @param debugEnabled the debugEnabled to set
     */
    public void setDebugEnabled(boolean debugEnabled)
    {
        this.debugEnabled = debugEnabled;
    }
    
    private boolean useBootstrapUI;
    
    public boolean isUseBootstrapUI() {
		return useBootstrapUI;
	}

	public void setUseBootstrapUI(boolean useBootstrapUI) {
		this.useBootstrapUI = useBootstrapUI;
	}
	
	private String requestContentType;

	public String getRequestContentType() {
		return requestContentType;
	}

	public void setRequestContentType(String requestContentType) {
		this.requestContentType = requestContentType;
	}
	
	private boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
