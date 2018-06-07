**GLOG Game Scrapper**

Start GLOG Database:

```
mongod.exe --dbpath data
```

Import data:

```
C:\Users\luisoft\apps\mongodb-3.6.4\bin\mongoimport --db glogdb --collection games --file data.json
```

**TODO**
- [ ] Currently saving the scrapped data in a XML file, need to save it in JSON.

