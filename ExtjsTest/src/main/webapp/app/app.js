Ext.application({
    name: 'AM',

    appFolder: 'app',
	
	controllers: [
        'Users'
    ], 
	
	

    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            items: [
                {
                    xtype: 'userlist',
                    title: 'Users',
                    html : 'List of users will go here'
                }
            ]
            
        });
    }
});