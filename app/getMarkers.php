<?php 
require 'config.php'; //makes connection to database
require 'functions/user.php'; //functions user reference

$response_array['status'] = 'tbd';

$mapMarkers = array();
$secret = @$_GET['secret'];

if(!isset($secret))
	exit('error');
	
user::isAuthorized($secret);


//Get most recent update for each user.
$query="SELECT 
  userupdates.id,
  userupdates.userId as usuarioID,
  Latitude,
  Longitude,
  Accuracy,
  Heading,
  dateModified
FROM userupdates
  INNER JOIN (
	SELECT userId, MAX(id) AS maxsign FROM userupdates GROUP BY userId
  ) ms ON userupdates.userId = ms.userId AND id = maxsign";
	
$result = mysql_query($query)or die($query . " -\n ".mysql_error());

$i = 0;
while ($row = @mysql_fetch_assoc($result)){
	$nome = user::getUserNameByID($row['usuarioID']);
	$foto = user::getUserPhotoByID($row['usuarioID']);
	$phpdate = strtotime( $row['dateModified'] );
	$data = date( 'd-m-Y H:i:s', $phpdate );
	$mapMarkers[$i]['lat']= $row['Latitude'];
	$mapMarkers[$i]['lng']= $row['Longitude'];
	$mapMarkers[$i]['accuracy']= $row['Accuracy'];
	$mapMarkers[$i]['heading']= $row['Heading'];
	$mapMarkers[$i]['data']= $data;
	$mapMarkers[$i]['nome'] = $nome;
	if($foto != null)
		$mapMarkers[$i]['foto'] = $foto;
	$i++;
}


$response_array['status'] = 'success';
$response_array['markers']= json_encode($mapMarkers);

echo json_encode($response_array);
mysql_close($con);

?>