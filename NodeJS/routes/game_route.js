/**
 * Created by ross on 4/12/2016.
 */
var express = require('express');
var router = express.Router();
var gameDal = require('../model/game_dal');

router.get('/all', function(req, res) {
    gameDal.GetAll(function (err, result) {
            if (err) throw err;
            res.render('game/get_game_data.ejs', {rs: result});
        }
    );
});


router.get('/', function (req, res) {
    gameDal.GetByID(req.query.game_id, function (err, result) {
            if (err) throw err;
            res.render('game/display_game_info.ejs', {rs: result, game_id: req.query.game_id});
        }
    );
});

router.get('/save', function(req, res, next) {
    console.log("title equals: " + req.query.title);
    console.log("tagline equals: " + req.query.tagline);
    gameDal.Insert(req.query, function (err, result){
        if (err) {
            res.send(err);
        } else {
            res.render('game/game_success.ejs', {rs:result});
        }
    })
});

// Added for Lab 10

router.get('/new', function(req, res) {
    genreDal.GetAll( function(err, result){
        if(err) {
            res.send("Error: " + err);
        }
        else {
            res.render('game/game_insert_form.ejs', {genre: result});
        }
    });

});

// Added for Lab 10

router.post('/insert_game', function(req, res) {
    console.log(req.body);
    gameDal.Insert(req.body.title, req.body.tagline, req.body.genre_id,
        function(err){
            if(err){
                res.send('Fail!<br />' + err);
            } else {
                //res.send('Success!')
                // user will be shown list of updated games
                res.redirect('/game/all');
            }
        });


});

// Added for Lab 10

router.get('/edit', function(req, res){
    console.log('/edit game_id:' + req.query.game_id);

    gameDal.GetByID(req.query.game_id, function(err, game_result){
        if(err) {
            console.log(err);
            res.send('error: ' + err);
        }
        else {
            console.log(game_result);
            genreDal.GetAll(function(err, genre_result){
                console.log(genre_result);
                res.render('game/game_edit_form', {rs: game_result, genres: genre_result, message: req.query.message});
            });
        }
    });
});

// Added for Lab 10

router.post('/update_game', function(req,res){
    console.log(req.body);
    // first update the game
    gameDal.Update(req.body.game_id, req.body.title, req.body.tagline, req.body.genre_name,
        function(err){
            var message;
            if(err) {
                console.log(err);
                message = 'error: ' + err.message;
            }
            else {
                // Success popup
            }
            // next update the genres
            gameDal.GetByID(req.body.game_id, function(err, game_info){
                genreDal.GetAll(function(err, genre_result){
                    console.log(genre_result);
                    res.redirect('/game/all');
                    //res.redirect('/game/edit?game_id=' + req.body.game_id + '&message=' + message);
                    //res.render('game/game_edit_form', {rs: game_info, genres: genre_result});
                });
            });

        });
});

// Added for Lab 10

router.get('/delete', function(req, res){
    console.log(req.query);
    gameDal.GetByID(req.query.game_id, function(err, result) {
        if(err){
            res.send("Error: " + err);
        }
        else if(result.length != 0) {
            gameDal.DeletegameGenreById(req.query.game_id, function (err) {});
            gameDal.DeleteById(req.query.game_id, function (err) {
                //res.write(result[0].title + ' Successfully Deleted');
                res.redirect('/game/all');
            });
        }
        else {
            res.send('game does not exist in the database.');
        }
    });
});

module.exports = router;