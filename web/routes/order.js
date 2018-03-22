var express = require('express');
var formidable = require('formidable');
var db = require('../db')
var router = express.Router();

//order/myList
router.get('/myList', function(req, res, next) {
   var cust_seq = req.query.cust_seq;
    var period = req.query.period;
    
    var sql =  "select seq, cust_seq, seller_seq, menu_seq, num, time_order+'0000-00-00T09:00:00.000Z' as time_server, time_take+'0000-00-00T09:00:00.000Z' as time_take, time_order, message, permit from `order` where cust_seq = ? and time_order > DATE_ADD(now(), INTERVAL "+period+") and (permit=-1 or permit = 4)";   
  
    db.get().query(sql, cust_seq, function(err, rows) {

      if (err)
         console.log(err);
        else{
            console.log(JSON.stringify(rows));
            res.status(200).json(rows);
          }
    });
});

//order/showList
router.get('/showList', function(req, res, next) {
   var cSeq = req.query.cSeq;
    var sql =  "select seq, cust_seq, seller_seq, menu_seq, num, time_order+'0000-00-00T09:00:00.000Z' as time_order, time_take+'0000-00-00T09:00:00.000Z' as time_take, permit, count(*) as `count` from `order` where cust_seq = ? and permit!=4 and permit!=-1 group by time_order,seller_seq";   
   
    db.get().query(sql, cSeq, function(err, rows) {

      if (err)
         console.log(err);
        else{
            console.log(JSON.stringify(rows));
            res.status(200).json(rows);
          }
    });
});

//order/dialog/showList
router.get('/dialog/showList', function(req, res, next) {
   var cSeq = req.query.cSeq;
    var sSeq = req.query.sSeq;
    var oTime = req.query.oTime;
    var sql = "select * from `order` where cust_seq=? and seller_seq = ? and time_order=?";
  
    db.get().query(sql, [cSeq,sSeq, oTime], function(err, rows) {
      if (err)
         console.log(err);
        else{
            console.log(JSON.stringify(rows));
            res.status(200).json(rows);
          }
    });
});

//order/sendCustPermit
router.post('/sendCustPermit', function(req, res){
    var cSeq = req.query.cSeq;
    var sSeq = req.query.sSeq;
    var oTime = req.query.oTime;
    var sql = "update `order` set permit = '4' where cust_seq=? and seller_seq = ? and time_order=?";
    
    db.get().query(sql, [cSeq, sSeq, oTime], function(err, result){
       if (err) return res.sendStatus(400);
                         
         res.sendStatus(200);
    });
});



//order/list
router.get('/list', function(req, res, next) {
   var seller_seq = req.query.seller_seq;
   var period = req.query.period;

   if (!seller_seq) {
      return res.sendStatus(400);
   }
   console.log("period: "+period);
   switch(period){
   case "-1 day":
	   var sql = "select * from `order` "+
	 	 "where seller_seq = ? " +
	     "and DATE_FORMAT(time_take,'%Y-%m-%d') = CURDATE() order by time_take";
	   break;
   default:
	   var sql = "select * from `order` where seller_seq = ? and time_order > DATE_ADD(now(), INTERVAL "+period+") order by time_take";
	   break;
   }

   console.log("sql: "+sql);
   db.get().query(sql, seller_seq, function(err, rows) {

      if (err)
         console.log(err);
      if (rows.length > 0) {
         res.status(200).json(rows);
      } else {
         res.sendStatus(400);
      }
   });
});

//order/today_list
router.get('/today_list', function(req, res, next) {
   var seller_seq = req.query.seller_seq;
   var clicked = req.query.clicked;

   console.log("누른것:"+clicked);

   if (!seller_seq) {
      return res.sendStatus(400);
   }

   switch(clicked){
   case "new_order":
	   var sql = "select * from `order` where seller_seq = ? and permit = 0";
	   break;

   case "preparing":
	   var sql = "select * from `order` "+
			 	 "where seller_seq = ? " +
			     "and DATE_FORMAT(time_take,'%Y-%m-%d') = CURDATE() " +
			     "and (permit = 1 or permit = 2 or permit = 3 ) order by time_take " ;
	   break;

   case "total":
	   var sql = "select * from `order` where seller_seq = ? and DATE_FORMAT(time_take,'%Y-%m-%d') >= CURDATE() order by time_take";
	   break;
   }

   console.log("sql: "+sql);
   db.get().query(sql, seller_seq, function(err, rows) {

      if (err)
         console.log(err);
      if (rows.length > 0) {
         res.status(200).json(rows);
      } else {
         res.sendStatus(400);
      }
   });
});


//order/sendPermit
router.post('/sendPermit', function(req, res){
    var permit = req.query.permit;
    var seq = req.query.seq;

    console.log("permit:"+permit+" seq:"+seq);

    var sql = "update `order` set permit = ? where seq = ? limit 1; ";

    db.get().query(sql, [permit, seq], function(err, result){
       if (err) return res.sendStatus(400);
         res.status(200).send('' + result.insertId);
       });
 });


//order/sendTotal_price
router.post('/sendTotal_price', function(req, res){
    var seq = req.query.seq;
    var total_price = req.query.total_price;

    console.log("seq:"+seq+" total_price:"+total_price);

    var sql = "update `order` set total_price = ? where seq = ? limit 1; ";

    db.get().query(sql, [total_price, seq], function(err, result){
    	if (err) return res.sendStatus(400);
         res.status(200).send('' + result.insertId);
       });
 });

//order/getMonthSales
router.get('/getMonthSales', function(req, res, next) {
   var seller_seq = req.query.seller_seq;

   console.log("seller_seq는 " + seller_seq);

   if (!seller_seq) {
      return res.sendStatus(400);
   }

   var sql = "select EXTRACT ( MONTH FROM time_order ) as month, SUM ( total_price ) as sales " +
           "from ( select time_order, total_price " +
                 "from `order` " +
                 "where seller_seq = ? and permit = 4 ) SALES " +
           "group by EXTRACT ( MONTH FROM time_order ); ";


   console.log("sql : " + sql);

   db.get().query(sql, seller_seq, function(err, rows) {

      if (err)
         console.log(err);
      console.log("rows : " + JSON.stringify(rows));
      console.log("row.length : " + rows.length);
      if (rows.length > 0) {
         res.status(200).json(rows);
      } else {
         res.sendStatus(400);
      }
   });
});

//order/sendOrder
router.post('/sendOrder', function(req, res){

    var cust_seq = req.query.cSeq;
    var menu_seq = req.body.menu_seq;
    var seller_seq = req.body.seller_seq;
    var num = req.body.num;
    var message = req.body.message;
    var time_take = req.body.time_take;

    var sql = "insert into `order` (cust_seq,menu_seq,seller_seq,num,message,time_take) values(?,?,?,?,?,?);";

    console.log("sql : " + sql);

    db.get().query(sql,[cust_seq,menu_seq,seller_seq,num,message,time_take], function(err, result){
       if (err) return res.sendStatus(400);
         res.status(200).send('' + result.insertId);
       });
 });

///order/getWaitingNumber/:seller_seq
router.get('/getWaitingNumber/:seller_seq', function(req, res, next){
	var seller_seq = req.params.seller_seq;
	var sql_select = "select count(*) as cnt from `order` " +
					 "where seller_seq = ? " +
					 "and DATE_FORMAT(time_take,'%Y-%m-%d') = CURDATE() " +
					 "and (permit = 1 or permit = 2 or permit = 3 )";

	db.get().query(sql_select, seller_seq, function(err, rows){
		if(rows.length > 0)
			res.status(200).json(rows[0].cnt);
		else
			res.sendStatus(400);
		if(err){
			console.log(err);
			res.sendStatus(400);
		}
	});
});


module.exports = router;