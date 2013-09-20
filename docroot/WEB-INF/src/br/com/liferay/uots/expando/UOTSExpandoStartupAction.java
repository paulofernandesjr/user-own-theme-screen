package br.com.liferay.uots.expando;

import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.User;
import com.liferay.portlet.expando.DuplicateColumnNameException;
import com.liferay.portlet.expando.DuplicateTableNameException;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;

public class UOTSExpandoStartupAction extends SimpleAction {
	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#SimpleAction()
	 */
	public UOTSExpandoStartupAction() {
		super();
	}

	/* (non-Java-doc)
	 * @see com.liferay.portal.kernel.events.SimpleAction#run(String[] arg0)
	 */
	public void run(String[] ids) throws ActionException {
		try {
			// Get a reference to the ExpandoTable (User class)
			ExpandoTable table = null;
			long companyId = Long.parseLong(ids[0]);
			try {
			 	table = ExpandoTableLocalServiceUtil.addDefaultTable(
				 	companyId, User.class.getName());
			}
			catch(DuplicateTableNameException dtne) {
			 	table = ExpandoTableLocalServiceUtil.getDefaultTable(
				 	companyId, User.class.getName());
			}

			// Add the ExpandoColumn ("comments-astronauts")
			ExpandoColumn column = null;
			long tableId = table.getTableId();
			try {
				column = ExpandoColumnLocalServiceUtil.addColumn(
					tableId, "current-theme", ExpandoColumnConstants.STRING);

				// Add Unicode Properties
				UnicodeProperties properties = new UnicodeProperties();
				properties.setProperty(
						ExpandoColumnConstants.INDEX_TYPE, Boolean.TRUE.toString());

				column.setTypeSettingsProperties(properties);
				ExpandoColumnLocalServiceUtil.updateExpandoColumn(column);
			}
			catch(DuplicateColumnNameException dcne) {

				// Get the ExpandoColumn ("comments-astronauts")
				column = ExpandoColumnLocalServiceUtil.getColumn(
						tableId, "current-theme");
			}

		}
		catch(Exception e) {
			_log.error(e);
		}
		
		
	}
	

	private static Log _log = LogFactoryUtil.getLog(UOTSExpandoStartupAction.class);
}