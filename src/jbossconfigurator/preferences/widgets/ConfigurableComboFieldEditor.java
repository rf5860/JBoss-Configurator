package jbossconfigurator.preferences.widgets;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ConfigurableComboFieldEditor extends FieldEditor {

	public ConfigurableComboFieldEditor(String name, String labelText, String entryNamesAndValues[][], Composite parent) {
		init(name, labelText);
		Assert.isTrue(checkArray(entryNamesAndValues));
		fEntryNamesAndValues = entryNamesAndValues;
		createControl(parent);
	}

	private boolean checkArray(String table[][]) {
		if (table == null)
			return false;
		for (int i = 0; i < table.length; i++) {
			String array[] = table[i];
			if (array == null || array.length != 2)
				return false;
		}

		return true;
	}

	protected void adjustForNumColumns(int numColumns) {
		if (numColumns > 1) {
			Control control = getLabelControl();
			int left = numColumns;
			if (control != null) {
				((GridData) control.getLayoutData()).horizontalSpan = 1;
				left--;
			}
			((GridData) fCombo.getLayoutData()).horizontalSpan = left;
		} else {
			Control control = getLabelControl();
			if (control != null)
				((GridData) control.getLayoutData()).horizontalSpan = 1;
			((GridData) fCombo.getLayoutData()).horizontalSpan = 1;
		}
	}

	protected void doFillIntoGrid(Composite parent, int numColumns) {
		int comboC = 1;
		if (numColumns > 1)
			comboC = numColumns - 1;
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
		control = getComboBoxControl(parent);
		gd = new GridData();
		gd.horizontalSpan = comboC;
		gd.horizontalAlignment = 4;
		control.setLayoutData(gd);
		control.setFont(parent.getFont());
	}

	protected void doLoad() {
		updateComboForValue(getPreferenceStore().getString(getPreferenceName()));
	}

	protected void doLoadDefault() {
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()));
	}

	protected void doStore() {
		if (fValue == null) {
			getPreferenceStore().setToDefault(getPreferenceName());
			return;
		} else {
			getPreferenceStore().setValue(getPreferenceName(), fValue);
			return;
		}
	}

	public int getNumberOfControls() {
		return 2;
	}

	private Combo getComboBoxControl(Composite parent) {
		if (fCombo == null) {
			fCombo = new Combo(parent, 8);
			fCombo.setFont(parent.getFont());
			for (int i = 0; i < fEntryNamesAndValues.length; i++)
				fCombo.add(fEntryNamesAndValues[i][0], i);

			fCombo.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent evt) {
					String oldValue = fValue;
					String name = fCombo.getText();
					fValue = getValueForName(name);
					setPresentsDefaultValue(false);
					fireValueChanged("field_editor_value", oldValue, fValue);
				}

			});
		}
		return fCombo;
	}

	private String getValueForName(String name) {
		for (int i = 0; i < fEntryNamesAndValues.length; i++) {
			String entry[] = fEntryNamesAndValues[i];
			if (name.equals(entry[0]))
				return entry[1];
		}

		return fEntryNamesAndValues[0][0];
	}

	private void updateComboForValue(String value) {
		fValue = value;
		for (int i = 0; i < fEntryNamesAndValues.length; i++)
			if (value.equals(fEntryNamesAndValues[i][1])) {
				fCombo.setText(fEntryNamesAndValues[i][0]);
				return;
			}

		if (fEntryNamesAndValues.length > 0) {
			fValue = fEntryNamesAndValues[0][1];
			fCombo.setText(fEntryNamesAndValues[0][0]);
		}
	}

	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		getComboBoxControl(parent).setEnabled(enabled);
	}

	public void updateNameValue(String[] nameValue) {
		if (fCombo != null) {
			String name = nameValue[0];
			for (int i = 0; i < fEntryNamesAndValues.length; i++) {
				String entryName = fEntryNamesAndValues[i][0];
				if (entryName != null && name != null && entryName.trim().equals(name.trim())) {
					fEntryNamesAndValues[i][1] = nameValue[1];
					return;
				}
			}
			String[][] newValueList = new String[fEntryNamesAndValues.length + 1][2];
			System.arraycopy(fEntryNamesAndValues, 0, newValueList, 0, fEntryNamesAndValues.length);
			newValueList[fEntryNamesAndValues.length] = nameValue;
			fEntryNamesAndValues = newValueList;
			fCombo.add(nameValue[0]);
		}
	}

	private Combo fCombo;
	private String fValue;
	private String fEntryNamesAndValues[][];

}