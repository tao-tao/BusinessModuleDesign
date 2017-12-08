package avicit.ui.word;

import java.awt.Color;
import java.awt.SystemColor;

import com.lowagie.text.Font;
import com.lowagie.text.rtf.style.RtfFont;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

/**
 * @author Tao Tao
 *
 */
public interface FontsAndColorsRepository {
	public static RtfParagraphStyle title1Font = new TitleFonts(RtfParagraphStyle.STYLE_HEADING_1, "黑 体", Font.BOLD, 12).getStyle();
	public static RtfParagraphStyle title2Font = new TitleFonts(RtfParagraphStyle.STYLE_HEADING_2, "黑 体", Font.NORMAL, 15).getStyle();
	public static RtfParagraphStyle title3Font = new TitleFonts(RtfParagraphStyle.STYLE_HEADING_3, "黑 体", Font.NORMAL, 12).getStyle();

	public static RtfFont font1 = new RtfFont("宋 体", 11, Font.ITALIC, Color.BLUE);
	public static RtfFont font2 = new RtfFont("宋 体", 12, Font.NORMAL, Color.BLACK);
	public static RtfFont font3 = new RtfFont("宋 体", 11, Font.BOLD, Color.BLACK);
	public static RtfFont font4 = new RtfFont("宋 体", 11, Font.NORMAL, Color.BLUE);
	public static RtfFont font5 = new RtfFont("黑 体", 11, Font.NORMAL, Color.BLACK);
	public static RtfFont font6 = new RtfFont("黑 体", 11, Font.BOLD, Color.BLACK);

	public static Color backgroundColor = new Color(SystemColor.window.getRGB());
}
