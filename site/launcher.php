<?php
    header('Content-Type: text/html; charset=cp1251');
	define('INCLUDE_CHECK',true);
	include("connect.php");
	include_once("loger.php");
	include_once("uuid.php");
	include_once("security.php");
    @$x  = $_POST['action'];
    @$x = str_replace(" ", "+", $x);
    @$yd = Security::decrypt($x, $key2);
    @list($action, $client, $login, $postPass, $launchermd5, $ctoken) = explode(':', $yd);
	
	try {
		
	if (!preg_match("/^[a-zA-Z0-9_-]+$/", $login) || !preg_match("/^[a-zA-Z0-9_-]+$/", $action)) {
	
		exit(Security::encrypt("errorLogin<$>", $key1));
    }	
	

    if($ctoken == "null") {


	if($crypt === 'hash_md5' || $crypt === 'hash_authme' || $crypt === 'hash_xauth' || $crypt === 'hash_cauth' || $crypt === 'hash_joomla' || $crypt === 'hash_joomla_new' || $crypt === 'hash_wordpress' || $crypt === 'hash_dle' || $crypt === 'hash_launcher' || $crypt === 'hash_drupal' || $crypt === 'hash_imagecms') {
		$stmt = $db->prepare("SELECT $db_columnUser,$db_columnPass FROM $db_table WHERE $db_columnUser= :login");
		$stmt->bindValue(':login', $login);
		$stmt->execute();
		$stmt->bindColumn($db_columnPass, $realPass);
		$stmt->bindColumn($db_columnUser, $realUser);
		$stmt->fetch();
	} else if ($crypt === 'hash_ipb' || $crypt === 'hash_vbulletin' || $crypt === 'hash_punbb') {
		
		$stmt = $db->prepare("SELECT $db_columnUser,$db_columnPass,$db_columnSalt FROM $db_table WHERE $db_columnUser= :login");
		$stmt->bindValue(':login', $login);
		$stmt->execute();
		$stmt->bindColumn($db_columnPass, $realPass);
		$stmt->bindColumn($db_columnSalt, $salt);
		$stmt->bindColumn($db_columnUser, $realUser);
		$stmt->fetch();
	} else if($crypt == 'hash_xenforo') {
		
		$stmt = $db->prepare("SELECT scheme_class, $db_table.$db_columnId,$db_table.$db_columnUser,$db_tableOther.$db_columnId,$db_tableOther.$db_columnPass FROM $db_table, $db_tableOther WHERE $db_table.$db_columnId = $db_tableOther.$db_columnId AND $db_table.$db_columnUser= :login");
		$stmt->bindValue(':login', $login);
		$stmt->execute();
		$stmt->bindColumn($db_columnPass, $salt);
		$stmt->bindColumn($db_columnUser, $realUser);
		$stmt->fetch();
		$stmt->execute();
		$stmt->bindColumn($db_columnPass, $realPass);
		$stmt->bindColumn('scheme_class', $scheme_class);
		$stmt->fetch();	
		if($scheme_class==='XenForo_Authentication_Core') {
			$salt = substr($salt,105,64);
			$realPass = substr($realPass,22,64);
		} else {
			$salt = false;
			$realPass = substr($realPass,22,60);
	    }
	} else die(Security::encrypt("badhash<$>", $key1));

	$checkPass = hash_name($crypt, $realPass, $postPass, @$salt);

	if($useantibrut) {	
		$ip  = getenv('REMOTE_ADDR');	
		$time = time();
		$bantime = $time+(10);
		$stmt = $db->prepare("SELECT sip,time FROM lnch_sip WHERE sip='$ip' AND time>'$time'");
		$stmt->execute();
		$row = $stmt->fetch(PDO::FETCH_ASSOC);
		$real = $row['sip'];
		if($ip == $real) {
			$stmt = $db->prepare("DELETE FROM lnch_sip WHERE time < '$time';");
			$stmt->execute();
			exit(Security::encrypt("temp<$>", $key1));
		}
		
		if ($login !== $realUser) {
			$stmt = $db->prepare("INSERT INTO lnch_sip (sip, time) VALUES ('$ip', '$bantime')");
			$stmt->execute();
			exit(Security::encrypt("errorLogin<$>", $key1));
		}
		if(!strcmp($realPass,$checkPass) == 0 || !$realPass) {
			$stmt = $db->prepare("INSERT INTO lnch_sip (sip, time) VALUES ('$ip', '$bantime')");
			$stmt->execute();
			exit(Security::encrypt("errorLogin<$>", $key1));
		}

    } else {
		if ($login !== $realUser) {
			exit(Security::encrypt("errorLogin<$>", $key1)); }
		if(!strcmp($realPass,$checkPass) == 0 || !$realPass) die(Security::encrypt("errorLogin<$>", $key1));
    }}

        if($ctoken == "null") {
         	$acesstoken = token();
        } else {
         	$acesstoken = $postPass;
        }
		$sessid = token();
        $stmt = $db->prepare("SELECT id, user, token FROM lnch_usersession WHERE user= :login");
		$stmt->bindValue(':login', $login);
		$stmt->execute();
		$rU = $stmt->fetch(PDO::FETCH_ASSOC);
		if($rU['user'] != null) {
            $realUser = $rU['user'];
		}

        if($ctoken != "null") {

		if($rU['token'] != $acesstoken ) {
	        	exit(Security::encrypt("errorLogin<$>", $key1));
			}
	    }
		if($login == $rU['user']) {
            if($ctoken == "null") {
				$stmt = $db->prepare("UPDATE lnch_usersession SET session = '$sessid', token = :token WHERE user= :login");
				$stmt->bindValue(':token', $acesstoken);
            }
            else {
            	$stmt = $db->prepare("UPDATE lnch_usersession SET session = '$sessid' WHERE user= :login");
            }
			$stmt->bindValue(':login', $login);
			$stmt->execute();
		}
		else if($ctoken == "null" || $login != $rU['user']) {
			$stmt = $db->prepare("INSERT INTO lnch_usersession (user, session, md5, token) VALUES (:login, '$sessid', :md5, '$acesstoken')");
			$stmt->bindValue(':login', $realUser);
			$stmt->bindValue(':md5', str_replace('-', '', uuidConvert($realUser)));
			$stmt->execute();
		}
	
	if($useban) {
	    $time = time();
	    $tipe = '2';
		$stmt = $db->prepare("SELECT name From $banlist WHERE name= :login AND type<'$tipe' AND temptime>'$time'");
		$stmt->bindValue(':login', $login);
		$stmt->execute();
	    if($stmt->rowCount()) {
			$stmt = $db->prepare("SELECT name,temptime From $banlist WHERE name= :login AND type<'$tipe' AND temptime>'$time'");
			$stmt->bindValue(':login', $login);
			$stmt->execute();
			$row = $stmt->fetch(PDO::FETCH_ASSOC);
			exit(Security::encrypt('Временный бан до '.date('d.m.Yг. H:i', $row['temptime'])." по времени сервера", $key1));
	    }
			$stmt = $db->prepare("SELECT name FROM $banlist WHERE name= :login AND type<'$tipe' AND temptime='0'");
			$stmt->bindValue(':login', $login);
			$stmt->execute();
		if($stmt->rowCount()) {
	      exit(Security::encrypt("Вечный бан", $key1));
	    }
	}
    
	if($action == 'auth') {

	if($checklauncher) {
	if($launchermd5 != null) {
    if($launchermd5 == @$md5launcherexe) {
		    $check = "1";
		    }
		    if($launchermd5 == @$md5launcherjar) {
		       $check = "1";
		    }
		}
		if(!@$check == "1") {
			exit(Security::encrypt("badlauncher<$>_$masterversion", $key1));
		}
	}

        if($assetsfolder)
        { $z = "/"; } else { $z = ".zip"; }

		if(!file_exists("clients/assets".$z)||!file_exists("clients/".$client."/bin/")||!file_exists("clients/".$client."/mods/")||!file_exists("clients/".$client."/coremods/")||!file_exists("clients/".$client."/config.zip"))
		die(Security::encrypt("client<$> $client", $key1));

        $md5user  = strtoint(xorencode(str_replace('-', '', uuidConvert($realUser)), $protectionKey));
        $md5zip	  = @md5_file("clients/".$client."/config.zip");
        $md5ass	  = @md5_file("clients/assets.zip");
        $sizezip  = @filesize("clients/".$client."/config.zip");
        $sizeass  = @filesize("clients/assets.zip");
		$echo1    =  "$masterversion<:>$md5user<:>".$md5zip."<>".$sizezip."<:>".$md5ass."<>".$sizeass."<br>".$realUser.'<:>'.strtoint(xorencode($sessid, $protectionKey)).'<br>'.$acesstoken.'<br>';

        if($assetsfolder) {
            echo Security::encrypt($echo1.str_replace("\\", "/",checkfiles('clients/'.$client.'/bin/').checkfiles('clients/'.$client.'/mods/').checkfiles('clients/'.$client.'/coremods/').checkfiles('clients/assets')).'<::>assets/indexes<:b:>assets/objects<:b:>assets/virtual<:b:>'.$client.'/bin<:b:>'.$client.'/mods<:b:>'.$client.'/coremods<:b:>', $key1);
        } else {
            echo Security::encrypt($echo1.
            	str_replace("\\", "/",checkfiles('clients/'.$client.'/bin/').checkfiles('clients/'.$client.'/mods/').checkfiles('clients/'.$client.'/coremods/')).'<::>'.$client.'/bin<:b:>'.$client.'/mods<:b:>'.$client.'/coremods<:b:>', $key1);
        }
  
	} else echo "Запрос составлен неверно";
	
	} catch(PDOException $pe) {
		die(Security::encrypt("errorsql<$>", $key1).$logger->WriteLine($log_date.$pe));  //вывод ошибок MySQL в m.log
	}
	//===================================== Вспомогательные функции ==================================//

	function xorencode($str, $key) {
		while(strlen($key) < strlen($str)) {
			$key .= $key;
		}
		return $str ^ $key;
	}

	function strtoint($text) {
		$res = "";
		for ($i = 0; $i < strlen($text); $i++) $res .= ord($text{$i}) . "-";
		$res = substr($res, 0, -1);
		return $res;
	}

	function hash_name($ncrypt, $realPass, $postPass, $salt) {
		$cryptPass = false;
		
		if ($ncrypt === 'hash_xauth') {
				$saltPos = (strlen($postPass) >= strlen($realPass) ? strlen($realPass) : strlen($postPass));
				$salt = substr($realPass, $saltPos, 12);
				$hash = hash('whirlpool', $salt . $postPass);
				$cryptPass = substr($hash, 0, $saltPos) . $salt . substr($hash, $saltPos);
		}

		if ($ncrypt === 'hash_md5' or $ncrypt === 'hash_launcher') {
				$cryptPass = md5($postPass);
		}

		if ($ncrypt === 'hash_dle') {
				$cryptPass = md5(md5($postPass));
		}

		if ($ncrypt === 'hash_cauth') {
				if (strlen($realPass) < 32) {
						$cryptPass = md5($postPass);
						$rp = str_replace('0', '', $realPass);
						$cp = str_replace('0', '', $cryptPass);
						(strcasecmp($rp,$cp) == 0 ? $cryptPass = $realPass : $cryptPass = false);
				}
				else $cryptPass = md5($postPass);
		}

		if ($ncrypt === 'hash_authme') {
				$ar = preg_split("/\\$/",$realPass);
				$salt = $ar[2];
				$cryptPass = '$SHA$'.$salt.'$'.hash('sha256',hash('sha256',$postPass).$salt);
		}

		if ($ncrypt === 'hash_joomla') {
				$parts = explode( ':', $realPass);
				$salt = $parts[1];
				$cryptPass = md5($postPass . $salt) . ":" . $salt;
		}
				
		if ($ncrypt === 'hash_imagecms') {
		        $majorsalt = '';
				if ($salt != '') {
					$_password = $salt . $postPass;
				} else {
					$_password = $postPass;
				}
				
				$_pass = str_split($_password);
				
				foreach ($_pass as $_hashpass) {
					$majorsalt .= md5($_hashpass);
				}
				
				$cryptPass = crypt(md5($majorsalt), $realPass);
		}

		if ($ncrypt === 'hash_joomla_new' or $ncrypt === 'hash_wordpress' or $ncrypt === 'hash_xenforo') {
		
				if($ncrypt === 'hash_xenforo' and $salt!==false) {
					return $cryptPass = hash('sha256', hash('sha256', $postPass) . $salt);
				}
				
				$itoa64 = './0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
				$cryptPass = '*0';
				if (substr($realPass, 0, 2) == $cryptPass)
					$cryptPass = '*1';

				$id = substr($realPass, 0, 3);
				# We use "$P$", phpBB3 uses "$H$" for the same thing
				if ($id != '$P$' && $id != '$H$')
					return $cryptPass = crypt($postPass, $realPass);

				$count_log2 = strpos($itoa64, $realPass[3]);
				if ($count_log2 < 7 || $count_log2 > 30)
					return $cryptPass = crypt($postPass, $realPass);

				$count = 1 << $count_log2;

				$salt = substr($realPass, 4, 8);
				if (strlen($salt) != 8)
					return $cryptPass = crypt($postPass, $realPass);

					$hash = md5($salt . $postPass, TRUE);
					do {
						$hash = md5($hash . $postPass, TRUE);
					} while (--$count);

				$cryptPass = substr($realPass, 0, 12);
				
				$encode64 = '';
				$i = 0;
				do {
					$value = ord($hash[$i++]);
					$encode64 .= $itoa64[$value & 0x3f];
					if ($i < 16)
						$value |= ord($hash[$i]) << 8;
					$encode64 .= $itoa64[($value >> 6) & 0x3f];
					if ($i++ >= 16)
						break;
					if ($i < 16)
						$value |= ord($hash[$i]) << 16;
					$encode64 .= $itoa64[($value >> 12) & 0x3f];
					if ($i++ >= 16)
						break;
					$encode64 .= $itoa64[($value >> 18) & 0x3f];
				} while ($i < 16);
				
				$cryptPass .= $encode64;

				if ($cryptPass[0] == '*')
					$cryptPass = crypt($postPass, $realPass);
		}
		
		if ($ncrypt === 'hash_ipb') {
				$cryptPass = md5(md5($salt).md5($postPass));
		}
		
		if ($ncrypt === 'hash_punbb') {
				$cryptPass = sha1($salt.sha1($postPass));
		}

		if ($ncrypt === 'hash_vbulletin') {
				$cryptPass = md5(md5($postPass) . $salt);
		}

		if ($ncrypt === 'hash_drupal') {
				$setting = substr($realPass, 0, 12);
				$itoa64 = './0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
				$count_log2 = strpos($itoa64, $setting[3]);
				$salt = substr($setting, 4, 8);
				$count = 1 << $count_log2;
				$input = hash('sha512', $salt . $postPass, TRUE);
				do $input = hash('sha512', $input . $postPass, TRUE);
				while (--$count);

				$count = strlen($input);
				$i = 0;
		  
				do {
						$value = ord($input[$i++]);
						$cryptPass .= $itoa64[$value & 0x3f];
						if ($i < $count) $value |= ord($input[$i]) << 8;
						$cryptPass .= $itoa64[($value >> 6) & 0x3f];
						if ($i++ >= $count) break;
						if ($i < $count) $value |= ord($input[$i]) << 16;
						$cryptPass .= $itoa64[($value >> 12) & 0x3f];
						if ($i++ >= $count) break;
						$cryptPass .= $itoa64[($value >> 18) & 0x3f];
				} while ($i < $count);
				$cryptPass =  $setting . $cryptPass;
				$cryptPass =  substr($cryptPass, 0, 55);
		}
		
		return $cryptPass;
	}

	    function checkfiles($path) {
        $objects = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($path), RecursiveIteratorIterator::SELF_FIRST);
        $massive = "";
		    foreach($objects as $name => $object) {
			    $basename = basename($name);
			    $isdir = is_dir($name);
			    if ($basename!="." and $basename!=".." and !is_dir($name)){
			     	$str = str_replace('clients/', "", str_replace($basename, "", $name));
			     	$massive = $massive.$str.$basename.':>'.md5_file($name).':>'.filesize($name).'<:>';
			    }
		    }
		    return $massive;
        }

        function token() {
        $chars="0123456789abcdef";
        $max=64;
        $size=StrLen($chars)-1;
        $password=null;
        while($max--)
        $password.=$chars[rand(0,$size)];

          return $password;
        }