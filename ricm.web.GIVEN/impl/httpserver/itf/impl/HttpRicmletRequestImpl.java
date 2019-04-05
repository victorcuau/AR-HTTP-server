package httpserver.itf.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;

import httpserver.itf.HttpResponse;
import httpserver.itf.HttpRicmlet;
import httpserver.itf.HttpRicmletRequest;
import httpserver.itf.HttpRicmletResponse;
import httpserver.itf.HttpSession;

public class HttpRicmletRequestImpl extends HttpRicmletRequest {

	Hashtable<String, String> arguments = new Hashtable<String, String>();
	Hashtable<String, String> cookies = new Hashtable<String, String>();

	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}

	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getArg(String name) {
		return arguments.get(name);
		/*
		 * if (m_ressname.contains("?" + name + "=")){ return
		 * m_ressname.substring(m_ressname.indexOf("?" + name + "=") + name.length() +
		 * 2, m_ressname.indexOf("&")); } else if ( m_ressname.contains("&" + name +
		 * "=") ) { System.out.println(m_ressname.substring(m_ressname.indexOf("&" +
		 * name + "=") + name.length() + 2)); int test = m_ressname.indexOf("&" + name +
		 * "=") + name.length() + 2; return m_ressname.substring(m_ressname.indexOf("&"
		 * + name + "=") + name.length() + 2,
		 * m_ressname.substring(m_ressname.indexOf("&" + name + "=") + name.length() +
		 * 2).indexOf("&") + test + 2); } return null;
		 */
	}

	public String getCookie(String name) {
		return cookies.get(name);
	}

	public void process(HttpResponse resp) throws Exception {
		if (m_method.compareTo("GET") == 0) {
			String className;
			if (m_ressname.indexOf("?") < 0) {
				className = (m_ressname.substring(10)).replace("/", "."); // 10 = length("/ricmlets/")
			} else {
				className = (m_ressname.substring(10, m_ressname.indexOf("?"))).replace("/", "."); // 10 = length("/ricmlets/")
				String args[] = (m_ressname.substring(m_ressname.indexOf("?") + 1)).split("&");
				for (int i = 0; i < args.length; i++) {
					String arg[] = args[i].split("=");
					arguments.put(arg[0], arg[1]);
				}
			}

			try {
				HttpRicmlet instance = m_hs.getInstance(className);
				instance.doGet(this, (HttpRicmletResponse) resp);

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				resp.setReplyError(404, "Ricmlet not found");
			}

		}

	}

}
