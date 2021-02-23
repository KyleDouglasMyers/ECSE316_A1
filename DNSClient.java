


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class DNSClient {

    //DNSClient values to create a request
    //Byte arrays for request and response
    public byte[] serverInByte = new byte[4];
    public byte[] responseInBytes = new byte[1024];

    //Default Nums
    public int portNum = 53;
    public int tOut = 5000; 
    public int retryMax = 3;
    
    public int attemptsMade = 0;

    //Server Info
    public String domain;
    public String server;

    public int time;
   
    public Type qType = Type.A;

    //Entry Point
    public static void main(String[] args) throws Exception {

        if(args.length<2){
            System.out.println("Need an ip and a domain name\n");
            return;
        }

        //Create DNS Client to make request
        DNSClient dnsClient = new DNSClient(args);

        //Attempt to make request with DNS Client configured
        dnsClient.attemptRequest();

    }

    public DNSClient(String[] inputs) {

        //Parsing info into the DNSClient Instance
        String input;
        int j = 0;
        while (j < inputs.length) {
            input = inputs[j];
            switch (input) {
                case "-t":
                    j++;
                    try {
                        tOut = Integer.parseInt(input);
                    } catch (NumberFormatException e) {

                        System.out.println("The timeout option was not an integer:\n" + e.getMessage());
                    }
                    j++;
                    break;
                case "-r":
                    j++;
                    try {
                        retryMax = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("The retry option was not an integer:\n" + e.getMessage());
                    }
                    j++;
                    break;
                case "-p":
                    j++;
                    try {
                        portNum = Integer.parseInt(input);
                    } catch (NumberFormatException e) {

                        System.out.println("The port option was not an integer:\n" + e.getMessage());
                    }
                    j++;
                    break;
                case "-mx":
                    j++;
                    //qType = "SET A PROPER QTYPE TO MX";
                    qType = Type.MX;
                    j++;
                    break;
                case "-ns":

                    //qType = "SET A PROPER QTYPE TO NX";
                	qType = Type.NS;
                    j++;
                    break;
                default:
                
                	if (input.startsWith("@")) {
                        server = input.substring(1);
                        String[] ipArray = server.split("\\.");
                        int t = 0;
                        for (String token : ipArray) {
                            try {
                                serverInByte[t] = (byte) Integer.parseInt(token);
                            } catch (NumberFormatException e) {

                                System.out.println("The ip value was not an integer:\n" + e.getMessage());
                            }
                            t++;

                        }
                       
                        
                    }
                    domain = inputs[j];
                    j++;
              
            }
        }

        System.out.println("DnsClient sending request for " + domain);
		System.out.println("Server: " + server);
		System.out.println("Request Type: " + qType);

    }

    public void attemptRequest() {
        if(attemptsMade>retryMax){
            System.out.println("Too many retries, exiting\n");
            return;
        }
        else{
            attemptsMade++;
        }

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(this.tOut);
            
            DNSRequest request = new DNSRequest(domain, qType);
            byte[] reqBytes = request.getBytes();
            
            InetAddress address = InetAddress.getByAddress(this.serverInByte);

            DatagramPacket request_packet = new DatagramPacket(reqBytes, reqBytes.length, address, portNum);
            DatagramPacket response_packet = new DatagramPacket(this.responseInBytes, responseInBytes.length);

            double startTime = System.currentTimeMillis();

            clientSocket.send(request_packet);
            clientSocket.receive(response_packet);

            double endTime = System.currentTimeMillis();

            clientSocket.close();

            time = (int) (endTime - startTime) / 1000;

            DNSResponse response = new DNSResponse(reqBytes, responseInBytes);
            response.printResponse(this);

        } catch (SocketTimeoutException e) {
            System.out.println("Socket has timed out");
            attemptRequest();
       
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        attemptsMade = 0;
    }
    
    public int getAttemptsMade() {
    	return this.attemptsMade;
    }
    
    public int getTime() {
    	return this.time;
    }

}
