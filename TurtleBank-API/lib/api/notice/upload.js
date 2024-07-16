const express = require('express');
const router = express.Router();
const multer = require('multer');
const { runRsync } = require("../../../middlewares/rsync");

const upload = multer({
  storage: multer.diskStorage({
      destination: function (req, file, cb) {
	      console.error("file")
	      console.error(file)
          cb(null, file.fieldname);
      },
      filename: function (req, file, cb) 
        {
		console.error("Upload_file.originalname : ", file.originalname)
          cb(null, Buffer.from(file.originalname, 'ascii').toString('utf8' ));
      },
  }),
});

router.get('/', (req, res) => {
  res.send('<html><body><h1>Welcome to the file upload page</h1></body></html>');
});

router.post('/', upload.any(), (req, res) => {
  console.log(req.file);
  // runRsync;
});

module.exports = router;

