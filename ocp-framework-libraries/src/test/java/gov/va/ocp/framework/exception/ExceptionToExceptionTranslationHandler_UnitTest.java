package gov.va.ocp.framework.exception;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import gov.va.ocp.framework.exception.ExceptionToExceptionTranslationHandler;
import gov.va.ocp.framework.exception.OcpRuntimeException;
import gov.va.ocp.framework.service.ServiceException;

/**
 * Unit tests the ExceptionToExceptionTranslationHandler
 *
 * @author Jon Shrader
 */
public class ExceptionToExceptionTranslationHandler_UnitTest {

	@Test
	public void handleViaTranslation_Defenses() throws Throwable {
		ExceptionToExceptionTranslationHandler handler = new ExceptionToExceptionTranslationHandler();

		// this should do nothing, should die gracefully and not result in any exceptions
		handler.handleViaTranslation(null, null, null);
		handler.handleViaTranslation(getMethodToUseInTests(), null, null);
		handler.handleViaTranslation(null, null, new RuntimeException());

	}

	@Test
	public void handleViaTranslation_NoDefaultType() throws NoSuchMethodException, SecurityException {
		ExceptionToExceptionTranslationHandler handler = new ExceptionToExceptionTranslationHandler();

		// asserts RuntimeException wrapped as a custom type
		RuntimeException runtimeException = new RuntimeException("RuntimeException on purpose for unit test");
		try {
			handler.handleViaTranslation(getMethodToUseInTests(), null, runtimeException);
			Assert.fail("expected exception");
		} catch (Throwable throwable) {
			Assert.assertTrue(throwable instanceof OcpRuntimeException);
			Assert.assertEquals(runtimeException, throwable.getCause());
		}

		// asserts the default custom type isn't wrapped
		OcpRuntimeException ocpRuntimeException =
				new OcpRuntimeException("OcpRuntimeException on purpose for unit test");
		try {
			handler.handleViaTranslation(getMethodToUseInTests(), null, ocpRuntimeException);
			Assert.fail("expected exception");
		} catch (Throwable throwable) {
			Assert.assertTrue(throwable instanceof OcpRuntimeException);
			Assert.assertEquals(ocpRuntimeException, throwable);
			Assert.assertNull(throwable.getCause());
		}

		// asserts the subclass of default custom type isn't wrapped
		ServiceException serviceException = new ServiceException("ServiceException on purpose for unit test");
		try {
			handler.handleViaTranslation(getMethodToUseInTests(), null, serviceException);
			Assert.fail("expected exception");
		} catch (Throwable throwable) {
			Assert.assertTrue(throwable instanceof ServiceException);
			Assert.assertEquals(serviceException, throwable);
			Assert.assertNull(throwable.getCause());
		}

	}

	/**
	 * Gets the Method to use in tests. Method isn't critical for these tests.
	 *
	 * @return the method to use in tests
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 */
	private Method getMethodToUseInTests() throws NoSuchMethodException, SecurityException {
		return ExceptionToExceptionTranslationHandler.class.getMethod("handleViaTranslation", Method.class, Object[].class,
				Throwable.class);
	}

}
