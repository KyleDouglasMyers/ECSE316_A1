package com.A1;
import java.nio.ByteBuffer;

import com.A1.Type;


public class ResourceRecord {
	
	public String name;
	public Type type;
	public int CLASS;
	public int TTL;
	public int RDLENGTH;
	//unsure of the data type for data, since it depends on the type
	public RData rd;
	
	public boolean AA;
	
	public int len;
	public byte[] response; 
	//only if type MX
	public int pref;
	
	//not sure what else to put in constructor, but class should be 1 by default
	public ResourceRecord(Type qType, boolean aa, int length, byte[] response) {
		this.type = qType;
		this.AA = aa;
		this.CLASS = 1;
		this.response = response;
		
		this.rd = getRData(length);
		length += rd.length;
		length += 4;
		
		byte[] arr = new byte[4];
        
		for(int i = 0; i<4; i++) {
			arr[i] = response[length + i];
		}
		ByteBuffer bb = ByteBuffer.wrap(arr);
        this.TTL = bb.getInt();
		
	}
	
	public RData getRData(int i) {
		RData result = new RData();
		String domain = "";
		int size = this.response[i];
		boolean go = true;
		int c = 0;
		while (size != 0) {
			if (!go) {
				domain += ".";
			}
			if ((size & 0xC0) == (int)0xC0) {
				byte[] offset = { (byte) (response[i] & 0x3F), response[i + 1] };
				ByteBuffer wrapped = ByteBuffer.wrap(offset);
				domain += getRData(wrapped.getShort()).domain;
				i += 2;
				c += 2;
				size = 0;
			} else {
				 domain = "";
			     for (int a = 1; a <= response[i]; a++) {
			    	domain += (char) response[i + a];
			     }
			     i-=1;
			     i += size + 1;
			     c += size + 1;
			     size = response[i];
			}
			go = false;
		}
		result.domain = domain;
		this.name = domain;
		result.length = c;
		return result;
	}
	
	
	
}


