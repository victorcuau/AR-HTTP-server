package httpserver.itf.impl;

import java.io.IOException;

import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpSession;


public class HttpRicmletRequestImpl extends HttpRicmletRequest {

	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}

	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getArg(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCookie(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void process(HttpResponse resp) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	

	
}
