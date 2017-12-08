package avicit.ui.platform.common.ui;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import avicit.ui.platform.common.data.IDataProvider;

public class ComboBoxCellEditor extends CellEditor
{

    public ComboBoxCellEditor(Composite r_Parent, IDataProvider r_DataProvider)
    {
        this(r_Parent, r_DataProvider, 0);
    }

    public ComboBoxCellEditor(Composite r_Parent, IDataProvider r_DataProvider, int r_Style)
    {
        super(r_Parent, r_Style);
        listProvider = r_DataProvider;
        String t_Items[] = listProvider.getKeys();
        if(t_Items != null)
        {
            comboBox.removeAll();
            comboBox.setItems(t_Items);
            setValueValid(true);
        }
        comboBox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent r_Event)
            {
                keyReleaseOccured(r_Event);
            }
        });
        comboBox.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent r_Event)
            {
                applyEditorValueAndDeactivate();
            }
            public void widgetSelected(SelectionEvent r_Event)
            {
                key = comboBox.getText();
            }
        });
        comboBox.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent r_Event)
            {
                if(r_Event.detail == 2 || r_Event.detail == 4)
                    r_Event.doit = false;
            }
        });
        comboBox.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent r_Event)
            {
                ComboBoxCellEditor.this.focusLost();
            }
        });
    }

    protected Control createControl(Composite r_Parent)
    {
        comboBox = new CCombo(r_Parent, getStyle());
        comboBox.setFont(r_Parent.getFont());
        return comboBox;
    }

    void applyEditorValueAndDeactivate()
    {
        Object newValue = doGetValue();
        markDirty();
        boolean isValid = isCorrect(newValue);
        setValueValid(isValid);
        fireApplyEditorValue();
        deactivate();
    }

    protected Object doGetValue()
    {
        return key;
    }

    protected void doSetFocus()
    {
        comboBox.setFocus();
    }

    protected void doSetValue(Object r_Value)
    {
        Assert.isTrue(comboBox != null);
        key = listProvider.getKey(r_Value);
        if(key != null)
            comboBox.setText(key);
    }

    protected void focusLost()
    {
        if(isActivated())
            applyEditorValueAndDeactivate();
    }

    protected void keyReleaseOccured(KeyEvent r_Event)
    {
        if(r_Event.character == '\033')
            fireCancelEditor();
        else
        if(r_Event.character == '\t')
            applyEditorValueAndDeactivate();
    }

    private IDataProvider listProvider;
    CCombo comboBox;
    String key;
    private static final int defaultStyle = 0;
}