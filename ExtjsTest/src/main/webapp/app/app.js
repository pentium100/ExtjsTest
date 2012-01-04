Ext.application({
    name: 'AM',

    appFolder: 'app',
	
	controllers: [
        'Users', 'Contracts'
    ], 
	
	

    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            items: [
                {
                    xtype: 'contractList',
                    title: 'Contracts',
                    html : 'List of users will go here'
                }
            ]
            
        });
    }
});