import Type;
public class Answer {
	
	public String name;
	public Type type;
	public int CLASS;
	public int TTL;
	public int RDLENGTH;
	//unsure of the data type for data, since it depends on the type
	public String RDATA;
	
	//not sure what else to put in constructor, but class should be 1 by default
	public Answer() {
		this.CLASS = 1;
	}
	
	
	
	
}

