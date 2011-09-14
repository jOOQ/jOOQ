<html>
	<head>
		<title>jOOQ</title>
		<link href="css/jooq.css" type="text/css" rel="stylesheet">
		<link href="js/prettify/prettify.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="js/prettify/prettify.js"></script>
		<script type="text/javascript" src="js/prettify/lang-sql.js"></script>
		<script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
	</head>
	<body onload="prettyPrint()">
		<div id="wrapper">
			<div class="block">
				<div id="tweets">
					<div class="tweet-item-left">
						<a href="http://twitter.com/share" class="twitter-share-button" data-text="jOOQ - A nice database abstraction library for Java" data-count="horizontal" data-via="lukaseder">Tweet</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
					</div>
					<div class="tweet-item-left">
						<g:plusone size="medium"></g:plusone>
					</div>
					<div class="tweet-item-left">
						<div id="fb-root"></div>
						<script>(function(d, s, id) {
						  var js, fjs = d.getElementsByTagName(s)[0];
						  if (d.getElementById(id)) {return;}
						  js = d.createElement(s); js.id = id;
						  js.src = "//connect.facebook.net/en_US/all.js#appId=232666253447462&xfbml=1";
						  fjs.parentNode.insertBefore(js, fjs);
						}(document, 'script', 'facebook-jssdk'));</script>
						<div class="fb-like" data-send="false" data-layout="button_count" data-width="450" data-show-faces="true" data-font="verdana"></div>
					</div>
				</div>
			    <div id="navigation">
			    	<div class="navigation-item-left">
			    		<a href="index.php" title="jOOQ Home Page">Home</a>
		    		</div>
			    	<div class="navigation-item-left">
			    		<a href="https://sourceforge.net/projects/jooq/files/" title="jOOQ Download">Download</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="manual.php" title="jOOQ User Manual">Manual</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="http://jooq.sourceforge.net/javadoc/latest/" title="jOOQ Main Javadoc">Javadoc</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="notes.php" title="jOOQ Release Notes">Release Notes</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="https://sourceforge.net/apps/trac/jooq/report/6" title="jOOQ Roadmap">Roadmap</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="contribute.php" title="Contribute to jOOQ">Contribute</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="https://sourceforge.net/project/project_donations.php?group_id=283484" title="Donate to jOOQ, if you like it!">Donate</a>
			    	</div>
			    	<div class="navigation-item-left">
			    		<a href="links.php" title="Interesting links for jOOQ users">Links</a>
			    	</div>
			    </div>

				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td width="700">
							<h1>jOOQ : A peace treaty between SQL and Java</h1></td>
						<td align="right"><img src="img/logo.png" alt="jOOQ Logo"/></td>
					</tr>
				</table>

				<?php printSlogan(); ?>
				<?php printContent(); ?>
				
				<br/>
				<br/>
			</div>
		</div>
		
		<a href="https://github.com/lukaseder/jOOQ"> <img
			alt="Fork me on GitHub" src="img/forkme.png"
			style="position: absolute; top: 0; right: 0; border: 0;"> </a>
	</body>
</html>
