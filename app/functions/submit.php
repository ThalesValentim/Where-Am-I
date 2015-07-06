<?php 
class Submit {

	static $response_array = array('advanced'=>0,'message'=>'','status'=>'');
	static $userId = 0;
	static $deviceId  = 0;
	static $passcode = 12345 ;
	static $marker = 0;
	static $latitude = 0;
	static $longitude = 0;
	static $accuracy = 0;
	static $heading = 0;

	
	static public function processSubmit(){	
		global $con;	
		
		self::getParams();
		self::$userId = self::getUserId(self::$deviceId, self::$passcode);
		
		
		//check if user is authorized:
		if (self::$userId  != -1){
			//check if is like the last position in db
			if(!self::isSamePosition()){
				self::addLocationToDB();
			}else{
				self::updateOnlyPositionDate();
				//self::$response_array['message'] = 'same position';	
			}
		}
		else{
			self::$response_array['message'] = 'not authorized';	
		}

		echo json_encode(self::$response_array);
		mysql_close($con);
	}



	/**
	* Return int of userId or -1 if not valid
	*/
	static function getUserId($deviceId, $passcode){
		
		if($passcode=='' || !is_numeric($passcode)){
			return -1;
		}
		
		//retrieve entry with that passcode	
		$query="SELECT id, deviceId, advancedAccount FROM `users` WHERE deviceId =  '".$deviceId."' LIMIT 0,1";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());								
		
		if(is_resource($result) && mysql_num_rows($result) > 0 ){
			$row = mysql_fetch_array($result);
			self::$response_array['advanced'] = $row['advancedAccount'];
			
			//New entry
			if ($row['deviceId'] == NULL) {
				//set users.deviceId and return true
				$query="UPDATE users SET deviceId = ".$deviceId." WHERE id =  ".$row['id'];
				$result = mysql_query($query)or die($query . " -\n ".mysql_error());			
				return $row['id'];
			}
			else{ // already activated
				if ($row['deviceId'] == $deviceId){

					return $row['id'];
				}
				else{
					return -1;
				}
			}
		}
		else{
			return -1;
		}
	}
		

	static function addLocationToDB(){		
	
		$query='INSERT INTO `userupdates`(`id`, userId, Latitude, Longitude, Accuracy, Heading, jsonString, dateModified)
					VALUES ("", '.self::$userId .',"'.self::$latitude.'","'.self::$longitude.'","'.self::$accuracy.'","'.self::$heading.'",NULL,CONVERT_TZ(NOW(), "+00:00","-00:00"))';
					
		$result = mysql_query($query)or die($query."   - ".mysql_error());
		self::$response_array['id'] = mysql_insert_id();
		self::$response_array['message'] = 'Successful update.';
		self::$response_array['status'] = 'success';

	}
	
	static function updateOnlyPositionDate(){		
	
		$query='UPDATE `userupdates` set dateModified = CONVERT_TZ(NOW(), "+00:00","-00:00") WHERE userId = '.self::$userId .' ORDER by id DESC limit 1';
					
		$result = mysql_query($query)or die($query."   - ".mysql_error());
		self::$response_array['id'] = mysql_insert_id();
		self::$response_array['message'] = 'Same position, date successful updated.';
		self::$response_array['status'] = 'success';

	}
	
	
	static function isSamePosition(){
		self::getParams();
		self::$userId = self::getUserId(self::$deviceId, self::$passcode);
		//Get most recent update for selected user and return false or true -- thales 1707/2014
		$query="SELECT 
		  userupdates.id,
		  Latitude,
		  Longitude,
		  Accuracy,
		  Heading
		FROM userupdates
		  WHERE userID = ".self::$userId." ORDER by id DESC limit 1 ";
			
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_fetch_row($result);
		$num_row = mysql_num_rows($result);
		if($num_row > 0){
			$row_latitude = substr($row[1], 0, 10);
			$row_longitude = substr($row[2], 0, 10);
			$get_latitude = substr(self::$latitude, 0, 10);
			$get_longitude = substr(self::$longitude, 0, 10);
			if($row_latitude == $get_latidue || $get_longitude == $row_longitude)
				return true;
			else
				return false;
		}else{
			return false;
		}
	}
	
	static function getParams(){
		global $con;	

		if (isset($_GET['userid']) 
			&& isset($_GET['latitude']) 
			&& isset($_GET['longitude']) 
			&& isset($_GET['accuracy']) 
			&& isset($_GET['heading'])){
			
			self::$deviceId = $_GET['userid'];
			self::$latitude = substr($_GET['latitude'], 0, 10);
			self::$longitude = substr($_GET['longitude'], 0, 10);
			self::$accuracy = $_GET['accuracy'];
			self::$heading = $_GET['heading'];

		}
		else{
			self::$response_array['message'] = 'missing params';	
			echo json_encode(self::$response_array);
			mysql_close($con);
			die();
		}
	}
}