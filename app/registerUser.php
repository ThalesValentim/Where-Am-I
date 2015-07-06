<?php 

	require 'config.php'; //makes connection to database	
	require 'functions/user.php'; //functions reference	
	
	date_default_timezone_set('America/Sao_Paulo');// brazil timezone
	
	//variables
	$deviceID = $_GET['userid'];
	$user = $_GET['username'];
	$pass = $_GET['password'];

	//check userid exist in url 
	if(!isset($deviceID) || $deviceID == '' || !isset($user) || $user == '' || !isset($pass) || $pass == ''){
		exit("Parametros inválidos");

	
	if (user::register($deviceID, $user, $pass ))
		echo "Sucesso";
	else
		echo "Erro";

?>