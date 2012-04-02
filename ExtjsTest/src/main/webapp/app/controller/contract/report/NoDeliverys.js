Ext.define('AM.controller.contract.report.NoDeliverys', {
			extend : 'Ext.app.Controller',

			views : ['contract.report.NoDelivery'],
			stores : ['contract.report.NoDelivery'],
			models : ['contract.report.NoDelivery'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'NoDelivery button[action=search]' : {
								click : this.submitReport
							},

							'NoDelivery button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('NoDelivery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var filter = {};
				var record = panel.down('form').getValues();

				if (record.contract_type !=undefined&&record.contract_type != "") {
					filter.type = "string";
					filter.field = "contract_type";
					filter.value = record.contract_type;
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

				if (record.signDateFrom != "") {
					filter.type = "date";
					filter.field = "sign_date";
					filter.comparison = 'ge';
					filter.value = record.signDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.signDateTo != "") {
					filter.type = "date";
					filter.field = "sign_date";
					filter.comparison = 'le';
					filter.value = record.signDateTo;
					tmp.push(Ext.apply({}, filter));
				}
				

				if (record.model != "") {
					filter.type = "string";
					filter.field = "model";
					filter.value = record.model;
					tmp.push(Ext.apply({}, filter));
				}
                
				param = 'filter='+encodeURI(Ext.JSON.encode(tmp))+'&excel=true&start=0&limit=10000000';
				window.open('reports/noDeliverys?'+param);

				
				

			},

			submitReport : function(button) {

				var panel = button.up('NoDelivery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var filter = {};
				var record = button.up('form').getValues();
				
				if (record.contract_type !=undefined&&record.contract_type != "") {
					filter.type = "string";
					filter.field = "contract_type";
					filter.value = record.contract_type;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.contract_no != "") {
					filter.type = "string";
					filter.field = "contract_no";
					filter.value = record.contract_no;
					tmp.push(Ext.apply({}, filter));
				}
				if (record.signDateFrom != "") {
					filter.type = "date";
					filter.field = "sign_date";
					filter.comparison = 'ge';
					filter.value = record.signDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.signDateTo != "") {
					filter.type = "date";
					filter.field = "sign_date";
					filter.comparison = 'le';
					filter.value = record.signDateTo;
					tmp.push(Ext.apply({}, filter));
				}
				

				if (record.supplier != "") {
					filter.type = "string";
					filter.field = "supplier";
					filter.value = record.supplier;
					tmp.push(Ext.apply({}, filter));
				}

				if (record.model != "") {
					filter.type = "string";
					filter.field = "model";
					filter.value = record.model;
					tmp.push(Ext.apply({}, filter));
				}

				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				store.load();
			}
		});