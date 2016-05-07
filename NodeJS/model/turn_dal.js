///////////
// Lab 8 //
///////////


var mysql   = require('mysql');
var db  = require('./db_connection.js');

/* DATABASE CONFIGURATION */
var connection = mysql.createConnection(db.config);

exports.GetAll = function(callback) {
    connection.query('SELECT * FROM game;',
        function (err, result) {
            if(err) {
                console.log(err);
                callback(true);
                return;
            }
            console.log(result);
            callback(false, result);
        }
    );
}


exports.GetByID = function(game_id, callback) {
    console.log(game_id);
    var query = 'SELECT * FROM game_info_view WHERE game_id=' + game_id;
    console.log(query);
    connection.query(query,
        function (err, result) {
            if(err) {
                console.log(err);
                callback(true);
                return;
            }
            callback(false, result);
        }
    );
}


////////////
// LAB 10 //
////////////

exports.Insert = function(title, tagline, genres, callback) {
    var values = [title, tagline];
    connection.query('INSERT INTO game (title, tagline) VALUES (?, ?)', values,
        function (err, result) {

            if (err == null && genres != null) {
                var genre_qry_values = [];

                if(genres instanceof Array) {
                    for (var i = 0; i < genres.length; i++) {
                        genre_qry_values.push([result.insertId, genres[i]]);
                    }
                }
                else {
                    genre_qry_values.push([result.insertId, genres]);
                }

                console.log(genre_qry_values);

                var genre_qry = 'INSERT INTO game_genre (game_id, genre_id) VALUES ?';

                connection.query(genre_qry, [genre_qry_values], function(err, genre_result){
                    if(err) {
                        Delete(result.insertId, function() {
                            callback(err);
                        });
                    }
                    else {
                        callback(err);
                    }
                });
            }
            else {
                callback(err);
            }
        });
}



var DeletegameGenres = function(game_id, callback) {
    var genre_qry = 'DELETE FROM game_genre WHERE game_id = ?';
    connection.query(genre_qry, game_id, function (err, result) {
        callback(err, result);
    });
};

var AddgameGenres = function(game_id, genre_ids, callback) {
    if (genre_ids != null) {
        var genre_qry_values = [];

        if (genre_ids instanceof Array) {
            for (var i = 0; i < genre_ids.length; i++) {
                genre_qry_values.push([game_id, genre_ids[i]]);
            }
        }
        else {
            genre_qry_values.push([game_id, genre_ids]);
        }

        var genre_qry = 'INSERT INTO game_genre (game_id, genre_id) VALUES ?';
        connection.query(genre_qry, [genre_qry_values], function (err) {
            callback(err);
        });
    }
};

exports.Update = function(game_id, title, tagline, genre_ids, callback) {
    console.log(game_id, title, tagline, genre_ids);
    var values = [title, tagline, game_id];

    connection.query('UPDATE game SET title = ?, tagline = ? WHERE game_id = ?', values,
        function(err, result){
            if(err) {
                console.log(this.sql);
                callback(err, null);
            }
            else {
                // delete all the existing genres for the game first
                DeletegameGenres(game_id, function(err, result) {
                    //then add them back in.
                    AddgameGenres(game_id, genre_ids, callback);
                });
            }
        });
}

var Delete = function(game_id, callback) {
//function Delete(game_id, callback) {
    // First delete game genres so there aren't foreign key constraint fails
    //DeletegameGenres (game_id, callback);
    var qry = 'DELETE FROM game WHERE game_id = ?';
    connection.query(qry, [game_id],
        function (err) {
            callback(err);
        });
}

exports.DeleteById = Delete;
exports.DeletegameGenreById = DeletegameGenres;