package avicit.ui.common.util;

import java.awt.Color;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.rtf.style.RtfFont;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

public class GenerateFontsUtil {

	private RtfParagraphStyle title1Font;
	private RtfParagraphStyle title2Font;
	private RtfParagraphStyle title3Font;
	private RtfFont font1;
	private RtfFont font2;
	private RtfFont font3;
	private RtfFont font4;
	private RtfFont font5;

	private GenerateFontsUtil(){
		initFonts();
	}

	private void initFonts() {
		title1Font = RtfParagraphStyle.STYLE_HEADING_1;
		title1Font.setAlignment(Element.ALIGN_LEFT);
		title1Font.setStyle(Font.BOLD);
		title1Font.setFontName("黑 体");
		title1Font.setSpacingBefore(6);
		title1Font.setSpacingAfter(6);
		title1Font.setSize(12);

		title2Font = RtfParagraphStyle.STYLE_HEADING_2;
		title2Font.setAlignment(Element.ALIGN_LEFT);
		title2Font.setStyle(Font.NORMAL);
		title2Font.setFontName("黑 体");
		title2Font.setSpacingBefore(6);
		title2Font.setSpacingAfter(6);

		title3Font = RtfParagraphStyle.STYLE_HEADING_3;
		title3Font.setAlignment(Element.ALIGN_LEFT);
		title3Font.setStyle(Font.NORMAL);
		title3Font.setFontName("黑 体");
		title3Font.setSpacingAfter(6);
		title3Font.setSpacingBefore(6);

		setContextFont(font1, "宋 体", 11, Font.ITALIC, Color.BLUE);
		setContextFont(font2, "宋 体", 12, Font.NORMAL, Color.BLACK);
		setContextFont(font3, "宋 体", 11, Font.BOLD, Color.BLACK);
		setContextFont(font4, "宋 体", 11, Font.NORMAL, Color.BLUE);
		setContextFont(font5, "黑 体", 11, Font.NORMAL, Color.BLACK);
	}

	private void setContextFont(RtfFont arg0, String arg1, float arg2, int arg3, Color arg4) {
		arg0 = new RtfFont(arg1, arg2, arg3, arg4);
	}

	public static GenerateFontsUtil getInstance(){
		return generateWordUtil;
	}

	private static GenerateFontsUtil generateWordUtil = new GenerateFontsUtil();
}
