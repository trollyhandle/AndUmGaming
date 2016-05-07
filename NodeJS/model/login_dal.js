var mysql   = require('mysql');
var db  = require('./db_connection.js');

/* DATABASE CONFIGURATION */
var connection = mysql.createConnection(db.config);

exports.GetByEmail = function(email, password, callback) {
    var query = 'CALL Account_GetByEmail(?, ?)';
    var query_data = [email, password];

    connection.query(query, query_data, function(err, result) {
        if(err){
            callback(err, null);
        }
        else if(result[0].length == 1) {
            /* NOTE: Stored Procedure results are wrapped in an extra array
             * and only one user record should be returned,
             * so return only the one result
             */
            callback(err, result[0][0]);
        }
        else {
            callback(err, null);
        }
    });
};