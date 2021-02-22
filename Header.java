

import java.nio.ByteBuffer;
import java.util.Random;

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
	public byte[] QDCOUNT;
	public byte[] ANCOUNT;
	public byte[] NSCOUNT;
	public byte[] ARCOUNT;

	public Header() {
		this.OPCODE = 0;
		this.Z = 0;
		this.ID = new byte[2];
		this.QDCOUNT = new byte[2];
		this.ANCOUNT = new byte[2];
		this.NSCOUNT = new byte[2];
		this.ARCOUNT = new byte[2];
		Random r = new Random();
		r.nextBytes(this.ID);
	}

	public Header(byte[] response) {
		// TODO: constructor for creating a header from the response

		byte[] id = { response[0], response[1] };
		this.ID = id;

		this.QR = ((response[2] >> 7) & 1) == 1;

		this.AA = ((response[2] >> 2) & 1) == 1;

		this.TC = ((response[2] >> 1) & 1) == 1;

		this.RD = ((response[2] >> 0) & 1) == 1;

		this.RA = ((response[3] >> 7) & 1) == 1;

		this.RCODE = response[3] & 0x0F;

		byte[] temp = { response[4], response[5] };

		this.QDCOUNT = temp;

		byte[] temp2 = { response[6], response[7] };

		this.ANCOUNT = temp2;

		byte[] temp3 = { response[8], response[9] };

		this.NSCOUNT = temp3;

		byte[] temp4 = { response[10], response[11] };

		this.ARCOUNT = temp4;
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

		// ANCOUNT, NSCOUNT, ARCOUNT all 0
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
		this.QR = false;
		this.OPCODE = 0;
		this.AA = false;
		this.TC = false;
		this.RD = true;

		this.RA = false;
		this.Z = 0;
		this.RCODE = 0;

		byte[] temp = { (byte) 0x00, (byte) 0x01 };

		this.QDCOUNT = temp;

		byte[] zero = { (byte) 0x00, (byte) 0x00 };

		this.ANCOUNT = zero;
		this.NSCOUNT = zero;
		this.ARCOUNT = zero;
	}

	// not sure if we need these

	public boolean isResponse() {
		return this.QR;
	}

	public boolean supportsRecursion() {
		return this.RA;
	}

	public void isAuthority() {
		this.AA = true;
	}

	public void isNotAuthority() {
		this.AA = false;
	}

	public void wasTruncated() {
		this.TC = true;
	}

	public void notTruncated() {
		this.TC = false;
	}
}
