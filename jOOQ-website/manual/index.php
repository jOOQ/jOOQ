
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../frame.php';
function printH1() {
    print 'Manual';
}
function printSlogan() {}
function printContent() {
    global $root;
?>
<p>
<a href="">Manual</a>
</p><ol>
<li>
<a href="<?=$root?>/manual/JOOQ" title="jOOQ classes and their use">jOOQ classes and their use</a>
<ol>
<li>
<a href="<?=$root?>/manual/JOOQ/ExampleDatabase" title="The example database">The example database</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Factory" title="The factory class">The factory class</a>
</li>
</ol>
</li>
<li>
<a href="<?=$root?>/manual/META" title="Meta model source code generation">Meta model source code generation</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL" title="DSL support">DSL support</a>
</li>
<li>
<a href="<?=$root?>/manual/Advanced" title="Advanced topics">Advanced topics</a>
</li>
</ol>
<?php 
}
?>

