<?php
require 'frame.php';
function getH1() {
	return 'Show us some love';
}
function getActiveMenu() {
	return "donate";
}
function printContent() {
	global $root;
    global $minorVersion;
	global $version;
?>

<h2>Get your geeky jOOQ t-shirt or mug from the shop</h2>
<p>
Get some of the jOOQ web shop's fancy merchandise and donate between 7&euro; and 12&euro; with your purchase. Happy shopping!
</p>
<p>
<a href="http://www.shirtcity.com/shop/jooq/" title="The jOOQ t-shirt and mug shop for true SQL nerds">http://www.shirtcity.com/shop/jooq/</a>
</p>

<h2>Donate directly through SourceForge / PayPal</h2>
<p>
If you don't need any merchandise, or if you want to donate a bigger amount, feel free to do so directly here:
</p>
<p>
<a href="https://sourceforge.net/p/jooq/donate" title="Donate to jOOQ">https://sourceforge.net/p/jooq/donate</a>
</p>
<p>
Your donations are highly valued and make up for the numerous amounts of hours (and love) put into jOOQ. It also helps buying those more and more demanding machines that can run 14 RDBMS in parallel, for integration tests :-)
</p>
<?php
}
?>