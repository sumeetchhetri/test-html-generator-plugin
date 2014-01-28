package com.testgen;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.testgen.view.Validation;
import com.testgen.view.ViewField;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author Sumeet Chhetri<br/>
 *         The testgen maven plugin main class which creates documentation for
 *         all the rest-ful services mentioned. Also creates test forms for
 *         testing the services, can create full documentation+test forms, only
 *         test forms or only documentation for the packages/classes mentioned. <br/>
 * 
 *         <pre>
 * {@code
 * 	<plugin>
 * 	<groupId>com.testgen</groupId>
 * 	<artifactId>test-html-generator-plugin</artifactId>
 * 	<version>0.0.1</version>
 * 	<configuration>
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
 * 	<executions>
 * 		<execution>
 * 			<goals>
 * 				<goal>testgen</goal>
 * 			</goals>
 * 		</execution>
 * 	</executions>
 * </plugin>
 * <plugin>
 *        <groupId>org.apache.maven.plugins</groupId>
 *        <artifactId>maven-war-plugin</artifactId>
 *        <version>2.2</version>
 *        <configuration>
 *            <webResources>
 *                <resource>
 *                    <directory>${project.reporting.outputDirectory}/testgen</directory>
 *                    <targetPath>testgen</targetPath>
 *                </resource>
 *            </webResources>
 *        </configuration>
 *   </plugin>
 *   }
 * </pre>
 */
@Mojo(name = "testgen", aggregator = false, executionStrategy = "always", inheritByDefault = true, instantiationStrategy = InstantiationStrategy.PER_LOOKUP, defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.TEST, requiresDirectInvocation = false, requiresOnline = false, requiresProject = true, threadSafe = true)
public class TestGeneratorMojo extends AbstractMojo {

	private static final Pattern CLASS_REGEX_PATTERN = Pattern
			.compile(
					".*\\s+class\\s+(\\w+)(\\s+extends\\s+(\\w+))?(\\s+implements\\s+([\\w,\\s]+))?\\s*\\{.*$",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE
							| Pattern.MULTILINE);

	private static final Pattern CLASS_REGEX_PATTERN_WO_CB = Pattern
			.compile(
					".*\\s+class\\s+(\\w+)(\\s+extends\\s+(\\w+))?(\\s+implements\\s+([\\w,\\s]+))?\\s*\\.*",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE
							| Pattern.MULTILINE);

	private static final Pattern METHOD_REGEX_PATTERN = Pattern
			.compile(
					".*(public|protected|private|static|final|\\s) +[\\w\\<\\>\\[\\]]+\\s+(\\w+)\\s*\\(.*?\\)\\s*.*",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE
							| Pattern.MULTILINE);

	private static final Pattern VARIABLE_REGEX_PATTERN = Pattern
			.compile(
					".*(public|protected|private|static|final|\\s) +[\\w\\<\\>\\[\\]]+\\s+(\\w+).*",
					Pattern.DOTALL | Pattern.CASE_INSENSITIVE
							| Pattern.MULTILINE);

	@Component
	protected MavenSession session;

	@Component
	private MavenProject project;

	@Parameter(alias = "docTestPaths")
	private String[] docTestPaths;

	/**
	 * @return the docTestPaths
	 */
	public String[] getDocTestPaths() {
		return docTestPaths;
	}

	/**
	 * @param docTestPaths
	 *            packageNames/classes to set
	 */
	public void setDocTestPaths(String[] docTestPaths) {
		this.docTestPaths = docTestPaths;
	}

	@Parameter(alias = "docPaths")
	private String[] docPaths;

	/**
	 * @return the docPaths
	 */
	public String[] getDocPaths() {
		return docPaths;
	}

	/**
	 * @param docPaths
	 *            packageNames/classes to set
	 */
	public void setDocPaths(String[] docPaths) {
		this.docPaths = docPaths;
	}

	@Parameter(alias = "testPaths")
	private String[] testPaths;

	/**
	 * @return the testPaths
	 */
	public String[] getTestPaths() {
		return testPaths;
	}

	/**
	 * @param testPaths
	 *            packageNames/classes to set
	 */
	public void setTestPaths(String[] testPaths) {
		this.testPaths = testPaths;
	}

	@Parameter(alias = "links")
	private String[] links;

	/**
	 * @return the testPaths
	 */
	public String[] getLinks() {
		return links;
	}

	/**
	 * @param testPaths
	 *            packageNames/classes to set
	 */
	public void setLinks(String[] links) {
		this.links = links;
	}

	@Parameter(alias = "targetDir", defaultValue = "${project.reporting.outputDirectory}")
	private String targetDir;

	/**
	 * @return the targetDir
	 */
	public String getTargetDir() {
		return targetDir;
	}

	/**
	 * @param targetDir
	 *            the targetDir to set
	 */
	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	@Parameter(alias = "sourceDir")
	private String sourceDir;

	/**
	 * @return the sourceDir
	 */
	public String getSourceDir() {
		return sourceDir;
	}

	/**
	 * @param sourceDir
	 *            the sourceDir to set
	 */
	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	@Parameter(alias = "resourcepath")
	private String resourcepath;

	/**
	 * @return the resourcepath
	 */
	public String getResourcepath() {
		return resourcepath;
	}

	/**
	 * @param resourcepath
	 *            the resourcepath to set
	 */
	public void setResourcepath(String resourcepath) {
		this.resourcepath = resourcepath;
	}

	@Parameter(alias = "urlPrefix")
	private String urlPrefix;

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	@Parameter(alias = "copywright", defaultValue = "Testgen 2012")
	private String copywright;

	/**
	 * @return the copywright
	 */
	public String getCopywright() {
		return copywright;
	}

	/**
	 * @param copywright
	 *            the copywright to set
	 */
	public void setCopywright(String copywright) {
		this.copywright = copywright;
	}

	@Parameter(alias = "uripath", defaultValue = "testgen")
	private String uripath;

	/**
	 * @return the uripath
	 */
	public String getUripath() {
		return uripath;
	}

	/**
	 * @param uripath
	 *            the uripath to set
	 */
	public void setUripath(String uripath) {
		this.uripath = uripath;
	}

	@Parameter(alias = "authextract")
	private String authExtract;

	@Parameter(alias = "apiauth")
	private String apiAuth;

	/**
	 * @return the authExtract
	 */
	public String getAuthExtract() {
		return authExtract;
	}

	/**
	 * @param authExtract
	 *            the authExtract to set
	 */
	public void setAuthExtract(String authExtract) {
		this.authExtract = authExtract;
	}

	/**
	 * @return the apiAuth
	 */
	public String getApiAuth() {
		return apiAuth;
	}

	/**
	 * @param apiAuth
	 *            the apiAuth to set
	 */
	public void setApiAuth(String apiAuth) {
		this.apiAuth = apiAuth;
	}

	@Parameter(alias = "loginpath")
	private String loginpath;

	@Parameter(alias = "loginuser")
	private String loginuser;

	@Parameter(alias = "loginpass")
	private String loginpass;

	@Parameter(alias = "loginmeth")
	private String loginmeth;

	/**
	 * @return the loginpath
	 */
	public String getLoginpath() {
		return loginpath;
	}

	/**
	 * @param loginpath
	 *            the loginpath to set
	 */
	public void setLoginpath(String loginpath) {
		this.loginpath = loginpath;
	}

	/**
	 * @return the loginuser
	 */
	public String getLoginuser() {
		return loginuser;
	}

	/**
	 * @param loginuser
	 *            the loginuser to set
	 */
	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	/**
	 * @return the loginpass
	 */
	public String getLoginpass() {
		return loginpass;
	}

	/**
	 * @param loginpass
	 *            the loginpass to set
	 */
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}

	/**
	 * @return the loginmeth
	 */
	public String getLoginmeth() {
		return loginmeth;
	}

	/**
	 * @param loginmeth
	 *            the loginmeth to set
	 */
	public void setLoginmeth(String loginmeth) {
		this.loginmeth = loginmeth;
	}

	@Parameter(alias = "debugEnabled")
	private boolean debugEnabled;

	/**
	 * @return the debugEnabled
	 */
	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	/**
	 * @param debugEnabled
	 *            the debugEnabled to set
	 */
	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	@Parameter(alias = "useBootstrapUI")
	private boolean useBootstrapUI;

	public boolean isUseBootstrapUI() {
		return useBootstrapUI;
	}

	public void setUseBootstrapUI(boolean useBootstrapUI) {
		this.useBootstrapUI = useBootstrapUI;
	}

	@Parameter(alias = "requestContentType")
	private String requestContentType;

	public String getRequestContentType() {
		return requestContentType;
	}

	public void setRequestContentType(String requestContentType) {
		this.requestContentType = requestContentType;
	}

	@Parameter(alias = "enabled", defaultValue = "true")
	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static class LinkObject {
		private String name;
		private String href;
		private String method;
		private int counter;

		public LinkObject(String name, String href, String method) {
			super();
			this.name = name;
			this.href = href;
			this.method = method;
		}

		public LinkObject(String name, String href, int counter) {
			super();
			this.name = name;
			this.href = href;
			this.counter = counter;
		}

		public LinkObject(String name, String href) {
			super();
			this.name = name;
			this.href = href;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public int getCounter() {
			return counter;
		}

		public void setCounter(int counter) {
			this.counter = counter;
		}
	}

	private Annotation[] getRestArgAnnotation(Annotation[] annotations) {

		Annotation[] annot = new Annotation[2];
		if (annotations != null && annotations.length > 0) {
			for (Annotation annotation : annotations) {
				if (annotation instanceof FormParam
						|| annotation instanceof QueryParam
						|| annotation instanceof RequestParam
						|| annotation instanceof HeaderParam
						|| annotation instanceof PathParam)
					annot[0] = annotation;
				if (annotation instanceof DefaultValue)
					annot[1] = annotation;
			}
		}
		return annot;
	}

	/**
	 * @param classes
	 *            Generates all the related documentation/test form HTML for all
	 *            the packages/classes mentioned, if the given class is not a
	 *            rest ful web service then only documentation for the
	 *            corresponding class will be generated
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void generate(List<ClassDocTestGen> classes) {
		try {
			File srcDir = null;
			if (getSourceDir() != null)
				srcDir = new File(project.getBasedir(), getSourceDir());
			if (srcDir == null || !srcDir.exists()) {
				srcDir = new File(project.getBasedir(), "src/main/java");
				if (!srcDir.exists()) {
					srcDir = null;
				}
			}

			File dir = new File(getTargetDir() + "/" + getUripath() + "/");
			dir.mkdirs();
			StringBuilder str = new StringBuilder();
			List<String> lst = new ArrayList<String>();

			File rDir = null;
			if (getResourcepath() != null)
				rDir = new File(project.getBasedir(), getResourcepath());
			if (rDir == null || !rDir.exists()) {
				rDir = new File(project.getBasedir(), "src/main/resources");
				if (!rDir.exists()) {
					rDir = null;
				}
			}

			InputStream resourcesIS = TestGeneratorMojo.class
					.getResourceAsStream("/resources.zip");
			if (resourcesIS != null) {
				unzipZipFile(resourcesIS, dir.getAbsolutePath());
			}
			resourcesIS.close();
			resourcesIS = TestGeneratorMojo.class
					.getResourceAsStream("/fonts.zip");
			if (resourcesIS != null) {
				unzipZipFile(resourcesIS, dir.getAbsolutePath());
			}
			resourcesIS.close();
			File originalIndex = new File(dir.getAbsolutePath() + "/index.html");
			if (originalIndex.exists()) {
				originalIndex.delete();
			}
			File index = new File(dir.getAbsolutePath()
					+ "/resources/index.html");

			int accordianCounter = 1;
			if (index != null && !isUseBootstrapUI()) {
				str.append("<h3><a class=\"asideLink\" href=\"index.html\"><span class=\"service\">Home</span></a></h3>");
				index.renameTo(new File(dir.getAbsolutePath() + "/index.html"));
			} else {
				str.append(getAccordianHeadingHTML(new LinkObject("Home",
						"index.html", accordianCounter++), null));
			}

			ViewField urlViewField = null;
			String[] lauthExtractTokens = null;
			String[] lapiAuthTokens = null;
			boolean isLoginPresent = false;
			if (getLoginpath() != null && getLoginuser() != null
					&& getLoginpass() != null) {
				if (getApiAuth() != null && getAuthExtract() != null) {
					String[] authExtractTokens = getAuthExtract().split(",");
					String[] apiAuthTokens = getApiAuth().split(",");
					if (authExtractTokens.length >= 2
							&& apiAuthTokens.length >= 2) {
						if ((authExtractTokens[1].equalsIgnoreCase("json")
								|| authExtractTokens[1]
										.equalsIgnoreCase("plain")
								|| authExtractTokens[1]
										.equalsIgnoreCase("header") || authExtractTokens[1]
									.equalsIgnoreCase("cookie"))
								&& (apiAuthTokens[1].equalsIgnoreCase("header")
										|| apiAuthTokens[1]
												.equalsIgnoreCase("postparam") || apiAuthTokens[1]
											.equalsIgnoreCase("queryparam"))) {
							lauthExtractTokens = authExtractTokens;
							lapiAuthTokens = apiAuthTokens;
						} else {
							// @TODO Throw Exception
						}
					}
				}

				String[] userdet = getLoginuser().split(",");
				String[] passdet = getLoginpass().split(",");

				if (userdet.length >= 2 || passdet.length >= 2) {
					if ((userdet[1].equalsIgnoreCase("header")
							|| userdet[1].equalsIgnoreCase("postparam")
							|| userdet[1].equalsIgnoreCase("queryparam") || userdet[1]
								.equalsIgnoreCase("json"))
							&& (passdet[1].equalsIgnoreCase("header")
									|| passdet[1].equalsIgnoreCase("postparam")
									|| passdet[1]
											.equalsIgnoreCase("queryparam") || passdet[1]
										.equalsIgnoreCase("json"))
							&& userdet[1].equalsIgnoreCase(passdet[1])) {
						List<ViewField> parameters = new ArrayList<ViewField>();

						ViewField viewField = new ViewField();
						viewField.setType("url");
						viewField
								.setLabel("93be7b20299b11e281c10800200c9a66_URL");
						String url = (getUrlPrefix() != null ? getUrlPrefix()
								: "");
						if (!url.trim().isEmpty()
								&& url.trim().charAt(url.trim().length() - 1) != '/')
							url = url.trim() + "/";
						url += getLoginpath();
						viewField.getValues().add(url);
						parameters.add(viewField);

						viewField = new ViewField();
						viewField
								.setType(userdet[1].equalsIgnoreCase("header") ? "header"
										: "text");
						viewField.setLabel(userdet[0]);
						parameters.add(viewField);

						viewField = new ViewField();
						viewField
								.setType(userdet[1].equalsIgnoreCase("header") ? "header"
										: "text");
						viewField.setLabel(passdet[0]);
						parameters.add(viewField);

						getLog().info("Creating login page");

						VelocityEngine engine = new VelocityEngine();
						engine.setProperty(RuntimeConstants.RESOURCE_LOADER,
								"classpath");
						engine.setProperty("classpath.resource.loader.class",
								ClasspathResourceLoader.class.getName());
						engine.init();
						StringWriter writer = new StringWriter();

						try {
							VelocityContext context = new VelocityContext();
							context.put("title", "Login");
							context.put("testname", "Login");
							context.put("testSubmitPath", getLoginpath());
							context.put("testPath", getLoginpath());
							context.put("formName", "Login");
							context.put("httpMethod",
									(getLoginmeth() == null ? "POST"
											: getLoginmeth()));
							context.put("enctype", "");
							context.put("genDoc", true);
							context.put("genTest", true);

							if (userdet[1].equalsIgnoreCase("authbasic")) {
								context.put("consumes", "authbasic");
							} else if (userdet[1].equalsIgnoreCase("postparam")) {
								context.put("consumes",
										MediaType.APPLICATION_FORM_URLENCODED);
								context.put(
										"enctype",
										" enctype=\""
												+ MediaType.APPLICATION_FORM_URLENCODED
												+ "\"" + " method=\""
												+ context.get("httpMethod")
												+ "\"");
							} else if (userdet[1].equalsIgnoreCase("json")) {
								context.put("consumes",
										MediaType.APPLICATION_JSON);
							} else {
								context.put("consumes", "");
							}

							if (lauthExtractTokens != null) {
								String jsfuncs = "var loginExtractionNm = \""
										+ lauthExtractTokens[0] + "\";\n";
								jsfuncs += "var loginExtractionTyp = \""
										+ lauthExtractTokens[1] + "\";\n";
								jsfuncs += "var authTokNm = \""
										+ lapiAuthTokens[0] + "\";\n";
								jsfuncs += "var authTokTyp = \""
										+ lapiAuthTokens[1] + "\";\n";
								jsfuncs += "debugEnabled = " + isDebugEnabled()
										+ ";\n";
								context.put("jsfuncs", jsfuncs);
							} else {
								context.put("jsfuncs", "");
							}

							String html = "<br/><table>";
							html += "<tr><th>Parameter</th><th>Value</th></tr>";
							html += "<tr><td>HTTP Method</td><td>"
									+ context.get("httpMethod") + "</td></tr>";
							html += "<tr class=\"alt\"><td>Service URL</td><td>"
									+ url + "</td></tr>";
							html += "<tr><td>Username param</td><td>"
									+ userdet[0] + "</td></tr>";
							html += "<tr class=\"alt\"><td>Username type</td><td>"
									+ userdet[1] + "</td></tr>";
							html += "<tr><td>Password param</td><td>"
									+ passdet[0] + "</td></tr>";
							html += "<tr class=\"alt\"><td>Password type</td><td>"
									+ passdet[1] + "</td></tr>";
							html += "<tr><td>Authextract param</td><td>"
									+ lauthExtractTokens[0] + "</td></tr>";
							html += "<tr class=\"alt\"><td>Authextract type</td><td>"
									+ lauthExtractTokens[1] + "</td></tr>";
							html += "<tr><td>API auth param</td><td>"
									+ lapiAuthTokens[0] + "</td></tr>";
							html += "<tr class=\"alt\"><td>API auth type</td><td>"
									+ lapiAuthTokens[1] + "</td></tr>";
							html += "</table><br/><br/>";
							context.put("methodComments", html);

							context.put("formDetDesc", "");
							context.put("formDesc", "<b class=\""
									+ context.get("httpMethod").toString()
											.toLowerCase()
									+ "big\">POST</b>&nbsp;-&nbsp;"
									+ getLoginpath());
							context.put("vFields", parameters);

							if (!isUseBootstrapUI())
								engine.mergeTemplate(
										"/templates/formtemplate_static.vm",
										context, writer);
							else
								engine.mergeTemplate(
										"/templates/formtemplate.vm", context,
										writer);

							BufferedWriter fwriter = new BufferedWriter(
									new FileWriter(new File(
											dir.getAbsolutePath()
													+ "/TestLogin.html")));
							fwriter.write(writer.toString());
							fwriter.close();

							isLoginPresent = true;

							if (isDebugEnabled())
								getLog().debug("login");
							if (!isUseBootstrapUI())
								str.append("<h3><a class=\"asideLink\" href=\"TestLogin.html\"><span class=\"service\">TestLogin</span></a></h3>");
							else
								str.append(getAccordianHeadingHTML(
										new LinkObject("TestLogin",
												"TestLogin.html",
												accordianCounter++), null));
						} catch (Exception e1) {
							getLog().info("exception :" + e1.getMessage());
							getLog().error(e1);
						}
					}
				}
			}

			Map<String, String> mapOfClassComments = new HashMap<String, String>();
			Map<String, String> mapOfAsideLinkInfosStr = new HashMap<String, String>();
			Map<String, LinkObject> mapOfAsideLinkInfos = new HashMap<String, LinkObject>();
			Map<String, List<LinkObject>> mapOfAsideLinkInfoslnks = new HashMap<String, List<LinkObject>>();
			Map<String, Integer> classTypes = new HashMap<String, Integer>();
			for (ClassDocTestGen classDocTestGen : classes) {
				StringBuilder strc = new StringBuilder();
				Class claz = classDocTestGen.claz;
				String type = classDocTestGen.type;
				classTypes.put(claz.getSimpleName(), 0);

				Annotation theClassPath = claz.getAnnotation(Path.class);
				if (theClassPath == null)
					theClassPath = claz.getAnnotation(RequestMapping.class);

				LinkObject classhtml = null;
				List<LinkObject> classlinks = new ArrayList<LinkObject>();
				// Will create only documentation if this is not a rest ful web
				// service
				if (!type.equals("doc") && theClassPath != null) {
					if (theClassPath != null) {
						Map<String, String> mapOfComments = new HashMap<String, String>();
						if (srcDir != null) {
							generateDoc(claz, srcDir, mapOfComments,
									mapOfClassComments);
						}

						String classhtmlstr = null;
						if (isDebugEnabled())
							getLog().debug(
									"Generating for class - "
											+ claz.getSimpleName());
						if (!isUseBootstrapUI())
							classhtmlstr = "<h3><span class=\"service\">"
									+ claz.getSimpleName() + "</span><br/>";
						else
							classhtml = new LinkObject(claz.getSimpleName(),
									null, accordianCounter++);

						Method[] methods = claz.getMethods();
						boolean closehdr = false;
						for (Method method : methods) {
							Annotation theMethodPath = method
									.getAnnotation(Path.class);
							if (theMethodPath == null)
								theMethodPath = method
										.getAnnotation(RequestMapping.class);
							if (theMethodPath != null) {
								if (isDebugEnabled())
									getLog().debug(
											"Generating for method - "
													+ method.getName());

								List<ViewField> parameters = new ArrayList<ViewField>();
								String completeServicePath = null;
								String httpMethod = "";
								String consumes = "text/plain";

								if (theClassPath instanceof Path
										&& theMethodPath instanceof Path) {
									Path cpath = (Path) theClassPath;
									Path mpath = (Path) theMethodPath;
									completeServicePath = cpath.value()
											+ mpath.value();
								} else if (theClassPath instanceof RequestMapping
										&& theMethodPath instanceof RequestMapping) {
									RequestMapping cpath = (RequestMapping) theClassPath;
									RequestMapping mpath = (RequestMapping) theMethodPath;
									completeServicePath = cpath.value()[0]
											+ mpath.value()[0];
									httpMethod = mpath.method()[0].name();
									consumes = mpath.consumes()[0];
								} else {
									throw new Exception(
											"Invalid Annotation found on the Service class - "
													+ claz.getSimpleName());
								}

								if (completeServicePath != null
										&& completeServicePath.charAt(0) == '/') {
									completeServicePath = completeServicePath
											.substring(1);
								}

								ViewField viewField = new ViewField();
								viewField.setType("url");
								viewField
										.setLabel("93be7b20299b11e281c10800200c9a66_URL");
								String url = (getUrlPrefix() != null ? getUrlPrefix()
										: "");
								if (!url.trim().isEmpty()
										&& url.trim().charAt(
												url.trim().length() - 1) != '/')
									url = url.trim() + "/";
								url += completeServicePath;
								viewField.getValues().add(url);
								parameters.add(viewField);
								urlViewField = viewField;

								String completeServiceSubmitPath = url;
								Type[] argTypes = method
										.getGenericParameterTypes();
								Annotation[][] argAnot = method
										.getParameterAnnotations();
								ViewField contentvf = null;
								for (int i = 0; i < argTypes.length; i++) {
									Annotation[] annotations = getRestArgAnnotation(argAnot[i]);
									String formpnm = null;
									boolean isheaderParam = false;
									String defValue = null;
									if (annotations[0] != null) {
										if (annotations[0] instanceof FormParam)
											formpnm = ((FormParam) annotations[0])
													.value();

										if (annotations[0] instanceof RequestParam)
											formpnm = ((RequestParam) annotations[0])
													.value();

										if (annotations[0] instanceof QueryParam) {
											if (completeServiceSubmitPath
													.indexOf("?") == -1)
												completeServiceSubmitPath += "?";
											completeServiceSubmitPath += ((QueryParam) annotations[0])
													.value()
													+ "={"
													+ ((QueryParam) annotations[0])
															.value() + "}&";
											urlViewField.getValues().remove(0);
											urlViewField.getValues().add(
													completeServiceSubmitPath);
											continue;
										}

										if (annotations[0] instanceof HeaderParam) {
											formpnm = ((HeaderParam) annotations[0])
													.value();
											isheaderParam = true;
										}

										if (annotations[0] instanceof PathParam) {
											continue;
										}

									}
									if (annotations[1] != null)
										defValue = ((DefaultValue) annotations[1])
												.value();
									List<Type> heirarchies = new ArrayList<Type>();
									updateViewFields(argTypes[i], parameters,
											null, formpnm, isheaderParam, null,
											heirarchies, defValue);
									contentvf = getViewField(argTypes[i]);
								}

								VelocityContext context = new VelocityContext();
								context.put("title", claz.getSimpleName());
								context.put("testname", claz.getSimpleName());
								context.put("testSubmitPath",
										completeServiceSubmitPath);
								context.put("testPath", completeServicePath);
								context.put("formName", claz.getSimpleName()
										+ "_form");

								if (type.equals("doc-test")) {
									context.put("genDoc", true);
									context.put("genTest", true);
								} else {
									context.put("genDoc", false);
									context.put("genTest", true);
								}

								classTypes
										.put(claz.getSimpleName(), classTypes
												.get(claz.getSimpleName()) + 1);

								Annotation hm = method
										.getAnnotation(POST.class);
								if (hm != null) {
									httpMethod = "POST";
								} else {
									hm = method.getAnnotation(GET.class);
									if (hm != null) {
										httpMethod = "GET";
									} else {
										hm = method.getAnnotation(PUT.class);
										if (hm != null) {
											httpMethod = "PUT";
										} else {
											hm = method
													.getAnnotation(DELETE.class);
											if (hm != null) {
												httpMethod = "DELETE";
											}
										}
									}
								}

								context.put("httpMethod", httpMethod);

								Annotation annot = method
										.getAnnotation(Consumes.class);
								if (annot != null) {
									consumes = ((Consumes) annot).value()[0];
								}

								if (consumes
										.equals(MediaType.MULTIPART_FORM_DATA)) {
									context.put(
											"enctype",
											"action="
													+ completeServiceSubmitPath
													+ "\" enctype=\""
													+ MediaType.MULTIPART_FORM_DATA
													+ "\" method=\""
													+ httpMethod.toLowerCase()
													+ "\"");
								} else if (consumes
										.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
									context.put(
											"enctype",
											" enctype=\""
													+ MediaType.APPLICATION_FORM_URLENCODED
													+ "\" method=\""
													+ httpMethod.toLowerCase()
													+ "\"");
								} else {
									context.put("enctype", "");
								}

								if ((httpMethod.equals("POST") || httpMethod
										.equals("PUT"))
										&& getRequestContentType() != null
										&& !"".equals(getRequestContentType()
												.trim())) {
									if ("json"
											.equalsIgnoreCase(getRequestContentType()
													.trim())) {
										consumes = MediaType.APPLICATION_JSON;
									} else if ("xml"
											.equalsIgnoreCase(getRequestContentType()
													.trim())) {
										consumes = MediaType.APPLICATION_XML;
									}
								}

								if (contentvf != null
										&& contentvf.getValue() != null
										&& !isPrimitive(contentvf.getValue()
												.getClass())) {
									if (consumes
											.equals(MediaType.APPLICATION_JSON)) {
										ObjectMapper jsonMapper = new ObjectMapper();
										String content = new ObjectMapper()
												.writeValueAsString(contentvf
														.getValue());
										// content = content.replaceAll("'",
										// "\'");
										context.put("request_content", content);

										jsonMapper
												.configure(
														DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
														false);
										jsonMapper
												.configure(
														DeserializationConfig.Feature.AUTO_DETECT_FIELDS,
														true);
										String schemaJson = jsonMapper
												.generateJsonSchema(
														contentvf.getValue()
																.getClass())
												.toString();
										schemaJson = schemaJson.replaceAll("'",
												"\'");
										context.put("schemaJson", schemaJson);

									} else if (consumes
											.equals(MediaType.APPLICATION_XML)) {
										JAXBContext jcontext = JAXBContext
												.newInstance(contentvf
														.getValue().getClass());
										Marshaller m = jcontext
												.createMarshaller();
										m.setProperty(
												Marshaller.JAXB_FORMATTED_OUTPUT,
												Boolean.FALSE);
										StringWriter writer = new StringWriter();
										m.marshal(contentvf.getValue(), writer);
										String content = writer.toString();
										// content = content.replaceAll("'",
										// "\'");
										context.put("request_content", content);

										ObjectMapper jsonMapper = new ObjectMapper();
										AnnotationIntrospector introspector = new AnnotationIntrospector.Pair(
												new JaxbAnnotationIntrospector(),
												new JacksonAnnotationIntrospector());
										jsonMapper
												.setAnnotationIntrospector(introspector);
										JsonSchema schema = jsonMapper
												.generateJsonSchema(contentvf
														.getValue().getClass());
										String schemaJson = schema.toString();
										schemaJson = schemaJson.replaceAll("'",
												"\'");
										context.put("schemaJson", schemaJson);
									}
								}

								context.put("consumes", consumes);

								if (lauthExtractTokens != null) {
									String jsfuncs = "var loginExtractionNm = \""
											+ lauthExtractTokens[0] + "\";\n";
									jsfuncs += "var loginExtractionTyp = \""
											+ lauthExtractTokens[1] + "\";\n";
									jsfuncs += "var authTokNm = \""
											+ lapiAuthTokens[0] + "\";\n";
									jsfuncs += "var authTokTyp = \""
											+ lapiAuthTokens[1] + "\";\n";
									jsfuncs += "debugEnabled = "
											+ isDebugEnabled() + ";\n";
									context.put("jsfuncs", jsfuncs);
								} else {
									context.put("jsfuncs", "");
								}

								context.put("methodComments", "");
								for (Map.Entry<String, String> entry : mapOfComments
										.entrySet()) {
									String tokbfr = entry.getKey().substring(0,
											entry.getKey().indexOf("("));
									String tokafr = entry.getKey().substring(
											entry.getKey().indexOf("(") + 1);
									if (tokbfr != null && tokafr != null) {
										String[] args = tokafr.split(",");
										args = neutralizeargs(args);
										String[] tokens = tokbfr.split(" ");
										if (tokens[tokens.length - 1]
												.equals(method.getName())
												&& method.getParameterTypes().length == args.length) {
											if (isDebugEnabled())
												getLog().debug(method.getName());
											boolean flag = true;
											for (int i = 0; i < args.length; i++) {
												String t = args[i];
												if (t.indexOf("{") != -1) {
													t = t.substring(0,
															t.indexOf("{"));
												}
												if (t.indexOf(")") != -1) {
													int bo = StringUtils
															.countMatches(t,
																	"(");
													int bc = StringUtils
															.countMatches(t,
																	")");
													if (bc > bo) {
														t = t.substring(
																0,
																t.lastIndexOf(")"));
													}
												}
												if (isDebugEnabled())
													getLog().debug(
															t
																	+ " "
																	+ method.getGenericParameterTypes()[i]
																			.toString());
												String[] argbrk = t
														.split("\\s+");
												if ((argbrk.length == 2 && compareMethArgs(
														argbrk[0],
														method.getGenericParameterTypes()[i]))
														|| (argbrk.length > 2 && compareMethArgs(
																argbrk[argbrk.length - 2],
																method.getGenericParameterTypes()[i]))) {
													flag &= true;
												} else {
													flag &= false;
												}
											}
											if (flag) {
												String htmlText = parseCommentsToHTML(
														entry.getValue(),
														dir,
														rDir,
														httpMethod,
														completeServiceSubmitPath,
														consumes);
												context.put("methodComments",
														htmlText);
												break;
											}
										}
									}
								}

								context.put("formDetDesc", "");
								context.put("formDesc", "<b class=\""
										+ httpMethod.toLowerCase() + "big\">"
										+ httpMethod.toUpperCase()
										+ "</b>&nbsp;-&nbsp;"
										+ completeServiceSubmitPath);
								context.put("vFields", parameters);

								StringBuilder buil = new StringBuilder();
								List<String> uniqClasNames = new ArrayList<String>();

								for (ViewField viField : parameters) {
									if (viField.getClaz() != null
											&& !uniqClasNames.contains(viField
													.getClaz())) {
										uniqClasNames.add(viField.getClaz());
									}
								}

								int columnLimit = 3;
								int rows = uniqClasNames.size() / columnLimit;
								if (rows % columnLimit != 0)
									rows++;
								if (rows > 0) {
									buil.append("<table style=\"border:0px;table-layout:fixed;width:100%\">");
								}
								int columnNum = 0;
								for (String className : uniqClasNames) {
									if (columnNum == 0) {
										buil.append("<tr>");
									}
									buil.append("<td style=\"word-wrap:break-word;border:0px;\"><span class=\""
											+ className.replaceAll("'", "")
											+ "\" style=\"vertical-align:top;\">"
											+ className.replaceAll("'", "")
											+ "<input type=\"checkbox\" "
											+ "style=\"margin-right:10px;\" onchange=\"togglehideShowClassGroup('"
											+ className.replaceAll("'", "")
											+ "',this)\"/></span></td>");
									columnNum++;
									if (columnNum == columnLimit) {
										buil.append("</tr>");
										columnNum = 0;
									}
								}
								if (rows > 0) {
									if (columnNum <= columnLimit)
										buil.append("</tr>");
									buil.append("</table>");
								}
								context.put("shwhidgrps", buil.toString());

								VelocityEngine engine = new VelocityEngine();
								engine.setProperty(
										RuntimeConstants.RESOURCE_LOADER,
										"classpath");
								engine.setProperty(
										"classpath.resource.loader.class",
										ClasspathResourceLoader.class.getName());
								engine.init();
								StringWriter writer = new StringWriter();
								try {
									if (!isUseBootstrapUI())
										engine.mergeTemplate(
												"/templates/formtemplate_static.vm",
												context, writer);
									else
										engine.mergeTemplate(
												"/templates/formtemplate.vm",
												context, writer);

									String fileName = getTargetDir() + "/"
											+ getUripath() + "/"
											+ claz.getName() + "_"
											+ method.getName() + ".html";
									BufferedWriter fwriter = new BufferedWriter(
											new FileWriter(new File(fileName)));
									fwriter.write(writer.toString());
									fwriter.close();

									lst.add(claz.getName() + "_"
											+ method.getName() + ".html");

									if (isDebugEnabled())
										getLog().debug(method.getName());
									if (!isUseBootstrapUI()) {
										if (classhtmlstr != null) {
											strc.append(classhtmlstr);
											classhtmlstr = null;
											closehdr = true;
										}
										strc.append("<span class=\"method\"><b class=\""
												+ httpMethod.toLowerCase()
												+ "\">"
												+ httpMethod
												+ "</b></span><a class=\"asideLink\" href=\""
												+ claz.getName()
												+ "_"
												+ method.getName()
												+ ".html"
												+ "\"><span class=\"sub-service\">"
												+ method.getName()
												+ "</span></a><br/>");
									} else
										classlinks.add(new LinkObject(method
												.getName(), claz.getName()
												+ "_" + method.getName()
												+ ".html", httpMethod
												.toLowerCase()));
								} catch (Exception e1) {
									getLog().error(e1);
								}
							}
						}
						if (closehdr) {
							strc.append("</h3>");
						}
					}
				} else {
					StringBuilder build = new StringBuilder();
					Map<String, String> mapOfComments = new HashMap<String, String>();
					if (srcDir != null) {
						generateDoc(claz, srcDir, mapOfComments,
								mapOfClassComments);
					}

					VelocityContext context = new VelocityContext();
					context.put("title", claz.getSimpleName());
					context.put("testname", claz.getSimpleName());
					context.put("testSubmitPath", "");
					context.put("testPath", "");
					context.put("formName", claz.getSimpleName() + "_form");
					context.put("methodComments", "");

					if (lauthExtractTokens != null) {
						String jsfuncs = "var loginExtractionNm = \""
								+ lauthExtractTokens[0] + "\";\n";
						jsfuncs += "var loginExtractionTyp = \""
								+ lauthExtractTokens[1] + "\";\n";
						jsfuncs += "var authTokNm = \"" + lapiAuthTokens[0]
								+ "\";\n";
						jsfuncs += "var authTokTyp = \"" + lapiAuthTokens[1]
								+ "\";\n";
						jsfuncs += "debugEnabled = " + isDebugEnabled() + ";\n";
						context.put("jsfuncs", jsfuncs);
					} else {
						context.put("jsfuncs", "");
					}
					context.put("enctype", "");

					context.put("genDoc", true);
					context.put("genTest", false);

					Map<String, PropertyDescriptor> mapofDesc = new HashMap<String, PropertyDescriptor>();
					PropertyDescriptor[] propertyDescriptors = Introspector
							.getBeanInfo(claz).getPropertyDescriptors();
					for (PropertyDescriptor field : propertyDescriptors) {
						if (field.getWriteMethod() != null) {
							mapofDesc.put(field.getName(), field);
						}
						if (isDebugEnabled())
							getLog().debug(field.getName());
					}

					List<Field> allFields = new ArrayList<Field>();

					List<Field> allPublicFields = Arrays.asList(claz
							.getFields());
					for (Field field : allPublicFields) {
						if (!java.lang.reflect.Modifier.isStatic(field
								.getModifiers()))
							allFields.add(field);
						if (isDebugEnabled())
							getLog().debug(
									field.getName() + " -> " + field.getType()
											+ " -> " + field.getGenericType());
					}
					List<Field> allPrivateFields = Arrays.asList(claz
							.getDeclaredFields());
					for (Field field : allPrivateFields) {
						if (mapofDesc.get(field.getName()) != null) {
							if (!java.lang.reflect.Modifier.isStatic(field
									.getModifiers())
									&& !allFields.contains(field))
								allFields.add(field);
							if (isDebugEnabled())
								getLog().debug(
										field.getName() + " -> "
												+ field.getType() + " -> "
												+ field.getGenericType());
						}
					}

					String content = mapOfClassComments.get(claz
							.getSimpleName());
					if (content != null
							&& !content.trim().equals("")
							&& !content.replace("\n", "").replace("\r", "")
									.equals("")) {
						build.append("<div class=\"doc-content-class\"><br/>"
								+ content + "<br/></div>");
					}
					String html = "<br/><b>Properties</b><br/><table><tr><th>Name</th><th>Type</th></tr>";
					boolean even = true;
					String alt = " class=\"alt\"";
					boolean showtable = false;
					for (Field field : allFields) {
						String temp = even ? "" : alt;
						html += "<tr" + (temp) + "><td>" + field.getName()
								+ "</td><td>" + field.getType().getSimpleName()
								+ "</td></tr>";
						even = !even;
						showtable = true;
					}
					if (showtable) {
						build.append(html + "</table><br/>");
					}

					for (Map.Entry<String, String> entry : mapOfComments
							.entrySet()) {
						String tokbfr = entry.getKey().substring(0,
								entry.getKey().indexOf("("));
						String tokafr = entry.getKey().substring(
								entry.getKey().indexOf("(") + 1);
						if (tokbfr != null && tokafr != null) {
							String[] args = tokafr.split(",");
							args = neutralizeargs(args);
							String[] tokens = tokbfr.split(" ");
							for (Method method : claz.getMethods()) {
								if (tokens[tokens.length - 1].equals(method
										.getName())
										&& (method.getParameterTypes().length == args.length || (method
												.getParameterTypes().length == 0 && args[0]
												.trim().charAt(0) == ')'))) {
									if (isDebugEnabled())
										getLog().debug(method.getName());
									boolean flag = true;
									if (method.getParameterTypes().length == 0
											&& args[0].trim().charAt(0) == ')') {
									} else {
										for (int i = 0; i < args.length; i++) {
											String t = args[i];
											if (t.indexOf("{") != -1) {
												t = t.substring(0,
														t.indexOf("{"));
											}
											if (t.indexOf(")") != -1) {
												int bo = StringUtils
														.countMatches(t, "(");
												int bc = StringUtils
														.countMatches(t, ")");
												if (bc > bo) {
													t = t.substring(0,
															t.lastIndexOf(")"));
												}
											}
											if (isDebugEnabled())
												getLog().debug(
														t
																+ " "
																+ method.getGenericParameterTypes()[i]
																		.toString());
											String[] argbrk = t.split("\\s+");
											if ((argbrk.length == 2 && compareMethArgs(
													argbrk[0],
													method.getGenericParameterTypes()[i]))
													|| (argbrk.length > 2 && compareMethArgs(
															argbrk[argbrk.length - 2],
															method.getGenericParameterTypes()[i]))) {
												flag &= true;
											} else {
												flag &= false;
											}
										}
									}
									if (flag) {
										String htmlText = parseCommentsToHTMLDoc(
												entry.getValue(), dir, rDir,
												method.getName());
										build.append(htmlText + "<br/>");
									}
								}
							}
						}
					}

					context.put("methodComments", build.toString());

					VelocityEngine engine = new VelocityEngine();
					engine.setProperty(RuntimeConstants.RESOURCE_LOADER,
							"classpath");
					engine.setProperty("classpath.resource.loader.class",
							ClasspathResourceLoader.class.getName());
					engine.init();
					StringWriter writer = new StringWriter();
					try {
						if (!isUseBootstrapUI())
							engine.mergeTemplate(
									"/templates/formtemplate_static.vm",
									context, writer);
						else
							engine.mergeTemplate("/templates/formtemplate.vm",
									context, writer);

						String fileName = getTargetDir() + "/" + getUripath()
								+ "/" + claz.getName() + ".html";
						BufferedWriter fwriter = new BufferedWriter(
								new FileWriter(new File(fileName)));
						fwriter.write(writer.toString());
						fwriter.close();

						lst.add(claz.getName() + ".html");
						if (!isUseBootstrapUI()) {
							String classhtmlstr = "<h3><span class=\"service\"><a class=\"asideLink\" href=\""
									+ claz.getName()
									+ ".html"
									+ "\">"
									+ claz.getSimpleName() + "</a></span><br/>";
							strc.append(classhtmlstr);
						} else
							classhtml = new LinkObject(claz.getSimpleName(),
									claz.getName() + ".html",
									accordianCounter++);
					} catch (Exception e1) {
						getLog().error(e1);
					}
				}

				if (!isUseBootstrapUI())
					mapOfAsideLinkInfosStr.put(claz.getName(), strc.toString());
				else {
					mapOfAsideLinkInfos.put(claz.getName(), classhtml);
					mapOfAsideLinkInfoslnks.put(claz.getName(), classlinks);
				}
			}

			StringBuilder nav = new StringBuilder();
			if (isLoginPresent) {
				if (!isUseBootstrapUI())
					nav.append("<li><a href=\"TestLogin.html\">Login</a></li>");
				else
					nav.append(getAlphabetDropDownHTML("Login",
							"TestLogin.html", null, null));
			}

			char current = 'A';
			while (current <= 'Z') {
				Map<String, List<LinkObject>> mapOfDropDownSublinks = new HashMap<String, List<LinkObject>>();
				List<LinkObject> ddlinks = new ArrayList<LinkObject>();

				int counter = 0;
				int subcounter = 0;
				for (String fileName : lst) {
					String origFile = fileName;
					if (fileName.indexOf(".") != -1) {
						String[] parts = fileName.split("\\.");
						fileName = parts[parts.length - 2] + ".html";
					}
					if (!fileName.equals("index.html")
							&& fileName.toUpperCase().startsWith(current + "")) {
						if (counter++ == 0) {
							if (!isUseBootstrapUI())
								nav.append("<li><a href=\"#\">" + current
										+ "</a><ul>");
						}
						if (fileName.indexOf("_") != -1) {
							if (classTypes.get(fileName.substring(0,
									fileName.lastIndexOf("_"))) != null
									&& classTypes.get(fileName.substring(0,
											fileName.lastIndexOf("_"))) > 0) {
								if (subcounter++ == 0) {
									if (!isUseBootstrapUI())
										nav.append("<li><a href=\"#\">"
												+ fileName.substring(
														0,
														fileName.lastIndexOf("_"))
												+ "</a><ul>");
									String temp = fileName.substring(fileName
											.lastIndexOf("_") + 1);
									temp = temp.substring(0, temp.indexOf("."));
									if (!isUseBootstrapUI())
										nav.append("<li><a class=\"asideLink\" href=\""
												+ origFile
												+ "\">"
												+ temp
												+ "</a></li>");

									if (isUseBootstrapUI()) {
										ddlinks.add(new LinkObject(fileName
												.substring(0, fileName
														.lastIndexOf("_")),
												null));
										mapOfDropDownSublinks.put(fileName
												.substring(0, fileName
														.lastIndexOf("_")),
												new ArrayList<LinkObject>());
										mapOfDropDownSublinks.get(
												fileName.substring(0, fileName
														.lastIndexOf("_")))
												.add(new LinkObject(temp,
														origFile));
									}
								} else {
									String temp = fileName.substring(fileName
											.lastIndexOf("_") + 1);
									temp = temp.substring(0, temp.indexOf("."));
									if (!isUseBootstrapUI())
										nav.append("<li><a class=\"asideLink\" href=\""
												+ origFile
												+ "\">"
												+ temp
												+ "</a></li>");
									else
										mapOfDropDownSublinks.get(
												fileName.substring(0, fileName
														.lastIndexOf("_")))
												.add(new LinkObject(temp,
														origFile));
								}
								if (classTypes.get(fileName.substring(0,
										fileName.lastIndexOf("_"))) != null
										&& classTypes.get(fileName.substring(0,
												fileName.lastIndexOf("_"))) == subcounter) {
									if (!isUseBootstrapUI())
										nav.append("</ul></li>");
									subcounter = 0;
								}
							} else {
								if (!isUseBootstrapUI())
									nav.append("<li><a class=\"asideLink\" href=\""
											+ origFile
											+ "\">"
											+ fileName.substring(0,
													fileName.indexOf("."))
											+ "</a></li>");
								else
									ddlinks.add(new LinkObject(
											fileName.substring(0,
													fileName.indexOf(".")),
											origFile));
							}
						} else {
							if (!isUseBootstrapUI())
								nav.append("<li><a class=\"asideLink\" href=\""
										+ origFile
										+ "\">"
										+ fileName.substring(0,
												fileName.indexOf("."))
										+ "</a></li>");
							else
								ddlinks.add(new LinkObject(fileName.substring(
										0, fileName.indexOf(".")), origFile));
						}
					}
				}
				if (counter > 0) {
					if (!isUseBootstrapUI())
						nav.append("</ul></li>");
					else
						nav.append(getAlphabetDropDownHTML(current + "", null,
								ddlinks, mapOfDropDownSublinks));
				}
				current++;
				counter = 0;
			}

			List<String> alst = new ArrayList<String>();
			alst.add("index.html");
			if (isLoginPresent) {
				alst.add("TestLogin.html");
			}
			alst.addAll(lst);

			Set<String> classNames = null;
			if (!isUseBootstrapUI())
				classNames = mapOfAsideLinkInfosStr.keySet();
			else
				classNames = mapOfAsideLinkInfos.keySet();
			List<String> classNamesLst = new ArrayList<String>(classNames);
			Collections.sort(classNamesLst, new ClassNameComprator());
			for (String clazNm : classNamesLst) {
				if (!isUseBootstrapUI())
					str.append(mapOfAsideLinkInfosStr.get(clazNm));
				else
					str.append(getAccordianHeadingHTML(
							mapOfAsideLinkInfos.get(clazNm),
							mapOfAsideLinkInfoslnks.get(clazNm)));
			}

			if (links != null && links.length > 0) {
				List<LinkObject> lnks = new ArrayList<LinkObject>();
				for (String link : links) {
					String temp = link;
					if (link.indexOf("/") != -1)
						link = link.substring(link.lastIndexOf("/") + 1);

					if (!isUseBootstrapUI()) {
						String linkhtml = "<h3><span class=\"service\"><a class=\"asideLink\" href=\""
								+ temp + "\">" + link + "</a></span><br/>";
						str.append(linkhtml);
					} else
						lnks.add(new LinkObject(link, temp));
				}
				if (isUseBootstrapUI())
					str.append(getAccordianHeadingHTML(new LinkObject(
							"Other Links", null, accordianCounter++), lnks));
			}

			for (String fileName : alst) {
				getLog().info(fileName);

				StringWriter writer = new StringWriter();

				VelocityContext context = new VelocityContext();
				context.put("title", "Home");
				context.put("testname", "");
				context.put("testSubmitPath", "");
				context.put("testPath", "");
				context.put("formName", "");

				context.put("genDoc", true);
				context.put("genTest", false);
				context.put("httpMethod", "");
				if (fileName.equals("index.html")) {
					StringBuilder build = new StringBuilder();
					for (Map.Entry<String, String> entry : mapOfClassComments
							.entrySet()) {
						build.append("<br/><b>&#8226;&nbsp;</b><a href=\"#\" onclick=\"toggle_visibility('"
								+ entry.getKey()
								+ "_comment')\" "
								+ "style=\"font-size:15px\">"
								+ entry.getKey()
								+ "</a>"
								+ "<div id=\""
								+ entry.getKey()
								+ "_comment\" class=\"doc-content-class\"><br/>"
								+ entry.getValue() + "<br/></div>");
					}
					context.put("methodComments", build.toString());
					context.put("classComments", build.toString());
				}

				context.put("formDetDesc", "");
				context.put("formDesc", "");
				context.put("vFields", new ArrayList<ViewField>());
				context.put("shwhidgrps", "");
				context.put("asideLinkValues", str.toString());
				context.put("copywright", getCopywright());
				context.put("navigationLinks", nav.toString());
				context.put("jsfuncs", "");

				VelocityEngine engine = new VelocityEngine();
				if (fileName.equals("index.html")) {
					if (!isUseBootstrapUI()) {
						engine.setProperty(RuntimeConstants.RESOURCE_LOADER,
								"file");
						engine.setProperty(
								RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
								dir.getAbsolutePath());
						engine.init();
						engine.mergeTemplate(fileName, context, writer);
					} else {
						engine.setProperty(RuntimeConstants.RESOURCE_LOADER,
								"classpath");
						engine.setProperty("classpath.resource.loader.class",
								ClasspathResourceLoader.class.getName());
						engine.init();
						engine.mergeTemplate("/templates/formtemplate.vm",
								context, writer);
					}
				} else {
					engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
					engine.setProperty(
							RuntimeConstants.FILE_RESOURCE_LOADER_PATH,
							dir.getAbsolutePath());
					engine.init();
					engine.mergeTemplate(fileName, context, writer);
				}

				BufferedWriter fwriter = new BufferedWriter(new FileWriter(
						new File(dir.getAbsolutePath() + "/" + fileName)));
				fwriter.write(writer.toString());
				fwriter.close();
			}

		} catch (Exception e) {
			getLog().error(e);
		}
	}

	private String getAlphabetDropDownHTML(String alphabet, String href,
			List<LinkObject> ddlinks,
			Map<String, List<LinkObject>> mapOfDropDownSublinks) {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		StringWriter writer = new StringWriter();
		try {
			engine.init();

			VelocityContext context = new VelocityContext();
			if (href == null) {
				href = "#";
			}
			context.put("href", href);
			context.put("alphabet", alphabet);
			context.put("links", ddlinks);
			context.put("sublinks", mapOfDropDownSublinks);

			engine.mergeTemplate("/templates/alphateical-dropdown.vm", context,
					writer);
			return writer.toString();
		} catch (Exception e1) {
			getLog().error(e1);
		}
		return null;
	}

	private String getAccordianHeadingHTML(LinkObject link,
			List<LinkObject> links) {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		StringWriter writer = new StringWriter();
		try {
			engine.init();

			VelocityContext context = new VelocityContext();
			context.put("link", link);
			context.put("links", links);

			engine.mergeTemplate("/templates/leftnav-accordian.vm", context,
					writer);
			return writer.toString();
		} catch (Exception e1) {
			getLog().error(e1);
		}
		return null;
	}

	/**
	 * @param claz
	 * @param srcDir
	 * @param mapOfComments
	 * @param mapOfClassComments
	 * @throws Exception
	 *             Generate documentation HTML for the java class mentioned.
	 */
	@SuppressWarnings("rawtypes")
	private void generateDoc(Class claz, File srcDir,
			Map<String, String> mapOfComments,
			Map<String, String> mapOfClassComments) throws Exception {
		try {
			String path = claz.getName().replace('.', '/') + ".java";
			File srcFileForClass = new File(srcDir, path);
			if (srcFileForClass.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(
						srcFileForClass));
				String temp = null;
				StringBuilder classLevelDoc = new StringBuilder();
				boolean blkCmSt = false;
				String prev = null;
				while ((temp = reader.readLine()) != null) {
					if (!blkCmSt && temp.trim().startsWith("//")) {
						prev = null;
						String tt = temp.trim().substring(2);
						classLevelDoc.append(tt);
					} else if (!blkCmSt && temp.trim().startsWith("/*")) {
						blkCmSt = true;
						prev = null;
						if (temp.trim().indexOf("*/") != -1) {
							temp = temp.trim().substring(
									temp.trim().indexOf("/*") + 2);
							int ce = temp.trim().indexOf("*/");
							boolean flag = false;
							if (ce > 1 && temp.trim().indexOf("/*", 0) != -1) {
								String tt = temp.trim();
								int ocounter = 0, ccounter = 0;
								while (tt.length() > 0) {
									tt = tt.trim();
									if (tt.indexOf("/*") != -1) {
										++ocounter;
										tt = tt.substring(tt.indexOf("/*") + 2);
									}
									if (tt.indexOf("*/") != -1) {
										--ccounter;
										tt = tt.substring(tt.indexOf("*/") + 2);
									}
									if (tt.indexOf("*/") == -1
											|| tt.indexOf("/*") == -1) {
										break;
									}
								}
								if (ocounter - ccounter == 1) {
									flag = true;
								}
							} else {
								flag = true;
							}
							if (flag) {
								blkCmSt = false;
								if (ce != 0) {
									String tt = temp.trim().substring(0, ce);
									if (tt.trim().startsWith("*"))
										classLevelDoc.append(tt.trim()
												.substring(1));
									else
										classLevelDoc.append(tt);
								}
							}
						} else {
							String tt = temp.trim().substring(2);
							if (tt.trim().startsWith("*"))
								classLevelDoc.append(tt.trim().substring(1));
							else
								classLevelDoc.append(tt);
						}
					} else if (blkCmSt && temp.trim().indexOf("*/") != -1) {
						prev = null;
						int ce = temp.trim().indexOf("*/");
						boolean flag = false;
						if (ce > 1 && temp.trim().indexOf("/*", 0) != -1) {
							String tt = temp.trim();
							int ocounter = 0, ccounter = 0;
							while (tt.length() > 0) {
								tt = tt.trim();
								if (tt.indexOf("/*") != -1) {
									++ocounter;
									tt = tt.substring(tt.indexOf("/*") + 2);
								}
								if (tt.indexOf("*/") != -1) {
									--ccounter;
									tt = tt.substring(tt.indexOf("*/") + 2);
								}
								if (tt.indexOf("*/") == -1
										|| tt.indexOf("/*") == -1) {
									break;
								}
							}
							if (ocounter - ccounter == 1) {
								flag = true;
							}
						} else {
							flag = true;
						}
						if (flag) {
							blkCmSt = false;
							if (ce != 0) {
								String tt = temp.trim().substring(0, ce);
								if (tt.trim().startsWith("*"))
									classLevelDoc
											.append(tt.trim().substring(1));
								else
									classLevelDoc.append(tt);
							}
						}
					} else if (blkCmSt) {
						prev = null;
						String tt = temp;
						if (tt.trim().startsWith("*"))
							classLevelDoc.append(tt.trim().substring(1));
						else
							classLevelDoc.append(tt);
						classLevelDoc.append("\n");
					} else if (!blkCmSt) {
						if (temp.trim().startsWith("@") && hasValidSyntax(temp)) {

						} else if (CLASS_REGEX_PATTERN.matcher(temp).matches()
								|| CLASS_REGEX_PATTERN_WO_CB.matcher(temp)
										.matches()) {
							if (isDebugEnabled())
								getLog().debug(temp);
							if (isDebugEnabled())
								getLog().debug(classLevelDoc.toString());
							mapOfClassComments.put(claz.getSimpleName(),
									classLevelDoc.toString());
							classLevelDoc = new StringBuilder();
							prev = null;
						} else if (METHOD_REGEX_PATTERN.matcher(temp).matches()
								&& hasValidSyntax(temp)) {
							if (isDebugEnabled())
								getLog().debug(temp);
							if (isDebugEnabled())
								getLog().debug(classLevelDoc.toString());
							mapOfComments.put(temp, classLevelDoc.toString());
							classLevelDoc = new StringBuilder();
							prev = null;
						} else if (!METHOD_REGEX_PATTERN.matcher(temp)
								.matches()
								&& VARIABLE_REGEX_PATTERN.matcher(temp)
										.matches()) {
							classLevelDoc = new StringBuilder();
							prev = null;
						} else {
							if (prev == null)
								prev = temp + "\n";
							else {
								prev += temp + "\n";
								if (CLASS_REGEX_PATTERN.matcher(temp).matches()
										|| CLASS_REGEX_PATTERN_WO_CB.matcher(
												temp).matches()) {
									if (isDebugEnabled())
										getLog().debug(prev);
									if (isDebugEnabled())
										getLog().debug(classLevelDoc.toString());
									if (mapOfClassComments.get(claz
											.getSimpleName()) == null)
										mapOfClassComments.put(
												claz.getSimpleName(),
												classLevelDoc.toString());
									classLevelDoc = new StringBuilder();
									prev = null;
								} else if (METHOD_REGEX_PATTERN.matcher(prev)
										.matches() && hasValidSyntax(prev)) {
									if (isDebugEnabled())
										getLog().debug(prev);
									if (isDebugEnabled())
										getLog().debug(classLevelDoc.toString());
									mapOfComments.put(
											prev.replaceAll("\n", " "),
											classLevelDoc.toString());
									classLevelDoc = new StringBuilder();
									prev = null;
								} else if (!METHOD_REGEX_PATTERN.matcher(temp)
										.matches()
										&& VARIABLE_REGEX_PATTERN.matcher(temp)
												.matches()) {
									classLevelDoc = new StringBuilder();
									prev = null;
								}
							}
						}
					}
				}
				reader.close();
			}
		} catch (Exception e) {

		}
	}

	private boolean hasValidSyntax(String methodSig) {
		int bo = StringUtils.countMatches(methodSig, "(");
		int bc = StringUtils.countMatches(methodSig, ")");
		return bo == bc;
	}

	/**
	 * @param value
	 * @param dir
	 * @param rdir
	 * @param httpMethod
	 * @param completeServiceSubmitPath
	 * @param consumes
	 * @return Generate HTMl documentation for rest ful services
	 */
	private String parseCommentsToHTML(String value, File dir, File rdir,
			String httpMethod, String completeServiceSubmitPath, String consumes) {
		String[] lines = value.split("\n");
		Map<String, Object> elements = new HashMap<String, Object>();
		elements.put("param", new ArrayList<String>());
		elements.put("author", "");
		elements.put("return", "");
		// elements.put("deprecated", "");
		// elements.put("exception", "");
		elements.put("content", "");
		StringBuilder build = new StringBuilder();
		String prevLine = "";
		for (String line : lines) {
			if (line.trim().toLowerCase().startsWith("@param")) {
				@SuppressWarnings("unchecked")
				List<String> lst = (List<String>) elements.get("param");
				lst.add(line.trim().substring(6));
			} else if (line.trim().toLowerCase().startsWith("@author")) {
				elements.put("author", line.trim().substring(7));
			} else if (line.trim().toLowerCase().startsWith("@return")) {
				elements.put("return", line.trim().substring(7));
			}
			/*
			 * else if(line.trim().toLowerCase().startsWith("@deprecated")) {
			 * elements.put("deprecated", line.trim().substring(11)); } else
			 * if(line.trim().toLowerCase().startsWith("@exception")) {
			 * elements.put("exception", line.trim().substring(10)); }
			 */
			else {
				if (line.trim().toLowerCase().startsWith("@image")
						&& rdir != null) {
					String fileName = line.trim().substring(6).trim();
					String desc = "";
					if (fileName.indexOf(" ") != -1) {
						desc = fileName.split(" ")[1];
						fileName = fileName.split(" ")[0];
					}
					File image = new File(rdir, fileName);
					if (image.exists()) {
						try {
							File tnImage = new File(dir, "/resources/"
									+ fileName);
							Thumbnails.of(image).size(400, 400).toFile(tnImage);
						} catch (Exception e) {
							e.printStackTrace();
						}
						/*
						 * try { copyFile(image, new File(dir,
						 * "/resources/"+fileName)); } catch (IOException e) {
						 * e.printStackTrace(); }
						 */
						build.append("<br/><p>&nbsp;</p><center><img class=\"docImage\" alt=\""
								+ desc
								+ "\" src=\"resources/"
								+ fileName
								+ "\"/><br/><span><b>"
								+ desc
								+ "</b></span></center><p>&nbsp;</p><br/>\n");
					} else {

						if (line.matches("<.*>|.*<.*>.*|.*<.*>|<.*>.*")) {
							if (line.trim().indexOf("<br/>") == -1) {
								if (prevLine.endsWith("<br/>"))
									line = line + "<br/>";
								else
									line = "<br/>" + line + "<br/>";
							} else {
								if (line.trim().indexOf("<br/>") != 0) {
									if (!prevLine.endsWith("<br/>"))
										line = "<br/>" + line;
								}
								if (line.trim().length() < 5
										|| !line.trim().endsWith("<br/>"))
									line = line + "<br/>";
							}
						}
						prevLine = line;
						build.append(line + "\n");
					}
				} else {
					if (line.matches("<.*>|.*<.*>.*|.*<.*>|<.*>.*")) {
						if (line.trim().indexOf("<br/>") == -1) {
							if (prevLine.endsWith("<br/>"))
								line = line + "<br/>";
							else
								line = "<br/>" + line + "<br/>";
						} else {
							if (line.trim().indexOf("<br/>") != 0) {
								if (!prevLine.endsWith("<br/>"))
									line = "<br/>" + line;
							}
							if (line.trim().length() < 5
									|| !line.trim().endsWith("<br/>"))
								line = line + "<br/>";
						}
					}
					prevLine = line;
					build.append(line + "\n");
				}
			}
		}

		elements.put("content", build.toString());

		String html = "<br/><table>";
		html += "<tr><th>Parameter</th><th>Value</th></tr>";
		html += "<tr><td>HTTP Method</td><td>" + httpMethod + "</td></tr>";
		html += "<tr class=\"alt\"><td>Service URL</td><td>"
				+ completeServiceSubmitPath + "</td></tr>";
		html += "<tr><td>Consumes</td><td>" + consumes + "</td></tr>";
		boolean even = false;
		String alt = " class=\"alt\"";
		if (!elements.get("author").equals("")) {
			String temp = even ? "" : alt;
			html += "<tr" + (temp) + "><td>Author</td><td>"
					+ elements.get("author") + "</td></tr>";
			even = !even;
		}
		if (elements.get("param") != null) {
			@SuppressWarnings("unchecked")
			List<String> lst = (List<String>) elements.get("param");
			if (lst.size() > 0) {
				int counter = 1;
				for (String param : lst) {
					String temp = even ? "" : alt;
					html += "<tr" + (temp) + "><td>Parameter " + (counter++)
							+ "</td><td>" + param + "</td></tr>";
					even = !even;
				}
			}
		}
		if (!elements.get("return").equals("")) {
			String temp = even ? "" : alt;
			html += "<tr" + (temp) + "><td>Returns</td><td>"
					+ elements.get("return") + "</td></tr>";
			even = !even;
		}
		/*
		 * if(elements.get("deprecated").equals("")) { String temp =
		 * even?"":alt; html +=
		 * "<tr"+(temp)+"><td>Deprecated</td><td>false</td></tr>"; even = !even;
		 * } else if(!elements.get("deprecated").equals("")) { String temp =
		 * even?"":alt; html +=
		 * "<tr"+(temp)+"><td>Deprecated</td><td>true</td></tr>"; even = !even;
		 * } if(!elements.get("exception").equals("")) { String temp =
		 * even?"":alt; html +=
		 * "<tr"+(temp)+"><td>Exception</td><td>"+elements.get
		 * ("exception")+"</td></tr>"; even = !even; }
		 */
		html += "</table>";
		if (!elements.get("content").equals(""))
			html += "<div class=\"doc-content\"><div style=\"margin-left:10px;line-height:1.7;\"><br/>"
					+ elements.get("content") + "<br/><br/></div></div>";
		if (isDebugEnabled())
			getLog().debug(html);
		return html;
	}

	/**
	 * @param value
	 * @param dir
	 * @param rdir
	 * @param methodName
	 * @return Generate HTML documentation for any non rest full class methods
	 */
	private String parseCommentsToHTMLDoc(String value, File dir, File rdir,
			String methodName) {
		String[] lines = value.split("\n");
		Map<String, Object> elements = new HashMap<String, Object>();
		elements.put("param", new ArrayList<String>());
		elements.put("author", "");
		elements.put("return", "");
		elements.put("deprecated", "");
		// elements.put("exception", "");
		elements.put("content", "");
		StringBuilder build = new StringBuilder();
		String prevLine = "";
		boolean isproperty = false;
		for (String line : lines) {
			if (line.trim().toLowerCase().startsWith("@param")) {
				@SuppressWarnings("unchecked")
				List<String> lst = (List<String>) elements.get("param");
				lst.add(line.trim().substring(6));
				isproperty = true;
			} else if (line.trim().toLowerCase().startsWith("@author")) {
				elements.put("author", line.trim().substring(7));
				isproperty = true;
			} else if (line.trim().toLowerCase().startsWith("@return")) {
				elements.put("return", line.trim().substring(7));
				isproperty = true;
			} else if (line.trim().toLowerCase().startsWith("@deprecated")) {
				elements.put("deprecated", line.trim().substring(11));
				isproperty = true;
			}
			/*
			 * else if(line.trim().toLowerCase().startsWith("@exception")) {
			 * elements.put("exception", line.trim().substring(10)); isproperty
			 * = true; }
			 */
			else {
				if (line.trim().toLowerCase().startsWith("@image")
						&& rdir != null) {
					String fileName = line.trim().substring(6).trim();
					String desc = "";
					if (fileName.indexOf(" ") != -1) {
						desc = fileName.split(" ")[1];
						fileName = fileName.split(" ")[0];
					}
					File image = new File(rdir, fileName);
					if (image.exists()) {
						try {
							File tnImage = new File(dir, "/resources/"
									+ fileName);
							Thumbnails.of(image).size(400, 400).toFile(tnImage);
						} catch (Exception e) {
							e.printStackTrace();
						}
						/*
						 * try { copyFile(image, new File(dir,
						 * "/resources/"+fileName)); } catch (IOException e) {
						 * e.printStackTrace(); }
						 */
						build.append("<br/><p>&nbsp;</p><center><img class=\"docImage\" alt=\""
								+ desc
								+ "\" src=\"resources/"
								+ fileName
								+ "\"/><br/><span><b>"
								+ desc
								+ "</b></span></center><p>&nbsp;</p><br/>\n");
					} else {

						if (line.matches("<.*>|.*<.*>.*|.*<.*>|<.*>.*")) {
							if (line.trim().indexOf("<br/>") == -1) {
								if (prevLine.endsWith("<br/>"))
									line = line + "<br/>";
								else
									line = "<br/>" + line + "<br/>";
							} else {
								if (line.trim().indexOf("<br/>") != 0) {
									if (!prevLine.endsWith("<br/>"))
										line = "<br/>" + line;
								}
								if (line.trim().length() < 5
										|| !line.trim().endsWith("<br/>"))
									line = line + "<br/>";
							}
						}
						prevLine = line;
						build.append(line + "\n");
					}
				} else {
					if (line.matches("<.*>|.*<.*>.*|.*<.*>|<.*>.*")) {
						if (line.trim().indexOf("<br/>") == -1) {
							if (prevLine.endsWith("<br/>"))
								line = line + "<br/>";
							else
								line = "<br/>" + line + "<br/>";
						} else {
							if (line.trim().indexOf("<br/>") != 0) {
								if (!prevLine.endsWith("<br/>"))
									line = "<br/>" + line;
							}
							if (line.trim().length() < 5
									|| !line.trim().endsWith("<br/>"))
								line = line + "<br/>";
						}
					}
					prevLine = line;
					build.append(line + "\n");
				}
			}
		}

		elements.put("content", build.toString());

		String html = "";
		if (isproperty) {
			html = "<br/><table>";
			html += "<tr><th>Parameter</th><th>Value</th></tr>";
			boolean even = true;
			String alt = " class=\"alt\"";
			if (!elements.get("author").equals("")) {
				String temp = even ? "" : alt;
				html += "<tr" + (temp) + "><td>Author</td><td>"
						+ elements.get("author") + "</td></tr>";
				even = !even;
			}
			if (elements.get("param") != null) {
				@SuppressWarnings("unchecked")
				List<String> lst = (List<String>) elements.get("param");
				if (lst.size() > 0) {
					int counter = 1;
					for (String param : lst) {
						String temp = even ? "" : alt;
						html += "<tr" + (temp) + "><td>Parameter "
								+ (counter++) + "</td><td>" + param
								+ "</td></tr>";
						even = !even;
					}
				}
			}
			if (!elements.get("return").equals("")) {
				String temp = even ? "" : alt;
				html += "<tr" + (temp) + "><td>Returns</td><td>"
						+ elements.get("return") + "</td></tr>";
				even = !even;
			}
			if (elements.get("deprecated").equals("")) {
				String temp = even ? "" : alt;
				html += "<tr" + (temp)
						+ "><td>Deprecated</td><td>false</td></tr>";
				even = !even;
			} else if (!elements.get("deprecated").equals("")) {
				String temp = even ? "" : alt;
				html += "<tr" + (temp)
						+ "><td>Deprecated</td><td>true</td></tr>";
				even = !even;
			}
			/*
			 * if(!elements.get("exception").equals("")) { String temp =
			 * even?"":alt; html +=
			 * "<tr"+(temp)+"><td>Exception</td><td>"+elements
			 * .get("exception")+"</td></tr>"; even = !even; }
			 */
			html += "</table>";
		}

		String content = (String) elements.get("content");
		if (!content.replace("\n", "").replace("\r", "").equals("")
				&& !content.replaceAll("<br/>", "").replace("\n", "")
						.replace("\r", "").equals("")) {
			html = "<div class=\"doc-content\"><div style=\"margin-left:10px;line-height:1.7;\"><br/>"
					+ "<b>"
					+ methodName
					+ "</b><br/>"
					+ content
					+ "<br/><br/></div></div>" + html;
		} else if (isproperty) {
			html = "<div class=\"doc-content\"><div style=\"margin-left:10px;line-height:1.7;\"><br/>"
					+ "<b>" + methodName + "</b><br/></div></div>" + html;
		}
		if (isDebugEnabled())
			getLog().debug(html);
		return html;
	}

	public static String getFileExtension(String fileName) {
		if (fileName == null || fileName.indexOf(".") == -1)
			return null;
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 *             Copy a file to some desired location
	 */
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();

			// previous code: destination.transferFrom(source, 0,
			// source.size());
			// to avoid infinite loops, should be:
			long count = 0;
			long size = source.size();
			while ((count += destination.transferFrom(source, count, size
					- count)) < size)
				;
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes" })
	private static List<Class> getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "rawtypes" })
	private static List<Class> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				Class claz = Thread
						.currentThread()
						.getContextClassLoader()
						.loadClass(
								packageName
										+ '.'
										+ file.getName().substring(0,
												file.getName().length() - 6));
				if (claz != null) {
					classes.add(claz);
				} else {
					classes.add(Class.forName(packageName
							+ '.'
							+ file.getName().substring(0,
									file.getName().length() - 6)));
				}
			}
		}
		return classes;
	}

	/**
	 * @param claz
	 * @param parameters
	 * @param naming
	 * @param formpnm
	 * @param isheaderParam
	 * @param heirarchy
	 * @throws Exception
	 *             Add new ViewFeild objects that represnt the form elements on
	 *             the test page of the give rest full service
	 */
	/*
	 * private void updateViewFields1(@SuppressWarnings("rawtypes") Class claz,
	 * List<ViewField> parameters, String naming, String formpnm, boolean
	 * isheaderParam, String heirarchy) throws Exception { if
	 * ((claz.equals(Integer.class) || claz.equals(String.class) ||
	 * claz.equals(Short.class) || claz.equals(Long.class) ||
	 * claz.equals(Double.class) || claz.equals(Float.class) ||
	 * claz.equals(Boolean.class) || claz.equals(int.class) ||
	 * claz.equals(short.class) || claz.equals(long.class) ||
	 * claz.equals(double.class) || claz.equals(float.class) ||
	 * claz.equals(boolean.class) || claz.equals(Number.class) ||
	 * claz.equals(Date.class)) && formpnm != null) { ViewField viewField = new
	 * ViewField(); viewField.setType(isheaderParam ? "header" : "text");
	 * viewField.setVarType(claz); String name = formpnm; if (naming != null)
	 * viewField.setLabel(naming + "['" + name + "']"); else
	 * viewField.setLabel(name); if (isDebugEnabled())
	 * getLog().debug(viewField.toString()); parameters.add(viewField); } else
	 * if (claz.equals(String.class) && formpnm == null && naming == null) {
	 * ViewField viewField = new ViewField(); viewField.setType(isheaderParam ?
	 * "header" : "textarea"); viewField.setVarType(claz);
	 * viewField.setLabel("content"); if (isDebugEnabled())
	 * getLog().debug(viewField.toString()); parameters.add(viewField); } else
	 * if (claz.getCanonicalName().equals(
	 * "org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput"))
	 * { ViewField viewField = new ViewField(); viewField.setType(isheaderParam
	 * ? "header" : "multipartform"); viewField.setLabel("Parameters");
	 * parameters.add(viewField); if (isDebugEnabled())
	 * getLog().debug(viewField.toString()); } else if
	 * (claz.getCanonicalName().equals("javax.servlet.http.HttpServletResponse")
	 * ||
	 * claz.getCanonicalName().equals("javax.servlet.http.HttpServletRequest"))
	 * { return; } else { Map<String,PropertyDescriptor> mapofDesc = new
	 * HashMap<String,PropertyDescriptor>(); PropertyDescriptor[]
	 * propertyDescriptors =
	 * Introspector.getBeanInfo(claz).getPropertyDescriptors(); for
	 * (PropertyDescriptor field : propertyDescriptors) { if
	 * (field.getWriteMethod() != null) { mapofDesc.put(field.getName(), field);
	 * } if (isDebugEnabled()) getLog().debug(field.getName()); }
	 * 
	 * List<Field> allFields = new ArrayList<Field>();
	 * 
	 * List<Field> allPublicFields = Arrays.asList(claz.getFields()); for (Field
	 * field : allPublicFields) { if
	 * (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))
	 * allFields.add(field); if (isDebugEnabled())
	 * getLog().debug(field.getName() + " -> " + field.getType() + " -> " +
	 * field.getGenericType()); } List<Field> allPrivateFields =
	 * Arrays.asList(claz.getDeclaredFields()); for (Field field :
	 * allPrivateFields) { if (mapofDesc.get(field.getName()) != null) { if
	 * (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
	 * !allFields.contains(field)) allFields.add(field); if (isDebugEnabled())
	 * getLog().debug(field.getName() + " -> " + field.getType() + " -> " +
	 * field.getGenericType()); } } for (Field field : allFields) { if
	 * (field.getType().equals(Integer.class) ||
	 * field.getType().equals(String.class) ||
	 * field.getType().equals(Short.class) || field.getType().equals(Long.class)
	 * || field.getType().equals(Double.class) ||
	 * field.getType().equals(Float.class) ||
	 * field.getType().equals(Boolean.class) ||
	 * field.getType().equals(int.class) || field.getType().equals(short.class)
	 * || field.getType().equals(long.class) ||
	 * field.getType().equals(double.class) ||
	 * field.getType().equals(float.class) ||
	 * field.getType().equals(boolean.class) ||
	 * field.getType().equals(Number.class) ||
	 * field.getType().equals(Date.class)) { ViewField viewField = new
	 * ViewField(); viewField.setType(isheaderParam ? "header" : "text");
	 * viewField.setVarType(field.getType()); String name = (formpnm == null ?
	 * field.getName() : formpnm); if (naming != null) {
	 * viewField.setLabel(naming + "['" + name + "']");
	 * viewField.setClaz(naming); } else viewField.setLabel(name);
	 * parameters.add(viewField); if (isDebugEnabled())
	 * getLog().debug(viewField.toString()); } else if
	 * (field.getType().equals(Map.class) ||
	 * field.getType().equals(HashMap.class) ||
	 * field.getType().equals(LinkedHashMap.class)) { ViewField viewField = new
	 * ViewField(); viewField.setType(isheaderParam ? "header" : "map"); if
	 * (field.getGenericType() instanceof ParameterizedType) { ParameterizedType
	 * type = (ParameterizedType) field.getGenericType(); if
	 * (type.getActualTypeArguments().length == 2) { if
	 * (!(type.getActualTypeArguments()[0].equals(Integer.class) ||
	 * type.getActualTypeArguments()[0].equals(String.class) ||
	 * type.getActualTypeArguments()[0].equals(Short.class) ||
	 * type.getActualTypeArguments()[0].equals(Long.class) ||
	 * type.getActualTypeArguments()[0].equals(Double.class) ||
	 * type.getActualTypeArguments()[0].equals(Float.class) ||
	 * type.getActualTypeArguments()[0].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(int.class) ||
	 * type.getActualTypeArguments()[0].equals(short.class) ||
	 * type.getActualTypeArguments()[0].equals(long.class) ||
	 * type.getActualTypeArguments()[0].equals(double.class) ||
	 * type.getActualTypeArguments()[0].equals(float.class) ||
	 * type.getActualTypeArguments()[0].equals(boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(Number.class) || type
	 * .getActualTypeArguments()[0].equals(Date.class)) &&
	 * (type.getActualTypeArguments()[1].equals(Integer.class) ||
	 * type.getActualTypeArguments()[1].equals(String.class) ||
	 * type.getActualTypeArguments()[1].equals(Short.class) ||
	 * type.getActualTypeArguments()[1].equals(Long.class) ||
	 * type.getActualTypeArguments()[1].equals(Double.class) ||
	 * type.getActualTypeArguments()[1].equals(Float.class) ||
	 * type.getActualTypeArguments()[1].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[1].equals(int.class) ||
	 * type.getActualTypeArguments()[1].equals(short.class) ||
	 * type.getActualTypeArguments()[1].equals(long.class) ||
	 * type.getActualTypeArguments()[1].equals(double.class) ||
	 * type.getActualTypeArguments()[1].equals(float.class) ||
	 * type.getActualTypeArguments()[1].equals(boolean.class) ||
	 * type.getActualTypeArguments()[1].equals(Number.class) || type
	 * .getActualTypeArguments()[1].equals(Date.class))) {
	 * viewField.setType("kcmap"); } else if
	 * ((type.getActualTypeArguments()[0].equals(Integer.class) ||
	 * type.getActualTypeArguments()[0].equals(String.class) ||
	 * type.getActualTypeArguments()[0].equals(Short.class) ||
	 * type.getActualTypeArguments()[0].equals(Long.class) ||
	 * type.getActualTypeArguments()[0].equals(Double.class) ||
	 * type.getActualTypeArguments()[0].equals(Float.class) ||
	 * type.getActualTypeArguments()[0].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(int.class) ||
	 * type.getActualTypeArguments()[0].equals(short.class) ||
	 * type.getActualTypeArguments()[0].equals(long.class) ||
	 * type.getActualTypeArguments()[0].equals(double.class) ||
	 * type.getActualTypeArguments()[0].equals(float.class) ||
	 * type.getActualTypeArguments()[0].equals(boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(Number.class) || type
	 * .getActualTypeArguments()[0].equals(Date.class)) &&
	 * !(type.getActualTypeArguments()[1].equals(Integer.class) ||
	 * type.getActualTypeArguments()[1].equals(String.class) ||
	 * type.getActualTypeArguments()[1].equals(Short.class) ||
	 * type.getActualTypeArguments()[1].equals(Long.class) ||
	 * type.getActualTypeArguments()[1].equals(Double.class) ||
	 * type.getActualTypeArguments()[1].equals(Float.class) ||
	 * type.getActualTypeArguments()[1].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[1].equals(int.class) ||
	 * type.getActualTypeArguments()[1].equals(short.class) ||
	 * type.getActualTypeArguments()[1].equals(long.class) ||
	 * type.getActualTypeArguments()[1].equals(double.class) ||
	 * type.getActualTypeArguments()[1].equals(float.class) ||
	 * type.getActualTypeArguments()[1].equals(boolean.class) ||
	 * type.getActualTypeArguments()[1].equals(Number.class) || type
	 * .getActualTypeArguments()[1].equals(Date.class))) {
	 * viewField.setType("vcmap"); } else if
	 * (!(type.getActualTypeArguments()[0].equals(Integer.class) ||
	 * type.getActualTypeArguments()[0].equals(String.class) ||
	 * type.getActualTypeArguments()[0].equals(Short.class) ||
	 * type.getActualTypeArguments()[0].equals(Long.class) ||
	 * type.getActualTypeArguments()[0].equals(Double.class) ||
	 * type.getActualTypeArguments()[0].equals(Float.class) ||
	 * type.getActualTypeArguments()[0].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(int.class) ||
	 * type.getActualTypeArguments()[0].equals(short.class) ||
	 * type.getActualTypeArguments()[0].equals(long.class) ||
	 * type.getActualTypeArguments()[0].equals(double.class) ||
	 * type.getActualTypeArguments()[0].equals(float.class) ||
	 * type.getActualTypeArguments()[0].equals(boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(Number.class) || type
	 * .getActualTypeArguments()[0].equals(Date.class)) &&
	 * !(type.getActualTypeArguments()[1].equals(Integer.class) ||
	 * type.getActualTypeArguments()[1].equals(String.class) ||
	 * type.getActualTypeArguments()[1].equals(Short.class) ||
	 * type.getActualTypeArguments()[1].equals(Long.class) ||
	 * type.getActualTypeArguments()[1].equals(Double.class) ||
	 * type.getActualTypeArguments()[1].equals(Float.class) ||
	 * type.getActualTypeArguments()[1].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[1].equals(int.class) ||
	 * type.getActualTypeArguments()[1].equals(short.class) ||
	 * type.getActualTypeArguments()[1].equals(long.class) ||
	 * type.getActualTypeArguments()[1].equals(double.class) ||
	 * type.getActualTypeArguments()[1].equals(float.class) ||
	 * type.getActualTypeArguments()[1].equals(boolean.class) ||
	 * type.getActualTypeArguments()[1].equals(Number.class) || type
	 * .getActualTypeArguments()[1].equals(Date.class))) {
	 * viewField.setType("kvcmap"); } } } String name = (formpnm == null ?
	 * field.getName() : formpnm); if (naming != null) {
	 * viewField.setLabel(naming + "['" + name + "']");
	 * viewField.setClaz(naming); } else viewField.setLabel(name); if
	 * (isDebugEnabled()) getLog().debug(viewField.toString());
	 * parameters.add(viewField); } else if (field.getType().equals(List.class)
	 * || field.getType().equals(ArrayList.class) ||
	 * field.getType().equals(LinkedList.class)) { ViewField viewField = new
	 * ViewField(); viewField.setType(isheaderParam ? "header" : "list"); if
	 * (field.getGenericType() instanceof ParameterizedType) { ParameterizedType
	 * type = (ParameterizedType) field.getGenericType(); if
	 * (type.getActualTypeArguments().length > 0) { if
	 * (type.getActualTypeArguments().length == 1 &&
	 * (type.getActualTypeArguments()[0].equals(Integer.class) ||
	 * type.getActualTypeArguments()[0].equals(String.class) ||
	 * type.getActualTypeArguments()[0].equals(Short.class) ||
	 * type.getActualTypeArguments()[0].equals(Long.class) ||
	 * type.getActualTypeArguments()[0].equals(Double.class) ||
	 * type.getActualTypeArguments()[0].equals(Float.class) ||
	 * type.getActualTypeArguments()[0].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(int.class) ||
	 * type.getActualTypeArguments()[0].equals(short.class) ||
	 * type.getActualTypeArguments()[0].equals(long.class) ||
	 * type.getActualTypeArguments()[0].equals(double.class) ||
	 * type.getActualTypeArguments()[0].equals(float.class) ||
	 * type.getActualTypeArguments()[0].equals(boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(Number.class) || type
	 * .getActualTypeArguments()[0].equals(Date.class))) { } else { String
	 * nnaming = naming; if (nnaming == null) nnaming = field.getName(); else
	 * nnaming += ("['" + field.getName() + "']"); String nheirarchy =
	 * heirarchy; boolean doit = true; if (nheirarchy == null) nheirarchy = "."
	 * + claz.getSimpleName() + "." + field.getType().getSimpleName() +
	 * type.getActualTypeArguments()[0].getClass().getSimpleName() + "."; else {
	 * if (nheirarchy.indexOf("." +
	 * type.getActualTypeArguments()[0].getClass().getSimpleName() + ".") == -1)
	 * nheirarchy += "." +
	 * type.getActualTypeArguments()[0].getClass().getSimpleName() + "."; else {
	 * doit = false;
	 * getLog().info("Ignoring recursive fields inside object heirarchy..."); }
	 * } if (doit) { updateViewFields((Class) type.getActualTypeArguments()[0],
	 * parameters, nnaming, null, false, nheirarchy);
	 * viewField.setType("clistwobj"); } else viewField.setType("clist"); } } }
	 * String name = (formpnm == null ? field.getName() : formpnm); if (naming
	 * != null) { viewField.setLabel(naming + "['" + name + "']");
	 * viewField.setClaz(naming); } else viewField.setLabel(name); if
	 * (isDebugEnabled()) getLog().debug(viewField.toString());
	 * parameters.add(viewField); } else if (field.getType().equals(Set.class)
	 * || field.getType().equals(HashSet.class) ||
	 * field.getType().equals(LinkedHashSet.class)) { ViewField viewField = new
	 * ViewField(); viewField.setType(isheaderParam ? "header" : "set"); if
	 * (field.getGenericType() instanceof ParameterizedType) { ParameterizedType
	 * type = (ParameterizedType) field.getGenericType(); if
	 * (type.getActualTypeArguments().length > 0) { if
	 * (type.getActualTypeArguments().length == 1 &&
	 * (type.getActualTypeArguments()[0].equals(Integer.class) ||
	 * type.getActualTypeArguments()[0].equals(String.class) ||
	 * type.getActualTypeArguments()[0].equals(Short.class) ||
	 * type.getActualTypeArguments()[0].equals(Long.class) ||
	 * type.getActualTypeArguments()[0].equals(Double.class) ||
	 * type.getActualTypeArguments()[0].equals(Float.class) ||
	 * type.getActualTypeArguments()[0].equals(Boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(int.class) ||
	 * type.getActualTypeArguments()[0].equals(short.class) ||
	 * type.getActualTypeArguments()[0].equals(long.class) ||
	 * type.getActualTypeArguments()[0].equals(double.class) ||
	 * type.getActualTypeArguments()[0].equals(float.class) ||
	 * type.getActualTypeArguments()[0].equals(boolean.class) ||
	 * type.getActualTypeArguments()[0].equals(Number.class) || type
	 * .getActualTypeArguments()[0].equals(Date.class))) { } else {
	 * viewField.setType("cset"); } } } String name = (formpnm == null ?
	 * field.getName() : formpnm); if (naming != null) {
	 * viewField.setLabel(naming + "['" + name + "']");
	 * viewField.setClaz(naming); } else viewField.setLabel(name); if
	 * (isDebugEnabled()) getLog().debug(viewField.toString());
	 * parameters.add(viewField); } else if
	 * (field.getType().getCanonicalName().equals
	 * ("org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput"
	 * )) { ViewField viewField = new ViewField();
	 * viewField.setType(isheaderParam ? "header" : "multipartform"); //
	 * viewField.setLabel(naming + "." + // propertyDescriptor.getName()); if
	 * (isDebugEnabled()) getLog().debug(viewField.toString());
	 * parameters.add(viewField); } else if (!claz.equals(field.getType())) {
	 * String nnaming = naming; if (nnaming == null) nnaming = field.getName();
	 * else nnaming += ("['" + field.getName() + "']"); String nheirarchy =
	 * heirarchy; boolean doit = true; if (nheirarchy == null) nheirarchy = "."
	 * + claz.getSimpleName() + "." + field.getType().getSimpleName() + ".";
	 * else { if (nheirarchy.indexOf("." + field.getType().getSimpleName() +
	 * ".") == -1) nheirarchy += "." + field.getType().getSimpleName() + ".";
	 * else { doit = false;
	 * getLog().info("Ignoring recursive fields inside object heirarchy..."); }
	 * } if (doit) updateViewFields(field.getType(), parameters, nnaming, null,
	 * false, nheirarchy); } else if (claz.equals(field.getType())) {
	 * getLog().info("Ignoring recursive fields..."); } } } }
	 */

	/**
	 * @param claz
	 * @param parameters
	 * @param naming
	 * @param formpnm
	 * @param isheaderParam
	 * @param heirarchy
	 * @throws Exception
	 *             Add new ViewFeild objects that represnt the form elements on
	 *             the test page of the give rest full service
	 */
	@SuppressWarnings("rawtypes")
	private void updateViewFields(Type claz, List<ViewField> parameters,
			String naming, String formpnm, boolean isheaderParam,
			String heirarchy, List<Type> heirarchies, String defValue)
			throws Exception {
		ViewField viewField = null;

		ParameterizedType type = null;
		Class clas = null;
		if (claz instanceof ParameterizedType) {
			type = (ParameterizedType) claz;
			clas = (Class) type.getRawType();
		} else {
			clas = (Class) claz;
		}

		if (isPrimitive(clas)) {
			viewField = new ViewField();
			viewField.setType(isheaderParam ? "header" : "text");
			viewField.setVarType(clas);
			String name = formpnm;
			if (naming != null) {
				if (name == null)
					viewField.setLabel(naming);
				else
					viewField.setLabel(naming + "['" + name + "']");
				if (viewField.getLabel() != null
						&& viewField.getLabel().indexOf("[") != -1) {
					viewField.setClaz(viewField.getLabel().substring(0,
							viewField.getLabel().lastIndexOf("[")));
				}
			} else
				viewField.setLabel(name);
			setValidation(clas, viewField);
			if (isDebugEnabled())
				getLog().debug(viewField.toString());
			if (defValue != null)
				viewField.getValues().add(defValue);
			parameters.add(viewField);
		} else if (claz.equals(String.class) && formpnm == null
				&& naming == null) {
			viewField = new ViewField();
			viewField.setType(isheaderParam ? "header" : "textarea");
			viewField.setVarType(clas);
			viewField.setLabel("content");
			if (isDebugEnabled())
				getLog().debug(viewField.toString());
			parameters.add(viewField);
		} else if (clas.getCanonicalName().equals(
				"javax.servlet.http.HttpServletResponse")
				|| clas.getCanonicalName().equals(
						"javax.servlet.http.HttpServletRequest")) {
			return;
		} else if (isMap(clas)) {
			viewField = new ViewField();
			viewField.setType("map");
			Type[] types = type.getActualTypeArguments();
			if (!isPrimitive(types[0]) && isPrimitive(types[1])) {
				viewField.setType("kcmap");
			} else if (isPrimitive(types[0]) && !isPrimitive(types[1])) {
				viewField.setType("vcmap");
			} else if (!isPrimitive(types[0]) && !isPrimitive(types[1])) {
				viewField.setType("kvcmap");
			}
			String name = formpnm;
			if (naming != null) {
				if (name == null)
					viewField.setLabel(naming);
				else
					viewField.setLabel(naming + "['" + name + "']");
				if (viewField.getLabel() != null
						&& viewField.getLabel().indexOf("[") != -1) {
					viewField.setClaz(viewField.getLabel().substring(0,
							viewField.getLabel().lastIndexOf("[")));
				}
			} else
				viewField.setLabel(name);
			if (isDebugEnabled())
				getLog().debug(viewField.toString());
			parameters.add(viewField);
		} else if (isCollection(clas)) {
			viewField = new ViewField();
			viewField.setType("list");
			viewField.setType((clas.getSimpleName().endsWith("Set")) ? "set"
					: "list");
			Type[] types = type.getActualTypeArguments();
			if (isPrimitive(types[0])) {
				setValidation(types[0], viewField);
			} else if (clas.getSimpleName().endsWith("Set")) {
				viewField.setType("cset");
			} else {
				viewField.setType("clist");
			}
			String name = formpnm;
			if (naming != null) {
				if (name == null)
					viewField.setLabel(naming);
				else
					viewField.setLabel(naming + "['" + name + "']");
				if (viewField.getLabel() != null
						&& viewField.getLabel().indexOf("[") != -1) {
					viewField.setClaz(viewField.getLabel().substring(0,
							viewField.getLabel().lastIndexOf("[")));
				}
			} else
				viewField.setLabel(name);
			if (isDebugEnabled())
				getLog().debug(viewField.toString());
			parameters.add(viewField);
		} else if ((clas.isInterface() || Modifier.isAbstract(clas
				.getModifiers()) && heirarchies.size() == 0)) {
			viewField = new ViewField();
			viewField.setType("multipartform");
			viewField.setLabel("Parameters");
			parameters.add(viewField);
			if (isDebugEnabled())
				getLog().debug(viewField.toString());
			return;
		} else {
			updateObjectViewFields(clas, parameters, naming, formpnm,
					isheaderParam, heirarchy, heirarchies);
		}
	}

	@SuppressWarnings("rawtypes")
	private List<Field> getAllFields(Class claz) {
		List<Field> allFields = new ArrayList<Field>();
		allFields.addAll(Arrays.asList(claz.getFields()));
		allFields.addAll(Arrays.asList(claz.getDeclaredFields()));

		if (!Object.class.equals(claz.getSuperclass())) {
			allFields.addAll(getAllFields(claz.getSuperclass()));
		}
		return allFields;
	}

	private String getHeirarchyStr(List<Type> fheirlst) {
		StringBuilder b = new StringBuilder();
		if (!fheirlst.isEmpty()) {
			for (Type type : fheirlst) {
				b.append(type.toString() + ".");
			}
		}
		return b.toString();
	}

	@SuppressWarnings({ "rawtypes" })
	private void updateObjectViewFields(Class claz, List<ViewField> parameters,
			String naming, String formpnm, boolean isheaderParam,
			String heirarchy, List<Type> heirarchies) throws Exception {

		if (heirarchies.contains(claz) || heirarchies.size() >= 2)
			return;
		heirarchies.add(claz);

		List<Field> allFields = getAllFields(claz);

		for (Field field : allFields) {

			if (Modifier.isStatic(field.getModifiers()))
				continue;

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			List<Type> fheirlst = new ArrayList<Type>(heirarchies);

			if (isDebugEnabled())
				getLog().info(
						"Parsing Class " + getHeirarchyStr(fheirlst)
								+ " field " + field.getName() + " type "
								+ field.getType().equals(boolean.class));

			String nnaming = naming;
			if (nnaming == null) {
				nnaming = field.getName();
				// formpnm = field.getName();
			} else {
				nnaming += ("['" + field.getName() + "']");
			}
			String nheirarchy = heirarchy;
			boolean doit = true;
			if (nheirarchy == null)
				nheirarchy = "." + claz.getSimpleName() + "."
						+ field.getType().getSimpleName() + ".";
			else {
				if (nheirarchy.indexOf("." + field.getType().getSimpleName()
						+ ".") == -1)
					nheirarchy += "." + field.getType().getSimpleName() + ".";
				else {
					doit = false;
					getLog().info(
							"Ignoring recursive fields inside object heirarchy...");
				}
			}
			if (doit) {
				if (isPrimitive(field.getType()) || isMap(field.getType())
						|| isCollection(field.getType())
						|| !claz.equals(field.getType())) {
					doit = true;
				} else {
					doit = false;
					getLog().info("Ignoring recursive fields...");
				}
				if (doit) {
					if (field.getType().isEnum())
						updateViewFields(String.class, parameters, nnaming,
								formpnm, false, nheirarchy, heirarchies, null);
					else
						updateViewFields(field.getGenericType(), parameters,
								nnaming, formpnm, false, nheirarchy,
								heirarchies, null);
				}
			}

		}
	}

	/**
	 * @param claz
	 * @param parameters
	 * @param naming
	 * @param formpnm
	 * @param isheaderParam
	 * @param heirarchy
	 * @throws Exception
	 *             Add new ViewFeild objects that represnt the form elements on
	 *             the test page of the give rest full service
	 */
	@SuppressWarnings("rawtypes")
	private ViewField getViewField(Type claz) throws Exception {
		ViewField viewField = null;
		try {
			ParameterizedType type = null;
			Class clas = null;
			if (claz instanceof ParameterizedType) {
				type = (ParameterizedType) claz;
				clas = (Class) type.getRawType();
			} else {
				clas = (Class) claz;
			}

			List<Type> heirarchies = new ArrayList<Type>();
			if (isMap(clas)) {
				viewField = new ViewField();
				viewField.setValue(getMapValue(clas,
						type.getActualTypeArguments(), heirarchies));
			} else if (isCollection(clas)) {
				viewField = new ViewField();
				viewField.setValue(getListSetValue(clas,
						type.getActualTypeArguments(), heirarchies));
			} else if (!clas.isInterface()) {
				viewField = new ViewField();
				viewField.setValue(getObject(claz, heirarchies));
			}
		} catch (Exception e) {
			getLog().error(e);
			getLog().info(
					"Invalid class, cannot be represented as a form/object in a test case - class name = "
							+ claz);
		}
		return viewField;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getObject(Class claz, List<Type> heirarchies)
			throws Exception {

		if (claz.isEnum())
			return claz.getEnumConstants()[0];

		if (isMap(claz)) {
			return getMapValue(claz, claz.getTypeParameters(), heirarchies);
		} else if (isCollection(claz)) {
			return getListSetValue(claz, claz.getTypeParameters(), heirarchies);
		} else if (claz.isInterface()
				|| Modifier.isAbstract(claz.getModifiers())) {
			return null;
		}

		if (heirarchies.contains(claz) || heirarchies.size() >= 2)
			return null;
		heirarchies.add(claz);

		Constructor cons = null;
		try {
			cons = claz.getConstructor(new Class[] {});
		} catch (Exception e) {
			getLog().error(
					"No public no-args constructor found for class "
							+ claz.getName());
			return null;
		}

		Object object = cons.newInstance(new Object[] {});
		List<Field> allFields = getAllFields(claz);

		for (Field field : allFields) {

			if (Modifier.isStatic(field.getModifiers()))
				continue;

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			List<Type> fheirlst = new ArrayList<Type>(heirarchies);

			if (isDebugEnabled())
				getLog().info(
						"Parsing Class " + getHeirarchyStr(fheirlst)
								+ " field " + field.getName() + " type "
								+ field.getType().equals(boolean.class));

			if (isPrimitive(field.getType())) {
				field.set(object, getPrimitiveValue(field.getType()));
			} else if (isMap(field.getType())) {
				ParameterizedType type = (ParameterizedType) field
						.getGenericType();
				field.set(
						object,
						getMapValue(field.getType(),
								type.getActualTypeArguments(), fheirlst));
			} else if (isCollection(field.getType())) {
				ParameterizedType type = (ParameterizedType) field
						.getGenericType();
				field.set(
						object,
						getListSetValue(field.getType(),
								type.getActualTypeArguments(), fheirlst));
			} else if (!claz.equals(field.getType())) {
				Object fieldval = getObject(field.getType(), fheirlst);
				field.set(object, fieldval);
			} else if (claz.equals(field.getType())) {
				if (isDebugEnabled())
					getLog().info("Ignoring recursive fields...");
			}
		}
		return object;
	}

	@SuppressWarnings("rawtypes")
	private Object getObject(Type claz, List<Type> heirarchies)
			throws Exception {

		ParameterizedType type = null;
		Class clas = null;
		if (claz instanceof ParameterizedType) {
			type = (ParameterizedType) claz;
			clas = (Class) type.getRawType();
		} else {
			clas = (Class) claz;
		}
		if (isPrimitive(clas)) {
			return getPrimitiveValue(clas);
		} else if (isMap(clas)) {
			return getMapValue(clas, type.getActualTypeArguments(), heirarchies);
		} else if (isCollection(clas)) {
			return getListSetValue(clas, type.getActualTypeArguments(),
					heirarchies);
		} else if (!clas.isInterface()) {
			return getObject(clas, heirarchies);
		}
		return null;
	}

	private Object getPrimitiveValue(Type claz) {
		if (isPrimitive(claz)) {
			if (claz.equals(boolean.class) || claz.equals(Boolean.class)) {
				Random rand = new Random();
				return rand.nextBoolean();
			} else if (claz.equals(Date.class)) {
				return new Date();
			} else if (claz.equals(Double.class) || claz.equals(double.class)) {
				Random rand = new Random(12345678L);
				return rand.nextDouble();
			} else if (claz.equals(Float.class) || claz.equals(float.class)) {
				Random rand = new Random(12345678L);
				return rand.nextFloat();
			} else if (claz.equals(String.class)) {
				return RandomStringUtils.randomAlphabetic(10);
			} else if (claz.equals(Long.class) || claz.equals(long.class)
					|| claz.equals(Number.class)) {
				Random rand = new Random();
				return new Long(rand.nextInt(123));
			} else if (claz.equals(Integer.class) || claz.equals(int.class)) {
				Random rand = new Random();
				return new Integer(rand.nextInt(123));
			} else if (claz.equals(Short.class) || claz.equals(short.class)) {
				Random rand = new Random();
				return new Short((short) rand.nextInt(123));
			}
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getMapValue(Type type, Type[] types, List<Type> heirarchies)
			throws Exception {

		Class claz = null;
		if (type.equals(Map.class) || type.equals(HashMap.class)
				|| type.equals(LinkedHashMap.class)) {
			if (type.equals(Map.class)) {
				claz = HashMap.class;
			} else {
				claz = (Class) type;
			}
		}

		Constructor cons = claz.getConstructor(new Class[] {});
		Object object = cons.newInstance(new Object[] {});

		for (int i = 0; i < 1; i++) {
			Object k = null;
			if (isPrimitive(types[0])) {
				k = getPrimitiveValue(types[0]);
			} else if (!heirarchies.contains(types[0])) {
				if (types[0] instanceof Class)
					k = getObject((Class) types[0], heirarchies);
				else
					k = getObject(types[0], heirarchies);
				heirarchies.remove(types[0]);
			}
			Object v = null;
			if (isPrimitive(types[1])) {
				v = getPrimitiveValue(types[1]);
			} else if (!heirarchies.contains(types[1])) {
				if (types[1] instanceof Class)
					v = getObject((Class) types[1], heirarchies);
				else
					v = getObject(types[1], heirarchies);
				heirarchies.remove(types[1]);
			}
			if (k == null && isDebugEnabled()) {
				getLog().info(types[0].toString());
				getLog().info(types[1].toString());
				getLog().error("Null key " + types[0]);
			}
			((Map) object).put(k, v);
		}
		if (!isPrimitive(types[0])) {
			heirarchies.add(types[0]);
		}
		if (!isPrimitive(types[1])) {
			heirarchies.add(types[1]);
		}
		return object;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getListSetValue(Type type, Type[] types,
			List<Type> heirarchies) throws Exception {

		Class claz = null;
		if (type.equals(List.class) || type.equals(Collection.class)
				|| type.equals(ArrayList.class)
				|| type.equals(LinkedList.class)) {
			if (type.equals(List.class) || type.equals(Collection.class)) {
				claz = ArrayList.class;
			} else {
				claz = (Class) type;
			}
		} else if (type.equals(Set.class) || type.equals(HashSet.class)
				|| type.equals(LinkedHashSet.class)) {
			if (type.equals(Set.class)) {
				claz = HashSet.class;
			} else {
				claz = (Class) type;
			}
		}

		Constructor cons = claz.getConstructor(new Class[] {});
		Object object = cons.newInstance(new Object[] {});

		if (types.length == 1) {
			for (int i = 0; i < 1; i++) {
				Object v = null;
				if (isPrimitive(types[0])) {
					v = getPrimitiveValue(types[0]);
				} else if (!heirarchies.contains(types[0])) {
					if (types[0] instanceof Class)
						v = getObject((Class) types[0], heirarchies);
					else
						v = getObject(types[0], heirarchies);
					heirarchies.remove(types[0]);
				}
				((Collection) object).add(v);
			}
			if (!isPrimitive(types[0])) {
				heirarchies.add(types[0]);
			}
		}
		return object;
	}

	private boolean isPrimitive(Type claz) {
		return (claz.equals(Integer.class) || claz.equals(String.class)
				|| claz.equals(Short.class) || claz.equals(Long.class)
				|| claz.equals(Double.class) || claz.equals(Float.class)
				|| claz.equals(Boolean.class) || claz.equals(int.class)
				|| claz.equals(short.class) || claz.equals(long.class)
				|| claz.equals(double.class) || claz.equals(float.class)
				|| claz.equals(boolean.class) || claz.equals(Number.class) || claz
					.equals(Date.class));
	}

	private void setValidation(Type claz, ViewField vf) {
		if (isPrimitive(claz)) {
			if (claz.equals(Integer.class) || claz.equals(Short.class)
					|| claz.equals(Long.class) || claz.equals(int.class)
					|| claz.equals(short.class) || claz.equals(long.class)
					|| claz.equals(Number.class)) {
				vf.getValidations().add(new Validation("number"));
			} else if (claz.equals(Double.class) || claz.equals(Float.class)
					|| claz.equals(double.class) || claz.equals(float.class)) {
				vf.getValidations().add(new Validation("float"));
			} else if (claz.equals(Boolean.class) || claz.equals(boolean.class)) {
				vf.getValidations().add(new Validation("float"));
			} else if (claz.equals(Date.class)) {
				vf.setType("date");
			}
		}
	}

	private boolean isCollection(Type claz) {
		return (claz.equals(List.class) || claz.equals(ArrayList.class)
				|| claz.equals(LinkedList.class) || claz.equals(Set.class)
				|| claz.equals(HashSet.class)
				|| claz.equals(LinkedHashSet.class) || claz
					.equals(Collection.class));
	}

	private boolean isMap(Type claz) {
		return (claz.equals(Map.class) || claz.equals(HashMap.class)
				|| claz.equals(LinkedHashMap.class) || claz
					.equals(TreeMap.class));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ClassLoader getClassLoader() {
		try {
			List classpathElements = project.getCompileClasspathElements();
			classpathElements.add(project.getBuild().getOutputDirectory());
			classpathElements.add(project.getBuild().getTestOutputDirectory());
			URL[] urls = new URL[classpathElements.size()];
			for (int i = 0; i < classpathElements.size(); i++) {
				urls[i] = new File((String) classpathElements.get(i)).toURI()
						.toURL();
			}
			return new URLClassLoader(urls, getClass().getClassLoader());
		} catch (Exception e) {
			getLog().error("Couldn't get the classloader.");
		}
		return getClass().getClassLoader();
	}

	/**
	 * @param zipFile
	 * @param directoryToExtractTo
	 *            Provides file unzip functionality
	 */
	public void unzipZipFile(InputStream zipFile, String directoryToExtractTo) {
		ZipInputStream in = new ZipInputStream(zipFile);
		try {
			File directory = new File(directoryToExtractTo);
			if (!directory.exists()) {
				directory.mkdirs();
				getLog().info("Creating directory for Extraction...");
			}
			ZipEntry entry = in.getNextEntry();
			while (entry != null) {
				try {
					File file = new File(directory, entry.getName());
					if (entry.isDirectory()) {
						file.mkdirs();
					} else {
						FileOutputStream out = new FileOutputStream(file);
						byte[] buffer = new byte[2048];
						int len;
						while ((len = in.read(buffer)) > 0) {
							out.write(buffer, 0, len);
						}
						out.close();
					}
					in.closeEntry();
					entry = in.getNextEntry();
				} catch (Exception e) {
					getLog().error(e);
				}
			}
		} catch (IOException ioe) {
			getLog().error(ioe);
			return;
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {

			InputStream io = new FileInputStream(args[0]);
			XStream xstream = new XStream(new XppDriver());
			xstream.processAnnotations(new Class[] { TestgenConfiguration.class });
			xstream.alias("docTestPaths", String[].class);
			xstream.alias("docPaths", String[].class);
			xstream.alias("testPaths", String[].class);
			xstream.alias("links", String[].class);
			xstream.alias("docTestPath", String.class);
			xstream.alias("docPath", String.class);
			xstream.alias("testPath", String.class);
			xstream.alias("link", String.class);

			TestgenConfiguration config = (TestgenConfiguration) xstream
					.fromXML(io);

			TestGeneratorMojo testGenerator = new TestGeneratorMojo();
			testGenerator.setDebugEnabled(config.isDebugEnabled());
			testGenerator.setEnabled(config.isEnabled());
			testGenerator.setApiAuth(config.getApiAuth());
			testGenerator.setAuthExtract(config.getAuthextract());
			testGenerator.setCopywright(config.getCopywright());
			testGenerator.setDocPaths(config.getDocPaths());
			testGenerator.setDocTestPaths(config.getDocTestPaths());
			testGenerator.setLinks(config.getLinks());
			testGenerator.setLoginmeth(config.getLoginmeth());
			testGenerator.setLoginpass(config.getLoginpass());
			testGenerator.setLoginpath(config.getLoginpath());
			testGenerator.setLoginuser(config.getLoginuser());
			testGenerator.setRequestContentType(config.getRequestContentType());
			testGenerator.setResourcepath(config.getResourcepath());
			testGenerator.setSourceDir(config.getSourceDir());
			testGenerator.setTargetDir(config.getTargetDir());
			testGenerator.setTestPaths(config.getTestPaths());
			testGenerator.setUripath(config.getUripath());
			testGenerator.setUrlPrefix(config.getUrlPrefix());
			testGenerator.setUseBootstrapUI(config.isUseBootstrapUI());
			testGenerator.execute();
		}
	}

	/**
	 * @param argStr
	 * @param argType
	 * @return Compare method string argument identifier to the actual Type
	 *         identifier
	 */
	private boolean compareMethArgs(String argStr, Type argType) {
		List<String> genTokens = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(argType.toString(), "<>,");
		while (tok.hasMoreTokens()) {
			String type = tok.nextToken().trim();
			if (isDebugEnabled())
				getLog().debug(type);
			genTokens.add(type);
		}
		int counter = 0;
		boolean flag = false;
		tok = new StringTokenizer(argStr, "<>,");
		while (tok.hasMoreTokens()) {
			String type = tok.nextToken().trim();
			String trgTyp = genTokens.get(counter++);
			if (type.equals(trgTyp)) {
				flag = true;
				if (isDebugEnabled())
					getLog().debug(type + " - " + trgTyp + " = exact-match");
			} else if (type
					.equals(trgTyp.substring(trgTyp.lastIndexOf(".") + 1))) {
				flag = true;
				if (isDebugEnabled())
					getLog().debug(type + " - " + trgTyp + " = dottted-match");
			} else {
				if (type.indexOf(")") != -1
						&& type.substring(type.lastIndexOf(")") + 1).equals(
								trgTyp)) {
					flag = true;
					if (isDebugEnabled())
						getLog().debug(
								type + " - " + trgTyp
										+ " = close-annot-misplaced-match");
				} else {
					if (isDebugEnabled())
						getLog().debug(type + " - " + trgTyp + " = no-match");
					return false;
				}
			}
		}
		if (counter < genTokens.size())
			return false;
		return flag;
	}

	/**
	 * @param args
	 * @return 'Neutralize generic characters in the string argument to a
	 *         comparable string array
	 */
	private String[] neutralizeargs(String[] args) {
		List<String> result = new ArrayList<String>();
		String prev = null;
		for (String arg : args) {
			if (prev != null)
				prev += "," + arg.trim();
			else
				prev = arg;
			int go = StringUtils.countMatches(prev, "<");
			int gc = StringUtils.countMatches(prev, ">");
			if (go == gc) {
				result.add(prev.trim());
				prev = null;
			}
		}
		return result.toArray(new String[result.size()]);
	}

	@SuppressWarnings("rawtypes")
	class ClassDocTestGen {
		Class claz;
		String type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@SuppressWarnings("rawtypes")
	public void execute() {
		if (!isEnabled())
			return;

		Thread currentThread = Thread.currentThread();
		ClassLoader oldClassLoader = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(getClassLoader());
			getLog().info("Inside execute");
			if (getDocTestPaths() == null && getDocPaths() == null
					&& getTestPaths() == null) {
				getLog().info("Nothing to generate..");
				return;
			}
			List<ClassDocTestGen> allClasses = new ArrayList<ClassDocTestGen>();
			if (getDocTestPaths() != null) {
				for (String item : getDocTestPaths()) {
					if (item.endsWith(".*")) {
						List<Class> classes = getClasses(item.substring(0,
								item.indexOf(".*")));
						if (classes != null && classes.size() > 0) {
							for (Class claz : classes) {
								ClassDocTestGen classDocTestGen = new ClassDocTestGen();
								classDocTestGen.claz = claz;
								classDocTestGen.type = "doc-test";
								allClasses.add(classDocTestGen);
							}
							getLog().info("Adding package " + item);
						} else {
							getLog().error("Error:package not found - " + item);
						}
					} else {
						try {
							ClassDocTestGen classDocTestGen = new ClassDocTestGen();
							classDocTestGen.claz = Thread.currentThread()
									.getContextClassLoader().loadClass(item);
							classDocTestGen.type = "doc-test";
							allClasses.add(classDocTestGen);
							getLog().info("Adding class " + item);
						} catch (Exception e) {
							getLog().error("Error:class not found - " + item);
						}
					}
				}
			}
			if (getDocPaths() != null) {
				for (String item : getDocPaths()) {
					if (item.endsWith(".*")) {
						List<Class> classes = getClasses(item.substring(0,
								item.indexOf(".*")));
						if (classes != null && classes.size() > 0) {
							for (Class claz : classes) {
								ClassDocTestGen classDocTestGen = new ClassDocTestGen();
								classDocTestGen.claz = claz;
								classDocTestGen.type = "doc";
								allClasses.add(classDocTestGen);
							}
							getLog().info("Adding package " + item);
						} else {
							getLog().error("Error:package not found - " + item);
						}

					} else {
						try {
							ClassDocTestGen classDocTestGen = new ClassDocTestGen();
							classDocTestGen.claz = Thread.currentThread()
									.getContextClassLoader().loadClass(item);
							classDocTestGen.type = "doc";
							allClasses.add(classDocTestGen);
							getLog().info("Adding class " + item);
						} catch (Exception e) {
							getLog().error("Error:class not found - " + item);
						}
					}
				}
			}
			if (getTestPaths() != null) {
				for (String item : getTestPaths()) {
					if (item.endsWith(".*")) {
						List<Class> classes = getClasses(item.substring(0,
								item.indexOf(".*")));
						if (classes != null && classes.size() > 0) {
							for (Class claz : classes) {
								ClassDocTestGen classDocTestGen = new ClassDocTestGen();
								classDocTestGen.claz = claz;
								classDocTestGen.type = "test";
								allClasses.add(classDocTestGen);
							}
							getLog().info("Adding package " + item);
						} else {
							getLog().error("Error:package not found - " + item);
						}
					} else {
						try {
							ClassDocTestGen classDocTestGen = new ClassDocTestGen();
							classDocTestGen.claz = Thread.currentThread()
									.getContextClassLoader().loadClass(item);
							classDocTestGen.type = "test";
							allClasses.add(classDocTestGen);
							getLog().info("Adding class " + item);
						} catch (Exception e) {
							getLog().error("Error:class not found - " + item);
						}
					}
				}
			}
			generate(allClasses);
			getLog().info("Done execute");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			currentThread.setContextClassLoader(oldClassLoader);
		}
	}
}
