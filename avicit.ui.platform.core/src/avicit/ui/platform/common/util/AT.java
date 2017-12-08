package avicit.ui.platform.common.util;

import junit.framework.Assert;

 public final class AT {

	public static void isNotNull(Object obj) {
		Assert.assertNotNull(obj);
	}
	
	public static void isNotNull(String msg,Object obj) {
		Assert.assertNotNull(msg,obj);
	}
	
	public static void isNull(Object obj) {
		Assert.assertNull(obj);
	}

	public static void isNull(String msg, Object obj) {
		Assert.assertNull(msg,obj);
	}

	public static void isTrue(boolean bl) {
		Assert.assertTrue(bl);
	}

	public static void isTrue(String msg, boolean bl) {
		Assert.assertTrue(msg, bl);
	}

	public static void fail() throws RuntimeException {
		Assert.fail();
	}

	public static void fail(String msg) throws RuntimeException {
		Assert.fail(msg);
	}
	
	public static void notsupported() throws RuntimeException {
		throw new RuntimeException("NotSupportedException");
	}

	public static void isFalse(boolean bl) {
		Assert.assertFalse(bl);
	}

	public static void isFalse(String msg, boolean bl) {
		Assert.assertFalse(msg, bl);
	}
	
	public static void throwe(Throwable e) throws RuntimeException {
			throw new RuntimeException(e);
	}

}
