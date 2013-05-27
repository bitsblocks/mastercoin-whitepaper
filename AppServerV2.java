/////////////////////////
// compile like javac -cp .;./jetty-distro/lib/* AppServer.java
//
// run w/ java -cp .;./jetty-distro/lib/* AppServer

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FilenameFilter;


public class AppServer
{
  public static void dumpSysInfo()
  {
    System.out.println( "== server sys info ==============================================" );
		
    System.out.println( "os.name:    " + System.getProperty( "os.name" ));
    System.out.println( "os.version: " + System.getProperty( "os.version" ));
    System.out.println( "os.arch:    " + System.getProperty( "os.arch" ));
    System.out.println( "" );

    System.out.println( "java.version: " + System.getProperty( "java.version" ));
    System.out.println( "java.vendor:  " + System.getProperty( "java.vendor" )); 
    System.out.println( "java.home:    " + System.getProperty( "java.home" ));
    System.out.println( "" );
			
    System.out.println( "java.vm.version: " + System.getProperty( "java.vm.version" ));
    System.out.println(	"java.vm.vendor:  " + System.getProperty( "java.vm.vendor" )); 
    System.out.println(	"java.vm.name:    " + System.getProperty( "java.vm.name" ));
    System.out.println(	"java.vm.info:    " + System.getProperty( "java.vm.info", "(n/a)" ));
    System.out.println( "" );


    System.out.println( "user.dir: " + System.getProperty( "user.dir" ));
	        
    String java_class_path[] = System.getProperty("java.class.path" ).split( ";" );        	
    System.out.println( "java.class.path:" );
    for( int i=0; i < java_class_path.length; i++ )
      System.out.println( "  ["+i+"] " + java_class_path[i] );
    
    
    String java_ext_dirs[] = System.getProperty( "java.ext.dirs" ).split( ";" );
    System.out.println( "java.ext.dirs:" );    
    for( int i=0; i < java_ext_dirs.length; i++ )
      System.out.println( "  ["+i+"] " + java_ext_dirs[i] );
    
    String java_library_path[] = System.getProperty( "java.library.path" ).split( ";" );
    System.out.println( "java.library.path:" );
    for( int i=0; i < java_library_path.length; i++ )
      System.out.println( "  ["+i+"] " + java_library_path[i] );
    
    System.out.println( "java.io.tmpdir: "    + System.getProperty( "java.io.tmpdir", "(n/a)" ) );
    System.out.println( "============================================================" );
  }
  
  public static void main(String[] args) throws Exception
  {
    dumpSysInfo();

    
			
    FilenameFilter filter = new FilenameFilter() {
	public boolean accept( File dir, String name ) 
	{
	  if( name.toLowerCase().endsWith( ".war" )) {
		return true;
 	  } else {
	        return false;
	  }
	}
    };

			
    File root = new File( "." );
    File webApps[] = root.listFiles( filter );

    int port = 8181;    
    Server server = new Server( port );
    
    for( File webApp : webApps )
    {
       System.out.println( "configure app: " + webApp.getName() );
		  
       String contextPath;
       if( webApps.length > 1 )
         contextPath = "/" + webApp.getName();
       else
         contextPath = "/";   // only one app; no need to use context path; use root by default


       String warPath = webApp.getCanonicalPath();  // note: might be exploded folder or zip
			  
       System.out.println( "  contextPath="+ contextPath + ", war=" + warPath );			  			  

       WebAppContext webapp = new WebAppContext();
       webapp.setContextPath( contextPath );
       webapp.setWar( warPath );
       server.setHandler( webapp );
    }  
    
    System.out.println( "Starting jetty web server on port " + port + "..." );
 
    server.start();

    System.out.println( "Ready to roll..." );

    server.join();
    
    System.out.println( "Bye" );
  }
}