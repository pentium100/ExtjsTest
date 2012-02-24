<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Portal Layout Sample</title>

    <link rel="stylesheet" type="text/css" href="js/extjs4/resources/css/ext-all.css" />
    <link rel="stylesheet" type="text/css" href="app/portal/portal.css" />

    
    <link rel="stylesheet" type="text/css" href="js/extjs4/examples/shared/example.css"/>
    <link rel="stylesheet" type="text/css" href="css/style.css" />

    

    <!-- shared example code -->
    

    <script type="text/javascript" src="js/extjs4/ext-all-debug.js"></script>
    <script type="text/javascript" src="js/extjs4/examples/shared/examples.js"></script>
    <script type="text/javascript" src="app/config.js"></script>
    
 
    
    
    <script type="text/javascript">
        
        Ext.Loader.setPath('AM', 'app');
        Ext.require([
            'Ext.layout.container.*',
            'Ext.resizer.Splitter',
            'Ext.fx.target.Element',
            'Ext.fx.target.Component',
            'Ext.window.Window',
            'AM.portal.classes.Portlet',

            'AM.portal.classes.PortalColumn',
            'AM.portal.classes.PortalPanel',
            
            'AM.portal.classes.PortalDropZone'
        ]);
	
	_DEFAULT_USER_NAME = '${userName}';



    </script>
    
    <script type="text/javascript" src="app/app_portal.js"></script>
    
</head>
<body>
    <span id="app-msg" style="display:none;"></span>
</body>
</html>

