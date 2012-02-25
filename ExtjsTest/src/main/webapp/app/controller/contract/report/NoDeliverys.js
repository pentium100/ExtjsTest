Ext.define('AM.controller.contract.report.NoDeliverys', {
			extend : 'Ext.app.Controller',

			views :  ['contract.report.NoDelivery'],
			stores : ['contract.report.NoDelivery'],
			models : ['contract.report.NoDelivery'],

			init : function(options) {

				Ext.apply(this, options);

				this.control({

							'NoDelivery button[action=search]' : {
								click : this.submitReport
							}
						});

			},

			submitReport : function(button) {
				
				var panel = button.up('NoDelivery');
				var grid = panel.down('gridpanel');
				var store = grid.getStore();

				var tmp = [];
				var filter = {};
				var record = button.up('form').getValues();
				if (record.contractNo != "") {
					filter.type = "string";
					filter.field = "contractNo";
					filter.value = record.contractNo;
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