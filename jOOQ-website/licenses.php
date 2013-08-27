<?php
require 'frame.php';
function getH1() {
	return 'Licensing';
}
function getActiveMenu() {
	return "support";
}
function printContent() {
	global $root;
    global $minorVersion;
	global $version;
?>

<h2>Licensing</h2>
<p>
	Come back soon to learn how to commercially license jOOQ.
</p>

<?php
}
?>