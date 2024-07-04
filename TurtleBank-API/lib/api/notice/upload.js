const express = require('express');
const router = express.Router();
const multer = require('multer');
const { runRsync } = require("../../../middlewares/rsync");

const upload = multer({
  storage: multer.diskStorage({
      destination: function (req, file, cb) {
          // cb(null, req.body.fid);/
          // cb(null, "../file");
          cb(null, "../file");
      },
      filename: function (req, file, cb) {
          cb(null, Buffer.from(file.originalname, 'ascii').toString('utf8' ));
      },
  }),
});

router.get('/', (req, res) => {
  res.send('<html><body><h1>Welcome to the file upload page</h1></body></html>');
});

router.post('/', upload.single('file'), (req, res) => {
  console.log(req.body.file);
  console.log(req.file);
  runRsync();
});

module.exports = router;


// 로컬 코드
// const express = require('express');
// const router = express.Router();
// const multer = require('multer');
// const upload = multer({
//   storage: multer.diskStorage({
//       destination: function (req, file, cb) {
//           console.log(req.body.fid);
//           cb(null, req.body.fid);
//       },
//       filename: function (req, file, cb) {
//           cb(null, file.originalname);
//       },
//   }),
// });

// router.get('/', (req, res) => {
//   res.send('<html><body><h1>Welcome to the file upload page</h1></body></html>');
// });

// router.post('/', upload.single('file'), (req, res) => {
//   console.log(res)
// });

// module.exports = router;

