import java.nio.ByteBuffer;


public class Header {

	public byte[] ID;
	public boolean QR;
	public int OPCODE;
	public boolean AA;
	public boolean TC;
	public boolean RD;
	public boolean RA;
	public int Z;
	public int RCODE;
	public int QDCOUNT;
	public int ANCOUNT;
	public int NSCOUNT;
	public int ARCOUNT;
	
	public Header() {
		this.OPCODE = 0;
		this.Z = 0;
		this.ID = new byte[2];
		Random r = new Random();
		r.nextBytes(this.ID);
	}
	
	/*
	 * use this method to get the byte array for a query request
	 */
	public byte[] packageHeader() {
		
		this.setReqDefaults();
		
		ByteBuffer bb = ByteBuffer.allocate(12);
		bb.put(this.ID);
		bb.put((byte) 0x01);
		bb.put((byte) 0x00);
		bb.put((byte) 0x00);
		bb.put((byte) 0x01);
		
		//ANCOUNT, NSCOUNT, ARCOUNT all 0
		bb.put((byte) 0x00);
		bb.put((byte) 0x00);
		bb.put((byte) 0x00);
		bb.put((byte) 0x00);
		bb.put((byte) 0x00);
		bb.put((byte) 0x00);
		return bb.array();
	}
	
	/*
	 * sets default attributes for requests
	 */
	public void setReqDefaults() {
		this.QR = 0;
		this.OPCODE = 0;
		this.AA = 0;
		this.TC = 0;
		this.RD = 1;
		
		this.RA = 0;
		this.Z = 0;
		this.RCODE = 0;
		
		this.QDCOUNT = 1;
		this.ANCOUNT = 0;
		this.NSCOUNT = 0;
		this.ARCOUNT = 0;
	}
	
	
	//not sure if we need these 
	
	public void isAuthority() {
		this.AA = 1;
	}
	
	public void isNotAuthority() {
		this.AA = 0;
	}
	
	public void wasTruncated() {
		this.TC = 1;
	}
	
	public void notTruncated() {
		this.TC = 0;
	}
}

