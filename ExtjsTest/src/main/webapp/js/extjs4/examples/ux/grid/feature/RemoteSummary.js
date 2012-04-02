Ext.define('Ext.ux.grid.feature.RemoteSummary', {
    extend : 'Ext.grid.feature.Summary',
    alias  : 'feature.remotesummary',

    generateSummaryData: function() {
        var me         = this,
            data       = {},
            remoteData = {},
            store      = me.view.store,
            reader     = store.proxy.reader,
            columns    = me.view.headerCt.getColumnsForTpl(),
            i          = 0,
            length     = columns.length,
            root, comp;

        if (me.remoteRoot && reader.rawData) { 
            // reset reader root and rebuild extractors to extract summaries data
            root        = reader.root;
            reader.root = me.remoteRoot;

            reader.buildExtractors(true);

            remoteData  = reader.getRoot(reader.rawData); 

            // restore initial reader configuration
            reader.root = root;
            reader.buildExtractors(true);
        }

        for (; i < length; i++) {
            comp          = columns[i];
            data[comp.id] = me.getSummary(store, comp.summaryType, comp.dataIndex, false);

            if (remoteData.hasOwnProperty(comp.dataIndex)) {
                data[comp.id] = remoteData[comp.dataIndex];
            }
        }

        return data;
    }
});