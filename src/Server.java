import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Map;

class Server extends  NanoHTTPD {

  public Server(String hostname, int port) throws IOException {
    super(hostname, port);
    start(NanoHTTPD.SOCKET_READ_TIMEOUT,false);
    System.out.println("\nRunning! Point your browsers to http://localhost:" + port +"/ \n");
  }

  @Override
  public Response serve(IHTTPSession session){
    String msg = "<html><body><h1>Hello server</h1>\n";
    Map<String, String> parms = session.getParms();
    if (parms.get("username") == null) {
      msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
    } else {
      msg += "<p>Hello, " + parms.get("username") + "!</p>";
    }
    return newFixedLengthResponse(msg + "</body></html>\n");
  }
}
