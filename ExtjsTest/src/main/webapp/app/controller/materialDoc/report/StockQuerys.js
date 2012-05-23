Ext.require(['AM.ux.grid.feature.RemoteSummary']);
Ext.define('AM.controller.materialDoc.report.StockQuerys', {
	extend : 'Ext.app.Controller',

	views : ['materialDoc.report.StockQuery'],
	stores : ['materialDoc.report.StockQuery'],
	models : ['materialDoc.report.StockQuery'],
	selected : Ext.create('Ext.util.MixedCollection'),
	isLoading : false,

	init : function(options) {

		Ext.apply(this, options);

		this.control({

					'StockQuery button[action=submit]' : {
						click : this.submitReport
					},

					'StockQuery button[action=exportToExcel]' : {
						click : this.exportToExcel
					}
					,
					'StockQuery' : {
						// select : this.saveSelection,
						deselect : this.deleteSelection
					}

				});

		var store = this.getMaterialDocReportStockQueryStore();
		store.on('beforeload', this.saveSelection, this);
		store.on('load', this.setSelection, this);

	},

	deleteSelection : function(selModel, record, index, eOpts) {
		if (!this.isLoading) {
			this.selected.removeAtKey(record.get("report_key"));
		}

	},
	saveSelection : function(store, operation, eOpts) {
		//console.log("i'm here!");
		this.isLoading = true;
		var grid = Ext.ComponentQuery.query('StockQuery')[0];
		Ext.each(grid.selModel.selected.keys, function(value, index) {

					var key = value.replace(
							"AM.model.materialDoc.report.StockQuery-", "")
					this.selected.add(key, value);
				}, this);

	},

	setSelection : function(store, records, successful, operation, eOpts) {

		var grid = Ext.ComponentQuery.query('StockQuery')[0];
		var selModel = grid.selModel;
		Ext.each(records, function(record) {

					key = record.get("report_key")
					if (this.selected.get(key) != null) {
						selModel.selectRange(record, record, true);
					}
				}, this)

		this.isLoading = false;

	},

	exportToExcel : function(button) {
		var panel = button.up('StockQuery');
		//var grid = panel.down('gridpanel');
		var grid = button.up('StockQuery');
		var store = grid.getStore();
        this.saveSelection();
		var record = panel.down('form').getValues();
		// //合同类型 合同号 供应商 付款方式 备注 规格 数量 单价 备注
		var tmp = [];
		tmp = this.getFilterParam(record, 'excel');
		var param;
		param = 'filter=' + encodeURIComponent(Ext.JSON.encode(tmp))
				+ '&excel=true&start=0&limit=10000000&endDate='
				+ encodeURI(record.endDate);

		window.open('reports/stockQuerys?' + param);

	},

	submitReport : function(button) {

		//var panel = button.up('StockQuery');
		//var grid = panel.down('gridpanel');
		var grid = button.up('StockQuery');
		var store = grid.getStore();

		var tmp = [];
		var record = button.up('form').getValues();
		tmp = this.getFilterParam(record, 'store');

		var p = store.getProxy();
		p.extraParams.filter = Ext.JSON.encode(tmp);
		p.extraParams.endDate = record.endDate;
		store.load();
	},

	getFilterParam : function(record, target) {
		var filter = {};
		var tmp = [];

		if (this.selected.getCount() > 0 && target == 'excel') {
			filter.type = "list";
			filter.field = ("(convert(varchar(40), stock.line_id_in)+'--'+stock.stock_location)");
			filter.comparison = 'list';
			filter.value = "";
			Ext.each(this.selected.keys, function(key) {

						filter.value = filter.value +"'"+key + "',";
					});
	        filter.value = filter.value +"'0'";					
			// filter.value = record.contract_no;

			tmp.push(Ext.apply({}, filter));

		}
		if (record.contract_no != "") {
			filter.type = "string";
			filter.field = "contract_no";
			filter.value = record.contract_no;
			tmp.push(Ext.apply({}, filter));
		}
		if (record.supplier != "") {
			filter.type = "string";
			filter.field = "supplier";
			filter.value = record.supplier;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.model_contract != "") {
			filter.type = "string";
			filter.field = "model_contract";
			filter.value = record.model_contract;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.docDateFrom != "") {
			filter.type = "date";
			filter.comparison = 'ge';
			filter.field = "material_doc.doc_date";
			filter.value = record.docDateFrom;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.docDateTo != "") {
			filter.type = "date";
			filter.comparison = 'le';
			filter.field = "material_doc.doc_date";
			filter.value = record.docDateTo;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.stock_location != "") {
			filter.type = "string";
			filter.field = "stock.stock_location";
			filter.value = record.stock_location;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.delivery_note != "") {
			filter.type = "string";
			filter.field = "delivery_note";
			filter.value = record.delivery_note;
			tmp.push(Ext.apply({}, filter));
		}
		if (record.plate_num != "") {
			filter.type = "string";
			filter.field = "plate_num";
			filter.value = record.plate_num;
			tmp.push(Ext.apply({}, filter));
		}

		if (record.batch_no != "") {
			filter.type = "string";
			filter.field = "batch_no";
			filter.value = record.batch_no;
			tmp.push(Ext.apply({}, filter));
		}

		return tmp;

	}
});