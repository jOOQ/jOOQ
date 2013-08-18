<?php
require 'access.php';
require 'MailChimp.class.php';

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

if ($_POST['newsletter'] == '1' && $_POST['email'] != '') {
    $chimp = new MailChimp($access['chimp']);
    $result = $chimp->call('lists/subscribe', array(
                    'id'                => '24ff762dd2',
                    'email'             => array('email'=>$_POST['email']),
                    'merge_vars'        => array('FNAME'=>$_POST['name']),
                    'double_optin'      => false,
                    'update_existing'   => true,
                    'replace_interests' => false,
                    'send_welcome'      => true,
              ));
}
?>