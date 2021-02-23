
import java.nio.ByteBuffer;
import java.util.Arrays;



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
        
        int l = 0; 
        l += rd.length;
        l+=10;
        
        this.len = rd.length;
        System.out.println(this.name);
	}
	
	public RData getRData(int i) {
		RData result = new RData();
		String domain = "";
		
		//System.out.println("i: " + i + " size:" + response.length);
		
		int size = this.response[i] & 0xff;
		
		System.out.println("s"+size);
		
		boolean dot = false;
		int c = 0;
		
		while (size != 0) {
			System.out.println("in loop");
			if (dot) {
				domain += ".";
			}
			
			if ((size & 0xc0) == (int)0xc0) {
				
				byte[] offset = { (byte) (response[i] & 0x3F), this.response[i + 1] };
	
				ByteBuffer wrapped = ByteBuffer.wrap(offset);
				
				domain += getRData(wrapped.getShort()).domain;
				i += 2;
				c += 2;
				size = 0;
			
			
			} else {
				
				if(response[i] > 0) {
					for (int a = 0; a < response[i]; a++) {
						if(response[i] > 0) {
							//System.out.println(a);
							domain += (char) (response[i + a +1]);
							//System.out.println((response[i + a +1]));
						}else {
							break;
						}
						
					}
				}
				i += size + 1;
			    c += size + 1;
			    size = response[i];
			}
			dot = true;
		}
		result.domain = domain;
		this.name = domain;
		result.length = c;
		return result;
	}
	
	
	
}


