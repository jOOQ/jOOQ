<?php
  $root = "";
  $minorVersion = "2.5";
  $version = $minorVersion . ".2";
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title><?php
		if (isset($home)) {
		  print "jOOQ is a fluent API for typesafe SQL query construction and execution";
		} else {
		  print getH1();
		}
		?></title>
		<meta property="og:title" content="jOOQ" />
		<meta property="og:type" content="website" />
		<meta property="og:url" content="http://www.jooq.org" />
		<meta property="og:image" content="http://www.jooq.org/img/logo.png" />
		<meta property="og:site_name" content="jOOQ" />
		<meta property="fb:admins" content="649865547" />

		<meta name="description" content="jOOQ, a fluent API for typesafe SQL query construction and execution."/>
		<meta name="author" content="Lukas Eder"/>
		<meta name="keywords" content="jOOQ, JDBC, database abstraction, source code generation, SQL, stored procedures, stored functions, UDT, UDF, typesafe, fluentAPI"/>

		<link href='http://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>
		<link href='http://fonts.googleapis.com/css?family=Special+Elite' rel='stylesheet' type='text/css'>
		<link href="<?=$root?>/css/jooq.css" type="text/css" rel="stylesheet" />
		<link href="<?=$root?>/js/prettify/prettify.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="<?=$root?>/js/jquery.js"></script>
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script type="text/javascript" src="<?=$root?>/js/prettify/prettify.js"></script>
		<script type="text/javascript" src="<?=$root?>/js/prettify/lang-sql.js"></script>
		<script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>

		<?php if (strpos($_SERVER['HTTP_HOST'], 'localhost') === false) { ?>
		<script type="text/javascript">
		  var _gaq = _gaq || [];
		  _gaq.push(['_setAccount', 'UA-30716479-1']);
		  _gaq.push(['_trackPageview']);

		  (function() {
		    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		  })();

		</script>
		<!-- Start of Woopra Code -->
		<script type="text/javascript">
		function woopraReady(tracker) {
			tracker.setDomain('jooq.org');
			tracker.setIdleTimeout(300000);
			tracker.track();
			return false;
		}
		(function() {
			var wsc = document.createElement('script');
			wsc.src = document.location.protocol+'//static.woopra.com/js/woopra.js';
			wsc.type = 'text/javascript';
			wsc.async = true;
			var ssc = document.getElementsByTagName('script')[0];
			ssc.parentNode.insertBefore(wsc, ssc);
		})();
		</script>
		<!-- End of Woopra Code -->
		<?php } ?>

		<script>
		$(document).ready(function() {
		    $("a").filter("[href*='http']").filter(":not([href*='jooq.org'])").attr("target", "_blank");
		});
		</script>
	</head>
	<body onload="prettyPrint()">
	    <div id="navigation">
	    <div class="wrapper">
		<div id="tweets">
			<div class="tweet-item">
				<a href="http://twitter.com/share"
					class="twitter-share-button"
					data-url="http://www.jooq.org"
					data-text="#jOOQ - A nice database abstraction library for Java"
					data-count="horizontal" data-via="JavaOOQ">Tweet</a>
				<script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
				&nbsp;
			</div>
			<div class="tweet-item">
				<g:plusone size="medium" href="http://www.jooq.org"></g:plusone>
				&nbsp;
			</div>
			<div class="tweet-item">
				<div id="fb-root"></div>
				<script>(function(d, s, id) {
				  var js, fjs = d.getElementsByTagName(s)[0];
				  if (d.getElementById(id)) {return;}
				  js = d.createElement(s); js.id = id;
				  js.src = "//connect.facebook.net/en_US/all.js#appId=232666253447462&xfbml=1";
				  fjs.parentNode.insertBefore(js, fjs);
				}(document, 'script', 'facebook-jssdk'));</script>
				<div class="fb-like"
					data-send="false"
					data-href="http://www.jooq.org"
					data-layout="button_count"
					data-width="450"
					data-show-faces="true"
					data-font="verdana"></div>
					&nbsp;
			</div>
			<div class="tweet-item">
				<script src="//platform.linkedin.com/in.js" type="text/javascript"></script>
				<script type="IN/Share" data-url="http://www.jooq.org" data-counter="right"></script>
			</div>
		</div>

	    <div id="menu">
	        <?php
	        $menu = getActiveMenu();
	        ?>
	        <div class="navigation-item-left <?php if ($menu == 'home') print 'navigation-item-active'?>">
	    		<a href="<?=$root?>/" title="jOOQ Home Page">Start</a>
    		</div>
	        <div class="navigation-item-left <?php if ($menu == 'learn') print 'navigation-item-active'?>">
	    		<a href="<?=$root?>/learn.php" title="Learn about jOOQ">Learn</a>
    		</div>
	        <div class="navigation-item-left <?php if ($menu == 'download') print 'navigation-item-active'?>">
	    		<a href="<?=$root?>/download.php" title="Download jOOQ">Download</a>
    		</div>
	        <div class="navigation-item-left <?php if ($menu == 'community') print 'navigation-item-active'?>">
	    		<a href="<?=$root?>/community.php" title="See who's behind jOOQ and contribute">Community</a>
    		</div>
	   	</div>
	   	</div>
	    </div>

		<div id="header">
		<div class="wrapper">
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td width="700" valign="top">
						<h1><?php print getH1(); ?></h1></td>
					<td align="right" valign="top"><img src="<?=$root?>/img/logo.png" alt="jOOQ Logo"/></td>
				</tr>
			</table>
		</div>
		</div>

		<div id="content">
		<div class="wrapper">
			<?php
			  printContent();
		    ?>
			<br/>
		</div>
		</div>

		<div id="footer">
		<div class="wrapper">
	 		<p class="right">
				<div style="float: left; width: 400px">
				    Copyright (c) 2009-<?=date('Y')?> by <a href="http://blog.jooq.org" title="Lukas's Blog about Java, SQL, and jOOQ">Lukas Eder</a>.
				    Distributed under the <a href="http://www.apache.org/licenses/LICENSE-2.0" title="Apache 2.0 License">Apache 2.0 licence</a>
				</div>
				<div style="float: right; width: 400px">
					Want to see your brand here? <a href="mailto:lukas.eder@gmail.com" title="contact me">Contact me</a> to partner up with jOOQ
				</div>
				<br/>
			</p>
		</div>
		</div>

		<a href="https://github.com/jOOQ/jOOQ"> <img
			alt="Fork me on GitHub" src="<?=$root?>/img/forkme.png"
			style="position: absolute; top: 0; right: 0; border: 0;"/> </a>
		<div style="display: none">
			<img src="/img/logo.png" alt="The jOOQ Logo" title="jOOQ Logo"/>
	</body>
</html>
