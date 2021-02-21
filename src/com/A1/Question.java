//import Type;
package src.com.A1;

import java.nio.ByteBuffer;
import com.A1.Type;

public class Question {
	
	public String QNAME;
	//not sure what type to use
	public Type QTYPE;
	public int QCLASS = 1;
	
	
	public Question(String qname, Type qtype) {
		this.QNAME = qname;
		this.QTYPE = qtype;
	}
	
	public byte[] packageQuestion() {
		
		ByteBuffer bb = ByteBuffer.allocate(findQNameLen() + 5);
		bb.put(packageQName());
		//empty byte to signal end of qname
		bb.put((byte) 0x00);
		
		bb.put(packageQType());
		
		//QCLASS is always 1
		bb.put((byte) 0x00);
		bb.put((byte) 0x01);
		return bb.array();
	}
	
	
	public int findQNameLen() {
		int len = 0;
		String labels[] = this.QNAME.split("\\.");
		len += labels.length;
		for(int i = 0; i<labels.length; i++) {
			len += labels[i].length();
		}
		return len;
	}
	
	public byte[] packageQName() {
		ByteBuffer toReturn = ByteBuffer.allocate(findQNameLen());
		String labels[] = this.QNAME.split("\\.");
		for(int i = 0; i< labels.length; i++) {
			toReturn.put((byte) labels[i].length());
			for(int j = 0; j < labels[i].length(); j++) {
				toReturn.put((byte) (int) labels[i].charAt(j));
			}
		}
		return toReturn.array();
	}
	
	public byte[] packageQType() {
		
		ByteBuffer toReturn = ByteBuffer.allocate(2);
		toReturn.put((byte) 0x00);
		if(this.QTYPE.equals(Type.A)) {
			toReturn.put((byte) 0x01);		
		} if(this.QTYPE.equals(Type.NS)) {
			toReturn.put((byte) 0x02);
		} if(this.QTYPE.equals(Type.MX)) {
			toReturn.put((byte) 0x0f);
		}
		
		return toReturn.array();
		
	}
	
	
}
