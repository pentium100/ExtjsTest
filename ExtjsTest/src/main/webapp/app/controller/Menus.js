Ext.define('AM.controller.Menus', {
			extend : 'Ext.app.Controller',

			refs : [{
						ref : 'menu',
						selector : '#menu-panel'
					}, {
						ref : 'tabpanel',
						selector : '#content-panel'
					}],
			init : function() {
				this.control({
							'#menu-panel' : {
								itemclick : this.itemclick
							}
						});

			},

			itemclick : function(view, record, item, index, e, eOpts) {
				var tabs = this.getTabpanel();

				if (!record.get("leaf")) {
					return;
				}

				var tab = tabs.down("#" + record.raw.controller);
				if (tab == undefined) {

					var c = this.getController(record.raw.controller);
					c.init();

					tab = tabs.add({
								// we use the tabs.items property to get the
								// length of current
								// items/tabs
								title : record.get("text"),
								layout : 'fit',
								closable: true,
								autoDestroy:true,
								

								autoScroll : true,
								bodyPadding : 0,
								xtype : record.raw.views,

								id : record.raw.controller

							});

				}
				tabs.setActiveTab(tab);
			}

		});