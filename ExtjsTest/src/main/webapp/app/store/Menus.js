Ext.define('AM.store.Menus',{
    extend: 'Ext.data.TreeStore',
    field:['id','text','leaf','controller','iconCls','views'],
    root: { 
        expanded: true 
    }, 
    proxy: { 
        type: 'ajax', 
        url: 'menus' 
    } 
})