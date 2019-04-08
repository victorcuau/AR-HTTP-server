package httpserver.itf.impl;

import java.util.Hashtable;

import httpserver.itf.HttpSession;

public class Session implements HttpSession {
	
	String id;
  public Hashtable<String,Object> values;
	
	Session(String id){
    this.id = id;
    values = new Hashtable<String,Object>();
  }

	public String getId() {
		return id;
	}

	public Object getValue(String key) {
		return values.get(key);
	}

	public void setValue(String key, Object value) {
		values.put(key, value);
	}

}
