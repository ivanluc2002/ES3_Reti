package org.example;
import java.io.*;
import java.net.*;
import java.util.Date;

import static java.lang.Integer.parseInt;

class Server {
    private static BufferedReader inFromClient;
    private static PrintWriter outToClient;
    ServerSocket socket;
    Socket aSocket;
    int port;

    Server(int port){
        this.port=port;
    }

    public void CreaSocket() throws Exception{
        try {
            socket = new ServerSocket(port);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Errore nella creazione del socket");
        }
    }
    public void Connessione() throws Exception{
        try{
            if(socket != null)
                aSocket = socket.accept();
            else{
                System.out.println("Errore nella creazione del sock");
            }
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Errore nella connessione");
        }
    }
    public void Lettura() throws IOException {
        try{
            inFromClient = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Lettura non riuscita");
        }
    }
    public static String Lettura1() throws Exception{
        return inFromClient.readLine();
    }
    public void Invio() throws IOException {
        try{
            outToClient = new PrintWriter(aSocket.getOutputStream(), true);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Invio non riuscito");
        }
    }

    public static void Invio1(String msg) throws IOException{
        outToClient.println(msg);
    }
    public void Reset() throws Exception{
        inFromClient=null;
        socket.close();
        socket=null;
        aSocket.close();
        aSocket=null;
    }

    public static BufferedReader getInFromClient() {
        return inFromClient;
    }
    public ServerSocket getSocket() {
        return socket;
    }
    public Socket getaSocket() {
        return aSocket;
    }


    public static void main(String[] argv) throws Exception {
        String stringa;
        Server server=null;
        int i=0;
        while (true) {
            try {
                server = new Server(4321);
                server.CreaSocket();
                server.Connessione();
                String clientId= server.getaSocket().getRemoteSocketAddress().toString();
                System.out.println("Client connesso");
                server.Lettura();
                server.Invio();
                Invio1("");
                do{
                    i++;
                    Date date=new Date();
                    if(i!=10) {
                        if (i != 5)
                            Invio1(String.valueOf(date.getTime()));
                        else
                            Invio1(String.valueOf(date.getTime() - 350));

                        stringa = server.Lettura1();
                        System.out.println(stringa);
                    }
                    else{
                        stringa=server.Lettura1();
                        System.out.println(stringa);
                    }

                    Thread.sleep(1000);
                } while (!stringa.equals("close"));
                server.Reset();}
            catch (IOException e){
                server.Reset();
                System.out.println("Disconnesso per un errore");
            }
        }
    }
}