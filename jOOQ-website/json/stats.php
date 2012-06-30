<?php
header("Content-Type: text/javascript");

echo '{"sourceforge": ';
echo file_get_contents(
  "http://sourceforge.net/projects/jooq/files/stats/json?start_date=" .
   date('Y-m-d', time() - (390 * 24 * 60 * 60)) .
  "&end_date=" .
   date('Y-m-d', time() - (30 * 24 * 60 * 60)));

// Maven statistics are manually downloaded from oss.sonatype.com:
echo ', "maven": {"2011-04":"23","2011-05":"30","2011-06":"43",' .
                 '"2011-07":"73","2011-08":"80","2011-09":"89",' .
                 '"2011-10":"89","2011-11":"302","2011-12":"236",' .
                 '"2012-01":"332","2012-02":"428","2012-03":"358",' .
                 '"2012-04":"461","2012-05":"573"}}';

?>