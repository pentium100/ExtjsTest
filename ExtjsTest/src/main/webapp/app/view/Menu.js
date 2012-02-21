Ext.define('AM.view.Menu',{
    extend: 'Ext.tree.Panel',
    initComponent : function(){
        Ext.apply(this,{
            id: 'menu-panel',
            alias: 'widget.ammenu',
            title: '系统菜单',
            iconCls:'home',
            margins : '0 0 -1 1',
            region:'west',
            border : false,
            enableDD : false,
            split: true,
            width : 212,
            minSize : 130,
            maxSize : 300,
            rootVisible: false,
            containerScroll : true,
            collapsible : true,
            autoScroll: false,
            store:Ext.create('AM.store.Menus')
        });
        this.callParent(arguments);
    }
})