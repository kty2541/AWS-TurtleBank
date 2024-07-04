var express = require('express');
var router = express.Router();
const ModelBoard = require("../../../models_board/index");
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");
const fs = require("fs");

router.post("/", decryptRequest, (req, res) => {
  var r = new Response();
  let filename = req.body.filename;
  ModelBoard.notices.findOne({
    attributes: ['filepath'], // filepath 컬럼만 선택
      where: {filepath: filename}
  }).then((data) => {
    console.error(data != null);
    if (data != null) {
      console.error(data.dataValues.filepath);
      download_file_Path = file_path + data.dataValues.filepath;
      fs.stat(download_file_Path, (err, stats) =>{
        console.log("파일 확인 중");
        console.log(err);
        if (err != null) {
            console.log("파일이 존재하지 않습니다.");
            r.status = 500;
            r.data = {
              message: "해당하는 파일이 없습니다.",
            };
            // return res.json(encryptResponse(r));
            return res.send();
        }
        res.download(download_file_Path);
      });
    } else {
      r.status = statusCodes.NOT_FOUND;
      r.data = {
        message: "해당하는 데이터가 없습니다.",
      };
      return res.json(encryptResponse(r));
    }
  })
  .catch((err) => {
    r.status = statusCodes.SERVER_ERROR;
    r.data = {
      message: err.toString(),
    };
    return res.send();
  });
});


router.get("/", (req, res) => {
  const filename = Buffer.from(req.query.filename, 'ascii').toString('utf8' );
  console.log(filename);
  const filePath = "../file/" + filename;
  // res.download(filePath, (error) => {
  //     if (error) {
  //         console.error(error);
  //         res.status = statusCodes.ERROR;
  //         res.status.send("파일을 찾는 중에 오류가 발생했습니다.");
  //     }
  // });
});

module.exports = router;