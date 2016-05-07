var express = require('express');
var router = express.Router();
var accountDAL = require('../model/login_dal');

/* GET home page. */
router.get('/', function(req, res, next) {
  var data = {
    title : 'Express'
  }
  if(req.session.account === undefined) {
    res.render('index', data);
  }
  else {
    data.firstname = req.session.account.firstname;
    res.render('index', data);
  }
});

router.get('/authenticate', function(req, res) {
  accountDAL.GetByEmail(req.query.email, req.query.password, function (err, account) {
    if (err) {
      res.send(err);
    }
    else if (account == null) {
      res.send('{"success" : "0","status" : 401}'); // HTTP status code 401 is unauthorized
    }
    else {
      req.session.account = account;
      if(req.session.originalUrl = '/login') {
        req.session.originalUrl = '/'; //don't send user back to login, instead forward them to the homepage.
      }
      res.send('{"success" : "1","status" : 200}'); // HTTP status code 200 is OK
    }
  });
});

router.get('/login', function(req, res, next) {
  if(req.session.account) {
    res.redirect('/'); //user already logged in so send them to the homepage.
  }
  else {
    res.render('authentication/login.ejs');
  }
});

router.get('/logout', function(req, res) {
  req.session.destroy( function(err) {
    res.render('authentication/logout.ejs');
  });
});

module.exports = router;

