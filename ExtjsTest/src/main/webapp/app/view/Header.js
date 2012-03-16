Ext.define('AM.view.Header2', {
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


Ext.define('AM.view.Header',{
    extend: 'Ext.Toolbar',
    initComponent : function(){
        Ext.apply(this,{
            
            
            region:"north",
            height:30,
            items:["<h1>内部信息中心</h1>",'->',"<a href='userdetails/logout' style='text-decoration:none;'><font color='#0000FF'>退出登录</font></a>&nbsp;&nbsp;"]
        });
        this.callParent(arguments);
    }
})