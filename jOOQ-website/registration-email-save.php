<?php
$file = 'users.csv';

$handle = fopen('users.csv', 'a');
fwrite($handle, 
  '"' . str_replace('"', '""', $_POST['name']) . '",' .
  '"' . str_replace('"', '""', $_POST['email']) . '",' .
  '"' . str_replace('"', '""', $_POST['country']) . '",' .
  '"' . str_replace('"', '""', $_POST['company_name']) . '",' .
  '"' . str_replace('"', '""', $_POST['company_size']) . '",' .
  '"' . str_replace('"', '""', $_POST['newsletter']) . '",' .
  '"' . date('Y-m-d H:i:s') . '"' . "\n"
);
fclose($handle);
?>