<?php 
  $root = "";
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>jOOQ - <?php print preg_replace('%<br/>%', ' - ', getH1()); ?></title>
		<meta property="og:title" content="jOOQ" />
		<meta property="og:type" content="website" />
		<meta property="og:url" content="http://www.jooq.org" />
		<meta property="og:image" content="http://www.jooq.org/img/logo.png" />
		<meta property="og:site_name" content="jOOQ" />
		<meta property="fb:admins" content="649865547" />
		
		<meta name="description" content="jOOQ effectively combines complex SQL, typesafety, source code generation, active records, stored procedures, UDTs, and Java in a fluent API. Supported DBs are DB2, Derby, Ingres, H2, HSQLDB, MySQL, Oracle, Postgres, SQLite, SQL Server, Sybase"/>
		<meta name="author" content="Lukas Eder"/>
		<meta name="keywords" content="jOOQ, JDBC, database abstraction, source code generation, SQL, stored procedures, stored functions, UDT, UDF, typesafe, fluentAPI"/>
		
		<link href="<?=$root?>/css/jooq.css" type="text/css" rel="stylesheet" />
		<link href="<?=$root?>/js/prettify/prettify.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="<?=$root?>/js/prettify/prettify.js"></script>
		<script type="text/javascript" src="<?=$root?>/js/prettify/lang-sql.js"></script>
		<script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
	</head>
	<body onload="prettyPrint()">
		<div id="wrapper">
			<div class="block">
			    <div id="navigation">
			        <?php 
			        $menu = getActiveMenu();
			        ?>
			    	<div class="navigation-item-left <?php if ($menu == 'home') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/" title="jOOQ Home Page">Home</a>
		    		</div>
			    	<div class="navigation-item-left <?php if ($menu == 'tutorial') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/tutorial.php" title="jOOQ Tutorial : The First Steps">Tutorial</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'download') print 'navigation-item-active'?>">
			    		<a href="https://sourceforge.net/projects/jooq/files/" title="jOOQ Download">Download</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'manual') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/manual" title="jOOQ User Manual">Manual</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'javadoc') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/javadoc/latest/" title="jOOQ Main Javadoc">Javadoc</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'notes') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/notes.php" title="jOOQ Release Notes">Release Notes</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'roadmap') print 'navigation-item-active'?>">
			    		<a href="https://sourceforge.net/apps/trac/jooq/report/6" title="jOOQ Roadmap">Roadmap</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'contribute') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/contribute.php" title="Contribute to jOOQ">Contribute</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'donations') print 'navigation-item-active'?>">
			    		<a href="https://sourceforge.net/project/project_donations.php?group_id=283484" title="Donate to jOOQ, if you like it!">Donate</a>
			    	</div>
			    	<div class="navigation-item-left <?php if ($menu == 'links') print 'navigation-item-active'?>">
			    		<a href="<?=$root?>/links.php" title="Interesting links for jOOQ users">Links</a>
			    	</div>
			    </div>

				<div id="content">
				<div id="tweets">
					<div class="tweet-item">
						<a href="http://twitter.com/share" 
							class="twitter-share-button" 
							data-url="http://www.jooq.org"
							data-text="#jOOQ - A nice database abstraction library for Java" 
							data-count="horizontal" data-via="JavaOOQ">Tweet</a>
						<script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
					</div>
					<div class="tweet-item">
						<g:plusone size="medium" href="http://www.jooq.org"></g:plusone>
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
					</div>
				</div>
				
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td width="700" valign="top">
								<h1><?php print getH1(); ?></h1></td>
							<td align="right" valign="top"><img src="<?=$root?>/img/logo.png" alt="jOOQ Logo"/></td>
						</tr>
					</table>
	
					<?php 
					  $slogan = getSlogan();
					  
					  if ($slogan != '') {
					  	print '<p class="slogan">' . $slogan . '</p>';
					  }
					  printContent();
				    ?>
					<br/>
				</div>
				
				<div id="footer">
					<p class="right">jOOQ is brought to you by <a href="http://lukaseder.wordpress.com" title="Lukas's Blog about Java, SQL, and jOOQ">Lukas Eder</a>. 
					Distributed under the <a href="http://www.apache.org/licenses/LICENSE-2.0" title="Apache 2 License">Apache 2 licence</a></p>
				</div>
			</div>
		</div>
		
		<a href="https://github.com/lukaseder/jOOQ"> <img
			alt="Fork me on GitHub" src="<?=$root?>/img/forkme.png"
			style="position: absolute; top: 0; right: 0; border: 0;"/> </a>
		<div style="display: none">
			<img src="/img/logo.png" title="jOOQ Logo"/>
		</div>
	</body>
</html>
