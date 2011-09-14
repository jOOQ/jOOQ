<?php 
require 'frame.php';
function printSlogan() {}
function printContent() {
	$contents = file('inc/RELEASENOTES.txt');

	for ($i = 0; $i < count($contents); $i++) {
		if ($i + 1 < count($contents) && substr($contents[$i + 1], 0, 3) == '===') {
			print '<h2>';
			print $contents[$i];
			print '</h2>';
		}
		else if ($i + 1 < count($contents) && substr($contents[$i + 1], 0, 3) == '---') {
			print '<h3>';
			print $contents[$i];
			print '</h3>';
		}
		
		else if (substr($contents[$i], 0, 3) == '===') {
			// Skip
		}
		else if (substr($contents[$i], 0, 3) == '---') {
			// Skip
		}
		else if (trim($contents[$i]) == '') {
			print '<p>';
		}
		else if (substr($contents[$i], 0, 1) == '#') {
			print '<table class="ticket-table">';
			
			while (trim($contents[$i]) != '') {
				$ticket = preg_replace('%#(\d+).*%', '$1', $contents[$i]);
				
				print '<tr>';
				print '<td width="80">';
				print '<a href="https://sourceforge.net/apps/trac/jooq/ticket/' . $ticket . '">#';
				print $ticket;
				print '</a>';
				print '</td>';
				print '<td>';
				print preg_replace('%#\d+\s+-\s+(.*)%', '$1', $contents[$i]);
				
				while (trim($contents[++$i]) != '' && substr($contents[$i], 0, 1) != '#') {
					print $contents[$i];
				}
				
				print '</td>';
				print '</tr>';
			}
			
			print '</table>';
		}
		else {
		    print markup($contents[$i]);
		}
	}
}

function markup($value) {
	$value = preg_replace('%(https?://\S+)%', '<a href="$1">$1</a>', $value);
	return $value;
}
?>