/////////////////////////
// compile like javac -cp .;./jetty-distro/lib/* SimpleServer.java
//
// run w/  java -cp .;./jetty-distro/lib/* SimpleServer


import org.eclipse.jetty.server.Server;

// -------------------------------------------------
// The simplest possible Jetty server.

public class SimpleServer
{
  public static void main(String[] args) throws Exception
  {
    Server server = new Server(8080);
    server.start();
    server.join();
  }
}