using System;
using System.IO;
using System.Text;
using System.Net;
using System.Threading.Tasks;

namespace HttpListenerExample
{
    class HttpServer
    {
        public static HttpListener listener;
        public static string url = "http://localhost:8080/";
        public static string pageData = "{\"name1\":\"hg\",\"name2\":\"hg\"}";


        public static async Task HandleIncomingConnections()
        {
            bool runServer = true;

            // While a user hasn't visited the `shutdown` url, keep on handling requests
            while (runServer)
            {
                // Will wait here until we hear from a connection
                HttpListenerContext ctx = await listener.GetContextAsync();

                // Peel out the requests and response objects
                HttpListenerRequest req = ctx.Request;
                HttpListenerResponse resp = ctx.Response;

                // Console.WriteLine(req.Headers);
                string headers = req.Headers.Get("Access-Control-Request-Headers");
                resp.Headers.Add("Access-Control-Allow-Headers", headers);
                Console.WriteLine(headers);

                string origin = req.Headers.Get("Origin");
                resp.Headers.Add("Access-Control-Allow-Origin", origin);
                resp.Headers.Add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
                resp.Headers.Add("Access-Control-Allow-Credentials", "true");
                resp.Headers.Add("Access-Control-Max-Age", "3600");

                if (req.HttpMethod =="OPTIONS")
                {
                    resp.StatusCode = 204;
                    resp.Close();
                    continue;
                }

                // Write the response info
                byte[] data = Encoding.UTF8.GetBytes(pageData);
                resp.ContentType = "text/html";
                resp.ContentEncoding = Encoding.UTF8;
                resp.ContentLength64 = data.LongLength;

                // Write out to the response stream (asynchronously), then close it
                await resp.OutputStream.WriteAsync(data, 0, data.Length);
                resp.Close();
            }
        }


        public static void Main(string[] args)
        {
            // Create a Http server and start listening for incoming connections
            listener = new HttpListener();
            listener.Prefixes.Add(url);
            listener.Start();
            Console.WriteLine("Listening for connections on {0}", url);

            // Handle requests
            Task listenTask = HandleIncomingConnections();
            listenTask.GetAwaiter().GetResult();

            // Close the listener
            listener.Close();
        }
    }
}
