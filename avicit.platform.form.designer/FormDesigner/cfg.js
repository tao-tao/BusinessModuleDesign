DocRefs = {

"BoxComponent.js":{
height:{type:"Number",desc:"The height of this component in pixels (defaults to auto)."}
,width:{type:"Number",desc:"The width of this component in pixels (defaults to auto)."}
,autoHeight:{type:"Boolean",desc:"True to use height:'auto', false to use fixed height (defaults to false)."}
,autoWidth:{type:"Boolean",desc:"True to use width:'auto', false to use fixed width (defaults to false)."}
,deferHeight:{type:"Boolean",desc:"True to defer height calculations to an external component, false to allow this component to set its own height (defaults to false)."}
},


"Button.js":{
text:{type:"String",desc:"The button text"}
,icon:{type:"String",desc:"The path to an image to display in the button (the image will be set as the background-image CSS property of the button by default, so if you want a mixed icon/text button, set cls:\"x-btn-text-icon\")"}
,handler:{type:"Function",desc:"A function called when the button is clicked (can be used instead of click event)"}
,scope:{type:"Object",desc:"The scope of the handler"}
,minWidth:{type:"Number",desc:"The minimum width for this button (used to give a set of buttons a common width)"}
,tooltip:{type:"String/Object",desc:"The tooltip for the button - can be a string or QuickTips config object"}
,hidden:{type:"Boolean",desc:"True to start hidden (defaults to false)"}
,disabled:{type:"Boolean",desc:"True to start disabled (defaults to false)"}
,pressed:{type:"Boolean",desc:"True to start pressed (only if enableToggle = true)"}
,toggleGroup:{type:"String",desc:"The group this toggle button is a member of (only 1 per group can be pressed, only applies if enableToggle = true)"}
,repeat:{type:"Boolean/Object",desc:"True to repeat fire the click event while the mouse is down. This can also be an {@link Ext.util.ClickRepeater} config object (defaults to false). @constructor Create a new button @param {Object} config The config object"}
,tabIndex:{type:"Number",desc:"Set a DOM tabIndex for this button (defaults to undefined)"}
,enableToggle:{type:"Boolean",desc:"True to enable pressed/not pressed toggling (defaults to false)"}
,menu:{type:"Mixed",desc:"Standard menu attribute consisting of a reference to a menu object, a menu id or a menu config blob (defaults to undefined)."}
,menuAlign:{type:"String",desc:"The position to align the menu to (see {@link Ext.Element#alignTo} for more details, defaults to 'tl-bl?')."}
,iconCls:{type:"String",desc:"A css class which sets a background image to be used as the icon for this button"}
,type:{type:"String",desc:"submit, reset or button - defaults to 'button'"}
,clickEvent:{type:"String",desc:"The type of event to map to the button's event handler (defaults to 'click')"}
,handleMouseEvents:{type:"Boolean",desc:"False to disable visual cues on mouseover, mouseout and mousedown (defaults to true)"}
,tooltipType:{type:"String",desc:"The type of tooltip to use. Either \"qtip\" (default) for QuickTips or \"title\" for title attribute."}
,cls:{type:"String",desc:"A CSS class string to apply to the button's main element."}
,template:{type:"Ext.Template",desc:"(Optional) An {@link Ext.Template} with which to create the Button's main element. This Template must contain numeric substitution parameter 0 if it is to display the text property. Changing the template could require code modifications if required elements (e.g. a button) aren't present."}
},


"ColorPalette.js":{
itemCls:{type:"String",desc:"The CSS class to apply to the containing element (defaults to \"x-color-palette\")"}
,value:{type:"String",desc:"The initial color to highlight (should be a valid 6-digit color hex code without the # symbol). Note that the hex codes are case-sensitive."}
,allowReselect:{type:"Boolean",desc:"If set to true then reselecting a color that is already selected fires the selection event"}
},


"Component.js":{
id:{type:"String",desc:"The unique id of this component (defaults to an auto-assigned id)."}
,cls:{type:"String",desc:"An optional extra CSS class that will be added to this component's Element (defaults to ''). This can be useful for adding customized styles to the component or any of its children using standard CSS rules."}
,style:{type:"String",desc:"A custom style specification to be applied to this component's Element. Should be a valid argument to {@link Ext.Element#applyStyles}."}
,ctCls:{type:"String",desc:"An optional extra CSS class that will be added to this component's container (defaults to ''). This can be useful for adding customized styles to the container or any of its children using standard CSS rules."}
,plugins:{type:"Object/Array",desc:"An object or array of objects that will provide custom functionality for this component. The only requirement for a valid plugin is that it contain an init method that accepts a reference of type Ext.Component. When a component is created, if any plugins are available, the component will call the init method on each plugin, passing a reference to itself. Each plugin can then call methods or respond to events on the component as needed to provide its functionality."}
,applyTo:{type:"Mixed",desc:"The id of the node, a DOM node or an existing Element corresponding to an existing element present in the DOM to render this component to. Using this config, a call to render() is not required."}
,renderTo:{type:"Mixed",desc:"The id of the node, a DOM node or an existing Element to render this component into. Using this config, a call to render() is not required."}
,disableClass:{type:"String",desc:"CSS class added to the component when it is disabled (defaults to \"x-item-disabled\")."}
,allowDomMove:{type:"Boolean",desc:"Whether the component can move the Dom node when rendering (defaults to true)."}
,autoShow:{type:"Boolean",desc:"True if the component should check for hidden classes (e.g. 'x-hidden' or 'x-hide-display') and remove them on render (defaults to false)."}
,hideMode:{type:"String",desc:"How this component should hidden. Supported values are \"visibility\" (css visibility), \"offsets\" (negative offset position) and \"display\" (css display) - defaults to \"display\"."}
,hideParent:{type:"Boolean",desc:"True to hide and show the component's container when hide/show is called on the component, false to hide and show the component itself (defaults to false). For example, this can be used as a shortcut for a hide button on a window by setting hide:true on the button when adding it to its parent container."}
},


"ComponentMgr.js":{
},


"Container.js":{
monitorResize:{type:"Boolean",desc:"True to automatically monitor window resize events to handle anything that is sensitive to the current size of the viewport. This value is typically managed by the chosen {@link #layout} and should not need to be set manually."}
,layout:{type:"String",desc:"The layout type to be used in this container. If not specified, a default {@link Ext.layout.ContainerLayout} will be created and used. Valid values are: accordion, anchor, border, card, column, fit, form and table. Specific config values for the chosen layout type can be specified using {@link #layoutConfig}."}
,layoutConfig:{type:"Object",desc:"This is a config object containing properties specific to the chosen layout (to be used in conjunction with the {@link #layout} config value). For complete details regarding the valid config options for each layout type, see the layout class corresponding to the type specified: {@link Ext.layout.Accordion}, {@link Ext.layout.AnchorLayout}, {@link Ext.layout.BorderLayout}, {@link Ext.layout.CardLayout}, {@link Ext.layout.ColumnLayout}, {@link Ext.layout.FitLayout}, {@link Ext.layout.FormLayout} and {@link Ext.layout.TableLayout}."}
,activeItem:{type:"String/Number",desc:"A string component id or the numeric index of the component that should be initially activated within the container's layout on render. For example, activeItem: 'item-1' or activeItem: 0 (index 0 = the first item in the container's collection). activeItem only applies to layout styles that can display items one at a time (like {@link Ext.layout.Accordion}, {@link Ext.layout.CardLayout} and {@link Ext.layout.FitLayout}). Related to {@link Ext.layout.ContainerLayout#activeItem}."}
,items:{type:"Mixed",desc:"A single item, or an array of items to be added to this container. Each item can be any type of object based on {@link Ext.Component}, or a valid config object for such an item. If a single item is being passed, it should be passed directly as an object reference (e.g., items: {...}). Multiple items should be passed as an array of objects (e.g., items: [{...}, {...}])."}
,defaults:{type:"Object",desc:"A config object that will be applied to all components added to this container either via the {@link #items} config or via the {@link #add} or {@link #insert} methods. The defaults config can contain any number of name/value property pairs to be added to each item, and should be valid for the types of items being added to the container. For example, to automatically apply padding to the body of each of a set of contained {@link Ext.Panel} items, you could pass: defaults: {bodyStyle:'padding:15px'}."}
,autoDestroy:{type:"Boolean",desc:"If true the container will automatically destroy any contained component that is removed from it, else destruction must be handled manually (defaults to true)."}
,hideBorders:{type:"Boolean",desc:"True to hide the borders of each contained component, false to defer to the component's existing border settings (defaults to false)."}
,defaultType:{type:"String",desc:"The default type of container represented by this object as registered in {@link Ext.ComponentMgr} (defaults to 'panel')."}
},


"CycleButton.js":{
items:{type:"Array",desc:"An array of {@link Ext.menu.CheckItem} <b>config</b> objects to be used when creating the button's menu items (e.g., {text:'Foo', iconCls:'foo-icon'})"}
,showText:{type:"Boolean",desc:"True to display the active item's text as the button text (defaults to false)"}
,prependText:{type:"String",desc:"A static string to prepend before the active item's text when displayed as the button's text (only applies when showText = true, defaults to '')"}
,changeHandler:{type:"Function",desc:"A callback function that will be invoked each time the active menu item in the button's menu has changed. If this callback is not supplied, the SplitButton will instead fire the {@link #change} event on active item change. The changeHandler function will be called with the following argument list: (SplitButton this, Ext.menu.CheckItem item)"}
},


"DataView.js":{
tpl:{type:"String/Array",desc:"The HTML fragment or an array of fragments that will make up the template used by this DataView. This should be specified in the same format expected by the constructor of {@link Ext.XTemplate}."}
,store:{type:"Ext.data.Store",desc:"The {@link Ext.data.Store} to bind this DataView to."}
,itemSelector:{type:"String",desc:"A CSS selector in any format supported by {@link Ext.DomQuery} that will be used to filter the data loaded from the store (defaults to '')."}
,multiSelect:{type:"Boolean",desc:"True to allow selection of more than one item at a time, false to allow selection of only a single item at a time or no selection at all, depending on the value of {@link #singleSelect} (defaults to false)."}
,singleSelect:{type:"Boolean",desc:"True to allow selection of exactly one item at a time, false to allow no selection at all (defaults to false). Note that if {@link #multiSelect} = true, this value will be ignored."}
,simpleSelect:{type:"Boolean",desc:"True to enable multiselection by clicking on multiple items without requiring the user to hold Shift or Ctrl, false to force the user to hold Ctrl or Shift to select more than on item (defaults to false)."}
,overClass:{type:"String",desc:"A CSS class to apply to each item in the view on mouseover (defaults to undefined)."}
,loadingText:{type:"String",desc:"A string to display during data load operations (defaults to undefined). If specified, this text will be displayed in a loading div and the view's contents will be cleared while loading, otherwise the view's contents will continue to display normally until the new data is loaded and the contents are replaced."}
,selectedClass:{type:"String",desc:"A CSS class to apply to each selected item in the view (defaults to 'x-view-selected')."}
,emptyText:{type:"String",desc:"The text to display in the view when there is no data to display (defaults to '')."}
},


"DatePicker.js":{
todayText:{type:"String",desc:"The text to display on the button that selects the current date (defaults to \"Today\")"}
,okText:{type:"String",desc:"The text to display on the ok button"}
,cancelText:{type:"String",desc:"The text to display on the cancel button"}
,todayTip:{type:"String",desc:"The tooltip to display for the button that selects the current date (defaults to \"{current date} (Spacebar)\")"}
,minDate:{type:"Date",desc:"Minimum allowable date (JavaScript date object, defaults to null)"}
,maxDate:{type:"Date",desc:"Maximum allowable date (JavaScript date object, defaults to null)"}
,minText:{type:"String",desc:"The error text to display if the minDate validation fails (defaults to \"This date is before the minimum date\")"}
,maxText:{type:"String",desc:"The error text to display if the maxDate validation fails (defaults to \"This date is after the maximum date\")"}
,format:{type:"String",desc:"The default date format string which can be overriden for localization support. The format must be valid according to {@link Date#parseDate} (defaults to 'm/d/y')."}
,disabledDays:{type:"Array",desc:"An array of days to disable, 0-based. For example, [0, 6] disables Sunday and Saturday (defaults to null)."}
,disabledDaysText:{type:"String",desc:"The tooltip to display when the date falls on a disabled day (defaults to \"\")"}
,disabledDatesRE:{type:"RegExp",desc:"JavaScript regular expression used to disable a pattern of dates (defaults to null)"}
,disabledDatesText:{type:"String",desc:"The tooltip text to display when the date falls on a disabled date (defaults to \"\")"}
,constrainToViewport:{type:"Boolean",desc:"True to constrain the date picker to the viewport (defaults to true)"}
,monthNames:{type:"Array",desc:"An array of textual month names which can be overriden for localization support (defaults to Date.monthNames)"}
,dayNames:{type:"Array",desc:"An array of textual day names which can be overriden for localization support (defaults to Date.dayNames)"}
,nextText:{type:"String",desc:"The next month navigation button tooltip (defaults to 'Next Month (Control+Right)')"}
,prevText:{type:"String",desc:"The previous month navigation button tooltip (defaults to 'Previous Month (Control+Left)')"}
,monthYearText:{type:"String",desc:"The header month selector tooltip (defaults to 'Choose a month (Control+Up/Down to move years)')"}
,startDay:{type:"Number",desc:"Day index at which the week should begin, 0-based (defaults to 0, which is Sunday)"}
},


"Editor.js":{
autosize:{type:"Boolean/String",desc:"True for the editor to automatically adopt the size of the underlying field, \"width\" to adopt the width only, or \"height\" to adopt the height only (defaults to false)"}
,revertInvalid:{type:"Boolean",desc:"True to automatically revert the field value and cancel the edit when the user completes an edit and the field validation fails (defaults to true)"}
,ignoreNoChange:{type:"Boolean",desc:"True to skip the the edit completion process (no save, no events fired) if the user completes an edit and the value has not changed (defaults to false). Applies only to string values - edits for other data types will never be ignored."}
,hideEl:{type:"Boolean",desc:"False to keep the bound element visible while the editor is displayed (defaults to true)"}
,value:{type:"Mixed",desc:"The data value of the underlying field (defaults to \"\")"}
,alignment:{type:"String",desc:"The position to align to (see {@link Ext.Element#alignTo} for more details, defaults to \"c-c?\")."}
,shadow:{type:"Boolean/String",desc:"\"sides\" for sides/bottom only, \"frame\" for 4-way shadow, and \"drop\" for bottom-right shadow (defaults to \"frame\")"}
,constrain:{type:"Boolean",desc:"True to constrain the editor to the viewport"}
,swallowKeys:{type:"Boolean",desc:"Handle the keydown/keypress events so they don't propagate (defaults to true)"}
,completeOnEnter:{type:"Boolean",desc:"True to complete the edit when the enter key is pressed (defaults to false)"}
,cancelOnEsc:{type:"Boolean",desc:"True to cancel the edit when the escape key is pressed (defaults to false)"}
,updateEl:{type:"Boolean",desc:"True to update the innerHTML of the bound element when the update completes (defaults to false)"}
},


"form/Action.js":{
url:{type:"String",desc:"The URL that the Action is to invoke."}
,method:{type:"String",desc:"The HTTP method to use to access the requested URL. Defaults to the {@link Ext.form.Form}'s method, or if that is not specified, the underlying DOM form's method."}
,params:{type:"Mixed",desc:"Extra parameter values to pass. These are added to the Form's {@link Ext.form.BasicForm#baseParams} and passed to the specified URL along with the Form's input fields."}
,success:{type:"Function",desc:"The function to call when a valid success return packet is recieved. The function is passed the following parameters: <ul> <li><code>form</code> : Ext.form.Form<div class=\"sub-desc\">The form that requested the action</div></li> <li><code>action</code> : Ext.form.Action<div class=\"sub-desc\">The Action class. The {@link #result} property of this object may be examined to perform custom postprocessing.</div></li> </ul>"}
,failure:{type:"Function",desc:"The function to call when a failure packet was recieved, or when an error ocurred in the Ajax communication. The function is passed the following parameters: <ul> <li><code>form</code> : Ext.form.Form<div class=\"sub-desc\">The form that requested the action</div></li> <li><code>action</code> : Ext.form.Action<div class=\"sub-desc\">The Action class. If an Ajax error ocurred, the failure type will be in {@link #failureType}. The {@link #result} property of this object may be examined to perform custom postprocessing.</div></li> </ul> / "}
,waitMsg:{type:"String",desc:"The message to be displayed by a call to {@link Ext.MessageBox#wait} during the time the action is being processed."}
,waitTitle:{type:"String",desc:"The title to be displayed by a call to {@link Ext.MessageBox#wait} during the time the action is being processed."}
,clientValidation:{type:"boolean",desc:"Applies to submit only. Pass true to call form.isValid() prior to posting to validate the form on the client (defaults to false)"}
},


"form/BasicForm.js":{
method:{type:"String",desc:"The request method to use (GET or POST) for form actions if one isn't supplied in the action options."}
,reader:{type:"DataReader",desc:"An Ext.data.DataReader (e.g. {@link Ext.data.XmlReader}) to be used to read data when executing \"load\" actions. This is optional as there is built-in support for processing JSON."}
,errorReader:{type:"DataReader",desc:"An Ext.data.DataReader (e.g. {@link Ext.data.XmlReader}) to be used to read data when reading validation errors on \"submit\" actions. This is completely optional as there is built-in support for processing JSON."}
,url:{type:"String",desc:"The URL to use for form actions if one isn't supplied in the action options."}
,fileUpload:{type:"Boolean",desc:"Set to true if this form is a file upload."}
,baseParams:{type:"Object",desc:"Parameters to pass with all requests. e.g. baseParams: {id: '123', foo: 'bar'}."}
,timeout:{type:"Number",desc:"Timeout for form actions in seconds (default is 30 seconds)."}
,trackResetOnLoad:{type:"Boolean",desc:"If set to true, form.reset() resets to the last loaded or setValues() data instead of when the form was first created."}
},


"form/Checkbox.js":{
focusClass:{type:"String",desc:"The CSS class to use when the checkbox receives focus (defaults to undefined)"}
,fieldClass:{type:"String",desc:"The default CSS class for the checkbox (defaults to \"x-form-field\")"}
,checked:{type:"Boolean",desc:"True if the the checkbox should render already checked (defaults to false)"}
,autoCreate:{type:"String/Object",desc:"A DomHelper element spec, or true for a default element spec (defaults to {tag: \"input\", type: \"checkbox\", autocomplete: \"off\"})"}
,boxLabel:{type:"String",desc:"The text that appears beside the checkbox"}
,inputValue:{type:"String",desc:"The value that should go into the generated input element's value attribute"}
},


"form/Combo.js":{
transform:{type:"Mixed",desc:"The id, DOM node or element of an existing select to convert to a ComboBox"}
,lazyRender:{type:"Boolean",desc:"True to prevent the ComboBox from rendering until requested (should always be used when rendering into an Ext.Editor, defaults to false)"}
,autoCreate:{type:"Boolean/Object",desc:"A DomHelper element spec, or true for a default element spec (defaults to: {tag: \"input\", type: \"text\", size: \"24\", autocomplete: \"off\"})"}
,store:{type:"Ext.data.Store",desc:"The data store to which this combo is bound (defaults to undefined)"}
,title:{type:"String",desc:"If supplied, a header element is created containing this text and added into the top of the dropdown list (defaults to undefined, with no header element)"}
,listWidth:{type:"Number",desc:"The width in pixels of the dropdown list (defaults to the width of the ComboBox field)"}
,displayField:{type:"String",desc:"The underlying data field name to bind to this CombBox (defaults to undefined if mode = 'remote' or 'text' if mode = 'local')"}
,valueField:{type:"String",desc:"The underlying data value name to bind to this CombBox (defaults to undefined if mode = 'remote' or 'value' if mode = 'local') Note: use of a valueField requires the user make a selection in order for a value to be mapped."}
,hiddenName:{type:"String",desc:"If specified, a hidden form field with this name is dynamically generated to store the field's data value (defaults to the underlying DOM element's name)"}
,listClass:{type:"String",desc:"CSS class to apply to the dropdown list element (defaults to '')"}
,selectedClass:{type:"String",desc:"CSS class to apply to the selected item in the dropdown list (defaults to 'x-combo-selected')"}
,triggerClass:{type:"String",desc:"An additional CSS class used to style the trigger button. The trigger will always get the class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified (defaults to 'x-form-arrow-trigger' which displays a downward arrow icon)."}
,shadow:{type:"Boolean/String",desc:"True or \"sides\" for the default effect, \"frame\" for 4-way shadow, and \"drop\" for bottom-right"}
,listAlign:{type:"String",desc:"A valid anchor position value. See {@link Ext.Element#alignTo} for details on supported anchor positions (defaults to 'tl-bl')"}
,maxHeight:{type:"Number",desc:"The maximum height in pixels of the dropdown list before scrollbars are shown (defaults to 300)"}
,triggerAction:{type:"String",desc:"The action to execute when the trigger field is activated. Use 'all' to run the query specified by the allQuery config option (defaults to 'query')"}
,minChars:{type:"Number",desc:"The minimum number of characters the user must type before autocomplete and typeahead activate (defaults to 4, does not apply if editable = false)"}
,typeAhead:{type:"Boolean",desc:"True to populate and autoselect the remainder of the text being typed after a configurable delay (typeAheadDelay) if it matches a known value (defaults to false)"}
,queryDelay:{type:"Number",desc:"The length of time in milliseconds to delay between the start of typing and sending the query to filter the dropdown list (defaults to 500 if mode = 'remote' or 10 if mode = 'local')"}
,pageSize:{type:"Number",desc:"If greater than 0, a paging toolbar is displayed in the footer of the dropdown list and the filter queries will execute with page start and limit parameters. Only applies when mode = 'remote' (defaults to 0)"}
,selectOnFocus:{type:"Boolean",desc:"True to select any existing text in the field immediately on focus. Only applies when editable = true (defaults to false)"}
,queryParam:{type:"String",desc:"Name of the query as it will be passed on the querystring (defaults to 'query')"}
,loadingText:{type:"String",desc:"The text to display in the dropdown list while data is loading. Only applies when mode = 'remote' (defaults to 'Loading...')"}
,resizable:{type:"Boolean",desc:"True to add a resize handle to the bottom of the dropdown list (defaults to false)"}
,handleHeight:{type:"Number",desc:"The height in pixels of the dropdown list resize handle if resizable = true (defaults to 8)"}
,editable:{type:"Boolean",desc:"False to prevent the user from typing text directly into the field, just like a traditional select (defaults to true)"}
,allQuery:{type:"String",desc:"The text query to send to the server to return all records for the list with no filtering (defaults to '')"}
,mode:{type:"String",desc:"Set to 'local' if the ComboBox loads local data (defaults to 'remote' which loads from the server)"}
,minListWidth:{type:"Number",desc:"The minimum width of the dropdown list in pixels (defaults to 70, will be ignored if listWidth has a higher value)"}
,forceSelection:{type:"Boolean",desc:"True to restrict the selected value to one of the values in the list, false to allow the user to set arbitrary text into the field (defaults to false)"}
,typeAheadDelay:{type:"Number",desc:"The length of time in milliseconds to wait until the typeahead text is displayed if typeAhead = true (defaults to 250)"}
,valueNotFoundText:{type:"String",desc:"When using a name/value combo, if the value passed to setValue is not found in the store, valueNotFoundText will be displayed as the field text if defined (defaults to undefined)"}
,lazyInit:{type:"Boolean",desc:"True to not initialize the list for this combo until the field is focused. (defaults to true)"}
,data:{type:"Object",desc:"the data model."}
},


"form/DateField.js":{
format:{type:"String",desc:"The default date format string which can be overriden for localization support. The format must be valid according to {@link Date#parseDate} (defaults to 'm/d/y')."}
,altFormats:{type:"String",desc:"Multiple date formats separated by \"|\" to try when parsing a user input value and it doesn't match the defined format (defaults to 'm/d/Y|m-d-y|m-d-Y|m/d|m-d|d')."}
,disabledDays:{type:"Array",desc:"An array of days to disable, 0 based. For example, [0, 6] disables Sunday and Saturday (defaults to null)."}
,disabledDaysText:{type:"String",desc:"The tooltip to display when the date falls on a disabled day (defaults to 'Disabled')"}
,disabledDates:{type:"Array",desc:"An array of \"dates\" to disable, as strings. These strings will be used to build a dynamic regular expression so they are very powerful. Some examples: <ul> <li>[\"03/08/2003\", \"09/16/2003\"] would disable those exact dates</li> <li>[\"03/08\", \"09/16\"] would disable those days for every year</li> <li>[\"^03/08\"] would only match the beginning (useful if you are using short years)</li> <li>[\"03/../2006\"] would disable every day in March 2006</li> <li>[\"^03\"] would disable every day in every March</li> </ul> In order to support regular expressions, if you are using a date format that has \".\" in it, you will have to escape the dot when restricting dates. For example: [\"03\\.08\\.03\"]."}
,disabledDatesText:{type:"String",desc:"The tooltip text to display when the date falls on a disabled date (defaults to 'Disabled')"}
,minValue:{type:"Date/String",desc:"The minimum allowed date. Can be either a Javascript date object or a string date in a valid format (defaults to null)."}
,maxValue:{type:"Date/String",desc:"The maximum allowed date. Can be either a Javascript date object or a string date in a valid format (defaults to null)."}
,minText:{type:"String",desc:"The error text to display when the date in the cell is before minValue (defaults to 'The date in this field must be after {minValue}')."}
,maxText:{type:"String",desc:"The error text to display when the date in the cell is after maxValue (defaults to 'The date in this field must be before {maxValue}')."}
,invalidText:{type:"String",desc:"The error text to display when the date in the field is invalid (defaults to '{value} is not a valid date - it must be in the format {format}')."}
,triggerClass:{type:"String",desc:"An additional CSS class used to style the trigger button. The trigger will always get the class 'x-form-trigger' and triggerClass will be <b>appended</b> if specified (defaults to 'x-form-date-trigger' which displays a calendar icon)."}
,autoCreate:{type:"String/Object",desc:"A DomHelper element spec, or true for a default element spec (defaults to {tag: \"input\", type: \"text\", size: \"10\", autocomplete: \"off\"})"}
},


"form/Field.js":{
invalidClass:{type:"String",desc:"The CSS class to use when marking a field invalid (defaults to \"x-form-invalid\")"}
,invalidText:{type:"String",desc:"The error text to use when marking a field invalid and no message is provided (defaults to \"The value in this field is invalid\")"}
,focusClass:{type:"String",desc:"The CSS class to use when the field receives focus (defaults to \"x-form-focus\")"}
,validationEvent:{type:"String/Boolean",desc:"The event that should initiate field validation. Set to false to disable automatic validation (defaults to \"keyup\")."}
,validateOnBlur:{type:"Boolean",desc:"Whether the field should validate when it loses focus (defaults to true)."}
,validationDelay:{type:"Number",desc:"The length of time in milliseconds after user input begins until validation is initiated (defaults to 250)"}
,autoCreate:{type:"String/Object",desc:"A DomHelper element spec, or true for a default element spec (defaults to {tag: \"input\", type: \"text\", size: \"20\", autocomplete: \"off\"})"}
,fieldClass:{type:"String",desc:"The default CSS class for the field (defaults to \"x-form-field\")"}
,msgTarget:{type:"String",desc:"The location where error text should display. Should be one of the following values (defaults to 'qtip'): <pre> Value Description ----------- ---------------------------------------------------------------------- qtip Display a quick tip when the user hovers over the field title Display a default browser title attribute popup under Add a block div beneath the field containing the error text side Add an error icon to the right of the field with a popup on hover [element id] Add the error text directly to the innerHTML of the specified element </pre>"}
,msgFx:{type:"String",desc:"<b>Experimental</b> The effect used when displaying a validation message under the field (defaults to 'normal')."}
,readOnly:{type:"Boolean",desc:"True to mark the field as readOnly in HTML (defaults to false) -- Note: this only sets the element's readOnly DOM attribute."}
,disabled:{type:"Boolean",desc:"True to disable the field (defaults to false)."}
,inputType:{type:"String",desc:"The type attribute for input fields -- e.g. radio, text, password (defaults to \"text\")."}
,tabIndex:{type:"Number",desc:"The tabIndex for this field. Note this only applies to fields that are rendered, not those which are built via applyTo (defaults to undefined)."}
,value:{type:"Mixed",desc:"A value to initialize this field with."}
,name:{type:"String",desc:"The field's HTML name attribute."}
,cls:{type:"String",desc:"A CSS class to apply to the field's underlying element."}
},


"form/FieldSet.js":{
checkboxToggle:{type:"Boolean",desc:"True to render a checkbox into the fieldset frame just in front of the legend (defaults to false). The fieldset will be expanded or collapsed when the checkbox is toggled."}
,checkboxName:{type:"String",desc:"The name to assign to the fieldset's checkbox if {@link #checkboxToggle} = true (defaults to '[checkbox id]-checkbox')."}
,labelWidth:{type:"Number",desc:"The width of labels. This property cascades to child containers."}
,itemCls:{type:"String",desc:"A css class to apply to the x-form-item of fields. This property cascades to child containers."}
,baseCls:{type:"String",desc:"The base CSS class applied to the fieldset (defaults to 'x-fieldset')."}
,layout:{type:"String",desc:"The {@link Ext.Container#layout} to use inside the fieldset (defaults to 'form')."}
},


"form/Form.js":{
labelWidth:{type:"Number",desc:"The width of labels. This property cascades to child containers."}
,itemCls:{type:"String",desc:"A css class to apply to the x-form-item of fields. This property cascades to child containers."}
,buttonAlign:{type:"String",desc:"Valid values are \"left,\" \"center\" and \"right\" (defaults to \"center\")"}
,minButtonWidth:{type:"Number",desc:"Minimum width of all buttons in pixels (defaults to 75)"}
,labelAlign:{type:"String",desc:"Valid values are \"left,\" \"top\" and \"right\" (defaults to \"left\"). This property cascades to child containers if not set."}
,monitorValid:{type:"Boolean",desc:"If true the form monitors its valid state <b>client-side</b> and fires a looping event with that state. This is required to bind buttons to the valid state using the config value formBind:true on the button."}
,monitorPoll:{type:"Number",desc:"The milliseconds to poll valid state, ignored if monitorValid is not true (defaults to 200)"}
},


"form/Hidden.js":{
},


"form/HtmlEditor.js":{
enableFormat:{type:"Boolean",desc:"Enable the bold, italic and underline buttons (defaults to true)"}
,enableFontSize:{type:"Boolean",desc:"Enable the increase/decrease font size buttons (defaults to true)"}
,enableColors:{type:"Boolean",desc:"Enable the fore/highlight color buttons (defaults to true)"}
,enableAlignments:{type:"Boolean",desc:"Enable the left, center, right alignment buttons (defaults to true)"}
,enableLists:{type:"Boolean",desc:"Enable the bullet and numbered list buttons. Not available in Safari. (defaults to true)"}
,enableSourceEdit:{type:"Boolean",desc:"Enable the switch to source edit button. Not available in Safari. (defaults to true)"}
,enableLinks:{type:"Boolean",desc:"Enable the create link button. Not available in Safari. (defaults to true)"}
,enableFont:{type:"Boolean",desc:"Enable font selection. Not available in Safari. (defaults to true)"}
,createLinkText:{type:"String",desc:"The default text for the create link prompt"}
,defaultLinkValue:{type:"String",desc:"The default value for the create link prompt (defaults to http:/ /)"}
,fontFamilies:{type:"Array",desc:"An array of available font families"}
},


"form/NumberField.js":{
fieldClass:{type:"String",desc:"The default CSS class for the field (defaults to \"x-form-field x-form-num-field\")"}
,allowDecimals:{type:"Boolean",desc:"False to disallow decimal values (defaults to true)"}
,decimalSeparator:{type:"String",desc:"Character(s) to allow as the decimal separator (defaults to '.')"}
,decimalPrecision:{type:"Number",desc:"The maximum precision to display after the decimal separator (defaults to 2)"}
,allowNegative:{type:"Boolean",desc:"False to prevent entering a negative sign (defaults to true)"}
,minValue:{type:"Number",desc:"The minimum allowed value (defaults to Number.NEGATIVE_INFINITY)"}
,maxValue:{type:"Number",desc:"The maximum allowed value (defaults to Number.MAX_VALUE)"}
,minText:{type:"String",desc:"Error text to display if the minimum value validation fails (defaults to \"The minimum value for this field is {minValue}\")"}
,maxText:{type:"String",desc:"Error text to display if the maximum value validation fails (defaults to \"The maximum value for this field is {maxValue}\")"}
,nanText:{type:"String",desc:"Error text to display if the value is not a valid number. For example, this can happen if a valid character like '.' or '-' is left in the field with no number (defaults to \"{value} is not a valid number\")"}
},


"form/Radio.js":{
},


"form/TextArea.js":{
growMin:{type:"Number",desc:"The minimum height to allow when grow = true (defaults to 60)"}
,growMax:{type:"Number",desc:"The maximum height to allow when grow = true (defaults to 1000)"}
,preventScrollbars:{type:"Boolean",desc:"True to prevent scrollbars from appearing regardless of how much text is in the field (equivalent to setting overflow: hidden, defaults to false)"}
,autoCreate:{type:"String/Object",desc:"A DomHelper element spec, or true for a default element spec (defaults to {tag: \"textarea\", style: \"width:100px;height:60px;\", autocomplete: \"off\"})"}
},


"form/TextField.js":{
grow:{type:"Boolean",desc:"True if this field should automatically grow and shrink to its content"}
,growMin:{type:"Number",desc:"The minimum width to allow when grow = true (defaults to 30)"}
,growMax:{type:"Number",desc:"The maximum width to allow when grow = true (defaults to 800)"}
,vtype:{type:"String",desc:"A validation type name as defined in {@link Ext.form.VTypes} (defaults to null)"}
,maskRe:{type:"String",desc:"An input mask regular expression that will be used to filter keystrokes that don't match (defaults to null)"}
,disableKeyFilter:{type:"Boolean",desc:"True to disable input keystroke filtering (defaults to false)"}
,allowBlank:{type:"Boolean",desc:"False to validate that the value length > 0 (defaults to true)"}
,minLength:{type:"Number",desc:"Minimum input field length required (defaults to 0)"}
,maxLength:{type:"Number",desc:"Maximum input field length allowed (defaults to Number.MAX_VALUE)"}
,minLengthText:{type:"String",desc:"Error text to display if the minimum length validation fails (defaults to \"The minimum length for this field is {minLength}\")"}
,maxLengthText:{type:"String",desc:"Error text to display if the maximum length validation fails (defaults to \"The maximum length for this field is {maxLength}\")"}
,selectOnFocus:{type:"Boolean",desc:"True to automatically select any existing field text when the field receives input focus (defaults to false)"}
,blankText:{type:"String",desc:"Error text to display if the allow blank validation fails (defaults to \"This field is required\")"}
,validator:{type:"Function",desc:"A custom validation function to be called during field validation (defaults to null). If available, this function will be called only after the basic validators all return true, and will be passed the current field value and expected to return boolean true if the value is valid or a string error message if invalid."}
,regex:{type:"RegExp",desc:"A JavaScript RegExp object to be tested against the field value during validation (defaults to null). If available, this regex will be evaluated only after the basic validators all return true, and will be passed the current field value. If the test fails, the field will be marked invalid using {@link #regexText}."}
,regexText:{type:"String",desc:"The error text to display if {@link #regex} is used and the test fails during validation (defaults to \"\")"}
,emptyText:{type:"String",desc:"The default text to display in an empty field (defaults to null)."}
,emptyClass:{type:"String",desc:"The CSS class to apply to an empty field to style the {@link #emptyText} (defaults to 'x-form-empty-field'). This class is automatically added and removed as needed depending on the current field value."}
},


"form/TimeField.js":{
minValue:{type:"Date/String",desc:"The minimum allowed time. Can be either a Javascript date object or a string date in a valid format (defaults to null)."}
,maxValue:{type:"Date/String",desc:"The maximum allowed time. Can be either a Javascript date object or a string date in a valid format (defaults to null)."}
,minText:{type:"String",desc:"The error text to display when the date in the cell is before minValue (defaults to 'The time in this field must be equal to or after {0}')."}
,maxText:{type:"String",desc:"The error text to display when the time is after maxValue (defaults to 'The time in this field must be equal to or before {0}')."}
,invalidText:{type:"String",desc:"The error text to display when the time in the field is invalid (defaults to '{value} is not a valid time - it must be in the format {format}')."}
,format:{type:"String",desc:"The default date format string which can be overriden for localization support. The format must be valid according to {@link Date#parseDate} (defaults to 'm/d/y')."}
,altFormats:{type:"String",desc:"Multiple date formats separated by \"|\" to try when parsing a user input value and it doesn't match the defined format (defaults to 'm/d/Y|m-d-y|m-d-Y|m/d|m-d|d')."}
,increment:{type:"Number",desc:"The number of minutes between each time value in the list (defaults to 15)."}
},


"form/TriggerField.js":{
triggerClass:{type:"String",desc:"An additional CSS class used to style the trigger button. The trigger will always get the class 'x-form-trigger' by default and triggerClass will be <b>appended</b> if specified. @constructor Create a new TriggerField. @param {Object} config Configuration options (valid {@Ext.form.TextField} config options will also be applied to the base TextField)"}
,triggerClass:{type:"String",desc:"A CSS class to apply to the trigger"}
,autoCreate:{type:"String/Object",desc:"A DomHelper element spec, or true for a default element spec (defaults to {tag: \"input\", type: \"text\", size: \"16\", autocomplete: \"off\"})"}
,hideTrigger:{type:"Boolean",desc:"True to hide the trigger element and display only the base text field (defaults to false)"}
},


"form/VTypes.js":{
},


"grid/AbstractSelectionModel.js":{
},


"grid/CellSelectionModel.js":{
},


"grid/CheckboxSelectionModel.js":{
header:{type:"String",desc:"Any valid text or HTML fragment to display in the header cell for the checkbox column (defaults to '&lt;div class=\"x-grid3-hd-checker\">&#160;&lt;/div>'). The default CSS class of 'x-grid3-hd-checker' displays a checkbox in the header and provides support for automatic check all/none behavior on header click. This string can be replaced by any valid HTML fragment, including a simple text string (e.g., 'Select Rows'), but the automatic check all/none behavior will only work if the 'x-grid3-hd-checker' class is supplied."}
,width:{type:"Number",desc:"The default width in pixels of the checkbox column (defaults to 20)."}
,sortable:{type:"Boolean",desc:"True if the checkbox column is sortable (defaults to false)."}
},


"grid/ColumnDD.js":{
},


"grid/ColumnModel.js":{
id:{type:"String",desc:"(Optional) Defaults to the column's initial ordinal position. A name which identifies this column. The id is used to create a CSS class which is applied to all table cells in that column of the form <pre>x-grid-td-<b>id<b></pre> <br><br> The {@link Ext.grid.Grid#autoExpandColumn} grid config option references the column via this identifier."}
,header:{type:"String",desc:"The header text to display in the Grid view."}
,dataIndex:{type:"String",desc:"(Optional) The name of the field in the grid's {@link Ext.data.Store}'s {@link Ext.data.Record} definition from which to draw the column's value. If not specified, the column's index is used as an index into the Record's data Array."}
,width:{type:"Number",desc:"(Optional) The initial width in pixels of the column. Using this instead of {@link Ext.grid.Grid#autoSizeColumns} is more efficient."}
,sortable:{type:"Boolean",desc:"(Optional) True if sorting is to be allowed on this column. Defaults to the value of the {@link #defaultSortable} property. Whether local/remote sorting is used is specified in {@link Ext.data.Store#remoteSort}."}
,locked:{type:"Boolean",desc:"(Optional) True to lock the column in place while scrolling the Grid. Defaults to false."}
,fixed:{type:"Boolean",desc:"(Optional) True if the column width cannot be changed. Defaults to false."}
,resizable:{type:"Boolean",desc:"(Optional) False to disable column resizing. Defaults to true."}
,hidden:{type:"Boolean",desc:"(Optional) True to hide the column. Defaults to false."}
,renderer:{type:"Function",desc:"(Optional) A function used to generate HTML markup for a cell given the cell's data value. See {@link #setRenderer}. If not specified, the default renderer uses the raw data value."}
,align:{type:"String",desc:"(Optional) Set the CSS text-align property of the column. Defaults to undefined."}
,mapping:{type:"String",desc:"(Optional) Set the data mapping."}
},


"grid/ColumnSplitDD.js":{
},


"grid/EditorGrid.js":{
clicksToEdit:{type:"Number",desc:"The number of clicks on a cell required to display the cell's editor (defaults to 2)"}
},


"grid/GridDD.js":{
},


"grid/GridEditor.js":{
},


"grid/GridPanel.js":{
columns:{type:"Array",desc:"An array of columns to auto create a ColumnModel"}
,store:{type:"Store",desc:"The Ext.data.Store the grid should use as it's data source"}
,cm:{type:"Store",desc:"An Ext.grid.ColumnModel for this grid"}
,sm:{type:"Object",desc:"The SelectionModel the grid should use to handle selections (shortcut of selModel)"}
,selModel:{type:"Object",desc:"The SelectionModel the grid should use to handle selections"}
,columns:{type:"Array",desc:"An array of columns to auto create a ColumnModel"}
,columns:{type:"Array",desc:"An array of columns to auto create a ColumnModel"}
,minColumnWidth:{type:"Number",desc:"The minimum width a column can be resized to. Defaults to 25."}
,monitorWindowResize:{type:"Boolean",desc:"True to autoSize the grid when the window resizes. Defaults to true."}
,maxRowsToMeasure:{type:"Boolean",desc:"If autoSizeColumns is on, maxRowsToMeasure can be used to limit the number of rows measured to get a columns size - defaults to 0 (all rows)."}
,trackMouseOver:{type:"Boolean",desc:"True to highlight rows when the mouse is over. Default is false."}
,enableDragDrop:{type:"Boolean",desc:"True to enable drag and drop of rows."}
,enableColumnMove:{type:"Boolean",desc:"True to enable drag and drop reorder of columns."}
,enableColumnHide:{type:"Boolean",desc:"True to enable hiding of columns with the header context menu."}
,enableHdMenu:{type:"Boolean",desc:"True to enable the drop down button for menu in the headers."}
,enableRowHeightSync:{type:"Boolean",desc:"True to manually sync row heights across locked and not locked rows."}
,stripeRows:{type:"Boolean",desc:"True to stripe the rows. Default is true."}
,autoExpandColumn:{type:"String",desc:"The id of a column in this grid that should expand to fill unused space. This id can not be 0."}
,autoExpandMin:{type:"Number",desc:"The minimum width the autoExpandColumn can have (if enabled). defaults to 50."}
,autoExpandMax:{type:"Number",desc:"The maximum width the autoExpandColumn can have (if enabled). Defaults to 1000."}
,view:{type:"Object",desc:"The {@link Ext.grid.GridView} used by the grid. This can be set before a call to render()."}
,loadMask:{type:"Object",desc:"An {@link Ext.LoadMask} config or true to mask the grid while loading (defaults to false)."}
,disableSelection:{type:"Boolean",desc:"(defaults to false)."}
,maxHeight:{type:"Number",desc:"Sets the maximum height of the grid - ignored if autoHeight is not on."}
},


"grid/GridView.js":{
enableRowBody:{type:"Boolean",desc:"True to add a second TR element per row that can be used to provide a row body that spans beneath the data row. Use the {@link #getRowClass} method's rowParams config to customize the row body."}
,autoFill:{type:"Boolean",desc:"True to auto expand the columns to fit the grid <b>when the grid is created</b>."}
,forceFit:{type:"Boolean",desc:"True to auto expand/contract the size of the columns to fit the grid width and prevent horizontal scrolling."}
},


"grid/GroupingView.js":{
hideGroupedColumn:{type:"Boolean",desc:"True to hide the column that is currently grouped"}
,hideGroupedColumn:{type:"Boolean",desc:"True to hide the column that is currently grouped"}
,startCollapsed:{type:"Boolean",desc:"True to start all groups collapsed"}
,enableGroupingMenu:{type:"Boolean",desc:"True to enable the grouping control in the column menu"}
,enableNoGroups:{type:"Boolean",desc:"True to allow the user to turn off grouping"}
,emptyGroupText:{type:"String",desc:"The text to display when there is an empty group value"}
,groupTextTpl:{type:"String",desc:"The template used to render the group text"}
},


"grid/PropertyGrid.js":{
},


"grid/RowNumberer.js":{
header:{type:"String",desc:"Any valid text or HTML fragment to display in the header cell for the row number column (defaults to '')."}
,width:{type:"Number",desc:"The default width in pixels of the row number column (defaults to 23)."}
,sortable:{type:"Boolean",desc:"True if the row number column is sortable (defaults to false)."}
},


"grid/RowSelectionModel.js":{
singleSelect:{type:"Boolean",desc:"True to allow selection of only one row at a time (defaults to false)"}
},


"Layer.js":{
shim:{type:"Boolean",desc:"False to disable the iframe shim in browsers which need one (defaults to true)"}
,shadow:{type:"String/Boolean",desc:"True to create a shadow element with default class \"x-layer-shadow\", or you can pass a string with a CSS class name. False turns off the shadow."}
,dh:{type:"Object",desc:"DomHelper object config to create element with (defaults to {tag: \"div\", cls: \"x-layer\"})."}
,constrain:{type:"Boolean",desc:"False to disable constrain to viewport (defaults to true)"}
,cls:{type:"String",desc:"CSS class to add to the element"}
,zindex:{type:"Number",desc:"Starting z-index (defaults to 11000)"}
,shadowOffset:{type:"Number",desc:"Number of pixels to offset the shadow (defaults to 3) @constructor @param {Object} config An object with config options. @param {String/HTMLElement} existingEl (optional) Uses an existing DOM element. If the element is not found it creates it."}
},


"layout/AccordianLayout.js":{
fill:{type:"Boolean",desc:"True to adjust the active item's height to fill the available space in the container, false to use the item's current height, or auto height if not explicitly set (defaults to true)."}
,autoWidth:{type:"Boolean",desc:"True to set each contained item's width to 'auto', false to use the item's current width (defaults to true)."}
,titleCollapse:{type:"Boolean",desc:"True to allow expand/collapse of each contained panel by clicking anywhere on the title bar, false to allow expand/collapse only when the toggle tool button is clicked (defaults to true). When set to false, {@link #hideCollapseTool} should be false also."}
,hideCollapseTool:{type:"Boolean",desc:"True to hide the contained panels' collapse/expand toggle buttons, false to display them (defaults to false). When set to true, {@link #titleCollapse} should be true also."}
,collapseFirst:{type:"Boolean",desc:"True to make sure the collapse/expand toggle button always renders first (to the left of) any other tools in the contained panels' title bars, false to render it last (defaults to false)."}
,animate:{type:"Boolean",desc:"True to slide the contained panels open and closed during expand/collapse using animation, false to open and close directly with no animation (defaults to false). Note: to defer to the specific config setting of each contained panel for this property, set this to undefined at the layout level."}
,activeOnTop:{type:"Boolean",desc:"True to swap the position of each panel as it is expanded so that it becomes the first item in the container, false to keep the panels in the rendered order (defaults to false)."}
},


"layout/AnchorLayout.js":{
},


"layout/BorderLayout.js":{
animFloat:{type:"Boolean",desc:"When a collapsed region's bar is clicked, the region's panel will be displayed as a floated panel that will close again once the user mouses out of that panel (or clicks out if autoHide = false). Setting animFloat to false will prevent the open and close of these floated panels from being animated (defaults to true)."}
,autoHide:{type:"Boolean",desc:"When a collapsed region's bar is clicked, the region's panel will be displayed as a floated panel. If autoHide is true, the panel will automatically hide after the user mouses out of the panel. If autoHide is false, the panel will continue to display until the user clicks outside of the panel (defaults to true)."}
,collapseMode:{type:"String",desc:"By default, collapsible regions are collapsed by clicking the expand/collapse tool button that renders into the region's title bar. Optionally, when collapseMode is set to 'mini' the region's split bar will also display a small collapse button in the center of the bar. In 'mini' mode the region will collapse to a thinner bar than in normal mode. By default collapseMode is undefined, and the only two supported values are undefined and 'mini'. Note that if a collapsible region does not have a title bar, then collapseMode must be set to 'mini' in order for the region to be collapsible by the user as the tool button will not be rendered."}
,margins:{type:"Object",desc:"An object containing margins to apply to the region in the format {left: (left margin), top: (top margin), right: (right margin), bottom: (bottom margin)}"}
,cmargins:{type:"Object",desc:"An object containing margins to apply to the region's collapsed element in the format {left: (left margin), top: (top margin), right: (right margin), bottom: (bottom margin)}"}
,collapsible:{type:"Boolean",desc:"True to allow the user to collapse this region (defaults to false). If true, an expand/collapse tool button will automatically be rendered into the title bar of the region, otherwise the button will not be shown. Note that a title bar is required to display the toggle button -- if no region title is specified, the region will only be collapsible if {@link #collapseMode} is set to 'mini'."}
,split:{type:"Boolean",desc:"True to display a {@link Ext.SplitBar} between this region and its neighbor, allowing the user to resize the regions dynamically (defaults to false). When split = true, it is common to specify a {@link #minSize} and {@link #maxSize} for the region."}
,floatable:{type:"Boolean",desc:"True to allow clicking a collapsed region's bar to display the region's panel floated above the layout, false to force the user to fully expand a collapsed region by clicking the expand button to see it again (defaults to true)."}
,minWidth:{type:"Number",desc:"The minimum allowable width in pixels for this region (defaults to 50)"}
,minHeight:{type:"Number",desc:"The minimum allowable height in pixels for this region (defaults to 50)"}
,splitTip:{type:"String",desc:"The tooltip to display when the user hovers over a non-collapsible region's split bar (defaults to \"Drag to resize.\"). Only applies if {@link #useSplitTips} = true."}
,collapsibleSplitTip:{type:"String",desc:"The tooltip to display when the user hovers over a collapsible region's split bar (defaults to \"Drag to resize. Double click to hide.\"). Only applies if {@link #useSplitTips} = true."}
,useSplitTips:{type:"Boolean",desc:"True to display a tooltip when the user hovers over a region's split bar (defaults to false). The tooltip text will be the value of either {@link #splitTip} or {@link #collapsibleSplitTip} as appropriate."}
},


"layout/CardLayout.js":{
deferredRender:{type:"Boolean",desc:"True to render each contained item at the time it becomes active, false to render all contained items as soon as the layout is rendered (defaults to false). If there is a significant amount of content or a lot of heavy controls being rendered into panels that are not displayed by default, setting this to true might improve performance."}
},


"layout/ColumnLayout.js":{
},


"layout/ContainerLayout.js":{
extraCls:{type:"String",desc:"An optional extra CSS class that will be added to the container (defaults to ''). This can be useful for adding customized styles to the container or any of its children using standard CSS rules."}
,renderHidden:{type:"Boolean",desc:"True to hide each contained item on render (defaults to false)."}
},


"layout/FitLayout.js":{
},


"layout/FormLayout.js":{
labelStyle:{type:"String",desc:"A CSS style specification string to add to each field label in this layout (defaults to '')."}
,elementStyle:{type:"String",desc:"A CSS style specification string to add to each field element in this layout (defaults to '')."}
,labelSeparator:{type:"String",desc:"The standard separator to display after the text of each form label (defaults to a colon ':'). To turn off separators completely specify empty string ''."}
},


"layout/TableLayout.js":{
columns:{type:"Number",desc:"The total number of columns to create in the table for this layout. If not specified, all panels added to this layout will be rendered into a single row using a column per panel."}
},


"LoadMask.js":{
removeMask:{type:"Boolean",desc:"True to create a single-use mask that is automatically destroyed after loading (useful for page loads), False to persist the mask element reference for multiple uses (e.g., for paged data widgets). Defaults to false."}
,msg:{type:"String",desc:"The text to display in a centered loading message box (defaults to 'Loading...')"}
,msgCls:{type:"String",desc:"The CSS class to apply to the loading message element (defaults to \"x-mask-loading\")"}
},


"menu/Adapter.js":{
},


"menu/BaseItem.js":{
handler:{type:"Function",desc:"A function that will handle the click event of this menu item (defaults to undefined)"}
,canActivate:{type:"Boolean",desc:"True if this item can be visually activated (defaults to false)"}
,activeClass:{type:"String",desc:"The CSS class to use when the item becomes activated (defaults to \"x-menu-item-active\")"}
,hideOnClick:{type:"Boolean",desc:"True to hide the containing menu after this item is clicked (defaults to true)"}
,hideDelay:{type:"Number",desc:"Length of time in milliseconds to wait before hiding after a click (defaults to 100)"}
},


"menu/CheckItem.js":{
group:{type:"String",desc:"All check items with the same group name will automatically be grouped into a single-select radio button group (defaults to '')"}
,itemCls:{type:"String",desc:"The default CSS class to use for check items (defaults to \"x-menu-item x-menu-check-item\")"}
,groupClass:{type:"String",desc:"The default CSS class to use for radio group check items (defaults to \"x-menu-group-item\")"}
,checked:{type:"Boolean",desc:"True to initialize this checkbox as checked (defaults to false). Note that if this checkbox is part of a radio group (group = true) only the last item in the group that is initialized with checked = true will be rendered as checked."}
},


"menu/ColorItem.js":{
},


"menu/ColorMenu.js":{
},


"menu/DateItem.js":{
},


"menu/DateMenu.js":{
},


"menu/Item.js":{
icon:{type:"String",desc:"The path to an icon to display in this menu item (defaults to Ext.BLANK_IMAGE_URL)"}
,text:{type:"String",desc:"The text to display for this item (defaults to '')"}
,iconCls:{type:"String",desc:"A CSS class which sets a background image to be used as the icon for this item (defaults to '')"}
,itemCls:{type:"String",desc:"The default CSS class to use for menu items (defaults to 'x-menu-item')"}
,canActivate:{type:"Boolean",desc:"True if this item can be visually activated (defaults to true)"}
,showDelay:{type:"Number",desc:"Length of time in milliseconds to wait before showing this item (defaults to 200)"}
},


"menu/Menu.js":{
defaults:{type:"Object",desc:"A config object that will be applied to all items added to this container either via the {@link #items} config or via the {@link #add} method. The defaults config can contain any number of name/value property pairs to be added to each item, and should be valid for the types of items being added to the menu."}
,items:{type:"Mixed",desc:"An array of items to be added to this menu. See {@link #add} for a list of valid item types."}
,minWidth:{type:"Number",desc:"The minimum width of the menu in pixels (defaults to 120)"}
,shadow:{type:"Boolean/String",desc:"True or \"sides\" for the default effect, \"frame\" for 4-way shadow, and \"drop\" for bottom-right shadow (defaults to \"sides\")"}
,subMenuAlign:{type:"String",desc:"The {@link Ext.Element#alignTo} anchor position value to use for submenus of this menu (defaults to \"tl-tr?\")"}
,defaultAlign:{type:"String",desc:"The default {@link Ext.Element#alignTo) anchor position value for this menu relative to its element of origin (defaults to \"tl-bl?\")"}
,allowOtherMenus:{type:"Boolean",desc:"True to allow multiple menus to be displayed at the same time (defaults to false)"}
},


"menu/MenuMgr.js":{
},


"menu/Separator.js":{
itemCls:{type:"String",desc:"The default CSS class to use for separators (defaults to \"x-menu-sep\")"}
,hideOnClick:{type:"Boolean",desc:"True to hide the containing menu after this item is clicked (defaults to false)"}
},


"menu/TextItem.js":{
text:{type:"String",desc:"The text to display for this item (defaults to '')"}
,hideOnClick:{type:"Boolean",desc:"True to hide the containing menu after this item is clicked (defaults to false)"}
,itemCls:{type:"String",desc:"The default CSS class to use for text items (defaults to \"x-menu-text\")"}
},


"MessageBox.js":{
},


"PagingToolbar.js":{
displayInfo:{type:"Boolean",desc:"True to display the displayMsg (defaults to false)"}
,pageSize:{type:"Number",desc:"The number of records to display per page (defaults to 20)"}
,displayMsg:{type:"String",desc:"The paging status message to display (defaults to \"Displaying {start} - {end} of {total}\")"}
,emptyMsg:{type:"String",desc:"The message to display when no records are found (defaults to \"No data to display\")"}
},


"PanelDD.js":{
},


"Panel.js":{
tbar:{type:"Object/Array",desc:"The top toolbar of the panel. This can be a {@link Ext.Toolbar} object, a toolbar config, or an array of buttons/button configs to be added to the toolbar. Note that this is not available as a property after render. To access the top toolbar after render, use {@link #getTopToolbar}."}
,bbar:{type:"Object/Array",desc:"The bottom toolbar of the panel. This can be a {@link Ext.Toolbar} object, a toolbar config, or an array of buttons/button configs to be added to the toolbar. Note that this is not available as a property after render. To access the bottom toolbar after render, use {@link #getBottomToolbar}."}
,header:{type:"Boolean",desc:"True to create the header element explicitly, false to skip creating it. By default, when header is not specified, if a {@link #title} is set the header will be created automatically, otherwise it will not. If a title is set but header is explicitly set to false, the header will not be rendered."}
,footer:{type:"Boolean",desc:"True to create the footer element explicitly, false to skip creating it. By default, when footer is not specified, if one or more buttons have been added to the panel the footer will be created automatically, otherwise it will not."}
,title:{type:"String",desc:"The title text to display in the panel header (defaults to ''). When a title is specified the header element will automatically be created and displayed unless {@link #header} is explicitly set to false."}
,buttons:{type:"Array",desc:"An array of {@link Ext.Button} instances (or valid button configs) to add to the footer of this panel"}
,autoLoad:{type:"Object/String/Function",desc:"A valid url spec according to the Updater {@link Ext.Updater#update} method. If autoLoad is not null, the panel will attempt to laod its contents immediately upon render."}
,frame:{type:"Boolean",desc:"True to render the panel with custom rounded borders, false to render with plain 1px square borders (defaults to false)."}
,border:{type:"Boolean",desc:"True to display the borders of the panel's body element, false to hide them (defaults to true). By default, the border is a 2px wide inset border, but this can be further altered by setting {@link #bodyBorder} to false."}
,bodyBorder:{type:"Boolean",desc:"True to display an interior border on the body element of the panel, false to hide it (defaults to true). This only applies when {@link #border} = true. If border = true and bodyBorder = false, the border will display as a 1px wide inset border, giving the entire body element an inset appearance."}
,bodyStyle:{type:"String",desc:"Custom CSS styles to be applied to the body element in the format expected by {@link Ext.Element#applyStyles} (defaults to null)."}
,iconCls:{type:"String",desc:"A CSS class that will provide a background image to be used as the panel header icon (defaults to '')."}
,collapsible:{type:"Boolean",desc:"True to make the panel collapsible and have the expand/collapse toggle button automatically rendered into the header tool button area, false to keep the panel statically sized with no button (defaults to false)."}
,tools:{type:"Array",desc:"An array of tool button configs to be added to the header tool area. Each tool config should contain the id of the tool, and can also contain an 'on' event handler config containing one or more event handlers to assign to this tool. The optional property 'hidden:true' can be included to hide the tool by default. Example usage: <pre><code> tools:[{ id:'refresh', // hidden:true, on:{ click: function(){ // refresh logic } } }] </code></pre> A valid tool id should correspond to the CSS classes 'x-tool-{id}' (normal) and 'x-tool-{id}-over' (mouseover). By default, the following tools are provided: toggle (default when collapsible = true), close, minimize, maximize, restore, gear, pin, unpin, right, left, up, down, refresh, minus, plus, search and save. Note that these tool classes only provide the visual button -- any required functionality must be provided by adding event handlers that implement the necessary behavior."}
,hideCollapseTool:{type:"Boolean",desc:"True to hide the expand/collapse toggle button when {@link #collapsible} = true, false to display it (defaults to false)."}
,titleCollapse:{type:"Boolean",desc:"True to allow expanding and collapsing the panel (when {@link #collapsible} = true) by clicking anywhere in the header bar, false to allow it only by clicking to tool button (defaults to false)."}
,autoScroll:{type:"Boolean",desc:"True to use overflow:'auto' on the panel's body element and show scroll bars automatically when necessary, false to clip any overflowing content (defaults to false)."}
,floating:{type:"Boolean",desc:"True to float the panel (absolute position it with automatic shimming and shadow), false to display it inline where it is rendered (defaults to false). Note that by default, setting floating to true will cause the panel to display at negative offsets so that it is hidden -- because the panel is absolute positioned, the position must be set explicitly after render (e.g., myPanel.setPosition(100,100);). Also, when floating a panel you should always assign a fixed width, otherwise it will be auto width and will expand to fill to the right edge of the viewport."}
,shadow:{type:"Boolean/String",desc:"True (or a valid Ext.Shadow {@link Ext.Shadow#mode} value) to display a shadow behind the panel, false to display no shadow (defaults to 'sides'). Note that this option opnly applies when floating = true."}
,shim:{type:"Boolean",desc:"False to disable the iframe shim in browsers which need one (defaults to true). Note that this option opnly applies when floating = true."}
,html:{type:"String",desc:"An arbitrary HTML fragment to use as the panel's body content (defaults to '')."}
,contentEl:{type:"String",desc:"The id of an existing HTML node to use as the panel's body content (defaults to '')."}
,keys:{type:"Object/Array",desc:"A KeyMap config object (in the format expected by {@link Ext.KeyMap#addBinding) used to assign custom key handling to this panel (defaults to null)."}
,baseCls:{type:"String",desc:"The base CSS class to apply to this panel's element (defaults to 'x-panel')."}
,collapsedCls:{type:"String",desc:"A CSS class to add to the panel's element after it has been collapsed (defaults to 'x-panel-collapsed')."}
,maskDisabled:{type:"Boolean",desc:"True to mask the panel when it is disabled, false to not mask it (defaults to true). Either way, the panel will always tell its contained elements to disable themselves when it is disabled, but masking the panel can provide an additional visual cue that the panel is disabled."}
,animCollapse:{type:"Boolean",desc:"True to animate the transition when the panel is collapsed, false to skip the animation (defaults to true if the {@link Ext.Fx} class is available, otherwise false)."}
,headerAsText:{type:"Boolean",desc:"True to display the panel title in the header, false to hide it (defaults to true)."}
,buttonAlign:{type:"String",desc:"The alignment of any buttons added to this panel. Valid values are 'right,' 'left' and 'center' (defaults to 'right')."}
,collapsed:{type:"Boolean",desc:"True to render the panel collapsed, false to render it expanded (defaults to false)."}
,collapseFirst:{type:"Boolean",desc:"True to make sure the collapse/expand toggle button always renders first (to the left of) any other tools in the panel's title bar, false to render it last (defaults to true)."}
,minButtonWidth:{type:"Number",desc:"Minimum width in pixels of all buttons in this panel (defaults to 75)"}
,elements:{type:"String",desc:"A comma-delimited list of panel elements to initialize when the panel is rendered. Normally, this list will be generated automatically based on the items added to the panel at config time, but sometimes it might be useful to make sure a structural element is rendered even if not specified at config time (for example, you may want to add a button or toolbar dynamically after the panel has been rendered). Adding those elements to this list will allocate the required placeholders in the panel when it is rendered. Valid values are: 'header,' 'body,' 'footer,' 'tbar' (top bar) abd 'bbar' (bottom bar) - defaults to 'body.'"}
},


"ProgressBar.js":{
value:{type:"Float",desc:"A floating point value between 0 and 1 (e.g., .5, defaults to 0)"}
,text:{type:"String",desc:"The progress bar text (defaults to '')"}
,textEl:{type:"Mixed",desc:"The element to render the progress text to (defaults to the progress bar's internal text element)"}
,id:{type:"String",desc:"The progress bar element's id (defaults to an auto-generated id)"}
,baseCls:{type:"String",desc:"The base CSS class to apply to the progress bar's wrapper element (defaults to 'x-progress')"}
},


"Shadow.js":{
mode:{type:"String",desc:"The shadow display mode. Supports the following options:<br /> sides: Shadow displays on both sides and bottom only<br /> frame: Shadow displays equally on all four sides<br /> drop: Traditional bottom-right drop shadow (default)"}
,offset:{type:"String",desc:"The number of pixels to offset the shadow from the element (defaults to 4)"}
},


"SplitBar.js":{
},


"SplitButton.js":{
arrowHandler:{type:"Function",desc:"A function called when the arrow button is clicked (can be used instead of click event)"}
,arrowTooltip:{type:"String",desc:"The title attribute of the arrow @constructor Create a new menu button @param {Object} config The config object"}
},


"TabPanel.js":{
},


"tips/QuickTip.js":{
},


"tips/QuickTips.js":{
},


"tips/Tip.js":{
minWidth:{type:"Number",desc:"The minimum width of the tip in pixels (defaults to 40)"}
,maxWidth:{type:"Number",desc:"The maximum width of the tip in pixels (defaults to 300)"}
,shadow:{type:"Boolean/String",desc:"True or \"sides\" for the default effect, \"frame\" for 4-way shadow, and \"drop\" for bottom-right shadow (defaults to \"sides\")"}
,defaultAlign:{type:"String",desc:"The default {@link Ext.Element#alignTo) anchor position value for this tip relative to its element of origin (defaults to \"tl-bl?\")"}
},


"tips/ToolTip.js":{
},


"Toolbar.js":{
},


"tree/AsyncTreeNode.js":{
loader:{type:"TreeLoader",desc:"A TreeLoader to be used by this node (defaults to the loader defined on the tree) @constructor @param {Object/String} attributes The attributes/config for the node or just a string with the text for the node "}
},


"tree/TreeDragZone.js":{
ddGroup:{type:"String",desc:"A named drag drop group to which this object belongs. If a group is specified, then this object will only interact with other drag drop objects in the same group (defaults to 'TreeDD')."}
},


"tree/TreeDropZone.js":{
allowParentInsert:{type:"Boolean",desc:"Allow inserting a dragged node between an expanded parent node and its first child that will become a sibling of the parent when dropped (defaults to false)"}
,allowContainerDrop:{type:"String",desc:"True if drops on the tree container (outside of a specific tree node) are allowed (defaults to false)"}
,appendOnly:{type:"String",desc:"True if the tree should only allow append drops (use for trees which are sorted, defaults to false)"}
,ddGroup:{type:"String",desc:"A named drag drop group to which this object belongs. If a group is specified, then this object will only interact with other drag drop objects in the same group (defaults to 'TreeDD')."}
,expandDelay:{type:"String",desc:"The delay in milliseconds to wait before expanding a target tree node while dragging a droppable node over the target (defaults to 1000)"}
},


"tree/TreeEditor.js":{
alignment:{type:"String",desc:"The position to align to (see {@link Ext.Element#alignTo} for more details, defaults to \"l-l\")."}
,hideEl:{type:"Boolean",desc:"True to hide the bound element while the editor is displayed (defaults to false)"}
,cls:{type:"String",desc:"CSS class to apply to the editor (defaults to \"x-small-editor x-tree-editor\")"}
,shim:{type:"Boolean",desc:"True to shim the editor if selects/iframes could be displayed beneath it (defaults to false)"}
,maxWidth:{type:"Number",desc:"The maximum width in pixels of the editor field (defaults to 250). Note that if the maxWidth would exceed the containing tree element's size, it will be automatically limited for you to the container width, taking scroll and client offsets into account prior to each edit."}
},


"tree/TreeEventModel.js":{
},


"tree/TreeFilter.js":{
},


"tree/TreeLoader.js":{
dataUrl:{type:"String",desc:"The URL from which to request a Json string which specifies an array of node definition object representing the child nodes to be loaded."}
,url:{type:"String",desc:"Equivalent to {@link #dataUrl}."}
,preloadChildren:{type:"Boolean",desc:"If set to true, the loader recursively loads \"children\" attributes when doing the first load on nodes."}
,baseParams:{type:"Object",desc:"(optional) An object containing properties which specify HTTP parameters to be passed to each request for child nodes."}
,baseAttrs:{type:"Object",desc:"(optional) An object containing attributes to be added to all nodes created by this loader. If the attributes sent by the server have an attribute in this object, they take priority."}
,uiProviders:{type:"Object",desc:"(optional) An object containing properties which specify custom {@link Ext.tree.TreeNodeUI} implementations. If the optional <i>uiProvider</i> attribute of a returned child node is a string rather than a reference to a TreeNodeUI implementation, this that string value is used as a property name in the uiProviders object."}
,clearOnLoad:{type:"Boolean",desc:"(optional) Default to true. Remove previously existing child nodes before loading."}
},


"tree/TreeNode.js":{
text:{type:"String",desc:"The text for this node"}
,expanded:{type:"Boolean",desc:"true to start the node expanded"}
,allowDrag:{type:"Boolean",desc:"false to make this node undraggable if DD is on (defaults to true)"}
,allowDrop:{type:"Boolean",desc:"false if this node cannot be drop on"}
,disabled:{type:"Boolean",desc:"true to start the node disabled"}
,icon:{type:"String",desc:"The path to an icon for the node. The preferred way to do this is to use the cls or iconCls attributes and add the icon via a CSS background image."}
,cls:{type:"String",desc:"A css class to be added to the node"}
,iconCls:{type:"String",desc:"A css class to be added to the nodes icon element for applying css background images"}
,href:{type:"String",desc:"URL of the link used for the node (defaults to #)"}
,hrefTarget:{type:"String",desc:"target frame for the link"}
,qtip:{type:"String",desc:"An Ext QuickTip for the node"}
,expandable:{type:"Boolean",desc:"If set to true, the node will always show a plus/minus icon, even when empty"}
,qtipCfg:{type:"String",desc:"An Ext QuickTip config for the node (used instead of qtip)"}
,singleClickExpand:{type:"Boolean",desc:"True for single click expand on this node"}
,uiProvider:{type:"Function",desc:"A UI <b>class</b> to use for this node (defaults to Ext.tree.TreeNodeUI)"}
,checked:{type:"Boolean",desc:"True to render a checked checkbox for this node, false to render an unchecked checkbox (defaults to undefined with no checkbox rendered) @constructor @param {Object/String} attributes The attributes/config for the node or just a string with the text for the node"}
},


"tree/TreeNodeUI.js":{
},


"tree/TreePanel.js":{
rootVisible:{type:"Boolean",desc:"false to hide the root node (defaults to true)"}
,lines:{type:"Boolean",desc:"false to disable tree lines (defaults to true)"}
,enableDD:{type:"Boolean",desc:"true to enable drag and drop"}
,enableDrag:{type:"Boolean",desc:"true to enable just drag"}
,enableDrop:{type:"Boolean",desc:"true to enable just drop"}
,dragConfig:{type:"Object",desc:"Custom config to pass to the {@link Ext.tree.TreeDragZone} instance"}
,dropConfig:{type:"Object",desc:"Custom config to pass to the {@link Ext.tree.TreeDropZone} instance"}
,ddGroup:{type:"String",desc:"The DD group this TreePanel belongs to"}
,ddAppendOnly:{type:"String",desc:"True if the tree should only allow append drops (use for trees which are sorted)"}
,ddScroll:{type:"Boolean",desc:"true to enable body scrolling"}
,containerScroll:{type:"Boolean",desc:"true to register this container with ScrollManager"}
,hlDrop:{type:"Boolean",desc:"false to disable node highlight on drop (defaults to the value of Ext.enableFx)"}
,hlColor:{type:"String",desc:"The color of the node highlight (defaults to C3DAF9)"}
,animate:{type:"Boolean",desc:"true to enable animated expand/collapse (defaults to the value of Ext.enableFx)"}
,singleExpand:{type:"Boolean",desc:"true if only 1 node per branch may be expanded"}
,selModel:{type:"Boolean",desc:"A tree selection model to use with this TreePanel (defaults to a {@link Ext.tree.DefaultSelectionModel})"}
,loader:{type:"Boolean",desc:"A TreeLoader for use with this TreePanel"}
,pathSeparator:{type:"String",desc:"The token used to separate sub-paths in path strings (defaults to '/') @constructor @param {Object} config"}
},


"tree/TreeSelectionModel.js":{
},


"tree/TreeSorter.js":{
folderSort:{type:"Boolean",desc:"True to sort leaf nodes under non leaf nodes"}
,property:{type:"String",desc:"The named attribute on the node to sort by (defaults to text)"}
,dir:{type:"String",desc:"The direction to sort (asc or desc) (defaults to asc)"}
,leafAttr:{type:"String",desc:"The attribute used to determine leaf nodes in folder sort (defaults to \"leaf\")"}
,caseSensitive:{type:"Boolean",desc:"true for case sensitive sort (defaults to false)"}
,sortType:{type:"Function",desc:"A custom \"casting\" function used to convert node values before sorting @constructor @param {TreePanel} tree @param {Object} config"}
},


"Viewport.js":{
},


"Window.js":{
modal:{type:"Boolean",desc:"True to make the window modal and mask everything behind it when displayed, false to display it without restricting access to other UI elements (defaults to false)."}
,animateTarget:{type:"String/Element",desc:"Id or element from which the window should animate while opening (defaults to null with no animation)."}
,resizeHandles:{type:"String",desc:"A valid {@link Ext.Resizable} handles config string (defaults to 'all'). Only applies when resizable = true."}
,manager:{type:"Ext.WindowGroup",desc:"A reference to the WindowGroup that should manage this window (defaults to {@link Ext.WindowMgr})."}
,baseCls:{type:"String",desc:"The base CSS class to apply to this panel's element (defaults to 'x-window')."}
,resizable:{type:"Boolean",desc:"True to allow user resizing at each edge and corner of the window, false to disable resizing (defaults to true)."}
,draggable:{type:"Boolean",desc:"True to allow the window to be dragged by the header bar, false to disable dragging (defaults to true). Note that by default the window will be centered in the viewport, so if dragging is disabled the window may need to be positioned programmatically after render (e.g., myWindow.setPosition(100, 100);)."}
,closable:{type:"Boolean",desc:"True to display the 'close' tool button and allow the user to close the window, false to hide the button and disallow closing the window (default to true)."}
,constrain:{type:"Boolean",desc:"True to constrain the window to the viewport, false to allow it to fall outside of the viewport (defaults to false). Optionally the header only can be constrained using {@link #constrainHeader}."}
,constrainHeader:{type:"Boolean",desc:"True to constrain the window header to the viewport, allowing the window body to fall outside of the viewport, false to allow the header to fall outside the viewport (defaults to false). Optionally the entire window can be constrained using {@link #constrain}."}
,plain:{type:"Boolean",desc:"True to render the window body with a transparent background so that it will blend into the framing elements, false to add a lighter background color to visually highlight the body element and separate it more distinctly from the surrounding frame (defaults to false)."}
,minimizable:{type:"Boolean",desc:"True to display the 'minimize' tool button and allow the user to minimize the window, false to hide the button and disallow minimizing the window (defaults to false). Note that this button provides no implementation -- the behavior of minimizing a window is implementation-specific, so the minimize event must be handled and a custom minimize behavior implemented for this option to be useful."}
,maximizable:{type:"Boolean",desc:"True to display the 'maximize' tool button and allow the user to maximize the window, false to hide the button and disallow maximizing the window (defaults to false). Note that when a window is maximized, the tool button will automatically change to a 'restore' button with the appropriate behavior already built-in that will restore the window to its previous size."}
,minHeight:{type:"Number",desc:"The minimum height in pixels allowed for this window (defaults to 100). Only applies when resizable = true."}
,minWidth:{type:"Number",desc:"The minimum width in pixels allowed for this window (defaults to 200). Only applies when resizable = true."}
,expandOnShow:{type:"Boolean",desc:"True to always expand the window when it is displayed, false to keep it in its current state (which may be collapsed) when displayed (defaults to true)."}
,closeAction:{type:"String",desc:"The action to take when the close button is clicked. The default action is 'close' which will actually remove the window from the DOM and destroy it. The other valid option is 'hide' which will simply hide the window by setting visibility to hidden and applying negative offsets, keeping the window available to be redisplayed via the {@link #show} method."}
},


"WindowManager.js":{
},

"-":{
	hideLabels:{type:"Boolean",desc:"True to hide labels (applies to subpanels, defaults to false"}
},

"grid/ec_grid.js":{
paging:{type:"Boolean",desc:"whether is paging."}
,pageSize:{type:"Number",desc:"the size of one page."}
,searchFormCfg:{type:"Object",desc:"the searchbar config."}
,formConfig:{type:"Object",desc:"the searchbar formconfig."}
,multiSelect:{type:"Boolean",desc:"the select model is multiable."}
,data:{type:"String/Object",desc:"the data model."}
,remoteSort:{type:"Boolean",desc:"the sort is done by the server."}
//,Model:{type:"Object",desc:"model of the grid"}
//,renderer:{type:"Object",desc:"the searchbar formconfig."}
//,tbar:{type:"Object",desc:"the toolbar formconfig."}
},
"ec_button.js":{
confirmMsg:{type:"String",desc:"confirm message."}
,confirmTitle:{type:"String",desc:"title of confirm message."}
,autoExpand:{type:"Number",desc:"the expand level of one tree."}
//,Model:{type:"Object",desc:"model of the grid"}
//,renderer:{type:"Object",desc:"the searchbar formconfig."}
//,tbar:{type:"Object",desc:"the toolbar formconfig."}
}

};
