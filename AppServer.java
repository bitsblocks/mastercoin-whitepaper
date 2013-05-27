/////////////////////////
// compile like javac -cp .;./jetty-distro/lib/* AppServer.java
//
// run w/ java -cp .;./jetty-distro/lib/* AppServer



import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class AppServer
{
  public static void main(String[] args) throws Exception
  {
    Server server = new Server(8080);
 
    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath("/");
    webapp.setWar( "./links.war" );
    server.setHandler(webapp);
 
    server.start();
    server.join();
  }
}