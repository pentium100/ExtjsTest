Ext.define('AM.controller.contract.report.OpenOrders', {
			extend : 'Ext.app.Controller',

			views : ['contract.report.OpenOrder'],
			stores : ['contract.report.OpenOrder'],
			models : ['contract.report.OpenOrder'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'OpenOrder button[action=search]' : {
								click : this.submitReport
							},

							'OpenOrder button[action=exportToExcel]' : {
								click : this.exportToExcel
							}
						});

			},

			exportToExcel : function(button) {
				var panel = button.up('OpenOrder');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var filter = {};
				var record = panel.down('form').getValues();
				
				if (record.model != "") {
					filter.type = "string";
					filter.field = "model";
					filter.value = record.model;
					tmp.push(Ext.apply({}, filter));
				}
                
				param = 'filter='+encodeURI(Ext.JSON.encode(tmp))+'&excel=true&start=0&limit=10000000';
				window.open('reports/openOrders?'+param);

				
				

			},

			submitReport : function(button) {

				var panel = button.up('OpenOrder');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var filter = {};
				var record = button.up('form').getValues();

				if (record.model != "") {
					filter.type = "string";
					filter.field = "model";
					filter.value = record.model;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.signDateFrom != "") {
					filter.type = "date";
					filter.field = "c.sign_date";
					filter.comparison = 'ge';
					filter.value = record.signDateFrom;
					tmp.push(Ext.apply({}, filter));
				}
				
				if (record.signDateTo != "") {
					filter.type = "date";
					filter.field = "c.sign_date";
					filter.comparison = 'le';
					filter.value = record.signDateTo;
					tmp.push(Ext.apply({}, filter));
				}
				

				var p = store.getProxy();
				p.extraParams.filter = Ext.JSON.encode(tmp);
				store.load();
			}
		});