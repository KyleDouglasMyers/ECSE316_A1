package com.A1;

public class DNSResponse {

	public Header responseHeader;
	public Question responseQuestion;
	public ResourceRecord[] answerRecs;
	public ResourceRecord[] additionalRecs;
	
	public DNSResponse(byte[] request, byte[] response) throws Exception {
		
		this.responseHeader = new Header(response);
		
		//checking certain bits in the response header to see if it was a success
		if(!this.responseHeader.isResponse()) {
			throw new Exception("This response is a query, not a response");
		}
		
		if(!this.responseHeader.supportsRecursion()) {
			throw new Exception("The server does not support recursive queries");
		}if(this.responseHeader.RCODE == 1) {
			throw new Exception("Format Error: the server was unable to interpret the query");
		} if(this.responseHeader.RCODE == 2) {
			throw new Exception("Server failure: the server was unable to process the query due to a problem in the server");
		}if(this.responseHeader.RCODE == 3) {
			throw new Exception("Name Error: the domain name in the query does not exist");
		}if(this.responseHeader.RCODE == 4) {
			throw new Exception("Not implemented: the server does not support this kind of query");
		}if(this.responseHeader.RCODE == 5) {
			throw new Exception("Refused: the server refuses to perform this operation due to policy");
		}
		
		//checks if the types are the same
		if(Question.unpackageQType(request) != Question.unpackageQType(response)) {
			throw new Exception("The types of the request and response don't match");
		}
		
		//TODO
		//create records
		this.answerRecs = new ResourceRecord[this.responseHeader.ANCOUNT[1] << 8 + this.responseHeader.ANCOUNT[0]];
		for(int i = 0; i<answerRecs.length; i++) {
			answerRecs[i] = new ResourceRecord(Question.unpackageQType(response), this.responseHeader.AA);
		}
	}
	
	public void printResponse() {
		//not sure where to get these variables from
		int time, numretries;
		System.out.println("Response recieved after " + time + " seconds (" + numretries + " retries)");
		System.out.println("*** Answer section (" + this.answerRecs.length + " records)***");
		for(int i = 0; i< this.answerRecs.length; i++) {
			//print stuff
		}
	}

}
