package httpserver.itf.impl;

import java.io.BufferedReader;
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
	public BufferedReader reader;

	public HttpRicmletRequestImpl(HttpServer hs, String method, String ressname) throws IOException {
		super(hs, method, ressname);
	}

	public HttpSession getSession() {
		Session session;
		String sessionID = cookies.get("ID");
		if (sessionID == null) {
			int cpt = 1;
			while(this.m_hs.sessions.containsKey(new Integer(cpt).toString())) {
				cpt++;
			}
			session = new Session(new Integer(cpt).toString());
			this.m_hs.sessions.put(session.getId(), session);
		}
		else {
			session = this.m_hs.sessions.get(sessionID);
			if (session == null) {
				session = new Session(getCookie("ID"));
				this.m_hs.sessions.put(session.getId(), session);
			}
		}
		return session;
	}

	public String getArg(String name) {
		return arguments.get(name);
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
			
			// Récupération des cookies
			String currentLine;
	    while(!(currentLine = reader.readLine()).isEmpty()) {
	      if(currentLine.startsWith("Cookie: ")) {
	        String cookieLine = currentLine.substring(8);
	        String cookie[] = cookieLine.split("=");
	        cookies.put(cookie[0], cookie[1]);
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
