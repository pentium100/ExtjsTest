<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
    <title>信息中心</title>

    <link rel="stylesheet" type="text/css" href="js/extjs4/resources/css/ext-all.css"/>

    <link rel="stylesheet" type="text/css" href="js/extjs4/examples/shared/example.css"/>
    <link rel="stylesheet" type="text/css" href="css/style.css" />
    <script type="text/javascript" src="js/extjs4/ext-debug.js"></script>
    <script type="text/javascript" src="js/extjs4/examples/shared/examples.js"></script>
    <script type="text/javascript" src="app/config.js"></script>
    
    <script type="text/javascript">
    
    
	Ext.Loader.setConfig({
			enabled : true
		});
	Ext.application({
			name : 'AM',
			appFolder : 'app',
			
			launch : function() {
			        
				

				var c = this.getController('AM.controller.Messages');
				//var param = {messageType:'${messageType}'};
				c.init();				
				var list = Ext.create('AM.view.message.MessageList')
				Ext.create('Ext.container.Viewport', {

							layout : 'fit',
							hideBorders : true,
							layout : 'fit',
							items : [list]

						});
						
				var proxy = list.getStore().getProxy();
				proxy.extraParams.messageType = '${messageType}';		
						

						
			}
		});
    
    
    
    </script>
    
    
</head>
<body></body>
</html>