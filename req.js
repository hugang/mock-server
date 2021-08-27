const https = require("https");
const http = require("http");

// read json file
const fs = require("fs");

let rawData = fs.readFileSync("req.json");
let jsonData = JSON.parse(rawData);

//parse target req
let target = jsonData.target;
for (let i = 0; i < jsonData.requests.length; i++) {
  const el = jsonData.requests[i];
  if (el.id === target) {
    const reqBody = el.body;
    const options = el.options;

    if (el.protocol.indexOf("https") > -1) {
      const req = https.request(options, (res) => {
        console.log(`statusCode: ${res.statusCode}`);

        res.on("data", (d) => {
          process.stdout.write(d);
        });
      });

      req.on("error", (error) => {
        console.error(error);
      });

      if (!isEmpty(reqBody)) {
        req.write(reqBody);
      }

      req.end();
    } else {
      const req = http.request(options, (res) => {
        console.log(`statusCode: ${res.statusCode}`);

        res.on("data", (d) => {
          process.stdout.write(d);
        });
      });

      req.on("error", (error) => {
        console.error(error);
      });

      if (!isEmpty(reqBody)) {
        req.write(reqBody);
      }

      req.end();
    }

    break;
  }
}

function isEmpty(obj) {
  if (obj === null || obj === undefined) {
    return true;
  }
  for (var prop in obj) {
    if (obj.hasOwnProperty(prop)) {
      return false;
    }
  }

  return JSON.stringify(obj) === JSON.stringify({});
}
