var express = require('express');
var formidable = require('formidable');
var db = require('../db')
var router = express.Router();

//member/:phone
router.get('/:phone', function(req, res, next) {
  var phone = req.params.phone;

  var sql_customer = "select * from customer where phone = ? limit 1;";
  var sql_seller = "select * from seller where phone = ? limit 1;";

  db.get().query(sql_customer, phone, function (err, rows, result, fields) {
	if(rows.length > 0){
		res.status(201).send(''+rows[0].seq);
	}else{
		db.get().query(sql_seller, phone, function (err, rows){
			if(rows.length > 0){
				res.status(202).send(''+rows[0].seq);
			}else
				res.sendStatus(400);
			if(err)
				console.log(err);
		});
	}
			if(err)
				console.log(err);
  });
});

//member/customer/:customer_seq
router.get('/customer/:customer_seq', function(req, res, next){
	var seq = req.params.customer_seq;
	var sql_select = "select * from customer where seq = ? limit 1;";
	
	db.get().query(sql_select, seq, function(err, rows){
		if(err){
			console.log(err);
			res.sendStatus(400);
		}
		res.status(200).json(rows[0]);

	});
});

//member/seller/:seller_seq
router.get('/seller/:seller_seq', function(req, res, next){
	var seq = req.params.seller_seq;
	var sql_select = "select * from seller where seq = ? limit 1;";
	
	db.get().query(sql_select, seq, function(err, rows){
		if(rows.length > 0)
			res.status(200).json(rows[0]);
		else
			res.sendStatus(400);
		if(err){
			console.log(err);
			res.sendStatus(400);
		}
	});
});

//member/customer
router.post('/customer', function(req, res) {
  var phone = req.body.phone;
  var name = req.body.name;
  var sextype = req.body.sextype;
  var birthday = req.body.birthday;

 var sql_insert = "insert into customer (phone, name, sextype, birthday) values(?, ?, ?, ?);";
   
  db.get().query(sql_insert, [phone, name, sextype, birthday], function (err, rows) {
    console.log("rows"+JSON.stringify(rows));
	 if (err) {
		  console.log("ERR "+err);
		  return res.sendStatus(400);
	  }
	  res.status(200).send(''+rows.insertId);

    });
  });

//member/seller
router.post('/seller', function(req, res) {
    var phone = req.body.phone;
    var name = req.body.name;
    var site = req.body.site;
    var tel = req.body.tel;
    var address = req.body.address;
    var webpage = req.body.webpage;
    //var img = req.body.img;

  console.log({name,site,tel,address,webpage,phone});

  var sql_count = "select count(*) as cnt " +
            "from seller " + 
            "where phone = ?;";

  var sql_insert = "insert into seller (name, site, tel, address, webpage, phone) values(?, ?, ?, ?, ?, ?);";
  var sql_update = "update seller set name = ?, site = ?, tel = ?, address = ?, webpage = ? where phone = ?; ";
  var sql_select = "select seq from seller where phone = ?; ";
  
  db.get().query(sql_count, phone, function (err, rows) {
    if (rows[0].cnt > 0) {
      console.log("sql_update : " + sql_update);

      db.get().query(sql_update, [name, site, tel, address, webpage, phone], function (err, result) {
        if (err) {
            console.log(err);
            return res.sendStatus(400);
        }
        console.log(result);

        db.get().query(sql_select, phone, function (err, rows) {
          if (err) return res.sendStatus(400);

          res.status(200).send('' + rows[0].seq);
        });
      });
    } else {
      console.log("sql_insert : " + sql_insert);

      db.get().query(sql_insert, [name,site,tel,address,webpage,phone], function (err, result) {
        if (err) return res.sendStatus(400);

        res.status(200).send('' + result.insertId);
      });
    }
  });
});




//member/img_upload
router.post('/img_upload', function (req, res) {
  var form = new formidable.IncomingForm();

  form.on('fileBegin', function (name, file){
    file.path = './public/member/' + file.name;
  });

  form.parse(req, function(err, fields, files) {
    var sql_update = "update customer set img = ? where seq = ?;";
	
    db.get().query(sql_update, [files.file.name, fields.member_seq], function (err, rows) {
      res.sendStatus(200);
    });
  });
});

module.exports = router;