<?php
header("Content-Type: text/javascript");

echo file_get_contents(
  "http://sourceforge.net/projects/jooq/files/stats/json?start_date=" .
   date('Y-m-d', time() - (360 * 24 * 60 * 60)) .
  "&end_date=" .
   date('Y-m-d'));
?>