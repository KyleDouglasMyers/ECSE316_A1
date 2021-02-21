package com.A1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DNSClient {

    //DNSClient values to create a request
    //Byte arrays for request and response
    public byte[] serverInByte = new byte[4];
    public byte[] responseInBytes = new byte[1024];

    //Default Nums
    public int portNum = 53;
    public int tOut = 500; //5 seconds in millis
    public int retryMax = 3;
    public int attemptsMade = 1;

    //Server Info
    public String domain;
    public String server;


    //FIX THIS
    //public QType qType = QType.A;
    //this maybe?
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
                        j++;
                        domain = inputs[j];

                    }
                    else{
                        System.out.println("Incorrect formatting");
                    }
            }
        }
       /* System.out.println("Attempting to make request with:\n");
        System.out.println("DNS request with domain: " + domain +"\n");
        System.out.println("DNS request with server: " + server +"\n");
        System.out.println("DNS request with port: " + portNum +"\n");
        System.out.println("DNS request with maximum retries: " + retryMax +"\n");
        System.out.println("DNS request with timeout: " + tOut +"\n");*/

        //i think we need it to be this according to the instructions:
        System.out.println("DnsClient sending request for " + domain);
		System.out.println("Server: " + server);
		System.out.println("Request Type: " + qType);

    }

    public void attemptRequest() {

        if(this.attemptsMade>retryMax){
            System.out.println("Too many retries, exiting\n");
            return;
        }
        else{
            this.attemptsMade++;
        }

        try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(this.tOut);
            InetAddress i_add = InetAddress.getByAddress(this.serverInByte);
            //DNSRequest request = new DNSRequest(domainName, qType);
            //i think you meant domain instead of domainName? not sure
            DNSRequest request = new DNSRequest(domain, qType);
            byte[] request_bytes = request.getBytes();

            DatagramPacket request_packet = new DatagramPacket(request_bytes, request_bytes.length, i_add, portNum);
            DatagramPacket response_packet = new DatagramPacket(this.responseInBytes, responseInBytes.length);

            long start = System.currentTimeMillis();

            socket.send(request_packet);
            socket.receive(response_packet);

            long end = System.currentTimeMillis();

            socket.close();

            System.out.println("Response received after " + (end - start) / 1000. + " seconds " + "("
                    + (numOfRetries - 1) + " retries)");

            DNSResponse response = new DNSResponse(request_bytes, response_bytes);
            response.printResponse();

        } catch (UnknownHostException e) {
            System.out.println("Error:Host is not known");
        } catch (SocketException e) {
            System.out.println("Error:Can't create socket");
        } catch (SocketTimeoutException e) {
            System.out.println("Error:Socket has timed out. Trying again ...");
            pollRequest(numOfRetries + 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
