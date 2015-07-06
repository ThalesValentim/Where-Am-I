<?php 

	require 'config.php'; //makes connection to database	
	require 'functions/user.php';
	
	date_default_timezone_set('America/Sao_Paulo');// brazil timezone
	
	//variables
	$deviceID = $_GET['userid'];

	//check is userid exist in url 
	if(!isset($deviceID) || $deviceID == '')
		exit('Parametros inválidos');

	
	if (user::getUserID())
		echo "Sucesso";
	else
		echo "Erro";
?>