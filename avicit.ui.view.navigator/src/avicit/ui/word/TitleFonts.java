package avicit.ui.word;

import com.lowagie.text.rtf.style.RtfParagraphStyle;

/**
 * @author Tao Tao
 *
 */
public class TitleFonts {

	RtfParagraphStyle style;

	public TitleFonts(RtfParagraphStyle style, String name, int font, float size) {
		this.style = style;
		this.style.setStyle(font);
		this.style.setFontName(name);
		this.style.setSpacingBefore(6);
		this.style.setSpacingAfter(6);
		this.style.setSize(size);
	}

	public RtfParagraphStyle getStyle() {
		return style;
	}
}
