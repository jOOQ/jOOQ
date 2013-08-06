<?php
require 'conf.php';
function manualHeader($isSingle, $forVersion) {
  global $minorVersion;
  $singleSuffix = ($isSingle ? '-single-page' : '');
  
  return '<p>This version of the manual is outdated. For the latest version, follow this link: ' .
         '<a href="http://www.jooq.org/doc/' . $minorVersion . '/manual' . $singleSuffix .
         '">http://www.jooq.org/doc/' . $minorVersion . '/manual' . $singleSuffix .
         '</a>.</p>';
}

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
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        
        <meta property="og:title" content="jOOQ" />
        <meta property="og:type" content="website" />
        <meta property="og:url" content="http://www.jooq.org" />
        <meta property="og:image" content="http://www.jooq.org/img/logo.png" />
        <meta property="og:site_name" content="jOOQ" />
        <meta property="fb:admins" content="649865547" />

        <meta name="description" content="jOOQ, a fluent API for typesafe SQL query construction and execution."/>
        <meta name="author" content="Lukas Eder"/>
        <meta name="keywords" content="jOOQ, JDBC, database abstraction, source code generation, SQL, stored procedures, stored functions, UDT, UDF, typesafe, fluentAPI, fluent API, jOOQL"/>

        <link href='http://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Special+Elite' rel='stylesheet' type='text/css'>
        <link href="<?=$root?>/css/jooq.css" type="text/css" rel="stylesheet" />
        <link href="<?=$root?>/css/jquery.modal.css" type="text/css" rel="stylesheet" />
        <link href="<?=$root?>/js/prettify/prettify.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" src="<?=$root?>/js/jquery.js"></script>
        <script type="text/javascript" src="<?=$root?>/js/jquery.modal.js"></script>
        <script type="text/javascript" src="<?=$root?>/js/jquery.cookie.js"></script>
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
        <?php } ?>
        <script>
        $(document).ready(function() {
            var $registration = $('#registration'),
                 track = function(event) {
                     var pageTracker;

                     try {                     
                         pageTracker = _gat._getTracker("UA-30716479-1");
                         
                         if (data.substring(0, 4) === 'http') {
                             pageTracker._trackPageview('/external/' + url.replace(/^https?\:\/\//i, ""));
                         }
                         else {
                             pageTracker._trackPageview('/event/' + event);
                         }
                     }
                     catch (ignore) {}
                 },
                 registration = function() {
                     var $email = $('#email'),
                         $save = $('.save'),
                         $survey = $('#survey'),
                         $noThanks = $('.no-thanks'),
                         fade = function($element) {
                             var heightBefore = $element.css('height'),
                                 heightAfter;
                                 
                             $element.html("Thank you!");
                             heightAfter = $element.css('height');
                             
                             $element.css('height', heightBefore);
                             $element.animate({
                                 height: heightAfter
                             }, {
                                 duration: 500,
                                 complete: function() {
                                     $element.fadeOut(1000, function() {
                                         if (!$email.is(':visible') && !$survey.is(':visible')) {
                                             $.modal.close();
                                         }
                                     });
                                 }
                             });
                         };
                         
                     $save.click(function() {
                         $.cookie("jooq-registration-email", $('input[name=email]').val());
                         track('email/clicked');
                         $.ajax({
                             type: "POST",
                             url: '<?=$root?>/registration-email-save.php',
                             data: $("#registration-email-form").serialize(),
                             success: function(data, textStatus, jqXHR) {
                                 fade($email);
                             }
                         });
                         
                         return false;
                     });
                     
                     $survey.click(function() {
                         $.cookie("jooq-registration-survey", "clicked");
                         track('survey/clicked');
                         fade($survey);
                         window.open('http://srvy.it/15yxTuO');
                     });
                     
                     $noThanks.click(function() {
                         var $this = $(this);
                         $.cookie($this.data("cookie"), "no-thanks");
                         track($this.data("fade") + '/no-thanks');
                         fade($('#' + $this.data("fade")));
                         
                         return false;
                     });

                     if ($.cookie("jooq-registration-email")) {
                         $email.hide();
                     }

                     if ($.cookie("jooq-registration-survey")) {
                         $survey.hide();
                     }
                     
                     $registration.modal();
                 };
            
            $("a").filter("[href*='http']")
                  .filter(":not([href*='jooq.org'])")
                  .click(function() {
                      var $this = $(this);
                      $this.attr("target", "_blank");
                      track($(this).attr("href"));
                  });
                  
            if ($registration) {
                if (!$.cookie("jooq-registration-email") ||
                    !$.cookie("jooq-registration-survey")) {
                    
                    setTimeout(registration, 200);
                }
            }
        });
        </script>
    </head>
    <body onload="prettyPrint()">
        <div id="navigation">
        <div class="wrapper">
        <div id="tweets">
            <?php
            /*
            <div class="tweet-item">
                <a href="http://twitter.com/share"
                    class="twitter-share-button"
                    data-url="http://www.jooq.org"
                    data-text="#jOOQ - Embedding #typesafe #SQL in #Java"
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
            */
            ?>

            <div class="tweet-item">
                <a href="https://plus.google.com/share?url=http://www.jooq.org" target="_blank">
                    <img src="<?=$root?>/img/social-g+.png" width="37" height="27" alt="Share jOOQ on Google+" title="Share jOOQ on Google+"/></a>
            </div>
            <div class="tweet-item">
                <a href="http://www.stumbleupon.com/submit?url=http://www.jooq.org" target="_blank">
                    <img src="<?=$root?>/img/social-su.png" width="37" height="27" alt="Share jOOQ on StumbleUpon" title="Share jOOQ on StumbleUpon"/></a>
            </div>
            <div class="tweet-item">
                <a href="http://twitter.com/share?text=%23jOOQ,+the+best+way+to+write+%23SQL+in+%23Java" target="_blank">
                    <img src="<?=$root?>/img/social-tw.png" width="37" height="27" alt="Share jOOQ on Twitter" title="Share jOOQ on Twitter"/></a>
            </div>
            <div class="tweet-item">
                <a href="http://www.linkedin.com/shareArticle?mini=true&url=http://www.jooq.org" target="_blank">
                    <img src="<?=$root?>/img/social-in.png" width="37" height="27" alt="Share jOOQ on LinkedIn" title="Share jOOQ on LinkedIn"/></a>
            </div>
            <div class="tweet-item">
                <a href="http://www.facebook.com/sharer.php?u=http://www.jooq.org" target="_blank">
                    <img src="<?=$root?>/img/social-fb.png" width="37" height="27" alt="Share jOOQ on Facebook" title="Share jOOQ on Facebook"/></a>
            </div>
            
            <?php
            /*
            <div class="tweet-item">
                <a href="http://github.com/jOOQ/jOOQ" target="_blank">
                    <img src="<?=$root?>/img/social-gh.png" width="37" height="27" alt="See jOOQ on GitHub" title="See jOOQ on GitHub" style="background-color: #111"/></a>
            </div>
            <div class="tweet-item">
                <a href="http://stackoverflow.com/questions/tagged/jooq" target="_blank">
                    <img src="<?=$root?>/img/social-so.png" width="37" height="27" alt="See jOOQ on Stack Overflow" title="See jOOQ on Stack Overflow" style="background-color: #eee"/></a>
            </div>
            */
            ?>
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
            <div class="navigation-item-left <?php if ($menu == 'news') print 'navigation-item-active'?>">
                <a href="<?=$root?>/news.php" title="What's new around jOOQ">News</a>
            </div>
            <div class="navigation-item-left <?php if ($menu == 'donate') print 'navigation-item-active'?>">
                <a href="<?=$root?>/donate.php" title="Show some love to the jOOQ developer">Donate</a>
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

        <!--
        <a href="https://github.com/jOOQ/jOOQ"> <img
            alt="Fork me on GitHub" src="<?=$root?>/img/forkme.png"
            style="position: absolute; top: 0; right: 0; border: 0;"/> </a>
            -->
        <div style="display: none">
            <img src="/img/logo.png" alt="The jOOQ Logo" title="jOOQ Logo"/>
        </div>
    </body>
</html>
