use the following code to read a json file and insert into a database using NODE

var fs = require("fs");
var content = fs.readFileSync("C:\\Users\\lestivalet\\dev\\stuff\\glog-scrapper\\data\\oldcomputer\\computers\\json\\1.json");
console.log("Output Content : \n"+ content);

var url = "mongodb://localhost:27017";
var MongoClient = require('mongodb').MongoClient;
MongoClient.connect(url, { useNewUrlParser: true }, function(err, client) {
    if (err) throw err;
    var db = client.db('mytestingdb');
    console.log("Switched to "+db.databaseName+" database");
    var doc = { name: "Roshan", age: "22" };
    db.collection("users").insertOne(doc, function(err, res) {
        if (err) throw err;
        console.log("Document inserted");
        client.close();
    });
});