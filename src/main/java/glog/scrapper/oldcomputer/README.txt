
// 1. define model
var mongoose = require('mongoose');
mongoose.Promise = global.Promise;
var Schema = mongoose.Schema;

var systemSchema = new Schema(
  {
    name: { type: String },
    type: { type: String },
    manufacturer: { type: String },
    country: { type: String },
    year: { type: String },
    technicalInformation: {
        ram: { type: String },
    }

  },
  {
    timestamps: true,
    toObject: { virtuals: true },
    toJSON: { virtuals: true },
    collection: 'system'   // force to use the name, if not moongoose add "s" at the and of the collection.
  }
);
var Systemx = mongoose.model('Systemx', systemSchema);

  
// 2. read data from oldcomputer.com
var fs = require("fs");
var content = JSON.parse(fs.readFileSync("C:\\Users\\lestivalet\\dev\\stuff\\glog-scrapper\\data\\oldcomputer\\computers\\json\\1.json"));
console.log("Output Content : \n"+ content);

// 3. import into db
var url = "mongodb://localhost:27017/mytestingdb";
mongoose.connect(url, { useNewUrlParser: true })
.then(()=> {

    const systemx = new Systemx({
        name: content.name,
        type: content.type,
        manufacturer: content.manufacturer,
        country: content.origin,
        year: content.year,
        technicalInformation: {
            ram: content.ram,
        }
            
        
    });

    console.log('go');

    systemx.save()
        .then(data => {
            console.log('OK');
            console.log(data);
            mongoose.disconnect();
        }).catch(err => {
            console.log("errrrrrr" +err )
    });

}).catch(err => {
    console.log('Could not connect to the database. Exiting now...');
    process.exit();
});
