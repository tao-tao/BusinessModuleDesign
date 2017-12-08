package avicit.ui.platform.common.util.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import avicit.ui.platform.common.util.AT;

public class SWTTextTransformer extends AbstractTransformer {

	private Text text;

	public SWTTextTransformer(String propName, Text text) {
		super(propName);
		this.text = text;
	}

	public Object getPropValue() {
		return this.text.getText();
	}

	public void setPropValue(Object value) {
		this.text.setText(null == value ? "" : (String) value);
	}

	private static class TextDataPusher {
		Control[] texts;
		Object[] data;

		public TextDataPusher(Control[] texts, Object[] data) {
			this.texts = texts;
			this.data = data;
			AT.isTrue(texts.length == data.length);
		}

		public void push() {
			int length = texts.length;
			for (int i = 0; i < length; i++) {
				Control cl = texts[i];
				Object value = data[i];
				if (null == value){
					continue;
				}

				if (cl instanceof Text) {
					((Text) cl).setText((String) value);

				} else if (cl instanceof Combo) {
					((Combo) cl).setText((String) value);

				} else if (cl instanceof Spinner) {
					((Spinner) cl).setSelection((Integer) value);

				} else {
					AT.fail("not supported type");
				}

				trim(cl);

			}
		}

		private String trim(String str) {
			while (needTrimBegin(str)) {
				str = str.substring(2);
			}
			while (needTrimEnd(str)) {
				str = str.substring(0, str.length() - 2);
			}

			return str;
		}

		private boolean needTrimEnd(String str) {
			return (str.endsWith("\n") || str.endsWith("\t"))
					&& str.length() > 2;
		}

		private boolean needTrimBegin(String str) {
			return (str.startsWith("\n") || str.startsWith("\t"))
					&& str.length() > 2;
		}

		private void trim(Control cl) {
			if (cl instanceof Text) {
				Text text2 = ((Text) cl);
				text2.setText(trim(text2.getText().trim()));
			} else if (cl instanceof Combo) {
				Combo combo = ((Combo) cl);
				combo.setText(trim(combo.getText().trim()));
			}

		}
	}

	public static void pushTextData(Control[] texts, Object[] data) {
		new TextDataPusher(texts, data).push();
	}

	public static void pullTextData(Object receiver, ControlBinder[] binders) {

		for (int i = 0; i < binders.length; i++) {
			ControlBinder cb = binders[i];
			Control cl = cb.ctl;
			Object value = null;
			if (cl instanceof Text) {
				value = ((Text) cl).getText(); // String
			} else if (cl instanceof Combo) {
				value = ((Combo) cl).getText(); // String
			} else if (cl instanceof Spinner) {
				value = new Integer(((Spinner) cl).getSelection()); // Integer
			} else {
				AT.fail();
			}

			try {
				PropertyUtils.setSimpleProperty(receiver, cb.prop, value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}

	}

}
