package text;

import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;

public class FormattableString {
	
	private JLabel jLabel = new JLabel();
	private String[] delim = new String[] {" "};
	private Font font;
	private FontMetrics fm;
	
	public void setDelimiters(String[] d) {
		this.delim = d;
	}
	
	public void setFont(Font f) {
		this.font = f;
	}
	
	//================================||
	// CONSTRUCTOR
	//================================||
	public FormattableString() {}
	
	/** 
	 * @param w - specified width bounds in px. a valid font must be set first
	 * @return formatted text to fit in width like |foo bar\n|baz bam\n| where "|" means width bound
	 */
	public String fitInWidth(String s, int w) {
		fm = jLabel.getFontMetrics(font);
		return formatTextWidthRecursion("", "", s, w);
	}
	
	/**
	 * explode the string using \n and get the widest row as biggestRowLength, calculating the height using ratio
	 * in a loop, reduce both w and h with 5% and try to fit text in new sizes
	 * @param w
	 * @param h
	 * @return
	 */
	public String fitInRatio(String s, int ratioX, int ratioY) {
		if (s.length() == 0) return "";
		String[] rows = s.split("\n");
		
		fm = jLabel.getFontMetrics(font);
		int widthBound = 1;
		// calculating start width based on existing string max width
		for (String row : rows) {
			int tmp = fm.stringWidth(row);
			widthBound = widthBound < tmp ? tmp : widthBound;
		}
		if (rows.length * fm.getHeight() > widthBound) return s;// daca incalca ratia
		
		String result = "";
		boolean resultExcededRatio = false;
		while (!resultExcededRatio) {
			widthBound -= (((widthBound+1) * 5 / 100) > 1 ? (widthBound+1) * 5 / 100 : 1);
			int requiredHeight = widthBound * ratioY / ratioX;
			String[] preResult = new String[rows.length];
			for (int i=0; i<rows.length; i++) {
				preResult[i] = fitInWidth(rows[i], widthBound);
			}
			// vezi cate randuri am in varianta asta
			if ( String.join("\n", preResult).split("\n").length * fm.getHeight() <= requiredHeight ) {
				result = String.join("\n", preResult);
			} 
			// iesi din loop si intoarce rezultatul anterior
			else if (widthBound <= 0) {
				resultExcededRatio = true;
			}
			else {
				resultExcededRatio = true;
			}
		}
		return result;
	}
	
	/**
	 * explode string with \n and grow it while row.length * row.height <= height
	 * @param s
	 * @param height
	 * @return
	 */
	public int getFontSizeToMatchHeight(String s, int height) {
		String[] rows = s.split("\n");
		String fontName = font.getName();
		int fontStyle = Font.PLAIN;
		int fontSize = 1;
		int textHeight = 0;
		while (textHeight <= height) {
			Font _font = new Font(fontName, fontStyle, fontSize);
			fm = jLabel.getFontMetrics(_font);
			double rowHeight = fm.getHeight();
			textHeight = (int) (rowHeight * rows.length);
			fontSize++;
		}
		return Math.max(fontSize-1, 1);
	}
	
	private String formatTextWidthRecursion(String dest, String chunk, String src, int w) {
		String sep = dest.length() > 0 ? "\n" : "";
		//System.out.println("dest -- " + dest + " chunk -- " + chunk + " src -- " + src);
		// sursa e goala -- trolling/ sursa epuizata
		if (chunk.length() == 0 && src.length() == 0)
			return dest;
		// sursa de la sine merge pusa pe un singur rand
		if (chunk.length() == 0 && fm.stringWidth(src) <= w)
			return dest+sep+src;
		// sursa a fost epuizata
		if (src.length() == 0 && chunk.length() > 0)
			return dest+sep+chunk;
		// sursa merge adaugata la dest fara alte masuratori
		if (fm.stringWidth(chunk+src) < w)
			return formatTextWidthRecursion(dest, chunk+src, "", w);
		// sursa merge tocata cu unul din delimitatori
	    for(int i=0; i<delim.length; i++) {
	        String _delim = delim[i];
	        int di = src.indexOf(_delim);
	        if (di > 0) {
	        	String piece = src.substring(0, di+_delim.length());
	            if (fm.stringWidth((chunk+piece).trim()) <= w)
	                return formatTextWidthRecursion(dest, chunk+piece, src.substring(di+_delim.length()), w);
	            if (fm.stringWidth(piece) <= w)
	            	return formatTextWidthRecursion(dest+sep+chunk, piece, src.substring(di+_delim.length()), w);
	        }
	    }
		if (chunk.length() > 0 && fm.stringWidth(src) <= w)
			return dest + sep + chunk + "\n" + src;
	    // sursa nu poate fi tocata cu unul din delimitatori
	    String chars = src.length() > 3 ? src.substring(0,3) : src; // chars nu vor avea niciodata spatiu, ca altfel nu ar ajunge pana aici...
	    String new_src = src.length() > 3 ? src.substring(3) : "";
	    // word e prea mare, iar chunk mai poate duce putin
	    if (fm.stringWidth(chunk+chars) <= w)
	    	return formatTextWidthRecursion(dest, chunk+chars, new_src, w);
	    // word prea mare, iar chunk e plin
	    return formatTextWidthRecursion(dest+sep+chunk, chars, new_src, w);
	}
}
