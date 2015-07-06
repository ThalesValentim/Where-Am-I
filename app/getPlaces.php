<?php 
	/* Thales Valentim 2013
		get inside places from db service
	*/
	
	require 'config.php'; //makes connection to database	
	require 'functions/user.php'; //functions user reference	
	require 'functions/location.php'; //functions locations reference	
	
	date_default_timezone_set('America/Sao_Paulo');// brazil timezone
	
	//variables
	$placeList = array();
	$userPosition = array();
	$deviceID = $_GET['userid'];
	$atualInsidePlaces = location::getInsidePlaces();

	//check userid exist in url 
	if(!isset($deviceID) || $deviceID == '')
		exit('Parametros inválidos');

	echo json_encode(getInsidePlaces());

?>