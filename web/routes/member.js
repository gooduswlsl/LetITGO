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
  var img = req.body.img;
  var regId = req.body.regId;

 var sql_insert = "insert into customer (phone, name, sextype, birthday, img, regId) values(?, ?, ?, ?, ?, ?);";

  db.get().query(sql_insert, [phone, name, sextype, birthday, img, regId], function (err, rows) {
    console.log("rows"+JSON.stringify(rows));
	 if (err) {
		  console.log("ERR "+err);
		  return res.sendStatus(400);
	  }
	  res.status(200).send(''+rows.insertId);

    });
  });



//member/changeProfile
router.post('/changeProfile', function(req, res) {
  var seq = req.body.seq;
  var name = req.body.name;
  var sextype = req.body.sextype;
  var birthday = req.body.birthday;
  var img = req.body.img;

  var sql_update = "update customer "+
 				  "set name = ?, sextype =? , birthday = ?, img = ? "+
                  "where seq = ? limit 1; ";

  	db.get().query(sql_update, [name, sextype, birthday, img, seq], function (err, result) {
	     if (err) return res.sendStatus(400);

	     res.status(200).send('' + result.insertId);
	   });
  });


//member/leaveMember
router.post('/leaveMember', function(req, res){
    var seq = req.query.seq;

    var sql_delete = "delete from customer where seq = ?; ";

    db.get().query(sql_delete, seq, function(err, result){
    	if (err) return res.sendStatus(400);
         res.status(200).send('' + result.insertId);
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
    var img = req.body.img;
    var latitude = req.body.latitude;
    var longitude = req.body.longitude;
    var regId = req.body.regId;


  console.log({name,site,tel,address,webpage,phone,img,latitude,longitude, regId});

  var sql_count = "select count(*) as cnt " +
            "from seller " +
            "where phone = ?;";

  var sql_insert = "insert into seller (name, site, tel, address, webpage, phone, img,latitude,longitude,regId) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
  var sql_update = "update seller set name = ?, site = ?, tel = ?, address = ?, webpage = ?, img = ?, latitude = ?, longitude = ?, regId = ? where phone = ?; ";
  var sql_select = "select seq from seller where phone = ?; ";

  db.get().query(sql_count, phone, function (err, rows) {
    if (rows[0].cnt > 0) {
      console.log("sql_update : " + sql_update);

      db.get().query(sql_update, [name, site, tel, address, webpage, phone, img, latitude, longitude, regId], function (err, result) {
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

      db.get().query(sql_insert, [name,site,tel,address,webpage,phone,img,latitude,longitude, regId], function (err, result) {
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
    file.path = './public/customer/' + file.name;
  });
    
    form.parse(req);

});

//member/img_upload2
router.post('/img_upload2', function (req, res) {
  var form = new formidable.IncomingForm();

  form.on('fileBegin', function (name, file){
    file.path = './public/seller/' + file.name;
  });

    form.parse(req);

});

//member/sendNewSellerRegId
router.post('/sendNewSellerRegId', function(req, res){
    var seq = req.query.seq;
    var regId = req.query.regId;

    console.log("seq:"+seq+" regId:"+regId);

    var sql = "update seller set regId = ? where seq = ? limit 1; ";

    db.get().query(sql, [regId, seq], function(err, result){
    	if (err) return res.sendStatus(400);
         res.status(200).send('' + result.insertId);
       });
 });

//member/sendNewCustomerRegId
router.post('/sendNewCustomerRegId', function(req, res){
    var seq = req.query.seq;
    var regId = req.query.regId;

    console.log("seq:"+seq+" regId:"+regId);

    var sql = "update customer set regId = ? where seq = ? limit 1; ";

    db.get().query(sql, [regId, seq], function(err, result){
    	if (err) return res.sendStatus(400);
         res.status(200).send('' + result.insertId);
       });
 });

 //member/changeSellerInfo
 router.post('/changeSellerInfo', function(req, res) {
 	  var seq = req.body.seq;
 	  var name = req.body.name;
 	  var site = req.body.site;
 	  var tel = req.body.tel;
 	  var address = req.body.address;
 	  var webpage = req.body.webpage;
 	  var img = req.body.img;
 	  var latitude = req.body.latitude;
 	  var longitude = req.body.longitude;
 	  var type = req.body.type;

 	  var sql_update = "update seller set name = ?, site = ?, tel = ?, address = ?, webpage = ?, img = ?, latitude = ?, longitude = ?, type = ? where seq = ? limit 1; ";

   	db.get().query(sql_update, [name, site, tel, address, webpage, img, latitude, longitude, type, seq], function (err, result) {
 	     if (err) return res.sendStatus(400);

 	     res.status(200).send('' + result.insertId);
 	   });
   });


 //member/leaveSeller
 router.post('/leaveSeller', function(req, res){
     var seq = req.query.seq;

     var sql_delete = "delete from seller where seq = ?; ";

     db.get().query(sql_delete, seq, function(err, result){
     	if (err) return res.sendStatus(400);
          res.status(200).send('' + result.insertId);
        });
  });



//member/getSellerRegId/:seller_seq
router.get('/getSellerRegId/:seller_seq', function(req, res, next){
	var seq = req.params.seller_seq;
	var sql_select = "select regId from seller where seq = ? limit 1;";

	db.get().query(sql_select, seq, function(err, rows){
		if(rows.length > 0)
			res.status(200).json(rows[0].regId);
		else
			res.sendStatus(400);
		if(err){
			console.log(err);
			res.sendStatus(400);
		}
	});
});

//member/getCustomerRegId/:customer_seq
router.get('/getCustomerRegId/:customer_seq', function(req, res, next){
	var seq = req.params.customer_seq;
	var sql_select = "select regId from customer where seq = ? limit 1;";

	db.get().query(sql_select, seq, function(err, rows){
		if(rows.length > 0)
			res.status(200).json(rows[0].regId);
		else
			res.sendStatus(400);
		if(err){
			console.log(err);
			res.sendStatus(400);
		}
	});
});


module.exports = router;