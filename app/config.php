<?php 
header('Access-Control-Allow-Origin: *'); //cors (use in mobile app service)

// Configrable Values
define('NUM_USERS', '2');			// # of users to be added to DB
define('PASSCODE_RANGE', '100');	// unique passcodes will use a random number in this range 
define('PAGE_SECRET', '12345');	// secret that map.js will post with getMarker calls

// Login Database details
$dbName='thales_gps';
$dbUser='thales_gps';
$dbPwd='M1M2M3@';


// Setup Database Connection
$con = mysql_connect("localhost",$dbUser,$dbPwd);
if (!$con)
	die('Could not connect: ' . mysql_error());

// Set the active mySQL database
$db_selected =mysql_select_db($dbName, $con);
if (!$db_selected)
	die ("Can\'t use db : " . mysql_error());

?>