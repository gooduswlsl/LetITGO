var express = require('express');
var formidable = require('formidable');
var db = require('../db')
var router = express.Router();


//menu/update
router.post('/update', function(req, res) {
var mSeq = req.body.mSeq;
var mName = req.body.mName;
var mImgUrl = req.body.mImgUrl;
var mPrice = req.body.mPrice;
var mDetail = req.body.mDetail;
var seller_seq = req.body.seller_seq;
var action = req.body.action;

console.log({mSeq, mName, mImgUrl, mPrice, mDetail, seller_seq, action});

var sql_insert = "insert into menu (mName, mImgUrl, mPrice, mDetail, seller_seq) values(?,?,?,?,?);";
var sql_update = "update menu "+
               "set mName = ?, mImgUrl =? ,mPrice = ?, mDetail = ? "+
               "where mSeq = ? limit 1; ";
var sql_delete = "delete from menu where mName = ?; ";


switch(action){

case 1: // 신규메뉴 등록
   console.log("sql_insert : " + sql_insert);
   db.get().query(sql_insert, [mName, mImgUrl, mPrice, mDetail, seller_seq], function (err, result) {
     if (err) return res.sendStatus(400);

     res.status(200).send('' + result.insertId);
   });
   break;
   
case 2: // 메뉴 수정
     
     console.log("수정하려는 mSeq는"+mSeq);
     
   console.log("sql_update : " + sql_update);

   db.get().query(sql_update, [mName, mImgUrl, mPrice, mDetail, mSeq], function (err, result) {
     if (err) return res.sendStatus(400);

     res.status(200).send('' + result.insertId);
   });
   break;
   
case 3: // 메뉴 삭제
   console.log("sql_delete : " + sql_delete);

   db.get().query(sql_delete, mName , function (err, result) {
     if (err) return res.sendStatus(400);

     res.status(200).send('' + result.insertId);
   });
   break;   
}
});

//menu/list
router.get('/list', function(req, res, next) {
var seller_seq = req.query.seller_seq;

console.log(seller_seq);

if(!seller_seq) { return res.sendStatus(400); } 

var sql = "select mSeq, mName, mImgUrl, mPrice, mDetail " +
         "from menu " + 
         "where seller_seq = ?;";

console.log("sql : " + sql);     

db.get().query(sql, seller_seq, function (err, rows) {
   console.log("rows : " + JSON.stringify(rows));
   console.log("row.length : " + rows.length);
   if (rows.length > 0) {
     res.status(200).json(rows);
   } else {
     res.sendStatus(400);
   }
});
});


//menu/icon_add
router.post('/icon_add', function (req, res) {
var form = new formidable.IncomingForm();

form.on('fileBegin', function (name, file){
 file.path = './public/menu/' + file.name;
});
form.parse(req);

});

//menu/sellerList
router.get('/sellerList', function(req, res){
    var sql = "select * from seller";
    db.get().query(sql, function(err, rows){
        if(rows.length>0)
            res.status(200).json(rows);
        
        else
            res.sendStatus(400);
       
        
    });
});

//menu/sellerList/:seller_seq
router.get('/sellerList/:sSeq', function(req, res){
    var sSeq = req.params.sSeq;
    var sql = "select * from seller where seq=?";
    
    db.get().query(sql, sSeq, function(err, rows){
        if(err)
            console.log(err);
        res.status(200).json(rows[0]);
       
    });
});

//menu/menuList
router.get('/menuList', function(req, res){
    var sql = "select * from menu";
    db.get().query(sql, function(err, rows){
        if(rows.length>0)
            res.status(200).json(rows);
        else
            res.sendStatus(400);
       
        
    });
});

//menu/menuList/:mSeq
router.get('/menuList/:mSeq', function(req, res){
    console.log("select");
    var mSeq = req.params.mSeq;
    var sql = "select * from menu where mSeq=?";
    
    db.get().query(sql, mSeq, function(err, rows){
        if(err)
            console.log(err);
        res.status(200).json(rows[0]);
        
    });
});

//menu/searchSeller
router.get('/searchSeller', function(req, res){
    var key = req.query.key;
    var sql = "select * from seller where name Like ?";
    

    db.get().query(sql, "%"+key+"%", function(err, rows){
        if(err)
            console.log(err);
        res.status(200).json(rows);
    });
 });

//menu/searchMenu
router.get('/searchMenu', function(req, res){
    var key = req.query.key;
    var sql = "select * from menu where mName Like ?";
    
     db.get().query(sql,"%"+key+"%" , function(err, rows){
         if(err)
             console.log(err);
         res.status(200).json(rows);
     });
});



module.exports = router;