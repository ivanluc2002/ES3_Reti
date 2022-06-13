package org.example;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
class Client {
    String serverIp;
    int port;
    static Socket clientSocket;
    static DataOutputStream outToServer;
    static BufferedReader inFromServer;

    public static Socket getClientSocket() {
        return clientSocket;
    }
    public void close() throws Exception{
        clientSocket.close();
    }

    public Client(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void connessione() throws IOException {
        clientSocket = new Socket(serverIp, port);
    }
    public void lettura() throws IOException {
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    public static String lettura1() throws Exception{
        String str=inFromServer.readLine();
        return str;
    }

    public void invio() throws IOException {
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
    }
    public static void invio1(String msg) throws Exception{
        outToServer.writeBytes(msg + '\n');
    }


    public static void main(String argv[]) throws Exception {
        int i=0;
        ArrayList<Long> delays=new ArrayList<>();
        String messaggio = "";
        String mesaggio1;
        Client client=new Client("localhost", 4321);
        client.connessione();
        client.lettura();
        client.invio();
        mesaggio1= client.lettura1();
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(mesaggio1);
        do {
            i++;
            if(i!=10) {
                mesaggio1 = client.lettura1();
                Date date = new Date();
                long ritardo=date.getTime() - Long.parseLong(mesaggio1);
                System.out.println(ritardo);
                System.out.println(mesaggio1);
                if(ritardo>300){
                    client.invio1("CL: HIGH DELAY "+ ritardo);
                }
                else {
                    client.invio1("CL: LOW DELAY "+ ritardo);
                }
                delays.add(ritardo);
            }
            else{
                long maximumDelay= Collections.max(delays);
                client.invio1("CL max ritardo:" + maximumDelay);
                client.invio1("esci");
                messaggio="q";
            }
        }while(!messaggio.equals("esci"));

        client.close();
    }
}