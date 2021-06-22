package glog.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLGrabber {
	private String url;
	private String destFile;

	public URLGrabber(String p_url) {
		this(p_url, null);
	}

	public URLGrabber(String p_url, String p_destFile) {
		this.url = p_url;
		this.destFile = p_destFile;
	}

	public boolean saveURL() {
		try {
			FileOutputStream outputFile = new FileOutputStream(this.destFile);
			BufferedWriter outputBuffer = new BufferedWriter(new OutputStreamWriter(outputFile));
			URL receita = new URL(this.url);
			BufferedReader in = new BufferedReader(new InputStreamReader(receita.openStream()));
			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				outputBuffer.write(inputLine + System.getProperty("line.separator"));
			}
			outputBuffer.flush();
			outputBuffer.close();
			outputFile.close();
			in.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getContents() {
		try {
			URL url = new URL(this.url);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
			InputStream is = httpcon.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));// "ISO-8859-1"));
			String inputLine = "";
			String s = "";
			while ((inputLine = in.readLine()) != null) {
				s += inputLine + "\n";
			}
			in.close();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean saveURLBinary() {
		try {
			File f = new File(this.destFile);
			if (f.exists()) {
				return false;
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(this.destFile));
			URL url = new URL(this.url);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
			InputStream is = httpcon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			int data;
			while ((data = bis.read()) != -1) {
				bos.write(data);
			}
			bos.flush();
			bos.close();
			bis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		URLGrabber ug = new URLGrabber(
				"https://media.virtualsheetmusic.com/imgprev/qh3aathfilq0dd3rolih5jgcm4/Miscellaneous/Christmas1/32.svg",
				"1.svg");
		ug.saveURLBinary();
	}

}
