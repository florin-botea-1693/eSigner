package text;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.JLabel;

public class FitTextInRectangle {
	
	private JLabel jLabel;
	public FontMetrics fm;
	int maxI = 0;
	
	//private FontMetrics fontMetrics;

	private String[] delimiters = new String[] {" ", "-", ",", ".", "@"};
	
	public FitTextInRectangle() {
		this.jLabel = new JLabel();
	}
	
	public ArrayList<Object> fit(String text, int widthLimit, int heightLimit, Font font) {
		String[] rows = text.split("\n");
		
		String fontName = font.getName();
		int fontStyle = Font.PLAIN;
		int fontSize = 1;

		int textHeight = 0;
		String resultText = "";
		String tmpResultText = "";
		while (textHeight <= heightLimit) {
			resultText = tmpResultText;
			
			//System.out.println("----------------font size " + (fontSize+1) + " " + fontName + "-----------------");
			//System.out.println("----------------last height was " + textHeight + " and limit is " + heightLimit);
			Font _font = new Font(fontName, fontStyle, fontSize); // in avans
			fm = jLabel.getFontMetrics(_font);
			String[] result = new String[rows.length];
			for (int i=0; i<rows.length; i++) {
				result[i] = this.formatTextWidth(rows[i], widthLimit, delimiters);
				for (String s : result[i].split("\n")) {
					//System.out.println("row width____" + fm.stringWidth(s) + " and limit is " + widthLimit);
				}
			}
			tmpResultText = String.join("\n", result);
			int countRows = tmpResultText.split("\n").length +1;
			double rowHeight = fm.getHeight();
			textHeight = (int) (rowHeight * countRows);
			//System.out.println("----------------height of " + textHeight + "reached and limit is " + heightLimit);
			fontSize++;
		}
		ArrayList<Object> response = new ArrayList<Object>();
		response.add(fontSize-2);
		response.add(resultText);
		return response;
	}
	
	public String formatTextInRatio(String text, Font font, int ratioX, int ratioY) {
		if (text.length() == 0) return "";
		String[] rows = text.split("\n");
		fm = jLabel.getFontMetrics(font);
		int biggestRowLength = 1;
		//System.out.println("-------------" + rows.length);
		for (String row : rows) {
			int tmp = fm.stringWidth(row);
			//System.out.println(tmp);
			biggestRowLength = biggestRowLength < tmp ? tmp : biggestRowLength;
			//System.out.println("-------------" + biggestRowLength);
		}
		if (rows.length * fm.getHeight() > biggestRowLength) return text;// daca incalca ratia
		
		String result = "";
		boolean resultExcededRatio = false;
		while (!resultExcededRatio) {
			biggestRowLength -= (((biggestRowLength+1) * 5 / 100) > 1 ? (biggestRowLength+1) * 5 / 100 : 1);
			int requiredHeight = biggestRowLength * ratioY / ratioX;
			String[] preResult = new String[rows.length];
			for (int i=0; i<rows.length; i++) {
				preResult[i] = this.formatTextWidth(rows[i], biggestRowLength, delimiters);
			}
			if ( String.join("\n", preResult).split("\n").length * fm.getHeight() <= requiredHeight ) {
				//System.out.println("I should continue");
				result = String.join("\n", preResult);
			} 
			else if (biggestRowLength <= 0) {
				//System.out.println("I should stop");
				resultExcededRatio = true;
			}
			else {
				//System.out.println("I should stop");
				resultExcededRatio = true;
			}
			//System.out.println("-----------------------------while----------------------------" + biggestRowLength + "\n" + result);
		}
		return result;
	}
	
	public String formatTextWidth(String src, int w, String[] delim) {
		return formatTextWidthRecursion("", "", src, w, delim);
	}
	
	private String formatTextWidthRecursion(String dest, String chunk, String src, int w, String[] delim) {
		 //System.out.println("src is "+ src + " and " + fm.stringWidth(src));
		maxI++;
		if (maxI > 100) {
			//return null;
		}
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
			return formatTextWidthRecursion(dest, chunk+src, "", w, delim);
		// sursa merge tocata cu unul din delimitatori
	    for(int i=0; i<delim.length; i++) {
	        String _delim = delim[i];
	        int di = src.indexOf(_delim);
	        if (di > 0) {
	        	String piece = src.substring(0, di+_delim.length());
	            if (fm.stringWidth((chunk+piece).trim()) <= w)
	                return formatTextWidthRecursion(dest, chunk+piece, src.substring(di+_delim.length()), w, delim);
	            if (fm.stringWidth(piece) <= w)
	            	return formatTextWidthRecursion(dest+sep+chunk, piece, src.substring(di+_delim.length()), w, delim);
	        }
	    }
		if (chunk.length() > 0 && fm.stringWidth(src) <= w)
			return dest + sep + chunk + "\n" + src;
	    // sursa nu poate fi tocata cu unul din delimitatori
	    String chars = src.length() > 3 ? src.substring(0,3) : src; // chars nu vor avea niciodata spatiu, ca altfel nu ar ajunge pana aici...
	    String new_src = src.length() > 3 ? src.substring(3) : "";
	    // word e prea mare, iar chunk mai poate duce putin
	    if (fm.stringWidth(chunk+chars) <= w)
	    	return formatTextWidthRecursion(dest, chunk+chars, new_src, w, delim);
	    // word prea mare, iar chunk e plin
	    return formatTextWidthRecursion(dest+sep+chunk, chars, new_src, w, delim);
	}
	
	public static void main(String[] args) {
		FitTextInRectangle app = new FitTextInRectangle();
		String f = app.formatTextInRatio("Botea Florin cel super tare si destept si frumos si cel mai cel", new Font("arial", Font.BOLD, 8), 400, 400);
		//System.out.println(f);
		//String str = "Digitally Signed by Florin Botea\n"+"Reason: semnez acest document ca asa vreau eu\n"+"Location: la mine acasa";
		//ArrayList<Object> result = app.fit(str, 70, 100, new Font("arial", Font.PLAIN, 1));
	}

	public int sizeInHeight(String text, Font font, int heightLimit) {
		String[] rows = text.split("\n");
		
		String fontName = font.getName();
		int fontStyle = Font.PLAIN;
		int fontSize = 1;

		int textHeight = 0;
		String resultText = "";
		String tmpResultText = "";
		while (textHeight <= heightLimit) {
			resultText = tmpResultText;
			Font _font = new Font(fontName, fontStyle, fontSize);
			fm = jLabel.getFontMetrics(_font);
			double rowHeight = fm.getHeight();
			textHeight = (int) (rowHeight * rows.length);
			fontSize++;
		}
		return fontSize-1;
	}
}
