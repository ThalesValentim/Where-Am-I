<?php 

	require 'config.php'; //makes connection to database	
	
	date_default_timezone_set('America/Sao_Paulo');// brazil timezone
	
	//variables
	$deviceID = $_GET['userid'];

	//check is userid exist in url 
	if(!isset($deviceID) || $deviceID == ''){
		echo "Parametros inválidos";
		exit();
	}

	
	if (getUserID()){
		echo "Sucesso";
	}else{
		echo "Erro";
	}
	
	function getUserID(){
		$deviceID = $_GET['userid'];
		$query = "SELECT id FROM `users` WHERE deviceId = '".$deviceID."'";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_num_rows($result);
		if($row > 0){
			return true;
		}else{
			return false;
		}
	}
?>