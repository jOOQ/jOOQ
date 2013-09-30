<?php
require 'frame.php';
function getH1() {
	return 'What\'s new around jOOQ';
}
function getActiveMenu() {
	return "news";
}
function printContent() {
	global $root;
    global $minorVersion;
	global $version;
?>


<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td colspan="3">
    <h2>Upcoming jOOQ events</h2>
</td>
</tr>
<tr>
    <th width="200" class="right">Date</th>
    <th width="40" class="right">Language</th>
    <th class="right">Event</th>
</tr>
<tr>
    <td class="right">October 2013</td>
    <td class="right">English</td>
    <td class="right">Hear about jOOQ and about lots of other good stuff at the <a href="http://www.medit-symposium.com">MEDIT Symposium</a> in Sicilly.</td>
</tr>
<td colspan="3">
    <h2>Past jOOQ events</h2>
</td>
<tr>
    <td class="right">September 2013</td>
    <td class="right">German</td>
    <td class="right">Join the jOOQ introductory sessions at the <a href="http://www.jugh.de/display/jugh/2013/08/06/JUGH-Treffen+26.+September+2013">JUGH</a> in Kassel, Germany</td>
</tr>
<tr>
    <td class="right">September 2013</td>
    <td class="right">German</td>
    <td class="right">Join the jOOQ training sessions at the <a href="http://www.ch-open.ch/wstage/workshop-tage/2013/aktuelles-programm-2013/ws-6-mit-jooq-und-opensource-datenbanken-sofort-produktiv-werden/">/ch/open workshop days</a> in Zurich, Switzerland</td>
</tr>
<tr>
    <td class="right">June 2013</td>
    <td class="right">German</td>
    <td class="right">Join the jOOQ introductory session at the <a href="http://www.jug.ch/html/events/2013/jooq_lu.html">JUGS</a> in Lucerne, Switzerland</td>
</tr>
<tr>
    <td class="right">July 2012</td>
    <td class="right">German</td>
    <td class="right">Join the jOOQ introductory session at the <a href="http://www.jug.ch/html/events/2012/jooq.html">JUGS</a> in Zurich, Switzerland</td>
</tr>

</table>

<?php
}
?>