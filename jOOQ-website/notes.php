<?php 
require 'frame.php';
function printH1() {
	print 'The history of jOOQ<br/>From 2009 to ' . date('Y');
}
function getSlogan() {
	return "jOOQ has come a long way. The community is growing as features are being added
			in the beginning, jOOQ was no more than type-safe querying for simple statements. See how
			jOOQ is growing to support almost all SQL constructs by 12 different RDBMS";
}
function getActiveMenu() {
	return "notes";
}
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
		else if (trim($contents[$i]) == '' && substr($contents[$i + 1], 0, 1) != '-') {
			print '<p>';
		}
		
		// Create an <ul> from a list of "dashed" elements 
		else if (substr($contents[$i], 0, 1) == '-') {
			print '<ul>';
			
			while (trim($contents[$i]) != '') {
				print '<li>';
				print preg_replace('%-\s+(.*)%', '$1', $contents[$i]);
				
				while (trim($contents[++$i]) != '' && substr($contents[$i], 0, 1) != '-') {
					print htmlentities($contents[$i]);
				}
				
				print '</li>';
			}
			
			print '</ul><p>';
		}
		
		// Create a ticket-table from a list of "hashed" elements
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
				print htmlentities(preg_replace('%#\d+\s+-\s+(.*)%', '$1', $contents[$i]));
				
				while (trim($contents[++$i]) != '' && substr($contents[$i], 0, 1) != '#') {
					print htmlentities($contents[$i]);
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
	$value = htmlentities($value);
	$value = preg_replace('%(https?://\S+)%', '<a href="$1">$1</a>', $value);
	return $value;
}
?>