var express = require('express');
var router = express.Router();

const axios = require("axios");
var {decryptRequest, decryptEnc, encryptResponse} = require("../../middlewares/crypt");
var {runRsync} = require("../../middlewares/rsync");
const fs = require("fs");

router.get("/", (req, res) => {
    const filename =  req.query.url
    runRsync;
    res.download(file_path + file_path);


    // const baseData = `{"filename" : "${filename}"}`;
    // axios({
    //     method: "post",
    //     url: api_url + "/api/notice/download",
    //     data: encryptResponse(baseData)
    // }).then((data) => {
    //     console.log(data!=null);
    //     if(data != null){
    //         // console.log(data.data);
    //         const readStream = fs.createReadStream('read');
    //         const writeStream = fs.createWriteStream(filename);
    //         readStream.writeStream(writeStream);
    //         res.download(writeStream);
    //     }
    // });
})

module.exports = router;