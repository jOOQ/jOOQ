<?php
/**
 * Super-simple, minimum abstraction MailChimp API v2 wrapper
 * 
 * Requires curl (I know, right?)
 * This probably has more comments than code.
 * 
 * @author Drew McLellan <drew.mclellan@gmail.com>
 * @version 1.0
 */
class MailChimp
{
    private $api_key;
    private $api_endpoint = 'https://<dc>.api.mailchimp.com/2.0/';



    /**
     * Create a new instance
     * @param string $api_key Your MailChimp API key
     */
    function __construct($api_key)
    {
        $this->api_key = $api_key;
        list(, $datacentre) = explode('-', $api_key);
        $this->api_endpoint = str_replace('<dc>', $datacentre, $this->api_endpoint);
    }




    /**
     * Call an API method. Every request needs the API key, so that is added automatically -- you don't need to pass it in.
     * @param  string $method The API method to call, e.g. 'lists/list'
     * @param  array  $args   An array of arguments to pass to the method. Will be json-encoded for you.
     * @return array          Associative array of json decoded API response.
     */
    public function call($method, $args=array())
    {
        return $this->_raw_request($method, $args);
    }




    /**
     * Performs the underlying HTTP request. Not very exciting
     * @param  string $method The API method to be called
     * @param  array  $args   Assoc array of parameters to be passed
     * @return array          Assoc array of decoded result
     */
    private function _raw_request($method, $args=array())
    {      
        $args['apikey'] = $this->api_key;
        $url = $this->api_endpoint.'/'.$method.'.json';

        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_TIMEOUT, 10);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($args));
        $result = curl_exec($ch);
        
        $err = fopen('error.log', 'a');
        fwrite($err, curl_error($ch));
        fclose($err);

        curl_close($ch);
        return $result ? json_decode($result, true) : false;
    }
}