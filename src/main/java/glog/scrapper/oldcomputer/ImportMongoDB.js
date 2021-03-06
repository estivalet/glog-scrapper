// db.system.drop()

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
    price: { type: String },
    description: { type: String },
    image: { type: String },
    shots: [],
    adverts: [],
    emulators: [],
    links: [],
    hardware: [],
    technicalInformation: {
        batteries: { type: String },
        buttons: { type: String },
        builtInGames: { type: String },
        colors: { type: String },
        controllers: { type: String },
        coprocessor: { type: String },
        cpu: { type: String },
        graphics: { type: String },
        gun: { type: String },
        keyboard: { type: String },
        language: { type: String },
        media: { type: String },
        numGames: { type: String },
        peripherals: { type: String },
        ports: { type: String },
        power: { type: String },
        ram: { type: String },
        rom: { type: String },
        size: { type: String },
        sound: { type: String },
        speed: { type: String },
        switches: { type: String },
        text: { type: String },
        vram: { type: String },
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


fs.readdir("C:\\Users\\lestivalet\\dev\\stuff\\glog-scrapper\\data\\oldcomputer\\pongs\\json", function (err, files) {
//fs.readdir("C:\\Temp\\oldcomputer", function (err, files) {
    //handling error
    if (err) {
        return console.log('Unable to scan directory: ' + err);
    } 
    //listing all files using forEach
    files.forEach(function (file) {
        // Do whatever you want to do with the file
       // console.log(file); 

        var content = JSON.parse(fs.readFileSync("C:\\Users\\lestivalet\\dev\\stuff\\glog-scrapper\\data\\oldcomputer\\pongs\\json\\" + file));
        //var content = JSON.parse(fs.readFileSync("C:\\temp\\oldcomputer\\" + file));
       // console.log("Output Content : \n"+ content);
        
        // 3. import into db
        var url = "mongodb://localhost:27017/barch";
        mongoose.connect(url, { useNewUrlParser: true })
        .then(()=> {
        
            content.hardware.forEach(function(hardware) {
                hardware.item.forEach(function(item) {
                    item.description = item.description
                                        .replace(/\u0026lt;/g,'')
                                        .replace(/\u0026gt;/g,'')
                                        .replace(/lt;/g,'')
                                        .replace(/gt;/g, '')
                                        .replace(/\u003d/g,'')
                                        .replace(/\u0026quot;/g,"")
                                        .replace(/\|/g,' ')
                                        .replace(/&nbsp;/g,' ')
                                        .replace(/brfont/g,'')
                                        .replace(/colorredstrong/g,'')
                                        .replace(/\/strong\/fontbrstrong/g,'')
                                        .replace(/\/strongbrblockquote\/blockquote/g,'')
                                        .replace(/\/strong\/fontbrblockquote/g,'')
                                        .replace(/\/p\/blockquote/g,'')
                                        .replace(/\&amp;/g,'&')
                                        .replace(/\/strongbrblockquote/g,'')
                                        .replace(/\/blockquote/g,'')
                                        .replace(/p classpetitgris/g,'')
                                        .replace(/\/b/g,'')
                });
            });

            const systemx = new Systemx({
                name: content.name,
                //type: content.type,
                //type: 'Consoles',
                //type: 'Computers',
                type: 'Pongs',
                manufacturer: content.manufacturer,
                country: content.origin,
                year: content.year,
                price: content.price,
                description: content.description.replace(/\n/g,'<br>'),
                image: content.image,
                shots: content.shot,
                adverts: content.advert,
                emulators: content.emulator,
                links: content.link,
                hardware: content.hardware,
                technicalInformation: {
                    batteries: content.batteries,
                    buttons: content.buttons,
                    builtInGames: content.builtInGames,
                    colors: content.colors,
                    controllers: content.controllers,
                    coprocessor: content.coprocessor,
                    cpu: content.cpu,
                    graphics: content.graphics,
                    gun: content.gun,
                    keyboard: content.keyboard,
                    language: content.language,
                    media: content.media,
                    numGames: content.numGames,
                    peripherals: content.peripherals,
                    ports: content.ports,
                    power: content.power,
                    ram: content.ram,
                    rom: content.rom,
                    size: content.size,
                    sound: content.sound,
                    speed: content.speed,
                    switches: content.switches,
                    text: content.text,
                    vram: content.vram
                    }
                    
                
            });
        
            //console.log('go');
        
            systemx.save()
                .then(data => {
                //    console.log('OK');
              //      console.log(data);
                    mongoose.disconnect();
                }).catch(err => {
                    console.log("errrrrrr" +err )
            });
        
        }).catch(err => {
            console.log('Could not connect to the database. Exiting now...');
            process.exit();
        });
                
    });
    console.log('DONE!')
});
