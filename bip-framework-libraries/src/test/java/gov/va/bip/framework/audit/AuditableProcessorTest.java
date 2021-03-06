package gov.va.bip.framework.audit;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

import javax.tools.JavaFileObject;

import org.junit.Test;

import com.google.testing.compile.JavaFileObjects;


/**
 * The Class AuditableProcessorTest.
 */
public class AuditableProcessorTest {

	private static final JavaFileObject AUDIT_DATE_VALID_VALUE =
			JavaFileObjects.forSourceLines(
					"test.AuditableTestSuccess",
					"package test;",
					"",
					"import gov.va.bip.framework.audit.annotation.Auditable; \n",
					"import gov.va.bip.framework.audit.AuditEvents; \n",
					"public class AuditableTestSuccess {",
					"  @gov.va.bip.framework.audit.annotation.Auditable(event = AuditEvents.SERVICE_AUDIT, activity = \"partnerPersonInfoByPtcpntId\", auditDate = \"'09/09/2009 12:36:39'\")",
					"  public int testMethod() { ",
					"        return -1; ",
					"    } ",
					"}");

	private static final JavaFileObject AUDIT_DATE_VALID_SPEL_VALUE =
			JavaFileObjects.forSourceLines(
					"test.AuditableTestSuccess",
					"package test;",
					"",
					"import gov.va.bip.framework.audit.annotation.Auditable; \n",
					"import gov.va.bip.framework.audit.AuditEvents; \n",
					"public class AuditableTestSuccess {",
					"  @gov.va.bip.framework.audit.annotation.Auditable(event = AuditEvents.SERVICE_AUDIT, activity = \"partnerPersonInfoByPtcpntId\", auditDate = \"{new java.text.SimpleDateFormat().format(new java.util.Date())}\")",
					"  public int testMethod() { ",
					"        return -1; ",
					"    } ",
					"}");
	
	private static final JavaFileObject AUDIT_DATE_EMPTY_VALUE =
			JavaFileObjects.forSourceLines(
					"test.AuditableTestSuccess",
					"package test;",
					"",
					"import gov.va.bip.framework.audit.annotation.Auditable; \n",
					"import gov.va.bip.framework.audit.AuditEvents; \n",
					"public class AuditableTestSuccess {",
					"  @gov.va.bip.framework.audit.annotation.Auditable(event = AuditEvents.SERVICE_AUDIT, activity = \"partnerPersonInfoByPtcpntId\", auditDate = \"\")",
					"  public int testMethod() { ",
					"        return -1; ",
					"    } ",
					"}");
	
	private static final JavaFileObject NO_AUDIT_DATE_INVALID_MODIFIER =
			JavaFileObjects.forSourceLines(
					"test.AuditableTestFail",
					"package test;",
					"",
					"import gov.va.bip.framework.audit.annotation.Auditable; \n",
					"import gov.va.bip.framework.audit.AuditEvents; \n",
					"public class AuditableTestFail {",
					"  @gov.va.bip.framework.audit.annotation.Auditable(event = AuditEvents.SERVICE_AUDIT, activity = \"partnerPersonInfoByPtcpntId\")",
					"  private final int testMethod() { ",
					"        return -1; ",
					"    } ",
					"}");

	@Test
	public void testSuccessAnnotationProcessValidAuditDate() {
		AuditableProcessor p = new AuditableProcessor();
		// assert
		assertAbout(javaSource()).that(AUDIT_DATE_VALID_VALUE).processedWith(p).compilesWithoutError();
	}

	@Test
	public void testSuccessAnnotationProcessValidSpelAuditDate() {
		AuditableProcessor p = new AuditableProcessor();
		// assert
		assertAbout(javaSource()).that(AUDIT_DATE_VALID_SPEL_VALUE).processedWith(p).compilesWithoutError();
	}

	@Test
	public void testSuccessAnnotationProcessEmptyAuditDate() {
		AuditableProcessor p = new AuditableProcessor();
		// assert
		assertAbout(javaSource()).that(AUDIT_DATE_EMPTY_VALUE).processedWith(p).compilesWithoutError();
	}

	@Test
	public void testFailAnnotationProcessInvalidModifier() {
		AuditableProcessor p = new AuditableProcessor();
		// assert
		assertAbout(javaSource()).that(NO_AUDIT_DATE_INVALID_MODIFIER).processedWith(p).failsToCompile();
	}
}