// Usage: node mock-server.js
// Description: mock server for development, read db.json for data
const http = require("http");
const fs = require("fs");

// 定义hostname和port
const hostname = "0.0.0.0";
const port = 9000;

const server = http.createServer((req, res) => {
  // default response
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

  // 读取db.json文件
  let rawData = fs.readFileSync("db.json");
  let jsonData = JSON.parse(rawData);

  // 遍历db.json文件，找到匹配的url
  let respType = "";
  let respFile = "";

  // 遍历db.json文件，找到匹配的url
  for (let i = 0; i < jsonData.length; i++) {
    const el = jsonData[i];
    let url = req.url;
    if (url.indexOf("?") > 0) {
      url = url.substring(0, url.indexOf("?"));
    }
    if (el.url === url && el.method === req.method && (!el.param || req.url.indexOf(el.param) > 0)) {
      console.log("matching url: " + req.url);
      respCode = el.code;
      respBody = JSON.stringify(el.data);
      respType = el.type;
      respFile = el.data;
      break;
    }
  }
  // 根据返回的类型，返回文件或者json
  if (respType) {
    const fileStream = fs.createReadStream(__dirname + "/" + respFile);
    res.writeHead(200, {
      "Content-Type": respType,
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Expose-Headers": "Content-Disposition",
      "Content-Disposition": "attachment; filename=" + respFile
    });
    fileStream.pipe(res);
  } else {
    res.writeHead(respCode, {
      "Content-Length": Buffer.byteLength(respBody),
      "Content-Type": "application/json",
    }).end(respBody);
  }
});
server.listen(port, hostname, () => {
  console.log("Server running at http://" + hostname + ":" + port);
});
