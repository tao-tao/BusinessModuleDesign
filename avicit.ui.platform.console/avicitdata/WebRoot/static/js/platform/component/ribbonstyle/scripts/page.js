///<reference path="jquery-1.3.2-vsdoc2.js" />
///<reference path="jquery.ribbon.js" />

$().ready(function() {
    $().Ribbon({ theme: 'windows7', backStage: true });

    $('ul.ribbon-theme li').click(function() { $().Ribbon({ theme: $(this).attr('class').substring(7), backStage: true }); });
    //$('.orb').click();
});