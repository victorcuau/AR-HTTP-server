package httpserver.itf.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import httpserver.itf.HttpRequest;
import httpserver.itf.HttpResponse;

public class HttpStaticRequest extends HttpRequest {
	static final int BUF_SZ = 1024;
	static final String DEFAULT_FILE = "index.html";
	
	static String folder = "../../";
	
	public HttpStaticRequest(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}
	
	public void process(HttpResponse resp) throws Exception {
		if (m_method.compareTo("GET") == 0) {
			File fileRequest = new File(m_hs.getFolder() + m_ressname);
			if(fileRequest.isFile() && !fileRequest.isDirectory()) {
				// Header
				resp.setReplyOk();
				resp.setContentLength((int)(fileRequest.length()));
				resp.setContentType(this.getContentType(m_ressname));
				
				// Body
				PrintStream ps = resp.beginBody();
				switch(this.getContentType(m_ressname)) {
					case "application/octet-stream":
					case "application/pdf":
					case "image/jpeg":
					case "image/gif":
					case "text/plain":
					case "text/html":
						byte[] bOut = new byte[1]; // On lit bit par bit (version non chunk)
						FileInputStream fis = new FileInputStream(fileRequest);
			      System.out.println(fileRequest.length() + " bytes to upload...");
			      while ((fis.read(bOut)) >= 0) {
			      	ps.write(bOut);
			      }
			      ps.flush();
						fis.close();
						System.out.println("\t Done!");
						break;
						
					/*
					case "text/plain":
					case "text/html":
						char[] bOut_char = new char[1]; // On lit bit par bit (version non chunk)
						FileInputStream fis_char = new FileInputStream(fileRequest);
			      System.out.println(fileRequest.length() + " bytes to upload...");
			      while ((fis_char.read(bOut_char)) >= 0) {
			      	ps.write(bOut_char);
			      }
			      ps.flush();
			      fis_char.close();
						System.out.println("\t Done!");
						break;
					*/

					default:
						new UnknownRequest(m_hs, m_method, m_ressname).process(resp);;
						//resp.setReplyError(404, "File type is not supported");
						break;
				}
				
				
			}
			else {
				resp.setReplyError(404, "File not found");
			}
		}
	}

}
