const PORT = [[3000,3001],[4000,4001]];

// const apiUrl = 'http://20.0.20.221:3000/api/mydata/send_btoa';
// const apiUrl = 'http://20.0.20.221:3000/api/mydata/send_btob';
// const test_api_url = "http://20.0.20.221:3000/api/mydata/b_api";
// const apiUrl = 'http://20.0.20.221:3000/api/mydata/res_account';


/*
PORT[0][0] 3000 => 블루 API (실드)
PORT[0][1] 3001 => 블루 WEB (실드)

PORT[1][0] 4000 => 레드 API (소드)
PORT[1][1] 3001 => 레드 WEB (소드)

*/
module.exports = {PORT};