var express = require('express');
var formidable = require('formidable');
var db = require('../db')
var router = express.Router();

//order/list
router.get('/list', function(req, res, next) {
	var seller_seq = req.query.seller_seq;

	console.log("seller_seq는 " + seller_seq);

	if (!seller_seq) {
		return res.sendStatus(400);
	}

	var sql = "select * from `order` where seller_seq = ?;";

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
			  		"where seller_seq = ? and permit = 2 ) SALES " +
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

module.exports = router;