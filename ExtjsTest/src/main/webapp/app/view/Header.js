Ext.define('AM.view.Header', {
    extend: 'Ext.Component',
    initComponent: function() {
        Ext.applyIf(this, {
            xtype: 'box',
            cls: 'header',
            region: 'north',
            html: '<h1>内部信息中心</h1>',
            height: 30
        });
        this.callParent(arguments);
    }
});