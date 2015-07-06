<?php
class user {

	public static function register($deviceID, $user, $pass){
		$query = "SELECT id FROM `users` WHERE deviceId = '".$deviceID."'";
		$query = "INSERT into users 
		(id, Nome, Senha, passcode, deviceId, activated, advancedAccount, Foto, DataCadastro)
		VALUES (NULL, '".$user."', '".$pass."', '12345', '".$deviceID."', 1, 0 , NULL, NOW())";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		
		if($result)
			return true;
		else
			return false;
	}

	public static function getUserID(){
		$deviceID = $_GET['userid'];
		$query = "SELECT id FROM `users` WHERE deviceId = '".$deviceID."'";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_fetch_array($result);
		return $row['id'];  
	}
	public static function getlastUserPosition(){
		//get most recent position update from user -- thales 17/07/2014
		$query="SELECT 
		  userupdates.id,
		  Latitude,
		  Longitude,
		  Accuracy,
		  Heading
		FROM userupdates
		  WHERE userID = ". self::getUserID()." ORDER by id DESC limit 1 ";
			
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_fetch_row($result);
		$userPosition = array(
			'Latitude' => substr($row[1], 0, 8),
			'Longitude' => substr($row[2], 0, 8)
		);
		
		return $userPosition;
	}
	
	//function to get user name from user id
	public static function getUserNameByID($id){
		$query="SELECT Nome from users WHERE id = ".$id."";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_fetch_array($result);
		return $row[0];
	}

	//function to get user photo from user id
	public static function getUserPhotoByID($id){
		$query="SELECT Foto from users WHERE id = ".$id."";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_fetch_array($result);
		return $row[0];
	}
	
	//make sure secret is set to stop random requests.
	public static function isAuthorized($secret){		
		if($secret != PAGE_SECRET){
			die("unauthorized, secret does not match.");
			$response_array['message'] = 'unauthorized, secret does not match';	
			$response_array['status'] = 'error';
		}
	}
}