/*
 * 
 * This file is part of Ext JS 4
 * 
 * Copyright (c) 2011 Sencha Inc
 * 
 * Contact: http://www.sencha.com/contact
 * 
 * GNU General Public License Usage This file may be used under the terms of the
 * GNU General Public License version 3.0 as published by the Free Software
 * Foundation and appearing in the file LICENSE included in the packaging of
 * this file. Please review the following information to ensure the GNU General
 * Public License version 3.0 requirements will be met:
 * http://www.gnu.org/copyleft/gpl.html.
 * 
 * If you are unsure which license is appropriate for your use, please contact
 * the sales department at http://www.sencha.com/contact.
 * 
 */
/**
 * @class Ext.app.Portal
 * @extends Object A sample portal layout application class.
 */
// TODO: Fill in the content panel -- no AccordionLayout at the moment
// TODO: Fix container drag/scroll support (waiting on Ext.lib.Anim)
// TODO: Fix Ext.Tool scope being set to the panel header
// TODO: Drag/drop does not cause a refresh of scroll overflow when needed
// TODO: Grid portlet throws errors on destroy (grid bug)
// TODO: Z-index issues during drag
Ext.define('AM.portal.Portal', {

	extend : 'Ext.container.Viewport',

	uses : ['AM.portal.classes.PortalPanel', 'AM.portal.classes.PortalColumn',
			'AM.portal.classes.GridPortlet', 'AM.portal.classes.ChartPortlet'],

	getTools : function() {
		return [{
					xtype : 'tool',
					type : 'gear',
					handler : function(e, target, panelHeader, tool) {
						var portlet = panelHeader.ownerCt;
						portlet.setLoading('Working...');
						Ext.defer(function() {
									portlet.setLoading(false);
								}, 2000);
					}
				}];
	},

	initComponent : function() {
		var content = '<div class="portlet-content">'
				+ Ext.example.shortBogusMarkup + '</div>';

		Ext.apply(this, {
			id : 'app-viewport',
			layout : {
				type : 'border',
				padding : '0 5 5 5' // pad the layout from the window edges
			},
			items : [{
						id : 'app-header',
						xtype : 'box',
						region : 'north',
						height : 40,
						html : '煤炭中心购销系统'
					}, {
						xtype : 'container',
						region : 'center',
						layout : 'border',
						items : [

								// Ext.create('AM.view.Menu', {
								// id : 'app-options'
								// })
								{
							id : 'app-options',
							title : 'Options',
							region : 'west',
							animCollapse : true,
							width : 200,
							minWidth : 150,
							maxWidth : 400,
							split : true,
							collapsible : true,
							layout : 'accordion',
							layoutConfig : {
								animate : true
							},
							items : [Ext.create('AM.view.Menu'), {
										html : content,
										title : 'Navigation',
										autoScroll : true,
										border : false,
										iconCls : 'nav'
									}, {
										title : 'Settings',
										html : content,
										border : false,
										autoScroll : true,
										iconCls : 'settings'
									}]
						}, Ext.create('AM.view.TabPanel', {

							items : [{
								id : 'app-portal',
								xtype : 'portalpanel',

								id : 'HomePage',
								title : '首页',
								iconCls : 'home',
								layout : 'fit',
								bodyPadding : '0 5 5 5',

								items : [{
									id : 'col-1',
									items : [{
										id : 'portlet-1',
										title : 'Grid Portlet',
										tools : this.getTools(),
										items : Ext
												.create('AM.portal.classes.GridPortlet'),
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									}, {
										id : 'portlet-2',
										title : 'Portlet 2',
										tools : this.getTools(),
										html : content,
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									}]
								}, {
									id : 'col-2',
									items : [{
										id : 'portlet-3',
										title : 'Portlet 3',
										tools : this.getTools(),
										html : '<div class="portlet-content">'
												+ '</div>',
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									}]
								}, {
									id : 'col-3',
									items : [{
										id : 'portlet-4',
										title : 'Stock Portlet',
										tools : this.getTools(),
										items : Ext
												.create('AM.portal.classes.ChartPortlet'),
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									}]
								}]

							}

							]

						})

						]
					}]
		});
		this.callParent(arguments);
	},

	onPortletClose : function(portlet) {
		this.showMsg('"' + portlet.title + '" was removed');
	},

	showMsg : function(msg) {
		var el = Ext.get('app-msg'), msgId = Ext.id();

		this.msgId = msgId;
		el.update(msg).show();

		Ext.defer(this.clearMsg, 3000, this, [msgId]);
	},

	clearMsg : function(msgId) {
		if (msgId === this.msgId) {
			Ext.get('app-msg').hide();
		}
	}
});
