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
Ext.define('AM.portal.Portal_OA', {

	extend : 'Ext.container.Viewport',

	uses : [
			'AM.view.message.MessageList', 'AM.view.message.MessageEdit'],

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
						xtype : 'container',
						region : 'center',
						layout : 'fit',
						items : [

							{
								id : 'app-portal',
								xtype : 'portalpanel',
								title : '首页',
								iconCls : 'home',
								layout : 'fit',
								bodyPadding : '0 5 5 5',

								items : [{
									id : 'col-1',
									items : [{
										id : 'portlet-1',
										title : '供应信息',
										tools : this.getTools(),
										items : Ext
												.create('AM.view.message.MessageList', {id:'messageList-1', messageType:"供应"}),
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									}, {
										id : 'portlet-2',
										title : '需求信息',
										tools : this.getTools(),
										items:  Ext
												.create('AM.view.message.MessageList', {id:'messageList-2', messageType:"需求"}),
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									},{ 
										id : 'portlet-3',
										title : '敞口信息',
										tools : this.getTools(),
										items:  Ext
												.create('AM.view.message.MessageList', {id:'messageList-3', messageType:"敞口"}),
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									},{
										id : 'portlet-4',
										title : '锁定信息',
										tools : this.getTools(),
										items:  Ext
												.create('AM.view.message.MessageList', {id:'messageList-4', messageType:"锁定"}),
										listeners : {
											'close' : Ext.bind(
													this.onPortletClose, this)
										}
									}	
									]
							
								}]

							}

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
