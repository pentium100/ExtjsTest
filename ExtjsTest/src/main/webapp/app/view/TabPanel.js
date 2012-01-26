Ext.define('AM.view.TabPanel',{
    extend: 'Ext.tab.Panel',
    initComponent : function(){
        Ext.apply(this,{
            id: 'content-panel',
            region: 'center',
            alias: 'widget.amtabpanel',
            defaults: {
               autoScroll:true,
               bodyPadding: 10
            },
            layout : 'fit',
            autoDestroy:true,
            activeTab: 0,
            border: false,
           //plain: true,
            items: [{
              id: 'HomePage',
              title: '首页',
              iconCls:'home',
              layout: 'fit'
            }]
        });
        this.callParent(arguments);
    }
})