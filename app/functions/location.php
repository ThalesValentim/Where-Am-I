<?php
class location {

	//funciton to calcule distance between two positions
	function distance($lat1, $lon1, $lat2, $lon2, $unit) {
		$theta = $lon1 - $lon2;
		$dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
		$dist = acos($dist);
		$dist = rad2deg($dist);
		$miles = $dist * 60 * 1.1515;
		$unit = strtoupper($unit);

		if ($unit == "K") {
			return ($miles * 1.609344);
		}else if($unit == "N") {
			return ($miles * 0.8684);
		}else{
			return $miles;
		}
	}

	function getInsidePlaces(){
		$insidePlaces = array();
		//get a list of inside places fom last user position
		$currentUserPosition = self::getlastUserPosition();
		$i = 0;
		foreach (self::listPlaces() as $key => $place){
			/* echo "LAT1:".$place['Latitude'];
			echo "LNG1:".$place['Longitude'];
			echo "LAT2:".$currentUserPosition['Latitude'];
			echo "LNG2:".$currentUserPosition['Longitude'];*/
			$distanceBetweenPlace = distance($place['Latitude'], $place['Longitude'], $currentUserPosition['Latitude'], $currentUserPosition['Longitude'], "k");
			if($distanceBetweenPlace <= 2){
				if(self::getLastPositionTime() <= 0.3){ 
					$insidePlaces[0]['id'] = $place['id'];
					$insidePlaces[0]['Nome'] = $place['Nome'];
					$insidePlaces[0]['Descricao'] = $place['Descricao'];
				}
			}
			$i++;
		}
		return $insidePlaces;
	}



	function getLastPositionTime(){
		$query="SELECT dateModified FROM userupdates WHERE userID = ". user::getUserID()." ORDER by id DESC limit 1 ";
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$row = mysql_fetch_row($result);
		$mysqldate = date("Y-m-d H:i:s"); 
		$hourdiff = round((strtotime($mysqldate) - strtotime($row[0]))/3600, 1);
		return $hourdiff;	
	}


	//function to list all places from db - thales 17/07/2014
	function listPlaces(){
		$query = 'SELECT id, Nome,Latitude,Longitude,Descricao FROM places;';
		$result = mysql_query($query)or die($query . " -\n ".mysql_error());
		$i=0;
		while($row = mysql_fetch_assoc($result)) { 
			$placeList[$i]['id'] = $row['id'];
			$placeList[$i]['Nome'] = $row['Nome'];
			$placeList[$i]['Latitude'] = $row['Latitude'];
			$placeList[$i]['Longitude'] = $row['Longitude'];
			$placeList[$i]['Descricao'] = $row['Descricao'];
			$i++;
		}
		return $placeList;
	}
}