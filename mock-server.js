const http = require("http");
const fs = require("fs");

const hostname = "localhost";
const port = 3000;

const server = http.createServer((req, res) => {
  let respCode = 404;
  let respBody = "not found";

  // get origin and set to response header (IE11 must)
  let origin = req.headers.origin;
  if (origin !== undefined) {
    res.setHeader("Access-Control-Allow-Origin", origin);
  } else {
    res.setHeader("Access-Control-Allow-Origin", "*");
  }
  res.setHeader("Access-Control-Allow-Credentials", "true");
  res.setHeader("Access-Control-Allow-Methods", "*");
  res.setHeader("Access-Control-Max-Age", "3600");

  // get access control request headers and set to response headers (IE11 must)
  let headers = req.headers["access-control-request-headers"];
  if (headers !== undefined) {
    res.setHeader("Access-Control-Allow-Headers", headers);
  } else {
    res.setHeader("Access-Control-Allow-Headers", "*");
  }

  // preflight request with options method need to be allowed
  if (req.method === "OPTIONS") {
    res.writeHead(204, {}).end();
    return;
  }

  let rawData = fs.readFileSync("db.json");
  let jsonData = JSON.parse(rawData);

  for (let i = 0; i < jsonData.length; i++) {
    const el = jsonData[i];
    let url = req.url;
    if (url.indexOf("?") > 0) {
      url = url.substr(0, url.indexOf("?"));
    }
    if (el.url === req.url && el.method === req.method) {
      respCode = el.code;
      respBody = JSON.stringify(el.data);
      break;
    }
  }

  res
    .writeHead(respCode, {
      "Content-Length": Buffer.byteLength(respBody),
      "Content-Type": "text/plain",
    })
    .end(respBody);
});
server.listen(port, hostname, () => {
  console.log("Server running at http://" + hostname + ":" + port);
});
