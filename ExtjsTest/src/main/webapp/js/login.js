/**
 * 
 */


/// <reference path="http://http://www.jb51.net/Resources/ExtJs/vswd-ext_2.0.2.js" /> 
//加载提示框 
Ext.QuickTips.init(); 
//创建命名空间 
Ext.namespace('XQH.ExtJs.Frame'); 
//主应用程序 
XQH.ExtJs.Frame.app = function() { 
} 
Ext.extend(XQH.ExtJs.Frame.app, Ext.util.Observable, { 
LoginLogo:new Ext.Panel({ 
id: 'loginLogo', 
height: 55, 
frame:true, 
bodyStyle:'padding-top:4px', 
html:'<h3><center>内部信息中心</center></h3>' 
}), 
//登陆表单 
LoginForm: new Ext.form.FormPanel({ 
id: 'loginForm', 
defaultType: 'textfield', 
labelAlign: 'right', 
labelWidth: 60, 
standardSubmit:true,  
url:'j_spring_security_check',
frame: true, 
//submit: function(){
    //this.getEl().dom.action = 'j_spring_security_check',
    //this.getEl().dom.method='POST',
	//var form = this.up('form').getForm()
    //this.submit({
    //});
	//form.action = 'j_spring_security_check';
	//form.method = 'POST';
	//form.submit();
	//this.submit();
//},

defaults: 
{ 
allowBlank: true 
}, 
items: 
[ 
{ 
name:'j_username', 
fieldLabel: '登陆账号', 
blankText: '账号不能为空', 
emptyText:'必填', 
id:'j_username',
anchor: '98%' 
}, 
{ 
name:'j_password', 
inputType: 'password', 
fieldLabel: '登陆密码', 
blankText: '密码不能为空', 
maxLength:10, 
id:'j_password',
anchor: '98%' 
} 
] 
}), 
//登陆窗口 
LoginWin: new Ext.Window({ 
id: 'loginWin', 
Title: '登陆', 
width: 350, 
height: 200, 
closable: false, 
collapsible: false, 
resizable:false, 
defaults: { 
border: false 
}, 
buttonAlign: 'center', 
buttons: [ 
{ text: '登陆',
  handler: function(){
	  var form = Ext.getCmp("loginForm").getForm();
	  form.action = 'j_spring_security_check';
	  form.method = 'POST';

	  form.submit();
	  
	 
  }} 
], 
layout: 'column', 
items: 
[ 
{ 
columnWidth:1, 
items: Ext.getCmp("loginLogo") 
}, 
{ 
columnWidth: 1, 
items: Ext.getCmp("loginForm") 
} 
] 
}), 
//初始化 
init: function() { 
this.LoginWin.show(); 
} 
}); 
//程序入口 
Ext.onReady(function() { 
var loginFrame = new XQH.ExtJs.Frame.app(); 
loginFrame.init(); 
}); 

