Ext.define('AM.view.South',{
    extend: 'Ext.Toolbar',
    initComponent : function(){
        Ext.apply(this,{
            id:"bottom",
            //frame:true,
            region:"south",
            height:23,
            items:["当前用户：Guest",'->',"技术支持:<a href='http://www.mhzg.net' target='_blank' style='text-decoration:none;'><font color='#0000FF'>http://www.itg.net</font></a>&nbsp;&nbsp;"]
        });
        this.callParent(arguments);
    }
})