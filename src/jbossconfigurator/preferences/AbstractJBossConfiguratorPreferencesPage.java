package jbossconfigurator.preferences;

import jbossconfigurator.preferences.constants.strings.PreferenceStringConstants;
import jbossconfigurator.preferences.constants.types.PreferenceTypeConstants;
import jbossconfigurator.preferences.widgets.ConfigurableComboFieldEditor;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class provides a means of adding/deleting/editing profiles for use with
 * JBoss.
 */

public class AbstractJBossConfiguratorPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	protected IPropertyChangeListener listener;

	public AbstractJBossConfiguratorPreferencesPage(int grid) {
		super(grid);
	}

	protected String[][] getProfiles() {
		String profiles = getPreferenceStore().getString(PreferenceTypeConstants.P_PROFILE_NAME);
		String[] profileList = profiles.split("}");
		if (profileList != null && profileList.length > 0 && profileList[0] != "") {
			String[][] nameValues = new String[profileList.length][2];
			for (int i = 0; i < profileList.length; i++) {
				String profile = profileList[i];
				String[] profileValues = profile.split(",");
				if (profileValues.length != 4) {
					nameValues[i][0] = "";
					nameValues[i][1] = "";
					continue;
				}
				String name = profileValues[0];
				String user = profileValues[1];
				String pass = profileValues[2];
				String url = profileValues[3];
				nameValues[i][0] = name;
				nameValues[i][1] = user + "," + pass + "," + url;
			}
			return nameValues;
		}

		return new String[][]{{"", ""}};
	}

	protected ConfigurableComboFieldEditor constructComboBox() {
		return new ConfigurableComboFieldEditor(PreferenceTypeConstants.P_PROFILE_NAME, PreferenceStringConstants.PROFILE, getProfiles(),
				getFieldEditorParent());
	}

	public void init(IWorkbench workbench) {
	}

	protected void createFieldEditors() {
	}
}
