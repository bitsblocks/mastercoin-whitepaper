import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


class HelloHandler extends AbstractHandler
{
  public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)   throws IOException, ServletException
  {
    response.setContentType("text/html;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    baseRequest.setHandled(true);
    response.getWriter().println("<h1>Hello World</h1>");
  }
}


public class HelloServer
{
  public static void main(String[] args) throws Exception
  {
    Server server = new Server(8080);
    server.setHandler( new HelloHandler() );

    server.start();
    server.join();
  }
}