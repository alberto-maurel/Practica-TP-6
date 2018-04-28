package es.ucm.fdi.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JTextArea;

public class TextStream extends OutputStream {
	
	private JTextArea areaTexto;
	
	public TextStream(JTextArea ta) {
		areaTexto = ta;
	}

	@Override
	public void write(int arg0) throws IOException {
		
	}
	
	public void write(byte[] b) {		
		try {
			String str = new String(b, "UTF-8");
			areaTexto.append(str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}

}
