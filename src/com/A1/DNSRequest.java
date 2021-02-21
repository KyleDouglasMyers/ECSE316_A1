package com.A1;

import java.nio.ByteBuffer;

public class DNSRequest {

	public String domain;
	public Type QType;
	public byte[] bytes;
	
	
	public DNSRequest(String domain, Type t) {
		this.domain = domain;
		this.QType = t;
		
		ByteBuffer buffer = ByteBuffer.allocate(17 + findQNameLen());
		buffer.put(new Header().packageHeader());
		buffer.put(new Question(this.domain, this.QType).packageQuestion());
		this.bytes = buffer.array();
	}

	public int findQNameLen() {
		int len = 0;
		String labels[] = this.domain.split("\\.");
		len += labels.length;
		for(int i = 0; i<labels.length; i++) {
			len += labels[i].length();
		}
		return len;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	
}
