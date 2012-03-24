Ext.define('AM.controller.materialDoc.report.StockQuerys', {
			extend : 'Ext.app.Controller',

			views :  ['materialDoc.report.StockQuery'],
			stores : ['materialDoc.report.StockQuery'],
			models : ['materialDoc.report.StockQuery'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'StockQuery button[action=submit]' : {
								click : this.submitReport
							},

							'StockQuery button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('StockQuery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				
				
				var record = panel.down('form').getValues();
				////合同类型	合同号	供应商	付款方式	备注	规格	数量	单价	备注
				var tmp = [];
				tmp = this.getFilterParam(record);
				var param;
				param = 'filter='+encodeURI(Ext.JSON.encode(tmp))+'&excel=true&start=0&limit=10000000&endDate='+encodeURI(record.endDate);				
				
				window.open('reports/stockQuerys?'+param);

				
				

			},

			submitReport : function(button) {

				var panel = button.up('StockQuery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var record = button.up('form').getValues();
				tmp = this.getFilterParam(record);
				
				
				
				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				p.extraParams.endDate = record.endDate;
				store.load();
			},
			
			
			getFilterParam:function(record){
				var filter = {};
				var tmp = [];
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


				if (record.warehouse != "") {
					filter.type = "string";
					filter.field = "stock.warehouse";
					filter.value = record.warehouse;
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